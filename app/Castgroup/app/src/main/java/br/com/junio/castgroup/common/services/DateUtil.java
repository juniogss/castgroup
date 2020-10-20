package br.com.junio.castgroup.common.services;

import android.text.format.DateFormat;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtil {

    public static long toLong(String date) {
        if (date == null) return 0;
        if (date.equals("")) return 0;

        Date conversion = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        sdf.setLenient(false);
        try {
            conversion = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return conversion != null ? conversion.getTime() : 0;
    }

    public static long toLongEnd(String date) {
        if (date == null) return 0;
        if (date.equals("")) return 0;

        Date conversion = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
        sdf.setLenient(false);
        try {
            conversion = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return conversion != null ? conversion.getTime() : 0;
    }

    @NotNull
    public static String toString(long date) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(date);
        return DateFormat.format("dd/MM/yyyy", cal).toString();
    }

    @NotNull
    public static String toString(String date) {
        if (date == null) return "";
        if (date.equals("")) return "";

        Date conversion = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        sdf.setLenient(false);
        try {
            conversion = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return outputFormat.format(conversion);
    }

}
