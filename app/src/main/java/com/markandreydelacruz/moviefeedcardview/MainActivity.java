package com.markandreydelacruz.moviefeedcardview;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.markandreydelacruz.moviefeedcardview.Adapter.MovieAdapter;
import com.markandreydelacruz.moviefeedcardview.Models.MovieModel;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    private static final String URL_MOVIE_FEED = "http://192.168.137.1/json/moviesData.php";
    private boolean doubleBackToExitPressedOnce = false;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<MovieModel> movieModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Display Icon on toolbar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        setTitle("Movie Feed 2.0");
        // Create default options which will be used for every
        //  displayImage(...) call if no options will be passed to this method
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        ImageLoader.getInstance().init(config); // Do it on Application start

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        E/RecyclerView: No adapter attached; skipping layout

        getInformation(URL_MOVIE_FEED);
//        Snackbar snack = Snackbar.make(getWindow().getDecorView().getRootView(), "Test", Snackbar.LENGTH_LONG)
//                .setAction("Action", null);
//        ViewGroup group = (ViewGroup) snack.getView();
//        group.setBackgroundColor(Color.parseColor("#1aa260"));
//        snack.show();
    }

    private void getInformation(String URL_MOVIE_FEED) {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.get(MainActivity.this, URL_MOVIE_FEED, new AsyncHttpResponseHandler() {

            ProgressDialog dialog;

            @Override
            public void onStart() {
                super.onStart();
                dialog = new ProgressDialog(MainActivity.this);
                dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                dialog.setMessage("Please wait...");
                dialog.setIndeterminate(false);
                dialog.setCancelable(false);
                dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    public void onCancel(DialogInterface dialog) {
                        Toast.makeText(getApplicationContext(), "Canceled", Toast.LENGTH_LONG).show();
                    }
                });
                dialog.show();
                recyclerView.setAdapter(null);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, final byte[] responseBody) {
                if (this.dialog.isShowing()) {
                    this.dialog.dismiss();
                }

//                JsonParser jsonParser = new JsonParser();
//                JsonArray jsonArray = jsonParser.parse(new String(responseBody))
//                        .getAsJsonObject().getAsJsonArray("movies");
//                Log.d("jsonArray:", String.valueOf(jsonArray));

                try {
                    JSONObject jsonObject = new JSONObject(new String(responseBody));
                    JSONArray jsonArray = jsonObject.optJSONArray("movies");
                    movieModelList = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject post = jsonArray.optJSONObject(i);

                        MovieModel movieModel = new Gson().fromJson(post.toString(), MovieModel.class);
//                        MovieModel movieModel = new MovieModel();
//                        movieModel.setMovie(post.optString("movie"));
//                        movieModel.setTagline(post.optString("tagline"));

                        movieModelList.add(movieModel);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                Toast.makeText(getApplicationContext(), new String(responseBody), Toast.LENGTH_LONG).show();
//                List<MovieModel> movieModels = Collections.singletonList(new Gson().fromJson(new String(responseBody), MovieModel.class));
                adapter = new MovieAdapter(movieModelList, null);
                recyclerView.setAdapter(adapter);


//                MovieModel movieModel = new Gson().fromJson(new String(responseBody), MovieModel.class);
//                List<MovieModel> test = Collections.singletonList(new Gson().fromJson(new String(responseBody), MovieModel.class));
//                movieModelList.add(movieModel);
//                Toast.makeText(getApplicationContext(), "hello", Toast.LENGTH_LONG).show();
//                adapter = new MovieAdapter(test, getApplicationContext());
//                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if (this.dialog.isShowing()) {
                    this.dialog.dismiss();
                }
                Toast.makeText(getApplicationContext(), "Connection failed. Try again.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                if (this.dialog.isShowing()) {
                    this.dialog.dismiss();
                }
            }
        });
    }

    private void shareApplication() {
        ApplicationInfo app = getApplicationContext().getApplicationInfo();
        String filePath = app.sourceDir;

        Intent intent = new Intent(Intent.ACTION_SEND);

        // MIME of .apk is "application/vnd.android.package-archive".
        // but Bluetooth does not accept this. Let's use "*/*" instead.
//        intent.setType("*/*");
        intent.setType("application/vnd.android.package-archive");

        // Append file and send Intent
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(filePath)));
        startActivity(Intent.createChooser(intent, "Share app via"));
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press back again to exit.", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_share) {
            shareApplication();
            return true;
        } else if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
