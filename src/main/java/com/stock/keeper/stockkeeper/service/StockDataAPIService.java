package com.stock.keeper.stockkeeper.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.stock.keeper.stockkeeper.service.DTO.StockApiDTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

@NoArgsConstructor
@Data
public class StockDataAPIService {
    private final String KeyAPI_Origin = "S9xvRnDm5whNDqDXaGriI3Wbcohpqy84";
    private final String KeyAPI_Support = "mayjkucLrChPDlhaTpnIqAC2hijvY1m0";
    private final String KeyAPI_Error_Case = "xbC5mDo7lJT4o0W40xReBhBXp4rYgC3h";

    private final String KeyAPI_Info = "s8WqYhKmz0ZJ07x4X9hpC1hTEZaOWfGd";
    private final String KeyAPI_Img = "2CyqFAsm0z7nVjB6bAnmarfnixK8mCGL";

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final Long twoDaysValue = 86_400_000L;

    public void getResponceByAPI(String ticker, String userId) {
        getInfoAboutCompany(ticker).subscribe(
                stock -> {
                    if (!stock.equals("Invalid data")) {
                        try {
                            fillInfoAboutStock(ticker, userId, stock);
                        } catch (JsonProcessingException e) {
                            System.err.println("JSON parsing exception");
                        }
                    }
                }
        );
    }

    private Mono<String> getInfoAboutCompany(String ticker) {
        return Mono
                .empty()
                .map(item -> {
                    try {
                        return getStockInfoViaJSON(ticker);
                    } catch (IOException e) {
                        return "Invalid data";
                    }
                });
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
                        stockData = getStockDataByDateViaJSON(ticker, formater.format(date.getTime() - pastTimeValue), KeyAPI_Origin);
                    } catch (IOException e) {
                        try {
                            stockData = getStockDataByDateViaJSON(ticker, formater.format(date.getTime() - pastTimeValue - twoDaysValue * 3), KeyAPI_Support);
                        } catch (IOException ex) {
                            try {
                                stockData = getStockDataByDateViaJSON(ticker, formater.format(date.getTime() - pastTimeValue + twoDaysValue * 3), KeyAPI_Error_Case);
                            } catch (IOException exp) {
                                System.err.println("Invalid data");
                            }
                        }
                    }
                    return stockData;
                });
    }

    private void fillInfoAboutStock(String ticker, String ownerId, String infoJSON) throws JsonProcessingException {
        final ObjectNode node = new ObjectMapper().readValue(infoJSON, ObjectNode.class);

        String name = node.get("results").get("name").asText();
        String description = node.get("results").get("description").asText();
        String urlIMG = node.get("results").get("branding").get("icon_url").asText() + "?apiKey=" + KeyAPI_Img;

        LocalDateTime nationalTime = LocalDateTime.now();

        //TODO создать акцию в БД

        getCostsData(ticker).subscribe(stringStockDTO -> {
            try {
                //TODO добавить в БД стоимости
                processDTOtoStock(stringStockDTO);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });
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

    private void processDTOtoStock(String stringStockDTO) throws JsonProcessingException {
        StockApiDTO stockApiDTO = objectMapper.readValue(stringStockDTO, StockApiDTO.class);
    }
}
