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
import project.domain.BookRecommendation;
import project.domain.User;
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

        //User story testejä varten
        if(recommendationsDao == null) {
            recommendationsDao = new SQLReadingDAO();
        }


        recService = new ReadingRecommendationService(null, recommendationsDao);
        TableCreator tc = new TableCreator();
        tc.createUser();
        tc.createReadingRecommendations();
        tc.createTags();
        tc.createRelatedCourses();
        
        port(getHerokuAssignedPort());

        get("/", (req,res) -> new ModelAndView(new HashMap<>(), "index"), new ThymeleafTemplateEngine());
        get("/list", (req,res) -> {
            HashMap map = new HashMap<>();
            map.put("recommendations", recService.loadRecommendations());
            return new ModelAndView(map, "list");
        }, new ThymeleafTemplateEngine());

        get("/login", (req,res) -> new ModelAndView(new HashMap<>(), "login"), new ThymeleafTemplateEngine());
        post("/login", (req, res) -> {
            String user = req.queryParamOrDefault("username", null);
            String pass = req.queryParamOrDefault("password", null);
            User loggedIn = (User) auth.login(user, pass);
            if (loggedIn == null) {
                return "{\"message\":\"Wrong username or password\"}";
            }
            recService.setUser(loggedIn);
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
                res.redirect("/");
           }
            return "{\"message\":\"Failure: " + auth.getErrorMessages() + "\"}";
        });

        get("/post", (req, res) -> {
            String cookie = req.cookie("login");
            if (cookie != null) {
                return new ModelAndView(new HashMap<>(), "post");
            }
            return new ModelAndView(new HashMap<>(), "index");
        }, new ThymeleafTemplateEngine());

        get("/post/blog", (req,res) -> {
            String cookie = req.cookie("login");
            if (cookie != null) {
                return new ModelAndView(new HashMap<>(), "postblog");
            }
            return new ModelAndView(new HashMap<>(), "index");
        }, new ThymeleafTemplateEngine());
        
        get("/post/book", (req,res) -> {
            String cookie = req.cookie("login");
            if (cookie != null) {
                return new ModelAndView(new HashMap<>(), "postbook");
            }
            return new ModelAndView(new HashMap<>(), "index");
        }, new ThymeleafTemplateEngine());

        post("/post", (req,res) -> {
            String cookie = req.cookie("login");
            if (cookie == null) {
                return new ModelAndView(new HashMap<>(), "index");
            }
            String type = req.queryParamOrDefault("type", null);
            if (type == null) {
                return "{\"message\":\"Failure\"}";
            }
            HashMap<String, String> info = new HashMap<>();
            info.put("type", req.queryParamOrDefault("type", null));
            info.put("headline", req.queryParamOrDefault("headline", null));
            info.put("writer", req.queryParamOrDefault("writer", null));
            if (type.equals("blog")) {
                info.put("url", req.queryParamOrDefault("url", null));
            } else if (type.equals("book")) {
                info.put("ISBN", req.queryParamOrDefault("ISBN", null));
            }
            info.put("tags", req.queryParamOrDefault("tags", null));
            info.put("courses", req.queryParamOrDefault("related_courses", null));
            if (recService.createRecommendation(info)) {
                return "{\"message\":\"Success\"}";
            }
            return "{\"message\":\"Something went wrong.\"}";
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
            BlogRecommendation blog = recService.findBlogId(id);
            HashMap map = new HashMap<>();
            map.put("blog", blog);
            return new ModelAndView(map, "blog");
        }, new ThymeleafTemplateEngine());
        
        get("/list/book/:id", (req,res) -> {
            int id = Integer.parseInt(req.params(":id"));
            BookRecommendation book = recService.findBookId(id);
            HashMap map = new HashMap<>();
            map.put("book", book);
            return new ModelAndView(map, "book");
        }, new ThymeleafTemplateEngine());

        get("/search", (req,res) -> {
            String cookie = req.cookie("login");
            if (cookie != null) {
                return new ModelAndView(new HashMap<>(), "search");
            }
            return new ModelAndView(new HashMap<>(), "index");
        }, new ThymeleafTemplateEngine());
        post("/search", (req,res) -> {
            String cookie = req.cookie("login");
            if (cookie == null) {
                return new ModelAndView(new HashMap<>(), "index");
            }
            String writer = req.queryParamOrDefault("writer", null);
            if (writer == null) {
                return null;//"{\"message\":\"Failure\"}";
            }
            ArrayList<Integer> indexes = recService.findIndexesByWriter(writer);
            if (indexes.isEmpty()) {
                return null;//"{\"message\":\"Found nothing\"}";
            }
            HashMap map = new HashMap<>();
            map.put("recommendations", recService.findRecommendationsByIDs(indexes));
            return new ModelAndView(map, "list");
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
