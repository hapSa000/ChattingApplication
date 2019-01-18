package in.test.com.chattingapplication;

public class MessagePojo {
    private String message;
    private String senderId;
    private String ReciverId;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReciverId() {
        return ReciverId;
    }

    public void setReciverId(String reciverId) {
        ReciverId = reciverId;
    }
}
