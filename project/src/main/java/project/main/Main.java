package project.main;
import java.util.HashMap;
import static spark.Spark.*;

import project.logic.AuthenticationService;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import spark.ModelAndView;

public class Main {
    public static void main(String args[]){
        AuthenticationService auth = new AuthenticationService();

        port(5000);
        get("/", (req,res) -> {
            HashMap map = new HashMap<>();
            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());
        get("/list", (req,res) -> "Recommendation list");

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
