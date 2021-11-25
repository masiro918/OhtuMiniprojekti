
package project.domain;

public class Comment {
    private String comment;
    
    public Comment(String c) {
        this.comment = c;
    }
    
    public void modify(String newComment) {
        this.comment = newComment;
    }
    
    public String getComment() {
        return this.comment;
    }
}
