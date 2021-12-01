package project.domain;

/**
 * A comment that can be added for a reading recommendation.
 */
public class Comment {

    private String comment;

    public Comment(String c) {
        this.comment = c;
    }

    /**
     * Replaces the content of the comment.
     * 
     * @param new content for comment
     */
    public void modify(String newComment) {
        this.comment = newComment;
    }
    
    /**
     * Returns the content of the comment.
     * 
     * @return the content of the comment
     */
    public String getContent() {
        return this.comment;
    }
}
