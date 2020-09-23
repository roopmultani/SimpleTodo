package com.example.simpletodo;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_ITEM_TEXT = "item_text";
    public static final String KEY_ITEM_POSITION ="item_position";
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

        //etItem.setText("I'm doing this from java!");

        //items = new ArrayList<>();
        loadItems();
        // items.add ("Buy milk");
        //  items.add ("Go to the gym");
        // items.add ("Have fun!");

        ItemsAdapter.OnLongCLickListener onLongCLickListener = new ItemsAdapter.OnLongCLickListener() {
            @Override
            public void onItemLongClicked(int position) {
                // delete the item from the model
                items.remove(position);

                // notify the adapter
                itemsAdapter.notifyItemRemoved(position);
                Toast.makeText(getApplicationContext(), "Item was removed", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        };

        ItemsAdapter.OnClickListener onClickListener = new ItemsAdapter.OnClickListener() {
            @Override
            public void onItemClicked(int position) {
                Log.d("MainActivity", "Single click at position" + position);

                // create the new activity
                Intent i = new Intent(MainActivity.this, EditActivity.class);

                // pass the data being edited
                i.putExtra(KEY_ITEM_TEXT, items.get(position));
                i.putExtra(KEY_ITEM_POSITION, position);

                // display the activity
                startActivityForResult(i, EDIT_TEXT_CODE);
            }
        };
        itemsAdapter = new ItemsAdapter(items, onLongCLickListener, onClickListener);
        rvItems.setAdapter(itemsAdapter);
        rvItems.setLayoutManager(new LinearLayoutManager(this));

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String todoItem = etItem.getText().toString();

                // add item to the model
                items.add(todoItem);

                // notify adapter that an item is inserted
                itemsAdapter.notifyItemInserted(items.size() - 1);
                etItem.setText("");
                Toast.makeText(getApplicationContext(), "Item was added", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        });
    }

    // handle the result of the edit activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == EDIT_TEXT_CODE){
            // Retreive the updated text value
            String itemText = data.getStringExtra(KEY_ITEM_TEXT);

            // extract the original position of the edited item from the posotion key
            int position = data.getExtras().getInt(KEY_ITEM_POSITION);

            // update the model at the right positions with new item text
            items.set(position, itemText);

            // notify the adapter
            itemsAdapter.notifyItemChanged(position);

            // persist the changed
            saveItems();
            Toast.makeText(getApplicationContext(), "Item updated successfully", Toast.LENGTH_SHORT).show();
        }
        else {
            Log.w("MainActivity", "Unknown call to onActivityResult");
        }
    }

    private File getDataFile() {
        return new File(getFilesDir(), "data.txt");
    }

    // this function will load items by reading every line of the data file
    private void loadItems (){
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(),Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("MainActivity", "Error reading items", e);
            items = new ArrayList<>();
        }
    }

    // this functions saves items by writing them into the data file

   private void saveItems(){
       try {
           FileUtils.writeLines(getDataFile(), items);
       } catch (IOException e) {
           Log.e("MainActivity", "Error writing items", e);
       }
   }
}