package com.udacity.sandwichclub.utils;

import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
/* sorry for this late submission I been really busy at school this is going to be my final year.
  * I am really going to make the best of this amazing opportunity att Luis Manon your student
 */

public class JsonUtils {

    //well I have seen this method gets call from detail activity which is not doing anything other than returning null
    //thus I am going to parse the json object and then create a new Sandwidch object with detail data to be able to populate the UI
    public static Sandwich parseSandwichJson(String data) {
        JSONObject json = null;
        Sandwich sandwich = null;
        try{
            json =  new JSONObject(data);
            if(json!=null)
            {
                //lets get the sandwitch name and the object array for common names for this typical sandwitch
                JSONObject name = json.getJSONObject("name");
                String sandwich_name =  name.getString("mainName");
                JSONArray AlsoKnownAs = name.getJSONArray("alsoKnownAs");
                List<String> alsoKAs = new ArrayList<String>();
                for(int i=0; i<AlsoKnownAs.length(); i++)
                { alsoKAs.add((String)AlsoKnownAs.get(i));}
                //now lets get other attributes to generate our Sandwitch object


                String placeO =   json.getString("placeOfOrigin").toString();
                if(placeO.length()==0){placeO = "Unknown";}
                String description =  json.getString("description").toString();
                String img_url = json.getString("image").toString();
                JSONArray ingredients  = json.getJSONArray("ingredients");
                List<String> ingred =  new ArrayList<>();
                for(int i=0; i<ingredients.length(); i++)
                {ingred.add((String)ingredients.get(i).toString());}

                sandwich = new Sandwich(sandwich_name,alsoKAs,placeO,description,img_url,ingred);

               // Log.d("JsonUtils",sandwich_name+" "+sandwich.toString());

            }
        }catch(JSONException e)
        {
            e.printStackTrace();
        }


        return sandwich;
    }
}
