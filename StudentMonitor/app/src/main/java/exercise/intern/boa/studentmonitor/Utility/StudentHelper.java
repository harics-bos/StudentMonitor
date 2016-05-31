package exercise.intern.boa.studentmonitor.Utility;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * A helper class that contains helper functions used by multiple classes
 */
public class StudentHelper {

    public static final int IMAGE_WIDTH=200;
    public static final int IMAGE_HEIGHT=200;
    public static final String DATE_SEPARATOR="/";
    public static final int MIN_YRS=5;
    public static final int MAX_YRS=12;

    private StudentHelper(){
        //Only static members
         }

    /*private static Map<String,String> dateRegex=new HashMap<String,String>();

    public static void populateDateRegexMap(){
        dateRegex.put("yyyy-MM-dd", "\\d{4}-\\d{2}-\\d{2}");
        dateRegex.put("yyyy/mm/dd", "\\d{4}/\\d{2}/\\d{2}");
    }*/

    //Helper method to find if the input date matches the required format
   /* public static boolean isDateProperFormat(String dateText,String dateFormat){
        if(dateRegex.size()==0)
            populateDateRegexMap();

        if(dateText==null||dateText.trim().equals("")){
            Log.d("Input Date","Date should not be null or empty");
            return false;
        }

        if(dateFormat==null || dateFormat.trim().equals("")){
            Log.d("Input Date Format","Date Format should not be null or empty");
            return false;
        }

        else{
            String regex=dateRegex.get(dateFormat);
            if(dateText.matches(regex)){
                return true;
            }else{
                return false;
            }

        }

    }*/

    /*public static boolean isYearsWithinLimit(String kidsDob, int minYr, int maxYr, String dateFormat){
        int yearDiff;
        int normalYearDays=365;
        int leapYearDays=366;
        Calendar cal=Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        int normalYearsCount=0;
        int leapYearsCount=0;
        try {
            Date dateFrom = sdf.parse(kidsDob);
            Date currentDate = new Date();

            cal.setTime(dateFrom);
            int startYear=cal.get(Calendar.YEAR);
            cal.setTime(currentDate);
            int endYear=cal.get(Calendar.YEAR);
            int daysBetween=(int)getDifferenceDays(dateFrom, currentDate);
            // Calculating the total number of leap years
            for(int yr=startYear;yr<=endYear;yr++){
                // If an year is divisble by 400 then its automatically divisible by 100 and 4 too
                if(yr%400==0)
                    leapYearsCount++;
            }

            //Calculating the total number of normal years
            normalYearsCount=((daysBetween)-(leapYearsCount*leapYearDays))/normalYearDays;
            yearDiff=normalYearsCount+leapYearsCount;
            if(yearDiff<minYr||yearDiff>maxYr)
                return false;
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return true;
    }*/

    /*public static long getDifferenceDays(Date d1, Date d2) {
        long diff = d2.getTime() - d1.getTime();
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }*/

    /*
        Returns the total number of mins betweeen these two Dates
     */
 /* public static long getDateDiffInMinutes(Date date1, Date date2) {
        long diffInMillies = date2.getTime() - date1.getTime();

        return TimeUnit.MINUTES.convert(diffInMillies,TimeUnit.MILLISECONDS);

    }*/

    // convert from bitmap to byte array
    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 0, stream);
        return stream.toByteArray();
    }

    // convert from byte array to bitmap
    public static Bitmap getImage(byte[] image) {
        int len=image.length;
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }


}
