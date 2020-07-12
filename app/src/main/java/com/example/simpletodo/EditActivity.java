package com.example.simpletodo;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Objects;

public class EditActivity extends AppCompatActivity {

    EditText etItem;
    Button btnSave;


    //@RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        etItem.findViewById(R.id.etItem);
        btnSave.findViewById(R.id.btnSave);

        getSupportActionBar().setTitle("Edit item");

        etItem.setText(getIntent().getStringExtra(MainActivity.KEY_ITEM_TEXT));

        //When the user is done editing, they click the save button.
        btnSave.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                //Create an intent which will contain the results.
                Intent intent = new Intent();
                //pass the data (results of editing)
                intent.putExtra(MainActivity.KEY_ITEM_TEXT,etItem.getText().toString());
                intent.putExtra(MainActivity.KEY_ITEM_POSITION,getIntent().getExtras().getInt(MainActivity.KEY_ITEM_POSITION));
                //Set the result of the intent
                setResult(RESULT_OK, intent);
                //finish activity, close the screen and go back.
                finish();

            }
        });

    }
}