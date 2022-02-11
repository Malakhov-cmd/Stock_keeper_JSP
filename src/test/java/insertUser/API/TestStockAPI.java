package insertUser.API;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stock.keeper.stockkeeper.service.DTO.StockApiDTO;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestStockAPI {

    private final String KeyAPI_Origin = "S9xvRnDm5whNDqDXaGriI3Wbcohpqy84";
    private final String KeyAPI_Support = "mayjkucLrChPDlhaTpnIqAC2hijvY1m0";
    private final String KeyAPI_Error_Case = "xbC5mDo7lJT4o0W40xReBhBXp4rYgC3h";

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final Long twoDaysValue = 86_400_000L;

    @Test
    public void getResponceByAPI() {
        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();

        Flux<String> ints = Flux.range(0, 4)
                .map(i -> {
                    Long pastTimeValue = 0L;
                    String stockData = "";

                    if (i == 0) pastTimeValue = 31_536_000_000L;
                    else if (i == 3) pastTimeValue = 0L;
                    else pastTimeValue = 31_536_000_000L - 7_884_000_000L * i;

                    try {
                        stockData = getStockDataByDateViaJSON("AAPL", formater.format(date.getTime() - pastTimeValue), KeyAPI_Origin);
                    } catch (IOException e) {
                        try {
                            stockData = getStockDataByDateViaJSON("AAPL", formater.format(date.getTime() - pastTimeValue - twoDaysValue * 3), KeyAPI_Support);
                        } catch (IOException ex) {
                            try {
                                stockData = getStockDataByDateViaJSON("AAPL", formater.format(date.getTime() - pastTimeValue + twoDaysValue * 3), KeyAPI_Error_Case);
                            } catch (IOException exp) {
                                System.out.println("Get");
                            }
                        }
                    }
                    return stockData;
                });
        ints.subscribe(stockDTO -> {
            try {
                StockApiDTO stockApiDTO = objectMapper.readValue(stockDTO, StockApiDTO.class);
                System.out.println(stockApiDTO.toString());
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });

        /*try {
            System.out.println(getStockDataByDateViaJSON("AAPL", formater.format(date.getTime() - 31_536_000_000L)));
        } catch (IOException e) {
            try {
                System.out.println(getStockDataByDateViaJSON("AAPL", formater.format(date.getTime() - 31_536_000_000L)));
            } catch (IOException ex) {
                try {
                    System.out.println(getStockDataByDateViaJSON("AAPL", formater.format(date.getTime() - 31_536_000_000L)));
                } catch (IOException exc) {
                    System.err.println("Data is invalid");
                }
            }
        }

        try {
            System.out.println(getStockDataByDateViaJSON("AAPL", formater.format(date.getTime() - 23_652_000_000L)));
        } catch (IOException e) {
            try {
                System.out.println(getStockDataByDateViaJSON("AAPL", formater.format(date.getTime() - 23_652_000_000L + 86_400_000L)));
            } catch (IOException ex) {
                try {
                    System.out.println(getStockDataByDateViaJSON("AAPL", formater.format(date.getTime() - 23_652_000_000L - 86_400_000L)));
                } catch (IOException exc) {
                    System.err.println("Data is invalid");
                }
            }
        }

        try {
            System.out.println(getStockDataByDateViaJSON("AAPL", formater.format(date.getTime() - 15_768_000_000L)));
        } catch (IOException e) {
            try {
                System.out.println(getStockDataByDateViaJSON("AAPL", formater.format(date.getTime() - 15_768_000_000L + 86_400_000L)));
            } catch (IOException ex) {
                try {
                    System.out.println(getStockDataByDateViaJSON("AAPL", formater.format(date.getTime() - 15_768_000_000L - 86_400_000L)));
                } catch (IOException exc) {
                    System.err.println("Data is invalid");
                }
            }
        }

        try {
            System.out.println(getStockDataByDateViaJSON("AAPL", formater.format(date.getTime() - 86_400_000L)));
        } catch (IOException e) {
            try {
                System.out.println(getStockDataByDateViaJSON("AAPL", formater.format(date.getTime() - 86_400_000L * 2)));
            } catch (IOException ex) {
                try {
                    System.out.println(getStockDataByDateViaJSON("AAPL", formater.format(date.getTime() - 86_400_000L * 3)));
                } catch (IOException exc) {
                    System.err.println("Data is invalid");
                }
            }
        }*/
    }


    private String getStockDataByDateViaJSON(String index, String date, String keyAPI) throws IOException {
        final URL url = new URL("https://api.polygon.io/v1/open-close/" + index + "/" + date + "?adjusted=false&apiKey=" + keyAPI);
        final HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setRequestMethod("GET");
        con.setRequestProperty("Content-Type", "application/json");

        final BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

        String inputLine;
        final StringBuilder content = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        return content.toString();

    }
}
