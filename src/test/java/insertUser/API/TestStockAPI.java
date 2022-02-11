package insertUser.API;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestStockAPI {

    @Test
    public void getResponceByAPI(){
        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();

        try {
            System.out.println(getStockDataByDateViaJSON("AAPL", formater.format(date.getTime()- 31_536_000_000L)));
        } catch (IOException e) {
            try {
                System.out.println(getStockDataByDateViaJSON("AAPL", formater.format(date.getTime()- 31_536_000_000L)));
            } catch (IOException ex) {
                try {
                    System.out.println(getStockDataByDateViaJSON("AAPL", formater.format(date.getTime()- 31_536_000_000L)));
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
                System.out.println(getStockDataByDateViaJSON("AAPL", formater.format(date.getTime() - 86_400_000L*2)));
            } catch (IOException ex) {
                try {
                    System.out.println(getStockDataByDateViaJSON("AAPL", formater.format(date.getTime() - 86_400_000L*3)));
                } catch (IOException exc) {
                    System.err.println("Data is invalid");
                }
            }
        }
    }


    private String getStockDataByDateViaJSON(String index, String date)  throws IOException {
        final URL url = new URL("https://api.polygon.io/v1/open-close/" + index +"/" + date + "?adjusted=false&apiKey=S9xvRnDm5whNDqDXaGriI3Wbcohpqy84");
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
