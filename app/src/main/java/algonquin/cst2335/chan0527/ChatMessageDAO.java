package algonquin.cst2335.chan0527;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ChatMessageDAO {

    //insertion, database returns id as long
    @Insert
    long insertMessage(ChatMessage cm);

    // query, get all from database:
    @Query("Select * from ChatMessage") // Table name is @Entity
    List<ChatMessage> getAllMessages();

    //deletion, number of rows deleted, should be 1 if id matches
    @Delete
    int deleteMessage(ChatMessage cm);

}
