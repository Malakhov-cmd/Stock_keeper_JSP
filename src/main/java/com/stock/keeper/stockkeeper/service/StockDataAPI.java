package com.stock.keeper.stockkeeper.service;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

@NoArgsConstructor
@Data
public class StockDataAPI {
    public void getStockData(String index) throws IOException {
        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();


        getStockDataByDateViaJSON(index, formater.format(date.getTime()- 31_536_000_000L));

        getStockDataByDateViaJSON(index, formater.format(date.getTime() - 23_652_000_000L));

        getStockDataByDateViaJSON(index, formater.format(date.getTime() - 15_768_000_000L));

        getStockDataByDateViaJSON(index, formater.format(date));
    }

    private String getStockDataByDateViaJSON(String index, String Date)  throws IOException {
        final URL url = new URL("https://api.polygon.io/v1/open-close/" + index +"/2021-10-21" + "?adjusted=false&apiKey=S9xvRnDm5whNDqDXaGriI3Wbcohpqy84");
        final HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setRequestMethod("GET");
        con.setRequestProperty("Content-Type", "application/json");

        try (final BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
            String inputLine;
            final StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            return content.toString();
        } catch (final Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }
}
