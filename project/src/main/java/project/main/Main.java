package project.main;
import project.db.SQLUserDAO;
import static spark.Spark.*;

import project.logic.AuthenticationService;

import spark.template.thymeleaf.ThymeleafTemplateEngine;
import spark.ModelAndView;

import project.db.SQLReadingDAO;
import project.domain.BlogRecommendation;
import java.util.ArrayList;
import java.util.HashMap;
import project.db.TableCreator;

import project.db.UserDAO;
import project.db.ReadingRecommendationDAO;
import project.domain.BookRecommendation;
import project.domain.PodcastRecommendation;
import project.domain.User;
import project.logic.ReadingRecommendationService;
import spark.Spark;

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
        Spark.staticFiles.location("/static");
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
            String cookie = req.cookie("login");
            HashMap map = new HashMap<>();
            if (cookie == null) {
                return new ModelAndView(map, "index");
            }
            map.put("recommendations", recService.loadRecommendations());
            map.put("username", cookie);
            return new ModelAndView(map, "list");
        }, new ThymeleafTemplateEngine());

        get("/login", (req,res) -> new ModelAndView(new HashMap(), "login"), new ThymeleafTemplateEngine());
        post("/login", (req, res) -> {
            String user = req.queryParamOrDefault("username", null);
            String pass = req.queryParamOrDefault("password", null);
            User loggedIn = (User) auth.login(user, pass);
            HashMap map = new HashMap();
            if (loggedIn == null) {
                map.put("message", "Väärä käyttäjänimi tai salasana!");
                return new ModelAndView(map, "login");
            }
            recService.setUser(loggedIn);
            res.cookie("login", user);
            res.redirect(String.format("/%s/home", user));
            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());
        
        get("/signup", (req,res) -> new ModelAndView(new HashMap<>(), "signup"), new ThymeleafTemplateEngine());
        post("/signup", (req, res) -> {
           if (auth.createUser(
               req.queryParamOrDefault("username", null),
               req.queryParamOrDefault("password", null)
           )) {
                res.redirect("/");
           }
           HashMap map = new HashMap();
           map.put("message", "Käyttäjänimi on varattu tai salasana ei täytä kaikkia ehtoja!");
           return new ModelAndView(map, "signup");
        }, new ThymeleafTemplateEngine());

        get("/logout", (req, res) -> {
            String cookie = req.cookie("login");
            if (cookie != null) {
                res.removeCookie("login");
                res.redirect("/");
            } 
            HashMap map = new HashMap();
            map.put("message", "Evästettä ei löytynyt!");
            return new ModelAndView(map, "login");
        }, new ThymeleafTemplateEngine());

        get("/post", (req, res) -> {
            String cookie = req.cookie("login");
            if (cookie != null) {
                HashMap map = new HashMap();
                map.put("username", cookie);
                return new ModelAndView(map, "post");
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
        
        get("/post/podcast", (req,res) -> {
            String cookie = req.cookie("login");
            if (cookie != null) {
                return new ModelAndView(new HashMap<>(), "postpodcast");
            }
            return new ModelAndView(new HashMap<>(), "index");
        }, new ThymeleafTemplateEngine());

        post("/post", (req,res) -> {
            String cookie = req.cookie("login");
            HashMap map = new HashMap();
            if (cookie == null) {
                return new ModelAndView(map, "index");
            }
            String type = req.queryParamOrDefault("type", null);
            if (type == null) {
                map.put("message", "Vinkkauksen tyyppi on virheellinen");
                return new ModelAndView(map, "post");
            }
            HashMap<String, String> info = new HashMap<>();
            info.put("type", req.queryParamOrDefault("type", null));
            info.put("headline", req.queryParamOrDefault("headline", null));
            info.put("writer", req.queryParamOrDefault("writer", null));
            if (type.equals("blog")) {
                info.put("url", req.queryParamOrDefault("url", null));
            } else if (type.equals("book")) {
                info.put("ISBN", req.queryParamOrDefault("ISBN", null));
            } else if (type.equals("podcast")) {
                info.put("podcastName", req.queryParamOrDefault("name", null));
                info.put("description", req.queryParamOrDefault("description", null));
            }
            info.put("tags", req.queryParamOrDefault("tags", null));
            info.put("courses", req.queryParamOrDefault("related_courses", null));
            if (recService.createRecommendation(info)) {
                map.put("recommendations", recService.loadRecommendations());
                return new ModelAndView(map, "list");
            }
            map.put("message", "Jokin meni vikaan");
            return new ModelAndView(map, "post");
        }, new ThymeleafTemplateEngine());
        
        post("/addcomment/:id", (req,res) -> {
            String cookie = req.cookie("login");
            if (cookie == null) {
                return new ModelAndView(new HashMap<>(), "index");
            }
            int id = Integer.parseInt(req.params(":id"));
            HashMap<String, String> info = recService.findRecommendationById(id);
            
            String type = info.get("type");

            info.put("comment", req.queryParamOrDefault("comment", null));
            if (recService.addComment(info.get("comment"), id)) {
                //return "{\"message\":\"Success\"}";
                if (type.equals("book")) {
                    res.redirect("/list/book/" + id);
                } else if (type.equals("podcast")) {
                    res.redirect("/list/podcast/" + id);
                } else if (type.equals("blog")) {
                    res.redirect("/list/blog/" + id);
                }
            }

            HashMap map = new HashMap();
            map.put("message", "Something went wrong.");
            return new ModelAndView(map, "signup");
            //return "{\"message\":\"Something went wrong.\"}";
        });
        
        post("/delete/:id", (req,res) -> {
           String cookie = req.cookie("login");
            if (cookie == null) {
                return "{\"message\":\"No permission.\"}";
            }
            int id = Integer.parseInt(req.params(":id"));
            
            if (recService.removeRecommendation(id)) {
                res.redirect("/list");
                return "{\"message\":\"Success\"}";
            } else {
                return "{\"message\":\"Something went wrong.\"}";
            }
        });

        get("/:user/home", (req, res) -> {
            HashMap map = new HashMap<>();
            String user = req.params(":user");
            String cookie = req.cookie("login");
            if (cookie.equals(user)) {
                map.put("username", user);
                return new ModelAndView(map, "home");
            }
            return new ModelAndView(new HashMap<>(), "home");
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
        
        get("/list/podcast/:id", (req,res) -> {
            int id = Integer.parseInt(req.params(":id"));
            PodcastRecommendation podcast = recService.findPodcastId(id);
            HashMap map = new HashMap();
            map.put("podcast", podcast);
            return new ModelAndView(map, "podcast");
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
            String headline = req.queryParamOrDefault("headline", null);
            HashMap map = new HashMap();
            if (writer == null && headline == null) {
                map.put("message", "Ei tuloksia.");
                return new ModelAndView(map, "search");
            }
            ArrayList<Integer> indexes = new ArrayList<>();
            if (headline == null) {
                indexes = recService.findIndexesByWriter(writer);
                if (indexes.isEmpty()) {
                    map.put("message", "Ei tuloksia.");
                    return new ModelAndView(map, "search");
                }
                map.put("recommendations", recService.findRecommendationsByIDs(indexes));
            } else {
                map.put("recommendations", recService.findRecommendationsByApproximateHeadline(headline));
            }
            
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
