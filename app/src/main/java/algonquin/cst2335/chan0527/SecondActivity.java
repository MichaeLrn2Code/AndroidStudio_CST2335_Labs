package algonquin.cst2335.chan0527;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import algonquin.cst2335.chan0527.databinding.ActivitySecondBinding;
public class SecondActivity extends AppCompatActivity {
    private ActivitySecondBinding varBinding2;
    private String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        varBinding2 = ActivitySecondBinding.inflate(getLayoutInflater());
        setContentView(varBinding2.getRoot());

        Intent fromPrevious = getIntent();
        String emailAddress = fromPrevious.getStringExtra("EmailAddress");

        // set TextView to "Welcome back" + email address
        varBinding2.emailView.setText("Welcome back " + emailAddress);

        // created a MyData object to store the phone Number that user typed
        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);

        // Retrieve the saved phone number and set to phone number edit text
        varBinding2.phoneNumText.setText(prefs.getString("phoneNumber", ""));

        // create phone button onclick listener
        varBinding2.phoneButton.setOnClickListener(click->{
            // get the input phone number
            phoneNumber= varBinding2.phoneNumText.getText().toString();
            // create a Intent object to store the input phone number
            Intent call = new Intent(Intent.ACTION_DIAL);
            call.setData(Uri.parse("tel:" + phoneNumber));
            startActivity(call);
        });

        // get picture if picture exist
        File file = new File( getFilesDir(), "Picture.png");
        if(file.exists()) {
            Bitmap theImage = BitmapFactory.decodeFile(file.getAbsolutePath());
            varBinding2.profileImage.setImageBitmap(theImage);
        }

        // set camera function
        ActivityResultLauncher<Intent> cameraResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();

                            Bitmap thumbnail = data.getParcelableExtra("data");
                            varBinding2.profileImage.setImageBitmap(thumbnail);
                            FileOutputStream fOut = null;

                            try { fOut = openFileOutput("Picture.png", Context.MODE_PRIVATE);
                                thumbnail.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                                fOut.flush();
                                fOut.close();
                            } catch (FileNotFoundException e)
                            { e.printStackTrace();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                } );

        // set camera onClickListener
        varBinding2.picButton.setOnClickListener(click->{
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                if(checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
//                    startActivity(cameraIntent);
//                else
//                    requestPermissions(new String[] {Manifest.permission.CAMERA}, 20);
//            }


            cameraResult.launch(cameraIntent);
        });

    }

    @Override
    protected void onPause() {
        super.onPause();

        // created a MyData object to store the phone Number that user typed
        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);

        // set the input phone number in MyData Object
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("phoneNumber", varBinding2.phoneNumText.getText().toString());
        editor.apply();

    }
}