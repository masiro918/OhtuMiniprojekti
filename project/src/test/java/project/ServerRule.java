package project;

import project.main.Main;

import org.junit.rules.ExternalResource;
import spark.Spark;

import project.db.UserDAO;
import project.db.ReadingRecommendationDAO;

import project.domain.User;
import project.domain.ReadingRecommendation;
import project.domain.BlogRecommendation;
import project.domain.BookRecommendation;


public class ServerRule extends ExternalResource {
    
    private final int port;

    public ServerRule(int port) {
        this.port = port;
    }

    @Override
    protected void before() throws Throwable {
        Spark.port(port);

        UserDAO users = new FakeUserDAO();
        ReadingRecommendationDAO recommendations = new FakeReadingRecommendationDAO();

        //Users for testing
        User user1 = new User("tester", "salasana123");
        users.add(user1);

        //ReadingRecommendations for testing
        ReadingRecommendation blog = new BlogRecommendation("Blog 1", "blog", "urli");
        ReadingRecommendation book = new BookRecommendation("Book 1", "book", "Writer");
        recommendations.add(blog);
        recommendations.add(book);


        //Need to add testing daos to the main
        // TODO
        //Main.setUserDao(users);
        
        
        Main.main(null);
    }

    @Override
    protected void after() {
        Spark.stop();
    }

}
