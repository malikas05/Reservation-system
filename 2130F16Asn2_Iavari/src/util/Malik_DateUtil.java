package util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Malik_DateUtil {
    private static SimpleDateFormat ft = 
      new SimpleDateFormat ("dd/MM/yyyy hh:mm");
    
    private static String DATE_PATTERN = "dd.MM.yyyy";
    private static final DateTimeFormatter DATE_FORMATTER = 
            DateTimeFormatter.ofPattern(DATE_PATTERN);


    public static String format(Date date) {
        if (date == null) {
            return null;
        }
        return ft.format(date);
    }
    
    public static Date parse(String dateString) {
        try {
            return ft.parse(dateString);
        } catch (ParseException e) {
            return null;
        }
    }
    
    public static String formatBirthDate(LocalDate date) {
        if (date == null) {
            return null;
        }
        return DATE_FORMATTER.format(date);
    }
    
    public static LocalDate parseBirthDate(String dateString) {
        try {
            return DATE_FORMATTER.parse(dateString, LocalDate::from);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    public static boolean validDate(String dateString) {
        return Malik_DateUtil.parseBirthDate(dateString) != null;
    }
   
    public static Date addDays(Date date) {
        long diffMill = new Date().getTime() - date.getTime();
        int diffDays = (int) TimeUnit.DAYS.convert(diffMill, TimeUnit.MILLISECONDS);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, diffDays * 2);
        return cal.getTime();
    }
}
