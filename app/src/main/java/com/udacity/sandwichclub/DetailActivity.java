package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;
    private static Sandwich sandwich = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //activity DetailActivity gets call and the Activity cicle onCreate becomes our first method to be call
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //nice here is our image view
        ImageView ingredientsIv = findViewById(R.id.image_iv);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
            logOut("intent is a null value");
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            logOut("our intent is not null but we can't find the extra_position integer value from our list view");
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        //we are calling the JsonUtils singleton and we are returning null if we are not even parsing the data
       sandwich = JsonUtils.parseSandwichJson(json);
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }

        populateUI();

        //wao didn't know about this library picaso This class is so fun!
        Picasso.with(this)
                .load(sandwich.getImage())
                .into(ingredientsIv);

        setTitle(sandwich.getMainName());
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }


    private void populateUI() {
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setDuration(1000);
        //lets populate the data for extra info about our sandwitch
        TextView alsoKnownAs = (TextView)findViewById(R.id.alsoknownas);
        TextView ingredients = (TextView)findViewById(R.id.ingredients);
        TextView placeOfOrigin = (TextView) findViewById(R.id.origin);
        TextView description = (TextView)findViewById(R.id.description);
        //lets set some animation to each widget

        alsoKnownAs.setAnimation(fadeIn);
        ingredients.setAnimation(fadeIn);
        placeOfOrigin.setAnimation(fadeIn);
        description.setAnimation(fadeIn);

        //lets populate the data
        if(sandwich!=null)
        {

            alsoKnownAs.setText(sandwich.getAlsoKnownAs().toString());
            ingredients.setText(sandwich.getIngredients().toString());
            placeOfOrigin.setText(sandwich.getPlaceOfOrigin().toString());
            description.setText(sandwich.getDescription().toString());
        }



    }



    private void logOut(String text)
    {
        Log.d(DetailActivity.class.getName(),text);
    }
}
