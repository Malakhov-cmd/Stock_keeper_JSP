package insertUser;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class JsonParsing {
    private final String testJSON = "{\n" +
            " \"results\": {\n" +
            "  \"ticker\": \"AAPL\",\n" +
            "  \"name\": \"Apple Inc.\",\n" +
            "  \"market\": \"stocks\",\n" +
            "  \"locale\": \"us\",\n" +
            "  \"primary_exchange\": \"XNAS\",\n" +
            "  \"type\": \"CS\",\n" +
            "  \"active\": true,\n" +
            "  \"currency_name\": \"usd\",\n" +
            "  \"cik\": \"0000320193\",\n" +
            "  \"composite_figi\": \"BBG000B9XRY4\",\n" +
            "  \"share_class_figi\": \"BBG001S5N8V8\",\n" +
            "  \"market_cap\": 2752110530240,\n" +
            "  \"phone_number\": \"(408) 996-1010\",\n" +
            "  \"address\": {\n" +
            "   \"address1\": \"ONE APPLE PARK WAY\",\n" +
            "   \"city\": \"CUPERTINO\",\n" +
            "   \"state\": \"CA\",\n" +
            "   \"postal_code\": \"95014\"\n" +
            "  },\n" +
            "  \"description\": \"Apple designs a wide variety of consumer electronic devices, including smartphones (iPhone), tablets (iPad), PCs (Mac), smartwatches (Apple Watch), AirPods, and TV boxes (Apple TV), among others. The iPhone makes up the majority of Apple's total revenue. In addition, Apple offers its customers a variety of services such as Apple Music, iCloud, Apple Care, Apple TV+, Apple Arcade, Apple Card, and Apple Pay, among others. Apple's products run internally developed software and semiconductors, and the firm is well known for its integration of hardware, software and services. Apple's products are distributed online as well as through company-owned stores and third-party retailers. The company generates roughly 40% of its revenue from the Americas, with the remainder earned internationally.\",\n" +
            "  \"sic_code\": \"3571\",\n" +
            "  \"sic_description\": \"ELECTRONIC COMPUTERS\",\n" +
            "  \"ticker_root\": \"AAPL\",\n" +
            "  \"homepage_url\": \"https://www.apple.com\",\n" +
            "  \"total_employees\": 154000,\n" +
            "  \"list_date\": \"1980-12-12\",\n" +
            "  \"branding\": {\n" +
            "   \"logo_url\": \"https://api.polygon.io/v1/reference/company-branding/d3d3LmFwcGxlLmNvbQ/images/2022-02-01_logo.svg\",\n" +
            "   \"icon_url\": \"https://api.polygon.io/v1/reference/company-branding/d3d3LmFwcGxlLmNvbQ/images/2022-02-01_icon.png\"\n" +
            "  },\n" +
            "  \"share_class_shares_outstanding\": 16334370000,\n" +
            "  \"weighted_shares_outstanding\": 16319441000\n" +
            " },\n" +
            " \"status\": \"OK\",\n" +
            " \"request_id\": \"f729c5e9805f44386693bfe0bba81466\"\n" +
            "}";

    @SneakyThrows
    @Test
    public void JSONparsingTest(){
        final ObjectNode node = new ObjectMapper().readValue(testJSON, ObjectNode.class);

        String name = node.get("results").get("name").asText();
        String desc = node.get("results").get("description").asText();
        String jsonImg = node.get("results").get("branding").get("icon_url").asText() + "?apiKey=" + "2CyqFAsm0z7nVjB6bAnmarfnixK8mCGL";

        Assertions.assertEquals("Apple Inc.", name);
        Assertions.assertEquals("Apple designs a wide variety of consumer electronic devices, including smartphones (iPhone), tablets (iPad), PCs (Mac), smartwatches (Apple Watch), AirPods, and TV boxes (Apple TV), among others. The iPhone makes up the majority of Apple's total revenue. In addition, Apple offers its customers a variety of services such as Apple Music, iCloud, Apple Care, Apple TV+, Apple Arcade, Apple Card, and Apple Pay, among others. Apple's products run internally developed software and semiconductors, and the firm is well known for its integration of hardware, software and services. Apple's products are distributed online as well as through company-owned stores and third-party retailers. The company generates roughly 40% of its revenue from the Americas, with the remainder earned internationally.", desc);
        Assertions.assertNotEquals("https://api.polygon.io/v1/reference/company-branding/d3d3LmFwcGxlLmNvbQ/images/2022-02-01_icon.png?apiKey=xbC5mDo7lJT4o0W40xReBhBXp4rYgC3h", jsonImg);
    }
}
