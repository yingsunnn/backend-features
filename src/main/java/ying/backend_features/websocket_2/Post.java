package ying.backend_features.websocket_2;

/**
 * Created by ying on 2017-04-16.
 */
public class Post {

    private long id;
    private String title;
    private String summary;
    private long userId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("title: ").append(getTitle())
                .append(" summary: ").append(getSummary());
        return str.toString();
    }
}
