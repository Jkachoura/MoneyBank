import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.io.IOUtils;

public class Post_JSON {
    public static void main(String[] args) {
        Post_JSON.GET_POST_JSON();
    }

    public static void GET_POST_JSON() {

    }

    public static String getBalance(String IBAN, int pinCode) {
        String query_url = "http://145.24.222.84:8000/balance";
        String json = "{ \"IBAN\" : \"" + IBAN + "\", \"Pincode\" : " + pinCode + " }";
        try {
            //server Connectie
            URL url = new URL(query_url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("GET");
            OutputStream os = conn.getOutputStream();
            os.write(json.getBytes("UTF-8"));
            os.close();

            // response reading
            int code = conn.getResponseCode();
            InputStream in = null;
            if (code == 200) {
                in = new BufferedInputStream(conn.getInputStream());
            } else {
                in = conn.getErrorStream();
            }
            String result = IOUtils.toString(in, "UTF-8");
            System.out.println("result: " + result + "code: " + code);
            in.close();
            conn.disconnect();

            switch (code) {
                case 200:
                    return "Succes" + result; //Succes
                case 404:
                    return "Account does not exist";
                case 401:
                    return "Incorrect" + result;
                case 403:
                    return "Blocked";
                case 400:
                    return "Wrong body";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String withdraw(String IBAN, int amount, int pinCode) {
        String query_url = "http://145.24.222.84:8000/withdraw";
        String json = "{ \"IBAN\" : \"" + IBAN + "\", \"Pincode\" : " + pinCode + ", \"Amount\" : " + amount + " }";
        try {
            //server Connectie
            URL url = new URL(query_url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("GET");
            OutputStream os = conn.getOutputStream();
            os.write(json.getBytes("UTF-8"));
            os.close();

            // response reading
            int code = conn.getResponseCode();
            InputStream in = null;
            if (code == 200) {
                in = new BufferedInputStream(conn.getInputStream());
            } else {
                in = conn.getErrorStream();
            }
            String result = IOUtils.toString(in, "UTF-8");
            System.out.println("result: " + result + "code: " + code);
            in.close();
            conn.disconnect();

            switch (code) {
                case 200:
                    return "Succes" + result; //Succes
                case 404:
                    return "Account does not exist";
                case 401:
                    return "Incorrect" + result;
                case 403:
                    return "Blocked";
                case 400:
                    return "Wrong body";
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
}
