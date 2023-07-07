package algonquin.cst2335.chan0527;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import algonquin.cst2335.chan0527.databinding.DetailsLayoutBinding;

public class MessageDetailsFragment extends Fragment {
    DetailsLayoutBinding binding;
    ChatMessage selected;

    public MessageDetailsFragment(ChatMessage m){
        selected = m;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = DetailsLayoutBinding.inflate(inflater);

        binding.messageText.setText(selected.getMessage());
        binding.timeText.setText(selected.getTimeSent());
        binding.sendOrReceive.setText("is Send or Receive ? (Send = 0; Receive = 1):  "+ selected.getIsSentButton());
        binding.databaseText.setText("Id = "+ selected.id);
        return binding.getRoot();

    }
}
