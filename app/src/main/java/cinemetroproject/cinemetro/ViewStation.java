package cinemetroproject.cinemetro;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.HorizontalScrollView;
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
public class ViewStation extends ActionBarActivity {

    private int idStation;
    private int actors; //count of actors
    private int movieImages;
    private TextView textViewTitle;
    private LinearLayout actorsScrollView;
    private TextView textViewDirector;
    private ImageButton directorImage;
    private TextView textViewInfo;
    private ImageButton showInMap;
    private Button goAheadButton;
    private HorizontalScrollView sharing;
    private Button facebookButton;
    private Button twitterButton;
    private Button instagramButton;
    private Button pinterestButton;
    private AlertDialog.Builder dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_station);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        idStation = intent.getIntExtra("button_id", 0);

        textViewTitle =(TextView)findViewById(R.id.titleYear);
        textViewTitle.setText(DbAdapter.getInstance().getMovieByStation(idStation).getTitle() + " " + DbAdapter.getInstance().getMovieByStation(idStation).getYear());
        textViewTitle.setBackgroundColor(getResources().getColor(R.color.line2));

        /** Getting a reference to the ViewPager defined the layout file */
        ViewPager pager = (ViewPager) findViewById(R.id.pagerImage);

        /** Getting fragment manager */
        FragmentManager fm = getSupportFragmentManager();

        movieImages = DbAdapter.getInstance().getMainPhotosOfMovie(idStation-6).size();
        /** Instantiating FragmentPagerAdapter */
        ImageAdapter pagerAdapter = new ImageAdapter(fm, movieImages);
        ImageFragment.id=idStation;
        ImageFragment.line=2;

        /** Setting the pagerAdapter to the pager object */
        pager.setAdapter(pagerAdapter);

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

        showInMap = (ImageButton)findViewById(R.id.showInMap);
        showInMap.setOnClickListener(showInMapButtonOnClickListener);

        goAheadButton = (Button) findViewById(R.id.go_ahead_button);
        goAheadButton.setOnClickListener(goAheadButtonOnClickListener);

        sharing = (HorizontalScrollView) findViewById(R.id.sharing);

        facebookButton = (Button) findViewById(R.id.facebook_button);
        facebookButton.setOnClickListener(new sharingOnClickListener("Facebook"));

        twitterButton = (Button) findViewById(R.id.twitter_button);
        twitterButton.setOnClickListener(new sharingOnClickListener("Twitter"));

        instagramButton = (Button) findViewById(R.id.instagram_button);
        instagramButton.setOnClickListener(new sharingOnClickListener("Instagram"));

        pinterestButton = (Button) findViewById(R.id.pinterest_button);
        pinterestButton.setOnClickListener(new sharingOnClickListener("Pinterest"));

        dialog = new AlertDialog.Builder(this);

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

    View.OnClickListener showInMapButtonOnClickListener = new View.OnClickListener(){

        @Override
        public void onClick(View view) {

            MapActivity.showInMap(1, idStation+1);

            Intent intent = new Intent(ViewStation.this, MapActivity.class);
            ViewStation.this.startActivity(intent);
        }};

    View.OnClickListener goAheadButtonOnClickListener = new View.OnClickListener(){

        @Override
        public void onClick(View view) {

            //user should sign up
            if(DbAdapter.getInstance().getActiveUser() == null){
                dialog.setTitle(getResources().getString(R.string.title_activity_RateActivity));
                dialog.setMessage(getResources().getString(R.string.must_sign_up));
                dialog.setPositiveButton("OK",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialog.setCancelable(true);
                    }
                });
                dialog.setIcon(R.drawable.logo_background);
                AlertDialog alert = dialog.create();
                alert.show();
            }
            //user has already rated for this station
            else if (DbAdapter.getInstance().getUserRatingForStation(idStation, DbAdapter.getInstance().getActiveUser().getId()) != -1){
                dialog.setTitle(getResources().getString(R.string.title_activity_RateActivity));
                dialog.setMessage(getResources().getString(R.string.already_rate));
                dialog.setPositiveButton("OK",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialog.setCancelable(true);
                    }
                });
                dialog.setIcon(R.drawable.logo_background);
                AlertDialog alert = dialog.create();
                alert.show();
            }
            //user can rate
            else {
                Intent intent = new Intent(ViewStation.this, RateActivity.class);
                intent.putExtra("line", 2);
                intent.putExtra("button_id", ++idStation);
                ViewStation.this.startActivity(intent);
            }
        }};

    public class sharingOnClickListener implements View.OnClickListener {

        String appName;

        public sharingOnClickListener(String appName) {
            this.appName = appName;
        }

        @Override
        public void onClick(View view) {

            boolean found = false;

            Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, "#CineMetro#" + DbAdapter.getInstance().getMovieByStation(idStation).getTitle());
            PackageManager pm = view.getContext().getPackageManager();
            List<ResolveInfo> activityList = pm.queryIntentActivities(shareIntent, 0);
            for (final ResolveInfo app : activityList) {
                if ((app.activityInfo.name).contains(appName)) {
                    final ActivityInfo activity = app.activityInfo;
                    final ComponentName name = new ComponentName(activity.applicationInfo.packageName, activity.name);
                    shareIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                    shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                    shareIntent.setComponent(name);
                    view.getContext().startActivity(shareIntent);
                    found = true;
                    break;
                }
            }
            if (found == false) {

                dialog.setTitle(appName);
                dialog.setMessage(R.string.noApp);
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialog.setCancelable(true);
                    }
                });
                AlertDialog alert = dialog.create();
                alert.show();
            }
        }};
}
