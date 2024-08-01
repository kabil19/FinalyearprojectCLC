package com.appli.clcapi.common.date;

import java.text.SimpleDateFormat;

public class DateUtils {

    public static String formatDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }
}
