package algonquin.cst2335.chan0527;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {ChatMessage.class}, version=1)
public abstract class MessageDatabase extends RoomDatabase {

    // Which DAO do we use for this database:
    abstract ChatMessageDAO getDAO();


}
