package cinemetroproject.cinemetro;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.sql.Time;
import java.util.List;

/**
 * Created by vivi dimitris on 22/9/2014.
 */
public class Timeline extends ActionBarActivity {

    private RadioButton[] rb = new RadioButton[10];//for now


    private LinearLayout scrollView;
    private LinearLayout mylayout;
    private TextView description;
    private RadioGroup rg;
    private RadioButton radioButton;
    private ImageView image;
    private View view;
    private Button prevstation;
    private int idCinema;
    private int selectedId;
    private TextView title;
    private ImageButton showInMap;
    private Button goAheadButton;
    private Button facebookButton;
    private Button twitterButton;
    private Button instagramButton;
    private Button pinterestButton;
    private AlertDialog.Builder dialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //full screen to timeline
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_timeline);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //create radiogroup
        rg = (RadioGroup) findViewById(R.id.radiogroup);
        rg.setOrientation(RadioGroup.VERTICAL);

        scrollView = (LinearLayout)findViewById(R.id.scrollView);
        mylayout = (LinearLayout)findViewById(R.id.mylayout);

        Intent intent = getIntent();
        idCinema = intent.getIntExtra("button_id", 0);
        title=(TextView)findViewById(R.id.title);
        title.setText(DbAdapter.getInstance().getTimelineStations().get(idCinema-15).getName());
        description = (TextView) findViewById(R.id.description);
        description.setText(DbAdapter.getInstance().getTimelineStationMilestones(idCinema-14).get(0).getDescription());
        image=(ImageView)findViewById(R.id.image);
       // rb[0].isChecked();
       try {
            Class res = R.drawable.class;
            Field field = res.getField(DbAdapter.getInstance().getTimelineStationMilestones(idCinema-14).get(0).getPhotoName());
            int drawableId = field.getInt(null);
            image.setBackgroundResource(drawableId);

        } catch (Exception e) {}
        int millestones= DbAdapter.getInstance().getTimelineStationMilestones(idCinema-14).size();

        for (int i=0; i<millestones; i++) {
            rb[i]  = new RadioButton(this);
            rb[i].setText(DbAdapter.getInstance().getTimelineStationMilestones(idCinema-14).get(i).getYear());
            rb[i].setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            rb[i].setId(i);
            rb[i].setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {


                    // get selected radio button from radioGroup
                     selectedId = rg.getCheckedRadioButtonId();

                    // find the radiobutton by returned id
                    radioButton = (RadioButton) findViewById(selectedId);
                    description = (TextView) findViewById(R.id.description);
                    description.setText(DbAdapter.getInstance().getTimelineStationMilestones(idCinema-14).get(selectedId).getDescription());
                    image=(ImageView)findViewById(R.id.image);

                    try {
                        Class res = R.drawable.class;
                        Field field = res.getField(DbAdapter.getInstance().getTimelineStationMilestones(idCinema-14).get(selectedId).getPhotoName());
                        int drawableId = field.getInt(null);
                        image.setBackgroundResource(drawableId);

                    } catch (Exception e) {}




                }


            });;
            rg.addView(rb[i]);
        }

        showInMap = (ImageButton)findViewById(R.id.showInMap);
        showInMap.setOnClickListener(showInMapButtonOnClickListener);

        goAheadButton = (Button) findViewById(R.id.go_ahead_button);
        goAheadButton.setOnClickListener(goAheadButtonOnClickListener);

        facebookButton = (Button) findViewById(R.id.facebook_button);
        facebookButton.setOnClickListener(new sharingOnClickListener("facebook"));

        twitterButton = (Button) findViewById(R.id.twitter_button);
        twitterButton.setOnClickListener(new sharingOnClickListener("twitter"));

        instagramButton = (Button) findViewById(R.id.instagram_button);
        instagramButton.setOnClickListener(new sharingOnClickListener("instagram"));

        pinterestButton = (Button) findViewById(R.id.pinterest_button);
        pinterestButton.setOnClickListener(new sharingOnClickListener("pinterest"));

        dialog = new AlertDialog.Builder(this);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.timeline, menu);
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
        String shareBody = "#CineMetro#" + DbAdapter.getInstance().getTimelineStations().get(idCinema-15).getName();
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "CineMetro");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    View.OnClickListener showInMapButtonOnClickListener = new View.OnClickListener(){

        @Override
        public void onClick(View view) {

            MapActivity.showInMap(1, idCinema);

            Intent intent = new Intent(Timeline.this, MapActivity.class);
            Timeline.this.startActivity(intent);
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
                AlertDialog alert = dialog.create();
                alert.show();
            }
            //user has already rated for this station
            else if (DbAdapter.getInstance().getUserRatingForStation(idCinema, DbAdapter.getInstance().getActiveUser().getId()) != -1){
                dialog.setTitle(getResources().getString(R.string.title_activity_RateActivity));
                dialog.setMessage(getResources().getString(R.string.already_rate));
                dialog.setPositiveButton("OK",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialog.setCancelable(true);
                    }
                });
                AlertDialog alert = dialog.create();
                alert.show();
            }
            //user can rate
            else {
                Intent intent = new Intent(Timeline.this, RateActivity.class);
                intent.putExtra("line", 3);
                intent.putExtra("button_id", idCinema-14);
                Timeline.this.startActivity(intent);
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
            shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, "#CineMetro#" + DbAdapter.getInstance().getTimelineStations().get(idCinema-15).getName());
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
