package com.udacity.sandwichclub;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import java.util.List;

public class DetailActivity extends AppCompatActivity implements OnMapReadyCallback {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;
    private static Sandwich sandwich = null;
    //this is our mapview component for the origin data
    private MapView mapView;
    //our google map object
    private GoogleMap map;


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

        //lets get the map object and instantiate our null mapView
        if (findViewById(R.id.map) != null) {

            if (savedInstanceState != null) {
                return;
            }

            // Create a new Fragment to be placed in the activity layout
            mapView = (MapView) findViewById(R.id.map);
            mapView.onCreate(savedInstanceState);
            mapView.onResume();
            mapView.getMapAsync(this);

        }//end of mapview

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

    //lets get lat and long of giving string location
    public Address getLoactionLatLong(String params)
    {
        Geocoder gc =  new Geocoder(getApplicationContext());
        Address location =null;

        try {
            List<Address> list = gc.getFromLocationName(params, 1);
            location = list.get(0);
            Toast.makeText(getApplicationContext(),"the origin : "+location.getLocality(),Toast.LENGTH_LONG).show();

        }catch (Exception e){e.printStackTrace();}

    return location;
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
            placeOfOrigin.setText(sandwich.getPlaceOfOrigin().toString());
            //lets point the map to the correct origin address location
            if(placeOfOrigin.getText().length() > 0 && !placeOfOrigin.getText().equals("Unknown"))
            {
                Address origin = getLoactionLatLong(placeOfOrigin.getText().toString());
                //now lets move map viewport to current place location
                if(origin!=null)
                MoveMapViewPort(sandwich.getMainName(),origin);

            }

            for(String s : sandwich.getAlsoKnownAs())
            {
              alsoKnownAs.append("\n* "+s.toString());
            }


            for(String s : sandwich.getIngredients())
            {
                ingredients.append("\n* "+s.toString());
            }


            description.setText(sandwich.getDescription().toString());
        }



    }



    private void logOut(String text)
    {
        Log.d(DetailActivity.class.getName(),text);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        populateUI();
    }

    public void MoveMapViewPort(String sandwich_name,Address location)
    {
       // Toast.makeText(getApplicationContext(),location.toString(),Toast.LENGTH_LONG).show();
            LatLng latlong = new LatLng(location.getLatitude(),location.getLongitude());
            map.addMarker(new MarkerOptions().position(latlong).title(sandwich_name));
            map.moveCamera(CameraUpdateFactory.newLatLng(latlong));

    }
}
