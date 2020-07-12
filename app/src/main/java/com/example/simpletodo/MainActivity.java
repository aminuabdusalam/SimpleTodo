package com.example.simpletodo;

//import android.os.FileUtils;
import org.apache.commons.io.FileUtils;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_ITEM_TEXT = "item_text";
    public static final String KEY_ITEM_POSITION = "item_position";
    public static final int EDIT_TEXT_CODE = 20;

    List<String> items;

    Button btnAdd;
    EditText etItem;
    RecyclerView rvItems;
    ItemsAdapter itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = findViewById(R.id.btnAdd);
        etItem = findViewById(R.id.etItem);
        rvItems = findViewById(R.id.rvItems);

        //etItem.setText("I am working on java!");
        //items = new ArrayList<>();
        //items.add("Complete project");
        //items.add("Solve a leetcode question");
        //items.add("Do laundry!");

        loadItems();

        ItemsAdapter.OnLongClickListener onLongClickListener = new ItemsAdapter.OnLongClickListener() {
            @Override
            public void onItemLongClicked(int position) {
                //Delete item from model.
                items.remove(position);
                //Notify the adapter.
                itemsAdapter.notifyItemRemoved(position);
                //Show the user a toast(brief pop-up) to show that item has been successfully added.
                Toast.makeText(getApplicationContext(),"Item was removed successfully.", Toast.LENGTH_SHORT).show();
                saveItems();

            }
        };

        ItemsAdapter.OnClickListener onClickListener = new ItemsAdapter.OnClickListener() {
            @Override
            public void onItemClicked(int position) {
                Log.d("MainActivity","Single click at position " + position);
                //Create the new activity (using an *intent* (a request to the android system)).
                Intent i = new Intent(MainActivity.this,EditActivity.class);
               //Pass the relevant data being edited.
                i.putExtra(KEY_ITEM_TEXT,items.get(position));
                i.putExtra(KEY_ITEM_POSITION,position);
                //Display the activity
                startActivityForResult(i,EDIT_TEXT_CODE);

            }
        };

        itemsAdapter = new ItemsAdapter(items,onLongClickListener,onClickListener);
        //set the adapter on the Recycler View
        rvItems.setAdapter(itemsAdapter);
        rvItems.setLayoutManager(new LinearLayoutManager(this));

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Grab content from edit text when user clicks on add.
                String todoItem = etItem.getText().toString();
                //Add item to the model.
                items.add(todoItem);
                //Notify the Adapter that an item is inserted.
                itemsAdapter.notifyItemInserted(items.size() - 1);
                //Clear the edit text once item has been added.
                etItem.setText("");
                //Show the user a toast(brief pop-up) to show that item has been successfully added.
                Toast.makeText(getApplicationContext(),"Item was added successfully.", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        });


        //add a handler for each view
    }

    // handle the result of the edit activity
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && requestCode == EDIT_TEXT_CODE){
            //Retrieve the updated text value
            assert data != null;
            String itemText = data.getStringExtra(KEY_ITEM_TEXT);
            //Extract the original position of theedited item from the position key
            int position = Objects.requireNonNull(data.getExtras()).getInt(KEY_ITEM_POSITION);

            //Update the model at the right position with new item text.
            items.set(position, itemText);
            //notify the adapter.
            itemsAdapter.notifyItemChanged(position);
            //persist the changes
            saveItems();
            Toast.makeText(getApplicationContext(),"Item updated successfully",Toast.LENGTH_SHORT).show();

        }else {
            Log.w("MainActivity","Unknown call to onActivityResult");
        }

    }

    //Returns the file that stores our list of to do items.
    private File getDataFile(){
        return new File(getFilesDir(),"data.txt");
    }

    //This function loads items by reading every line of the data file.
    private void loadItems(){ //Called only once when user starts up the app.

        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(),Charset.defaultCharset()));
        } catch (IOException e) {
            //Log errors to logcat: log is a way for developers to identify what is happening in their program.
            Log.e("MainActivity","Error reading items",e);
            //Set the arrayList to be empty so we have something to build our Recycler View off.
            items = new ArrayList<>();

        }

    }

    //This function saves items by writing them into the data file.
    private void saveItems(){//Runs when an item is added and when an item is rmeoved.
        try {
            FileUtils.writeLines(getDataFile(),items);
        } catch (IOException e) {
            Log.e("MainActivity","Error writing items",e);
        }
    }
}