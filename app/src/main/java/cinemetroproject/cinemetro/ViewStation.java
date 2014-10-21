package cinemetroproject.cinemetro;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by kiki__000 on 20-Jul-14.
 * Se ayto to activity anaparistatai to ViewStation
 * Dimiourgeitai otan epilegetai mia stasi apo LinesActivity
 */
public class ViewStation extends ActionBarActivity  {

    private int idStation;
    private int actors; //count of actors
    private int movieImages;
    private TextView textViewTitle;
    private LinearLayout movieImagesScrollView;
    private LinearLayout actorsScrollView;
    private TextView textViewDirector;
    private ImageButton directorImage;
    private TextView textViewInfo;
    private TextView points;
    private Button goAheadButton;
    private Button facebookButton;
    private Button twitterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //setTheme(android.R.style.Theme_Light_Panel);
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_view_station);

        Intent intent = getIntent();
        idStation = intent.getIntExtra("button_id", 0);

        textViewTitle =(TextView)findViewById(R.id.titleYear);
        textViewTitle.setText(DbAdapter.getInstance().getMovieByStation(idStation).getTitle() + " " + DbAdapter.getInstance().getMovieByStation(idStation).getYear());

        movieImages = DbAdapter.getInstance().getMainPhotosOfMovie(idStation-6).size();
        movieImagesScrollView = (LinearLayout)findViewById(R.id.movieImagesHsw);
        for (int i=0; i<=(movieImages-1); i++) {
            Button movieImage = new Button(this);
            try {
                Class res = R.drawable.class;
                Field field = res.getField((DbAdapter.getInstance().getMainPhotosOfMovie(idStation-6).get(i).getName()));
                int drawableId = field.getInt(null);
                movieImage.setBackgroundResource(drawableId);
                movieImage.setLayoutParams(new ViewGroup.LayoutParams(320, 300));
            } catch (Exception e) {}
            movieImage.setGravity(Gravity.BOTTOM | Gravity.CENTER);

            movieImagesScrollView.addView(movieImage);

        }

        directorImage = (ImageButton)findViewById(R.id.directorImage);
        try {
            Class res = R.drawable.class;
            Field field = res.getField(DbAdapter.getInstance().getActorPhotosOfMovie(idStation-6).get(0).getName());
            int drawableId = field.getInt(null);
            directorImage.setBackgroundResource(drawableId);
        } catch (Exception e) {}

        textViewDirector = (TextView)findViewById(R.id.directorName);
        textViewDirector.setText(DbAdapter.getInstance().getMovieByStation(idStation).getDirector() + "\n");

        actors = DbAdapter.getInstance().getMovieByStation(idStation).getActors().size();
        actorsScrollView = (LinearLayout)findViewById(R.id.actorsHsw);

        for (int i=0; i<actors; i++) {
            View actor = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.actor, null);
            Button imageActor = (Button) actor.findViewById(R.id.actorImage);
            Button nameActor = (Button) actor.findViewById(R.id.actorName);
            try {
                Class res = R.drawable.class;
                Field field = res.getField(DbAdapter.getInstance().getActorPhotosOfMovie(idStation-6).get(i+1).getName());
                int drawableId = field.getInt(null);
                imageActor.setBackgroundResource(drawableId);
            } catch (Exception e) {}
            String string = DbAdapter.getInstance().getMovieByStation(idStation).getActors().get(i);
            String[] parts = string.split(" ");
            nameActor.setText(parts[0] + "\n" + parts[1]);

            actorsScrollView.addView(actor);
        }

        textViewInfo = (TextView)findViewById(R.id.info);
        textViewInfo.setText(DbAdapter.getInstance().getMovieByStation(idStation).getDescription());

        points = (TextView) findViewById(R.id.points);
        points.setText(String.valueOf(DbAdapter.getInstance().getStationRating(idStation)) + "  ");

        goAheadButton = (Button) findViewById(R.id.go_ahead_button);
        goAheadButton.setOnClickListener(goAheadButtonOnClickListener);

        facebookButton = (Button) findViewById(R.id.facebook_button);
        facebookButton.setOnClickListener(facebookButtonOnClickListener);

        twitterButton = (Button) findViewById(R.id.twitter_button);
        twitterButton.setOnClickListener(twitterButtonOnClickListener);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.view_station, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            onBackPressed();
            return true;
        }
        switch (item.getItemId()) {
            case R.id.menu_item_share:
                shareIt();
                return true;
            default:
                onBackPressed();
                return true;
        }
    }

    //from actionbar
    private void shareIt() {

        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = "#CineMetro#" + DbAdapter.getInstance().getMovieByStation(idStation).getTitle();
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "CineMetro");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    View.OnClickListener goAheadButtonOnClickListener = new View.OnClickListener(){

        @Override
        public void onClick(View view) {

            Intent intent = new Intent(ViewStation.this, RateActivity.class);
            intent.putExtra("button_id", ++idStation);
            ViewStation.this.startActivity(intent);
        }};

    public View.OnClickListener facebookButtonOnClickListener = new View.OnClickListener(){

        @Override
        public void onClick(View view) {

            Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, "#CineMetro#" + DbAdapter.getInstance().getMovieByStation(idStation).getTitle());
            PackageManager pm = view.getContext().getPackageManager();
            List<ResolveInfo> activityList = pm.queryIntentActivities(shareIntent, 0);
            for (final ResolveInfo app : activityList) {
                if ((app.activityInfo.name).contains("facebook")) {
                    final ActivityInfo activity = app.activityInfo;
                    final ComponentName name = new ComponentName(activity.applicationInfo.packageName, activity.name);
                    shareIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                    shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                    shareIntent.setComponent(name);
                    view.getContext().startActivity(shareIntent);
                    break;
                }
            }
        }};

    View.OnClickListener twitterButtonOnClickListener = new View.OnClickListener(){

        @Override
        public void onClick(View view) {

            Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
            String shareBody = "#CineMetro#" + DbAdapter.getInstance().getMovieByStation(idStation).getTitle();
            shareIntent.setType("text/plain");
            shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            PackageManager pm = view.getContext().getPackageManager();
            List<ResolveInfo> activityList = pm.queryIntentActivities(shareIntent, 0);
            for (final ResolveInfo app : activityList) {
                if ((app.activityInfo.name).contains("twitter")) {
                    final ActivityInfo activity = app.activityInfo;
                    final ComponentName name = new ComponentName(activity.applicationInfo.packageName, activity.name);
                    shareIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                    shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                    shareIntent.setComponent(name);
                    view.getContext().startActivity(shareIntent);
                    break;
                }
            }

        }};
}
