package co.edu.escuelaing.securityprimerlive;

import static spark.Spark.*;

import spark.Request;
import spark.Response;

public class HelloService {

    private static SecureContext s;

    private static String url = "https://localhost:7654/service";

    public static void main(String[] args) {
        // API: secure(keystoreFilePath, keystorePassword,
        // truststoreFilePath,truststorePassword);
        s = SecureContext.initSecureContext();
        s.setUser("luis");
        s.setPassw("12345");
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

    public static Object serverRedirect(Request req, Response res) {
        String user = req.queryParams("user");
        String passwd = req.queryParams("passw");
        if (s.validate(user, passwd)) {
            res.redirect(url);
            return null;
        } else {
            res.redirect("error.html");
            return null;
        }
    };

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
