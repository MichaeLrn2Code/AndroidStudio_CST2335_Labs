package algonquin.cst2335.chan0527;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import algonquin.cst2335.chan0527.databinding.ActivityChatRoomBinding;
import algonquin.cst2335.chan0527.databinding.ReceiveMessageBinding;
import algonquin.cst2335.chan0527.databinding.SentMessageBinding;

public class ChatRoom extends AppCompatActivity {

    ActivityChatRoomBinding binding;

    ArrayList<ChatMessage> messages = new ArrayList<>();

    ChatRoomViewModel chatModel;
    private RecyclerView.Adapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        chatModel = new ViewModelProvider(this).get(ChatRoomViewModel.class);
        messages = chatModel.messages.getValue();

        if(messages == null){
            chatModel.messages.postValue(messages = new ArrayList<ChatMessage>());
        }

            binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());

            // onclickListener for send button
            binding.sendBtn.setOnClickListener(clk->{
//                messages.add(binding.chatroomText.getText().toString());

                SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MM-yyy hh-mm-ss a");
                String currentDateandTime = sdf.format(new Date());
                messages.add(new ChatMessage(binding.chatroomText.getText().toString(),currentDateandTime,true));

                /* notifyItemInserted(int position) function tells the Adapter which row has to be redrawn,
                whenever the ArrayList changes, have to notify the Adapter object that something has been inserted, or deleted.
                Since the new message adds to the back of the ArrayList, the row that needs updating is messages.size()-1
                 */
                myAdapter.notifyItemInserted(messages.size()-1);
                //clear the previous text
                binding.chatroomText.setText("");
            });

            binding.receiveBtn.setOnClickListener(clk->{
                SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MM-yyy hh-mm-ss a");
                String currentDateandTime = sdf.format(new Date());
                messages.add(new ChatMessage(binding.chatroomText.getText().toString(),currentDateandTime,false));

                /* notifyItemInserted(int position) function tells the Adapter which row has to be redrawn,
                whenever the ArrayList changes, have to notify the Adapter object that something has been inserted, or deleted.
                Since the new message adds to the back of the ArrayList, the row that needs updating is messages.size()-1
                 */
                myAdapter.notifyItemInserted(messages.size()-1); // redraw the missing row
                // clear the previous text
                binding.chatroomText.setText("");
            });

//        Once RecycleView is loaded, the only thing needed to do is call setAdapter( )
            binding.recycleView.setAdapter(myAdapter = new RecyclerView.Adapter<MyRowHolder>() {
                @NonNull
                @Override
               /* This function creates a ViewHolder object representing a single row in the list.
                    int viewType parameter tells what kind of viewType should be loaded. By default is 0
                */
                public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    // inflate the row layout
                    if (viewType==0) {
                        SentMessageBinding bindingSend =            //how big is parent?
                                SentMessageBinding.inflate(getLayoutInflater(),parent,false);
                        return new MyRowHolder(bindingSend.getRoot());
                    }else {
                        ReceiveMessageBinding bindingReceive = ReceiveMessageBinding.inflate(getLayoutInflater(),parent,false);

                        // this will initialize the row variables:
                        return new MyRowHolder(bindingReceive.getRoot());
                    }
                }

                @Override
//                This initializes a ViewHolder to go at the row specified by the position parameter.
                public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                    // this initialize the row to data

                    // retrieve the object that goes in row "position" in this list
                    // override the text in the rows:
                    ChatMessage obj = messages.get(position);
                    holder.messageText.setText(obj.getMessage());// put the string in position at messageText TextView
                    holder.timeText.setText(obj.getTimeSent());
                }

                @Override
//                This function just returns an int specifying how many items to draw.
                // the number of items
                public int getItemCount() {
                    return messages.size();
                }

//                This function returns a viewType number which gets passed into the onCreateViewHolder(ViewGroup parent, int viewType) function.
                @Override
                public int getItemViewType(int position){
                    // which layout to use for object at position?
                    ChatMessage chatMsg = messages.get(position);
                    if(chatMsg.getIsSentButton()==true){
                        return 0;
                    }else return 1;
                }

            });

          /*
         The RecyclerView supports 1 or more columns for showing data, can either scroll in a Vertical or Horizontal direction through the items.
         To specify a single column scrolling in a Vertical direction call:
         */
        binding.recycleView.setLayoutManager(new LinearLayoutManager(this));
    }

    //an object for representing everything that goes on a row in the list
    //maintain variables for what you want to set on each row in your list.
    public class MyRowHolder extends RecyclerView.ViewHolder{

        TextView messageText;
        TextView timeText;

        public MyRowHolder(@NonNull View itemView){
            super(itemView);
            messageText = itemView.findViewById(R.id.message);
            timeText = itemView.findViewById(R.id.time);
        }
    }

}