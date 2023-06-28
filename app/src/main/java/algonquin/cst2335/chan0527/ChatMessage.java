package algonquin.cst2335.chan0527;

public class ChatMessage {
    String message;
    String timeSent;
    boolean isSentButton;

    ChatMessage(String m, String t, boolean sent){
        this.message = m;
        this.timeSent = t;
        this.isSentButton = sent;
    }

    public String getMessage(){
        return message;
    }

    public String getTimeSent(){
        return timeSent;
    }

    public boolean getIsSentButton(){
        return isSentButton;
    }
}
