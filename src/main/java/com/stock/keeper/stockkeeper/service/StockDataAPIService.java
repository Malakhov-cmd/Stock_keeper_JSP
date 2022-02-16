package com.stock.keeper.stockkeeper.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.stock.keeper.stockkeeper.domain.Stock;
import com.stock.keeper.stockkeeper.repo.DataRepo;
import com.stock.keeper.stockkeeper.repo.DataRepository;
import com.stock.keeper.stockkeeper.service.DTO.StockApiDTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.http.client.fluent.Request;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

@NoArgsConstructor
@Data
public class StockDataAPIService {
    private final String KeyAPI_Origin = "S9xvRnDm5whNDqDXaGriI3Wbcohpqy84";

    private final String KeyAPI_Support_1 = "mayjkucLrChPDlhaTpnIqAC2hijvY1m0";
    private final String KeyAPI_Support_2 = "ron4VZCL7YfIexBDqT1LL6hXmII52ehk";
    private final String KeyAPI_Support_3 = "3N_1VkZEAzNy0sMdicgZO9TtA6BqqEfI";
    private final String KeyAPI_Support_4 = "DQmFDhwAUp3TYxz2jJhXXqZRUO2384uF";
    private final String KeyAPI_Support_5 = "2XooTLjwl0vAGVAe5z7q5uZLQSbDuXNp";
    private final String KeyAPI_Support_6 = "X7rbgiA_cC3Flx_ohpu1pmpyaDqFauAW";

    private final String KeyAPI_Refresher = "xbC5mDo7lJT4o0W40xReBhBXp4rYgC3h";

    private final String KeyAPI_Info = "s8WqYhKmz0ZJ07x4X9hpC1hTEZaOWfGd";
    private final String KeyAPI_Img = "2CyqFAsm0z7nVjB6bAnmarfnixK8mCGL";

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final Long dayValue = 86_400_000L;
    private DataRepository dataRepository = new DataRepo();

    public Stock getResponceByAPI(String ticker, Long userId) {
        try {
            if (!isAlreadyInStockList(ticker, userId)) {
                String stock = getStockInfoViaJSON(ticker);
                System.out.println("STOCK info : " + stock);
                if (!stock.equals("Invalid data")) {
                    try {
                       return fillInfoAboutStock(ticker, userId, stock);
                    } catch (JsonProcessingException e) {
                        System.err.println("JSON parsing exception");
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

    private Flux<String> getCostsData(String ticker, Long ownerId, String infoJSON) throws JsonProcessingException {
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
                        stockData = getStockDataByDateViaJSON(ticker, formater.format(date.getTime() - pastTimeValue), KeyAPI_Origin);
                    } catch (IOException e) {
                        try {
                            stockData = getStockDataByDateViaJSON(ticker, formater.format(date.getTime() - pastTimeValue - dayValue), KeyAPI_Support_1);
                        } catch (IOException ex) {
                            try {
                                stockData = getStockDataByDateViaJSON(ticker, formater.format(date.getTime() - pastTimeValue - dayValue * 2), KeyAPI_Support_2);
                            } catch (IOException exp) {
                                try {
                                    stockData = getStockDataByDateViaJSON(ticker, formater.format(date.getTime() - pastTimeValue - dayValue * 3), KeyAPI_Support_3);
                                } catch (IOException expt) {
                                    try {
                                        stockData = getStockDataByDateViaJSON(ticker, formater.format(date.getTime() - pastTimeValue - dayValue * 4), KeyAPI_Support_4);
                                    } catch (IOException exptn) {
                                        try {
                                            stockData = getStockDataByDateViaJSON(ticker, formater.format(date.getTime() - pastTimeValue - dayValue * 5), KeyAPI_Support_5);
                                        } catch (IOException exptnv) {
                                            try {
                                                stockData = getStockDataByDateViaJSON(ticker, formater.format(date.getTime() - pastTimeValue - dayValue * 6), KeyAPI_Support_6);
                                            } catch (IOException exptnvl) {
                                                System.err.println("Invalid data");
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

        getCostsData(ticker, ownerId, infoJSON).subscribe(stringStockDTO -> {
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
            urlIMG = node.get("results").get("branding").get("icon_url").asText() + "?apiKey=" + KeyAPI_Img;
        } catch (NullPointerException ex){
            urlIMG = "https://www.publicdomainpictures.net/pictures/280000/nahled/not-found-image-15383864787lu.jpg";
        }
        LocalDateTime initTime = LocalDateTime.now();

        return dataRepository.insertStock(initTime, description, urlIMG, ticker, name, ownerId);
    }

    private String getStockInfoViaJSON(String index)
            throws IOException {
        return Request
                .Get("https://api.polygon.io/v3/reference/tickers/" + index + "?apiKey=" + KeyAPI_Info)
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
            stockData = getStockDataByDateViaJSON(ticker, formater.format(date.getTime() - dayValue), KeyAPI_Refresher);
        } catch (IOException e) {
            try {
                stockData = getStockDataByDateViaJSON(ticker, formater.format(date.getTime() - dayValue * 2), KeyAPI_Refresher);
            } catch (IOException ex) {
                try {
                    stockData = getStockDataByDateViaJSON(ticker, formater.format(date.getTime() - dayValue * 3), KeyAPI_Refresher);
                } catch (IOException exc) {
                    try {
                        stockData = getStockDataByDateViaJSON(ticker, formater.format(date.getTime() - dayValue * 4), KeyAPI_Refresher);
                    } catch (IOException ioException) {
                        try {
                            stockData = getStockDataByDateViaJSON(ticker, formater.format(date.getTime() - dayValue * 5), KeyAPI_Refresher);
                        } catch (IOException exception) {
                            try {
                                stockData = getStockDataByDateViaJSON(ticker, formater.format(date.getTime() - dayValue * 6), KeyAPI_Refresher);
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
