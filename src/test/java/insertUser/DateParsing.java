package insertUser;

import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateParsing {
    @Test
    public void dateParsing(){
        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();


        System.out.println(formater.format(date));

        System.out.println(formater.format(date.getTime() - 15_768_000_000L));

        System.out.println(formater.format(date.getTime() - 23_652_000_000L));

        System.out.println(formater.format(date.getTime()- 31_536_000_000L));
    }
}
