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

public class Main {
    public static void main(String args[]) throws Exception{
        AuthenticationService auth = new AuthenticationService(new SQLUserDAO());
        SQLReadingDAO reader = new SQLReadingDAO();
        TableCreator tc = new TableCreator();
        

        port(5000);
        get("/", (req,res) -> new ModelAndView(new HashMap<>(), "index"), new ThymeleafTemplateEngine());
        get("/list", (req,res) -> {
            // Proof of concept. Poistetaan, kun blogien hakeminen tietokannasta onnistuu.
            try {
                tc.createReadingRecommendations();
            } catch (Exception ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            BlogRecommendation testBlog1 = new BlogRecommendation("Blog 1", "Blog", "https://test.blog.com");
            BlogRecommendation testBlog2 = new BlogRecommendation("Blog 2", "Blog", "https://test.blog2.org");
            
            try {
                reader.addBlog(testBlog1);
            } catch (Exception ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                reader.addBlog(testBlog2);
            } catch (Exception ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            ArrayList<ReadingRecommendationInterface> readingList = new ArrayList<ReadingRecommendationInterface>();
            try {
                readingList = reader.loadAll();
            } catch (Exception ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            HashMap map = new HashMap<>();
            map.put("recommendations", readingList);
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
    }
}
