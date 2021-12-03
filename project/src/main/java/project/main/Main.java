package project.main;
import java.util.List;
import project.db.SQLUserDAO;
import static spark.Spark.*;

import project.logic.AuthenticationService;

import spark.template.thymeleaf.ThymeleafTemplateEngine;
import spark.ModelAndView;

// /list-sivun proof of concept. Poistetaan, kun blogien hakeminen tietokannasta onnistuu.
import project.db.SQLReadingDAO;
import project.domain.ReadingRecommendationInterface;
import project.domain.BlogRecommendation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import project.db.TableCreator;

import project.db.UserDAO;
import project.db.ReadingRecommendationDAO;
import project.logic.ReadingRecommendationService;

public class Main {

    private static UserDAO userDao;
    private static ReadingRecommendationDAO recommendationsDao;
    private static AuthenticationService auth;
    private static ReadingRecommendationService recService;

    public static void setUserDao(UserDAO u) {
        userDao = u;
    }

    public static void setRecommendationDao(ReadingRecommendationDAO r) {
        recommendationsDao = r;
    }

    // localhost:5000
    private static int defaultPort = 5000;

    public static void main(String args[]) throws Exception{

        if(userDao == null) {
            auth = new AuthenticationService(new SQLUserDAO());
        } else {
            //Tämä tulee käyttöön user story testeissä
            auth = new AuthenticationService(userDao);
        }
        recService = new ReadingRecommendationService(null, new SQLReadingDAO());
        TableCreator tc = new TableCreator();
        tc.createUser();
        
        port(getHerokuAssignedPort());

        get("/", (req,res) -> new ModelAndView(new HashMap<>(), "index"), new ThymeleafTemplateEngine());
        get("/list", (req,res) -> {
            // Proof of concept. Poistetaan, kun blogien hakeminen tietokannasta onnistuu.
            try {
                tc.createReadingRecommendations();
                tc.createCommments();
            } catch (Exception ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            HashMap<String,String> testBlog1 = new HashMap<>();
            testBlog1.put("headline", "Blog 1");
            testBlog1.put("type", "blog");
            testBlog1.put("url", "https://test.blog.com");
            testBlog1.put("writer", "Bob the Blogger");
            recService.createRecommendation(testBlog1);
            
            HashMap<String,String> testBlog2 = new HashMap<>();
            testBlog2.put("headline", "Blog 2");
            testBlog2.put("type", "blog");
            testBlog2.put("url", "https://test.blog2.org");
            testBlog2.put("writer", "Ann the Author");
            recService.createRecommendation(testBlog2);
            
            HashMap map = new HashMap<>();
            map.put("recommendations", recService.loadRecommendations());
            return new ModelAndView(map, "list");
        }, new ThymeleafTemplateEngine());

        get("/login", (req,res) -> new ModelAndView(new HashMap<>(), "login"), new ThymeleafTemplateEngine());
        post("/login", (req, res) -> {
            String user = req.queryParamOrDefault("username", null);
            String pass = req.queryParamOrDefault("password", null);
            if (auth.login(
                user,
                pass
            ) == null) {
                return "{\"message\":\"Wrong username or password\"}";
            }
            res.cookie("login", user);
            res.redirect(String.format("/%s/home", user));
            return "{\"message\":\"Success\"}";
            }
        );

        get("/signup", (req,res) -> new ModelAndView(new HashMap<>(), "signup"), new ThymeleafTemplateEngine());
        post("/signup", (req, res) -> {
           if (auth.createUser(
               req.queryParamOrDefault("username", null),
               req.queryParamOrDefault("password", null)
           )) {
               return "{\"message\":\"Success\"}";
           }
            return "{\"message\":\"Failure\"}";
        });

        get("/post", (req,res) -> "Post a new recommendation");

        get("/post/blog", (req,res) -> {
            // TODO: Cookie check
            return new ModelAndView(new HashMap<>(), "postblog");
        }, new ThymeleafTemplateEngine());

        post("/post", (req,res) -> {
            // TODO: Cookie check
            String type = req.queryParamOrDefault("type", null);
            if (type == null) {
                return "{\"message\":\"Failure\"}";
            }
            // TODO: Call ReadingRecommendationService add method
            return "{\"message\":\"Success\"}";
        });

        get("/:user/home", (req, res) -> {
            HashMap map = new HashMap<>();
            String user = req.params(":user");
            String cookie = req.cookie("login");
            if (cookie.equals(user)) {
                map.put("username", user);
                return new ModelAndView(map, "home");
            }
            return null;
        }, new ThymeleafTemplateEngine());

        get("/list/blog/:id", (req,res) -> {
            int id = Integer.parseInt(req.params(":id"));
            // Metodia toistaiseksi ei olemassa
            BlogRecommendation blog = recService.findBlogId(id);
            HashMap map = new HashMap<>();
            map.put("blog", blog);
            return new ModelAndView(map, "blog");
        }, new ThymeleafTemplateEngine());
    }

    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return defaultPort;
    }
}
