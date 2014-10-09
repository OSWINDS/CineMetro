package cinemetroproject.cinemetro;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by kiki__000 on 28-Aug-14.
 *
 *
 * ViewCinema shows the features (photo and text) of a cinema
 */
public class ViewCinema extends ActionBarActivity {

    private LinearLayout scrollView;
    private TextView description;
    private Button goAheadButton;
    private Button facebookButton;
    private Button twitterButton;
    private int idCinema;
    private int countP;

    protected void onCreate(Bundle savedInstanceState) {
        //setTheme(android.R.style.Theme_Light_Panel);
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_view_cinema);

        Intent intent = getIntent();
        idCinema = intent.getIntExtra("button_id", 0);

        scrollView = (LinearLayout)findViewById(R.id.cinemaHsV);

        countP = DbAdapter.getInstance().getPhotosByStation(idCinema+1).size();
        for (int i=0; i<countP; i++) {
            Button imageCinema = new Button(this);
            try {
                Class res = R.drawable.class;
                Field field = res.getField(DbAdapter.getInstance().getPhotosByStation(idCinema+1).get(i).getName());
                int drawableId = field.getInt(null);
                imageCinema.setBackgroundResource(drawableId);
                imageCinema.setGravity(Gravity.BOTTOM | Gravity.CENTER);
                imageCinema.setLayoutParams(new ViewGroup.LayoutParams(320,300));
        } catch (Exception e) {}

            scrollView.addView(imageCinema);
        }

        description = (TextView)findViewById(R.id.description);
        description.setText(DbAdapter.getInstance().getStations().get(idCinema).getDescription());

        goAheadButton = (Button) findViewById(R.id.go_ahead_button);
        goAheadButton.setOnClickListener(goAheadButtonOnClickListener);

        facebookButton = (Button) findViewById(R.id.facebook_button);
        facebookButton.setBackgroundResource(R.drawable.facebook_share);
        facebookButton.setLayoutParams (new LinearLayout.LayoutParams(60, 60));
        facebookButton.setOnClickListener(facebookButtonOnClickListener);

        twitterButton = (Button) findViewById(R.id.twitter_button);
        twitterButton.setBackgroundResource(R.drawable.twitter_share);
        twitterButton.setLayoutParams (new LinearLayout.LayoutParams(50, 50));
        twitterButton.setOnClickListener(twitterButtonOnClickListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.view_cinema, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {

            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    View.OnClickListener goAheadButtonOnClickListener = new View.OnClickListener(){

        @Override
        public void onClick(View view) {

            Intent intent = new Intent(ViewCinema.this, RateActivity.class);
            intent.putExtra("button_id", idCinema+1);
            ViewCinema.this.startActivity(intent);
        }};

    View.OnClickListener facebookButtonOnClickListener = new View.OnClickListener(){

        @Override
        public void onClick(View view) {

            Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Content to share");
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
            String shareBody = "#CineMetro#" + DbAdapter.getInstance().getStations().get(idCinema).getName();
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
