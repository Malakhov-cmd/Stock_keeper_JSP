package insertUser;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Calendar;
import java.util.Date;

public class DateParsing {
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
}
