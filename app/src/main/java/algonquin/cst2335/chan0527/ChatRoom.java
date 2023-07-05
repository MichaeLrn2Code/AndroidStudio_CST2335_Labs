package algonquin.cst2335.chan0527;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.chan0527.databinding.ActivityChatRoomBinding;
import algonquin.cst2335.chan0527.databinding.ReceiveMessageBinding;
import algonquin.cst2335.chan0527.databinding.SentMessageBinding;

public class ChatRoom extends AppCompatActivity {

    ActivityChatRoomBinding binding;

    ArrayList<ChatMessage> messagesList = new ArrayList<>();

    ChatRoomViewModel chatModel;
    private RecyclerView.Adapter myAdapter;
    EditText chatroomText;

    ChatMessageDAO myDAO;

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

            binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());
            chatroomText = binding.chatroomText;
            recyclerView = binding.recycleView;
            chatModel = new ViewModelProvider(this).get(ChatRoomViewModel.class);
            messagesList = chatModel.messages.getValue();

            // Access the database:
            MessageDatabase db = Room.databaseBuilder(getApplicationContext(), MessageDatabase.class, "MyChatMessageDatabase").build(); //MyChatMessageDatabase is the actual file name in local device
            myDAO = db.getDAO();

            if(messagesList == null){
            chatModel.messages.postValue(messagesList = new ArrayList<ChatMessage>());

                // get all messages:
                Executor thread = Executors.newSingleThreadExecutor();
                thread.execute(()-> {
                    List<ChatMessage> fromDatabase = myDAO.getAllMessages();
                    messagesList.addAll(fromDatabase); // add previous messages
                    runOnUiThread(()->{
                        binding.recycleView.setAdapter(myAdapter);
                    });
                });
            }


        myAdapter = new RecyclerView.Adapter<MyRowHolder>() {
            @NonNull
            @Override
               /* This function creates a ViewHolder object representing a single row in the list.
                    int viewType parameter tells what kind of viewType should be loaded. By default is 0
                */
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                // inflate the row layout
                if (viewType==0) {
                    SentMessageBinding bindingSend =
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
                // retrieve the object that goes in row "position" in this list, override the text in the rows:
                ChatMessage obj = messagesList.get(position);
                holder.messageText.setText(obj.getMessage());// put the string in position at messageText TextView
                holder.timeText.setText(obj.getTimeSent());
            }

            @Override
//          This function just returns an int specifying how many items to draw.
            public int getItemCount() {
                return messagesList.size();
            }

            // Returns a viewType number which gets passed into the onCreateViewHolder(ViewGroup parent, int viewType) function.
            @Override
            public int getItemViewType(int position){
                // which layout to use for object at position?
                ChatMessage chatMsg = messagesList.get(position);
                if(chatMsg.getIsSentButton()==0){
                    return 0;
                }else return 1;
            }
        };

            // onclickListener for send button
            binding.sendBtn.setOnClickListener(clk->{
                String input = chatroomText.getText().toString();
                int type = 0;
                SimpleDateFormat sdf = new SimpleDateFormat("EE, dd-MM-yyy hh-mm-ss a");
                String currentDateandTime = sdf.format(new Date());
                ChatMessage newMessage = new ChatMessage(input,currentDateandTime,type);

                messagesList.add(newMessage);

                //insert into database
                Executor thread1 = Executors.newSingleThreadExecutor();
                thread1.execute(new Runnable() {
                    @Override
                    public void run() {
                        // run on a second processor:
                        newMessage.id = myDAO.insertMessage(newMessage);//<--- returns the id
                    }
                });
                /* notifyItemInserted(int position) function tells the Adapter which row has to be redrawn,
                whenever the ArrayList changes, have to notify the Adapter object that something has been inserted, or deleted.
                Since the new message adds to the back of the ArrayList, the row that needs updating is messages.size()-1
                 */
                myAdapter.notifyItemInserted(messagesList.size()-1); // update the row
                //clear the previous text
                chatroomText.setText("");
            });

        // onclickListener for receive button
            binding.receiveBtn.setOnClickListener(clk->{
                String input = chatroomText.getText().toString();
                int type = 1;
                SimpleDateFormat sdf = new SimpleDateFormat("EE, dd-MM-yyy hh-mm-ss a");
                String currentDateandTime = sdf.format(new Date());
                ChatMessage newMessage = new ChatMessage(input,currentDateandTime,type);

                messagesList.add(newMessage);

                Executor thread1 = Executors.newSingleThreadExecutor();
                thread1.execute(()->{
                    newMessage.id = myDAO.insertMessage(newMessage);
                });

                /* notifyItemInserted(int position) function tells the Adapter which row has to be redrawn,
                whenever the ArrayList changes, have to notify the Adapter object that something has been inserted, or deleted.
                Since the new message adds to the back of the ArrayList, the row that needs updating is messages.size()-1
                 */
                myAdapter.notifyItemInserted(messagesList.size()-1); // redraw the missing row
                // clear the previous text
                binding.chatroomText.setText("");
            });

//        Once RecycleView is loaded, the only thing needed to do is call setAdapter( )
            binding.recycleView.setAdapter(myAdapter);

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

            itemView.setOnClickListener(click->{
                AlertDialog.Builder builder = new AlertDialog.Builder( ChatRoom.this );
                builder.setMessage("Do you want to delete the message: "+ messageText.getText())
                        .setTitle("Question")
                //Show "Yes / No" button, if click "No" nothing happened, while click "Yes" delete the message row and from database
                        .setNegativeButton("No", ((dialog, clk) ->{} ))
                        .setPositiveButton("Yes",((dialog, clk) ->{
                            //delete this row
                            int whichRowClicked = getAbsoluteAdapterPosition();
                            ChatMessage cm = messagesList.get(whichRowClicked);

                            //delete from database
                            Executor thread = Executors.newSingleThreadExecutor();
                            thread.execute(()->{
                                // Background thread
                                myDAO.deleteMessage(cm);
                                messagesList.remove(whichRowClicked);

                                runOnUiThread(()->
                                        // On UI thread update the recyclerView
                                        myAdapter.notifyItemRemoved(whichRowClicked));
                            });

                            Snackbar.make(recyclerView,"Message was deleted", Snackbar.LENGTH_LONG )
                                    .setAction("Undo", clk2->{
                                        //reinsert the message:
                                        Executor thrd = Executors.newSingleThreadExecutor();
                                        thrd.execute(()->{ myDAO.insertMessage(cm); });
                                        messagesList.add(whichRowClicked,cm);
                                        runOnUiThread(()->myAdapter.notifyDataSetChanged());
                                    })
                                    .show(); // show snackbar

                        } ))
                        //show the window:
                        .create().show();

            });
        }
    }

}