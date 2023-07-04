package algonquin.cst2335.chan0527;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity // A table called ChatMessage
public class ChatMessage {
    @PrimaryKey(autoGenerate = true) // id is primary key, the database will increment ids
    @ColumnInfo(name = "ID")
    long id;
    @ColumnInfo(name = "MessageColumn")
    String message;
    @ColumnInfo(name = "TimeSentColumn")
    String timeSent;
    @ColumnInfo(name = "SendReceiveColumn")
    int isSentButton;

    public ChatMessage(){};

    public ChatMessage(String m, String t, int sent){
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

    public int getIsSentButton(){
        return isSentButton;
    }
}
