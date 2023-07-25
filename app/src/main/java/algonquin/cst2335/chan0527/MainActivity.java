package algonquin.cst2335.chan0527;

import static java.lang.Character.isDigit;
import static java.lang.Character.isLowerCase;
import static java.lang.Character.isUpperCase;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;

import algonquin.cst2335.chan0527.databinding.ActivityMainBinding;

/**
 * A page prompt user input password, and software validates the password.
 * @author Wai Wai Chan
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    protected String cityName;
    EditText cityNameInput = null;
    Button forecastBtn = null;
    RequestQueue queue = null;

    TextView temp;
    TextView maxTemp;
    TextView minTemp;
    TextView humid;
    ImageView weatherIcon;
    TextView desc;
    Bitmap image = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        cityNameInput = binding.cityTextField;
        forecastBtn = binding.forecastBtn;
        temp = binding.temp;
        maxTemp = binding.maxTemp;
        minTemp = binding.minTemp;
        humid = binding.humidity;
        desc = binding.description;
        weatherIcon = binding.icon;

        queue = Volley.newRequestQueue(this);

        forecastBtn.setOnClickListener(clk->{
            cityName = cityNameInput.getText().toString();
            String apiKey = "7e943c97096a9784391a981c4d878b22";
            String url="https://api.openweathermap.org/data/2.5/weather?q="
                    + URLEncoder.encode(cityName)
                    +"&appid="+ apiKey+ "&units=metric";

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                    (response)->{
                        try {
                            JSONObject mainObject = response.getJSONObject( "main" );
                            double current = mainObject.getDouble("temp");
                            double min = mainObject.getDouble("temp_min");
                            double max = mainObject.getDouble("temp_max");
                            int humidity = mainObject.getInt("humidity");
                            JSONArray weather = response.getJSONArray("weather");
                            JSONObject obj = weather.getJSONObject(0);
                            String iconName = obj.getString("icon");
                            String objDesc = obj.getString("description");


                            String imageUrl = "http://openweathermap.org/img/w/"+iconName+".png";
                            String pathname = getFilesDir()+ "/" + iconName+ ".png";
                            File file = new File(pathname);

                            if (file.exists()){
                                image = BitmapFactory.decodeFile(pathname);
                            }else{
                                ImageRequest imgReq = new ImageRequest(imageUrl, new Response.Listener<Bitmap>() {
                                    @Override
                                    public void onResponse(Bitmap bitmap) {
                                        try {
                                            image = bitmap;
                                            image.compress(Bitmap.CompressFormat.PNG, 100, MainActivity.this.openFileOutput(iconName+".png", Activity.MODE_PRIVATE));
                                        } catch (IOException e) {e.printStackTrace();}
                                    }
                                }, 1024, 1024, ImageView.ScaleType.CENTER, null,
                                    (error )->{ });
                                queue.add(imgReq);
                            }

                            runOnUiThread(()->{
                                temp.setText("The current temperature is " + current);
                                temp.setVisibility(View.VISIBLE);

                                maxTemp.setText("The max temperature is " + max);
                                maxTemp.setVisibility(View.VISIBLE);

                                minTemp.setText("The min temperature is " + min);
                                minTemp.setVisibility(View.VISIBLE);

                                humid.setText("The humidity is " + humidity+"%");
                                humid.setVisibility(View.VISIBLE);

                                desc.setText(objDesc);
                                desc.setVisibility(View.VISIBLE);

                                weatherIcon.setImageBitmap(image);
                                weatherIcon.setVisibility(View.VISIBLE);

                            });


                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                    },
                    (error)->{ });
            queue.add(request);// send request to server




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