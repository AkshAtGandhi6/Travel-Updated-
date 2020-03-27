package com.example.finallogin;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;


class GetNearbyPlacesData extends AsyncTask<Object, String, String>{

    private String googlePlacesData;
    String url;
    Context context;
    Activity activity;
    public GetNearbyPlacesData(Context context)
    {
        this.context=context;
        activity=(Activity) context;
    }

    @Override
    protected String doInBackground(Object... objects){

        url = (String)objects[0];

        DownloadURL downloadURL = new DownloadURL();
        try {
            googlePlacesData = downloadURL.readUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return googlePlacesData;
    }

    @Override
    protected void onPostExecute(String s){

        List<HashMap<String, String>> nearbyPlaceList;
        DataParser parser = new DataParser();
        nearbyPlaceList = parser.parse(s);
        Log.d("nearbyplacesdata","called parse method");
        showNearbyPlaces(nearbyPlaceList);
    }

    private void showNearbyPlaces(List<HashMap<String, String>> nearbyPlaceList)
    {
        String[] names=new String[nearbyPlaceList.size()];
        double[] ratings=new double[nearbyPlaceList.size()];
        for(int i = 0; i < nearbyPlaceList.size(); i++)
        {

            HashMap<String, String> googlePlace = nearbyPlaceList.get(i);
            if(googlePlace!=null)
            {
                String placeName = googlePlace.get("place_name");
                String vicinity = googlePlace.get("vicinity");
                double rating;
                if(googlePlace.get("rating").equals(""))
                {
                    rating=0.0;
                }
                else
                {
                    rating=Double.parseDouble(googlePlace.get("rating"));
                }

                names[i]=placeName;
                ratings[i]=rating;
                Log.d("GetNearbyPlacesData","Name: "+placeName+" rating: "+rating);
            }

//            TextView textView=(TextView) activity.findViewById(R.id.textView);
//            textView.append("Name: "+placeName+" Latitude: "+lat+" Longitude: "+lng+" rating: "+rating+"\n");

        }
        MyAdapter adapter=new MyAdapter(this.context, names, ratings);
        ListView listView=activity.findViewById(R.id.restaurants);
        listView.setAdapter(adapter);
        listView.setVisibility(View.VISIBLE);
    }



}
