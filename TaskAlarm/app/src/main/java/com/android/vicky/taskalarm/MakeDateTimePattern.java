package com.android.vicky.taskalarm;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Vicky on 3/24/2016.
 */
public class MakeDateTimePattern {
    public static String setDatePattern(String cDate) {
        SimpleDateFormat sdfPrev = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdfNew = new SimpleDateFormat("MMM dd, yyyy");
        Date fldRealDate = new Date();
        try {
            fldRealDate = sdfPrev.parse(cDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return sdfNew.format(fldRealDate);
    }
    public static String unsetDatePattern(String cDate) {
        SimpleDateFormat sdfPrev = new SimpleDateFormat("MMM dd, yyyy");
        SimpleDateFormat sdfNew = new SimpleDateFormat("yyyy-MM-dd");
        Date fldRealDate = new Date();
        try {
            fldRealDate = sdfPrev.parse(cDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return sdfNew.format(fldRealDate);
    }

    public  static String getCurrentDate() {
        Date crrntDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String cDate = sdf.format(crrntDate);
//        Log.d("In DB Current Date", cDate);
        return cDate;
    }
    public static String getCurrentTime() {
        Date crrntTime = new Date();
        SimpleDateFormat sdfTime = new SimpleDateFormat("hh:mm aa");
        String cTime = sdfTime.format(crrntTime);
//        Log.d("In DB Current Date", cTime);
        return cTime;
    }

}
