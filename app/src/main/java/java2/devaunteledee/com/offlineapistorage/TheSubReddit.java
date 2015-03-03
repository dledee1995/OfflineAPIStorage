package java2.devaunteledee.com.offlineapistorage;

import java.io.Serializable;

/**
 * Created by devaunteledee on 3/2/15.
 */
public class TheSubReddit implements Serializable {
    String title;
    String Author;

    public TheSubReddit(String title, String author) {
        this.title = title;
        Author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return Author;
    }

    public void setAuthor(String author) {
        Author = author;
    }

    @Override
    public String toString() {
        return "TheSubReddit{" +
                "title='" + title + '\'' +
                ", Author='" + Author + '\'' +
                '}';
    }
}
