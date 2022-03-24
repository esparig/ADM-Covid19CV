package es.uv.parcero.utils;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    private static String URL = "https://dadesobertes.gva.es/es/api/3/action/datastore_search?resource_id=7fd9a2bf-ffee-4604-907e-643a8009b04e&limit=1000";
            //"https://dadesobertes.gva.es/es/api/3/action/datastore_search?resource_id=382b283c-03fa-433e-9967-9e064e84f936&limit=1000";
    //                                                                                  38e6d3ac-fd77-413e-be72-aed7fa6f13c2
            //https://dadesobertes.gva.es/api/3/action/package_show?id=38e6d3ac-fd77-413e-be72-aed7fa6f13c2
    public static String getJsonFromAssets(Context context, String fileName) {
        String jsonString;
        try {
            InputStream is = context.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            jsonString = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return jsonString;
    }

    public static String getJsonFromHttp(String url) {
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        String jsonString;
        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            //add request header
            con.setRequestProperty("user-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36");
            con.setRequestProperty("accept", "application/json;");
            con.setRequestProperty("accept-language", "es");
            con.connect();
            int responseCode = con.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                throw new IOException("HTTP error code: " + responseCode);
            }
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
            int n;
            while ((n = in.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
            in.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        jsonString = writer.toString();
        Log.d("Utils -> getJsonFromHttp", jsonString);
        return jsonString;
    }

    public static String getStringFromDate(Date date) {
        return new SimpleDateFormat("dd/MM/yyyy").format(date);
    }

    public static Date getDateFromString(String date) throws ParseException {
        return new SimpleDateFormat("dd/MM/yyyy").parse(date);
    }


}