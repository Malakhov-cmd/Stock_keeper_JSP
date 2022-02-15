package insertUser;

import com.stock.keeper.stockkeeper.domain.Stock;
import com.stock.keeper.stockkeeper.repo.DataRepo;
import com.stock.keeper.stockkeeper.repo.DataRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Date;

public class DateParsing {
    private DataRepository dataRepository = new DataRepo();
    private final Long dayValue = 86_400_000L;

    @Test
    public void dateParsing() {
        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();


        System.out.println(formater.format(date));

        System.out.println(formater.format(date.getTime() - 15_768_000_000L));

        System.out.println(formater.format(date.getTime() - 23_652_000_000L));

        System.out.println(formater.format(date.getTime() - 31_536_000_000L));
    }

    @SneakyThrows
    @Test
    public void dateFromDatePicker() {
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendOptional(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                .toFormatter();
        String purposeDate = "03/08/2022";

        java.sql.Date sqlDate = java.sql.Date.valueOf(LocalDate.parse(purposeDate, formatter));

        System.out.println(sqlDate);
    }

    @Test
    public void dateCompare(){
        Stock stock = dataRepository.selectStockById(7L);

        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();

        System.out.println(formater.format(date.getTime() - dayValue));

        Assertions.assertEquals(false,
                stock
                        .getPriceList()
                        .stream()
                        .noneMatch(
                                price ->
                        formater.format(price.getDate().getTime())
                                .equals(formater.format(date.getTime() - dayValue))
                ));
    }
}
