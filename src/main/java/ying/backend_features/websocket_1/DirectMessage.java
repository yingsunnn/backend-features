package ying.backend_features.websocket_1;

/**
 * Created by ying on 2017-04-16.
 */
public class DirectMessage {

    private long id;
    private String message;
    private long userId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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
        str.append("Id: ").append(getId())
                .append(" User: ").append(getUserId())
                .append(" Message: ").append(getMessage());

        return str.toString();
    }
}
