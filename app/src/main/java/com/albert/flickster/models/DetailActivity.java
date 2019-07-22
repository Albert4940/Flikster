 package com.albert.flickster.models;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.TextView;

import com.albert.flickster.R;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

 public class DetailActivity extends YouTubeBaseActivity {
     private static final String YOUTUBE_API_KEY = "AIzaSyA6yJxGwUjgIAW_8fIKeAQAs3_F9povEAM";
     private static final String TRAILERS_API = "https://api.themoviedb.org/3/movie/%d/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
    TextView tvTitle;
    TextView tvOverview;
    RatingBar ratingbar;
    Movie movie;
     YouTubePlayerView youTubePlayerView;
     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        tvTitle = findViewById(R.id.tvTitle);
        tvOverview = findViewById(R.id.tvOverview);
        ratingbar = findViewById(R.id.ratingBar);
         youTubePlayerView =(YouTubePlayerView)findViewById(R.id.player);

        movie = Parcels.unwrap(getIntent().getParcelableExtra("movie"));

        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());
        ratingbar.setRating((float) movie.getVoteAverage());

         AsyncHttpClient client = new AsyncHttpClient();
         client.get(String.format(TRAILERS_API,movie.getMovieId()), new JsonHttpResponseHandler(){
             @Override
             public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                 super.onSuccess(statusCode, headers, response);
                 try {
                     JSONArray results = response.getJSONArray("results");
                     if(results.length()==0)
                     {
                         return;
                     }
                     JSONObject movie = results.getJSONObject(0);
                     String youtubeKey = movie.getString("key");
                     initializeYoutub(youtubeKey);
                 } catch (JSONException e) {
                     e.printStackTrace();
                 }
             }

             @Override
             public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                 super.onFailure(statusCode, headers, responseString, throwable);
                 Log.d("smile","JSON FAILED");
             }
         });


    }

     private void initializeYoutub(final String youtubeKey) {
         youTubePlayerView.initialize(YOUTUBE_API_KEY, new YouTubePlayer.OnInitializedListener() {
             @Override
             public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                 Log.d("smile","on init success");
                 youTubePlayer.cueVideo(youtubeKey);
             }

             @Override
             public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                 Log.d("smile","on init failuure");

             }
         });
     }
 }
