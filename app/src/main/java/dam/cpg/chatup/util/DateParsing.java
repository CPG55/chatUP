package dam.cpg.chatup.util;

import android.content.Context;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public final class DateParsing {

    private DateParsing() { }

    public static String convertToUTC (String registeredLocalTimeStringToParse) {

        String messageSentTimeParsedToUTC = "";

        // Parse message time to UTC Time.
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date localTimeStringParsedToDate = dateFormat.parse(registeredLocalTimeStringToParse);
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            messageSentTimeParsedToUTC = dateFormat.format(localTimeStringParsedToDate);

            Log.d(" Time parsed to UTC: ",  messageSentTimeParsedToUTC);

        } catch (ParseException parsingMessageTimeUTCException) {
            parsingMessageTimeUTCException.printStackTrace();
        }


        return messageSentTimeParsedToUTC;

    }

    public static String convertUTCToLocalTime (String messageSentTime) {

        String messageSentTimeParsedToLocalTime = "";

        // Convert the message timezone. Get the  device TimeZone.
        TimeZone currentTimeZone  =  Calendar.getInstance().getTimeZone();

        Log.d("Local Time zone: " , currentTimeZone.getDisplayName());

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date sentDate = dateFormat.parse(messageSentTime);
            dateFormat.setTimeZone(currentTimeZone);
            messageSentTimeParsedToLocalTime = dateFormat.format(sentDate);

            Log.d("Parsed to: " , currentTimeZone.getDisplayName());

        } catch (ParseException parsingMessageTimeException) {
            parsingMessageTimeException.printStackTrace();
        }

        return messageSentTimeParsedToLocalTime;

    }

    public static String convertDateToFriendly (String alreadyConvertedDateToLocalTime , Context context) {

        String moreFriendlyDate = "" ;

        long sentTimeInMillis;

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date dateToGetMillis = dateFormat.parse(alreadyConvertedDateToLocalTime);
            sentTimeInMillis = dateToGetMillis.getTime();
            TimeAgo timeAgo = new TimeAgo(context);
            moreFriendlyDate = timeAgo.timeAgo(sentTimeInMillis);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return moreFriendlyDate;
    }

}
