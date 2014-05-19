package com.example.teamlabdocsapp.app.api.helpers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by facetostool on 19.05.2014.
 */
public class TimeConvertHelper {
    public static String convertTime(String timeWithUTC) {
        String ALT_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.0000000+04:00";
        SimpleDateFormat sdf = new SimpleDateFormat(ALT_DATE_TIME_FORMAT);
        Date date = null;
        try {
            date = sdf.parse(timeWithUTC);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat newSdf = new SimpleDateFormat("dd.MM.yyyy HH:mm"); // Set your date format
        return newSdf.format(date);
    }
}
