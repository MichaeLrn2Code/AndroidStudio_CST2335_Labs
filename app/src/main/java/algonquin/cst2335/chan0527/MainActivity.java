package algonquin.cst2335.chan0527;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import algonquin.cst2335.chan0527.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding variableBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        variableBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(variableBinding.getRoot()); //load XML on screen

        TextView mytext = variableBinding.textview;
        Button btn = variableBinding.mybutton;
        EditText myedit = variableBinding.myedittext;
        String editString = myedit.getText().toString();
        btn.setOnClickListener((View v)->{
                mytext.setText( "Your edit text has: " + editString);
        });
    }


}