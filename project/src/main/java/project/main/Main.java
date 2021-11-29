package project.main;
import java.util.HashMap;
import project.db.SQLUserDAO;
import static spark.Spark.*;

import project.logic.ReadingRecommendationService;
import project.logic.AuthenticationService;

import project.db.SQLReadingDAO;
import project.domain.User;

import spark.template.thymeleaf.ThymeleafTemplateEngine;
import spark.ModelAndView;

public class Main {
    public static void main(String args[]){
        AuthenticationService auth = new AuthenticationService(new SQLUserDAO());

        port(5000);
        get("/", (req,res) -> new ModelAndView(new HashMap<>(), "index"), new ThymeleafTemplateEngine());
        get("/list", (req,res) -> {
            ReadingRecommendationService recommend = new ReadingRecommendationService(new User("change", "this"),
                                                     new SQLReadingDAO()); //TEMP!
            HashMap map = new HashMap<>();
            map.put("recommendations", recommend.getAllRecommendations());
            return new ModelAndView(map, "list");
        }, new ThymeleafTemplateEngine());

        get("/login", (req,res) -> new ModelAndView(new HashMap<>(), "login"), new ThymeleafTemplateEngine());
        post("/login", (req, res) -> {
            if (auth.login(
                req.queryParamOrDefault("username", null),
                req.queryParamOrDefault("password", null)
            ) == null) {
                return "{\"message\":\"Wrong username or password\"}";
            }
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
    }
}
