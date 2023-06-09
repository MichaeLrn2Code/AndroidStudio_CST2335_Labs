package algonquin.cst2335.chan0527;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import algonquin.cst2335.chan0527.databinding.ActivityMainBinding;
public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding varBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        varBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(varBinding.getRoot());

        //save the login email address that the user types in so that the next time they run the application,
        // the email address can be pre-filled from what was saved the previous time
        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        //get the value from MyData if anything is saved using a variable name "Email", if not exist return null
        String emailAddress = prefs.getString("Email", "");

        //set the text of the EditText to be the emailAddress variable that was just loaded.
        varBinding.emailText.setText(emailAddress);

        varBinding.loginButton.setOnClickListener( clk-> {
            Intent nextPage = new Intent(MainActivity.this, SecondActivity.class);

            nextPage.putExtra("EmailAddress",varBinding.emailText.getText().toString());

            //set the input email address in MyData object
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("Email", varBinding.emailText.getText().toString());

            editor.apply();

            startActivity(nextPage);
        } );



    }
}