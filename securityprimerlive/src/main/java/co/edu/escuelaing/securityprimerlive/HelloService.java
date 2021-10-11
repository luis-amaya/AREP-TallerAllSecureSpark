package co.edu.escuelaing.securityprimerlive;

import static spark.Spark.*;

import spark.Request;
import spark.Response;
import org.json.JSONObject;

public class HelloService {

    private static SecureContext s;

    private static URLReader urlReader;

    private static String url = "https://localhost:7654/service";

    public static void main(String[] args) {
        urlReader = URLReader.startURLOperation();
        s = SecureContext.initSecureContext();
        s.setUser("luis", "12345");
        staticFiles.location("/public");
        secure("keystores/ecikeystore.p12", "123456", null, null);
        port(getPort());
        get("/hello", (req, res) -> "Hello World!");
        get("/", "text/html", (req, res) -> {
            res.redirect("index.html");
            return null;
        });
        get("/service", "application/json", (req, res) -> serverRedirect(req, res));
        get("/setURL", (req, res) -> setURL(req, res));
    }

    public static Object serverRedirect(Request requ, Response resp) {
        String user = requ.queryParams("user");
        String passwd = requ.queryParams("passw");
        if (s.validate(user, passwd)) {
            get("/response", "application/json", (req, res) -> {
                res.type("application/json");
                return urlReader.redirectToPage(resp, url);
            });
            resp.redirect("/response");
            return urlReader.redirectToPage(resp, url);
        } else {
            resp.redirect("error.html");
            return null;
        }
    }

    public static Object setURL(Request req, Response res) {
        String newURL = req.queryParams("url");
        url = newURL;
        System.out.println(url);
        res.redirect("index.html");
        return null;
    }

    public static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 4567;
    }
}
