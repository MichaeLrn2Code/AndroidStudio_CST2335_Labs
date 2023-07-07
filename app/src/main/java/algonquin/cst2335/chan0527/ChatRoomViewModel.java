package algonquin.cst2335.chan0527;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

// Use a ViewModel class here to make the data survive the rotation change
public class ChatRoomViewModel extends ViewModel {
    public MutableLiveData<ArrayList<ChatMessage>> messages = new MutableLiveData<>();

    public MutableLiveData<ChatMessage> selectedMessage = new MutableLiveData<>();



}
