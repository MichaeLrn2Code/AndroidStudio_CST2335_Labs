package algonquin.cst2335.chan0527;

import static java.lang.Character.isDigit;
import static java.lang.Character.isLowerCase;
import static java.lang.Character.isUpperCase;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

/**
 * A page prompt user input password, and software validates the password.
 * @author Wai Wai Chan
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {

    /**
     * This holds the text at the center of the screen
     */
    TextView tv = null;
    /**
     * This holds the edit text at the below the text view
     */
    EditText et = null;

    /**
     * This holds the button on the bottom of the screen
     */
    Button btn = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         tv = findViewById(R.id.textView);
         et = findViewById(R.id.editText);
         btn = findViewById(R.id.button);

        btn.setOnClickListener(clk->{
            String pwd = et.getText().toString();

            if (checkPasswordComplexity(pwd)){
                tv.setText("Your Password meets the requirements");
            }else tv.setText("You shall not pass!");
        });
    }

    /**
     * Function to validate the input password
     * @param pwd the password user input
     * @return true or false if input password passes the validation
     */
    boolean checkPasswordComplexity(String pwd){
        boolean foundUpperCase, foundLowerCase, foundNumber, foundSpecial;
        foundUpperCase = foundLowerCase = foundNumber = foundSpecial = false;

        char[] pwdToChar = pwd.toCharArray();

        for (char c:pwdToChar){
            if (isDigit(c)){
                foundNumber = true;
            }

            if (isUpperCase(c)){
                foundUpperCase = true;
            }

            if (isLowerCase(c)){
                foundLowerCase = true;
            }

            if (isSpecialCharacter(c)){
                foundSpecial = true;
            }
        }

        if(!foundUpperCase)
        {
            CharSequence warning = "Password missing an Upper Case Letter";
            int duration = Toast.LENGTH_SHORT;
            Toast.makeText(this,warning,duration ).show(); ;
            return false;
        }

        else if( ! foundLowerCase)
        {
            CharSequence warning = "Password missing a Lower Case Letter";
            int duration = Toast.LENGTH_SHORT;
            Toast.makeText(this,warning,duration ).show();
            return false;
        }
        else if( ! foundNumber) {
            CharSequence warning = "Password missing a Number";
            int duration = Toast.LENGTH_SHORT;
            Toast.makeText(this,warning,duration ).show();
            return false;
        }
        else if(! foundSpecial) {
            CharSequence warning = "Password missing a Special Character";
            int duration = Toast.LENGTH_SHORT;
            Toast.makeText(this,warning,duration ).show();
            return false;
        }
        else
            return true;
    }

    /**
     * Function to check if password contain a special character
     * @param c the character in password
     * @return true or false if password contain a special character
     */
    boolean isSpecialCharacter(char c) {
        switch (c){
            case '#':
            case '?':
            case '*':
            case '$':
            case '%':
            case '&':
            case '@':
            case '!':
            case '^':
                return true;
            default:
                return false;
        }
    }

}