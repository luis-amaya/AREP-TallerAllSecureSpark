package co.edu.escuelaing.securityservice;

import static spark.Spark.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

public class HelloService {

    public static void main(String[] args) {
        secure("keystores/ecikeystore.p12", "123456", null, null);
        port(getPort());
        get("/", (req, res) -> "Hello World!");
        get("/service", "application/json", ((req, res) -> {
            res.type("application/json");
            return getLogs();
        }));
    }

    private static Object getLogs() throws IOException {
        URL url = new URL(
                "https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=IBM&interval=5min&apikey=demo");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        JSONObject resp = getJsonObject(connection.getResponseCode(), connection.getInputStream());
        connection.disconnect();
        return resp;
    }

    private static JSONObject getJsonObject(int responseCode, InputStream inputStream) throws IOException {
        JSONObject json;
        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            json = new JSONObject(response.toString());
        } else
            throw new IOException();
        return json;
    }

    public static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 7654;
    }

}
