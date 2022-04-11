package com.stock.keeper.stockkeeper.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.stock.keeper.stockkeeper.domain.Stock;
import com.stock.keeper.stockkeeper.repo.DataRepo;
import com.stock.keeper.stockkeeper.repo.DataRepository;
import com.stock.keeper.stockkeeper.service.DTO.StockApiDTO;
import com.stock.keeper.stockkeeper.util.AccountAPI;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.fluent.Request;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

@NoArgsConstructor
@Data
@Slf4j
public class StockDataAPIService {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final Long dayValue = 86_400_000L;
    private DataRepository dataRepository = new DataRepo();

    private AccountAPI accountAPI = new AccountAPI();

    public Stock getResponceByAPI(String ticker, Long userId) {
        try {
            if (!isAlreadyInStockList(ticker, userId)) {
                String stock = getStockInfoViaJSON(ticker);
                log.info("STOCK info : {}", stock);
                if (!stock.equals("Invalid data")) {
                    try {
                       return fillInfoAboutStock(ticker, userId, stock);
                    } catch (JsonProcessingException e) {
                        log.error("JSON parsing exception");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean isAlreadyInStockList(String ticker, Long userId) {
        return dataRepository
                .selectStocksByUsrId(userId)
                .stream()
                .anyMatch(stock -> stock.getIndex().equals(ticker));
    }

    private Flux<String> getCostsData(String ticker) {
        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();

        return Flux.range(0, 4)
                .map(i -> {
                    Long pastTimeValue = 0L;
                    String stockData = "";

                    if (i == 0) pastTimeValue = 31_536_000_000L;
                    else if (i == 3) pastTimeValue = 0L;
                    else pastTimeValue = 31_536_000_000L - 7_884_000_000L * i;

                    try {
                        stockData = getStockDataByDateViaJSON(ticker, formater.format(date.getTime() - pastTimeValue), accountAPI.getKeyAPI_Origin());
                    } catch (IOException e) {
                        try {
                            stockData = getStockDataByDateViaJSON(ticker, formater.format(date.getTime() - pastTimeValue - dayValue), accountAPI.getKeyAPI_Support_1());
                        } catch (IOException ex) {
                            try {
                                stockData = getStockDataByDateViaJSON(ticker, formater.format(date.getTime() - pastTimeValue - dayValue * 2), accountAPI.getKeyAPI_Support_2());
                            } catch (IOException exp) {
                                try {
                                    stockData = getStockDataByDateViaJSON(ticker, formater.format(date.getTime() - pastTimeValue - dayValue * 3), accountAPI.getKeyAPI_Support_3());
                                } catch (IOException expt) {
                                    try {
                                        stockData = getStockDataByDateViaJSON(ticker, formater.format(date.getTime() - pastTimeValue - dayValue * 4), accountAPI.getKeyAPI_Support_4());
                                    } catch (IOException exptn) {
                                        try {
                                            stockData = getStockDataByDateViaJSON(ticker, formater.format(date.getTime() - pastTimeValue - dayValue * 5), accountAPI.getKeyAPI_Support_5());
                                        } catch (IOException exptnv) {
                                            try {
                                                stockData = getStockDataByDateViaJSON(ticker, formater.format(date.getTime() - pastTimeValue - dayValue * 6), accountAPI.getKeyAPI_Support_6());
                                            } catch (IOException exptnvl) {
                                                log.error("Invalid data");
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    return stockData;
                });
    }

    private Stock fillInfoAboutStock(String ticker, Long ownerId, String infoJSON) throws JsonProcessingException {

        Stock stock = insertStock(ticker, ownerId, infoJSON);

        getCostsData(ticker).subscribe(stringStockDTO -> {
            try {
                StockApiDTO stockApiDTO = processDTOtoStock(stringStockDTO);
                dataRepository.insertPrice(
                        stockApiDTO.AveragePerDay(),
                        stockApiDTO.getFrom(),
                        stock.getId()
                );
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });

        return stock;
    }

    private Stock insertStock(String ticker, Long ownerId, String infoJSON) throws JsonProcessingException {
        final ObjectNode node = new ObjectMapper().readValue(infoJSON, ObjectNode.class);

        String name = node.get("results").get("name").asText();
        String description = node.get("results").get("description").asText();

        String urlIMG = "";
        try {
            urlIMG = node.get("results").get("branding").get("icon_url").asText() + "?apiKey=" + accountAPI.getKeyAPI_Img();
        } catch (NullPointerException ex){
            urlIMG = "https://www.publicdomainpictures.net/pictures/280000/nahled/not-found-image-15383864787lu.jpg";
        }
        LocalDateTime initTime = LocalDateTime.now();

        return dataRepository.insertStock(initTime, description, urlIMG, ticker, name, ownerId);
    }

    private String getStockInfoViaJSON(String index)
            throws IOException {
        return Request
                .Get("https://api.polygon.io/v3/reference/tickers/" + index + "?apiKey=" + accountAPI.getKeyAPI_Info())
                .execute()
                .returnContent()
                .asString();
    }

    private String getStockDataByDateViaJSON(String index, String date, String keyAPI)
            throws IOException {
        return Request
                .Get("https://api.polygon.io/v1/open-close/" + index + "/" + date + "?adjusted=false&apiKey=" + keyAPI)
                .execute()
                .returnContent()
                .asString();
    }

    private StockApiDTO processDTOtoStock(String stringStockDTO)
            throws JsonProcessingException {
        return objectMapper.readValue(stringStockDTO, StockApiDTO.class);
    }

    public Stock refreshPriceStock(Long stockId) {
        Stock stock = dataRepository.selectStockById(stockId);

        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();

        if (stock
                .getPriceList()
                .stream()
                .noneMatch(price ->
                        formater.format(price.getDate().getTime())
                                .equals(formater.format(date.getTime() - dayValue))
                )) {
                try {
                    StockApiDTO stockApiDTO = processDTOtoStock(getFreshPrices(stock.getIndex()));
                    dataRepository.insertPrice(
                            stockApiDTO.AveragePerDay(),
                            stockApiDTO.getFrom(),
                            stock.getId()
                    );

                    stock = dataRepository.selectStockById(stockId);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }

        }
        return stock;
    }

    private String getFreshPrices(String ticker) {
        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();

        String stockData = "";

        try {
            stockData = getStockDataByDateViaJSON(ticker, formater.format(date.getTime() - dayValue), accountAPI.getKeyAPI_Refresher());
        } catch (IOException e) {
            try {
                stockData = getStockDataByDateViaJSON(ticker, formater.format(date.getTime() - dayValue * 2), accountAPI.getKeyAPI_Refresher());
            } catch (IOException ex) {
                try {
                    stockData = getStockDataByDateViaJSON(ticker, formater.format(date.getTime() - dayValue * 3), accountAPI.getKeyAPI_Refresher());
                } catch (IOException exc) {
                    try {
                        stockData = getStockDataByDateViaJSON(ticker, formater.format(date.getTime() - dayValue * 4), accountAPI.getKeyAPI_Refresher());
                    } catch (IOException ioException) {
                        try {
                            stockData = getStockDataByDateViaJSON(ticker, formater.format(date.getTime() - dayValue * 5), accountAPI.getKeyAPI_Refresher());
                        } catch (IOException exception) {
                            try {
                                stockData = getStockDataByDateViaJSON(ticker, formater.format(date.getTime() - dayValue * 6), accountAPI.getKeyAPI_Refresher());
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
        return stockData;
    }
}
