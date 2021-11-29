package project.main;
import java.util.HashMap;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import spark.ModelAndView;

public class Main {
    public static void main(String args[]){
        port(5000);
        get("/", (req,res) -> {
            HashMap map = new HashMap<>();
            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());
        get("/list", (req,res) -> "Recommendation list");
        get("/login", (req,res) -> {
            return new ModelAndView(new HashMap<>(), "login");
        }, new ThymeleafTemplateEngine());
        get("/signup", (req,res) -> {
            return new ModelAndView(new HashMap<>(), "signup");
        }, new ThymeleafTemplateEngine());
        get("/post", (req,res) -> "Post a new recommendation");
    }
}
