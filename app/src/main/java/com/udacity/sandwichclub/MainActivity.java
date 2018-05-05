package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //well in this case we will be getting the string array for our list view UI
        String[] sandwiches = getResources().getStringArray(R.array.sandwich_names);
        //here we will initialize and adapter that will hold that string array on a simple list item 1 format
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, sandwiches);

        // Simplification: Using a ListView instead of a RecyclerView
        //lets get the listview by id
        ListView listView = findViewById(R.id.sandwiches_listview);
        //we set the adpater to our list view
        listView.setAdapter(adapter);
        //now lets register item listener to this adabter
        //in this listener we will call the method launchDetail activity with the current position we clicked
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                launchDetailActivity(position);
            }
        });
    }

    private void launchDetailActivity(int position) {
        //we are creating a new intent to launch our detail Activity child class from parent class
        Intent intent = new Intent(this, DetailActivity.class);
        //we put and Explicit content postion to our intent as parameters
        intent.putExtra(DetailActivity.EXTRA_POSITION, position);
        //now we will launch the new activity
        startActivity(intent);
    }
}
