package ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import algonquin.cst2335.chan0527.databinding.ActivityMainBinding;
import data.MainViewModel;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding variableBinding;
    private MainViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        model = new ViewModelProvider(this).get(MainViewModel.class);

        variableBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(variableBinding.getRoot()); //load XML on screen

        variableBinding.mybutton.setOnClickListener(click -> {
            model.editString.postValue(variableBinding.myedittext.getText().toString());
            model.editString.observe(this, s -> {
                variableBinding.myText.setText("Your edit text has: " + s);
            });
        });

        variableBinding.mycheckBox.setOnCheckedChangeListener( (btn, isChecked) -> {
            model.isSelected.postValue(isChecked);
        } );

        variableBinding.myswitch.setOnCheckedChangeListener( (btn, isChecked) -> {
            model.isSelected.postValue(isChecked);
        } );

        variableBinding.myradiobtn.setOnCheckedChangeListener( (btn, isChecked) -> {
            model.isSelected.postValue(isChecked);
        } );

        model.isSelected.observe(this,selected->{
            variableBinding.mycheckBox.setChecked(selected);
            variableBinding.myradiobtn.setChecked(selected);
            variableBinding.myswitch.setChecked(selected);

            CharSequence text = "The value is now: " + selected;
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(this, text, duration);
            toast.show();
        });

        variableBinding.myimagebutton.setOnClickListener(click->{
            int width = variableBinding.myimagebutton.getWidth();
            int height = variableBinding.myimagebutton.getHeight();

            CharSequence text = "The width = " + width + " and height = " + height;
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(this, text, duration);
            toast.show();
            });

    }
}
