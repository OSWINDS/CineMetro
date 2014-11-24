package cinemetroproject.cinemetro;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by kiki__000 on 28-Aug-14.
 *
 *
 * ViewCinema shows the features (photo and text) of a cinema
 */
public class ViewCinema extends FragmentActivity {

    private TextView textViewTitle;
    private TextView description;
    private ImageButton showInMap;
    private ImageButton goAheadButton;
    private ImageButton share;
    private Button facebookButton;
    private Button twitterButton;
    private Button instagramButton;
    private Button pinterestButton;
    private AlertDialog.Builder dialog;
    private int idCinema;
    private int countP;
    private LinearLayout layout1;
    private LinearLayout general_layout;
    private ViewFlipper vf;

    protected void onCreate(Bundle savedInstanceState) {
        //setTheme(android.R.style.Theme_Light_Panel);
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_view_cinema);
        vf = (ViewFlipper) findViewById(R.id.vf);

        Intent intent = getIntent();
        idCinema = intent.getIntExtra("button_id", 0);
        layout1 = (LinearLayout)findViewById(R.id.layout1);
        general_layout = (LinearLayout)findViewById(R.id.general_layout);

        textViewTitle =(TextView)findViewById(R.id.name);
        textViewTitle.setText(DbAdapter.getInstance().getStations().get(idCinema).getName());


        /** Getting a reference to the ViewPager defined the layout file */
        //ViewPager pager = (ViewPager) findViewById(R.id.pagerImage);

        /** Getting fragment manager */
        FragmentManager fm = getSupportFragmentManager();

        countP = DbAdapter.getInstance().getPhotosByStation(idCinema+1).size();
        /** Instantiating FragmentPagerAdapter */
        ImageAdapter pagerAdapter = new ImageAdapter(fm, countP/2);
        ImageFragment.id=idCinema;
        ImageFragment.line=1;

        /** Setting the pagerAdapter to the pager object */
        //   pager.setAdapter(pagerAdapter);

        description = (TextView)findViewById(R.id.description);
        description.setText(DbAdapter.getInstance().getStations().get(idCinema).getDescription());

        showInMap = (ImageButton)findViewById(R.id.showInMap);
        showInMap.setOnClickListener(showInMapButtonOnClickListener);

        goAheadButton =(ImageButton)findViewById(R.id.go_ahead);
        goAheadButton.setOnClickListener(goAheadButtonOnClickListener);

        share = (ImageButton) findViewById(R.id.share);
        share.setOnClickListener(ShareButtonOnClickListener);



        facebookButton = (Button) findViewById(R.id.facebook_button);
        facebookButton.setOnClickListener(new sharingOnClickListener("facebook"));
        facebookButton.setVisibility(View.INVISIBLE);

        twitterButton = (Button) findViewById(R.id.twitter_button);
        twitterButton.setOnClickListener(new sharingOnClickListener("twitter"));
        twitterButton.setVisibility(View.INVISIBLE);

        instagramButton = (Button) findViewById(R.id.instagram_button);
        instagramButton.setOnClickListener(new sharingOnClickListener("instagram"));
        instagramButton.setVisibility(View.INVISIBLE);

        pinterestButton = (Button) findViewById(R.id.pinterest_button);
        pinterestButton.setOnClickListener(new sharingOnClickListener("pinterest"));
        pinterestButton.setVisibility(View.INVISIBLE);
        general_layout = (LinearLayout)findViewById(R.id.general_layout);

        int r=DbAdapter.getInstance().getPhotosByStation(idCinema+1).size();
        for (int i = 0; i < r; i++) {
            try {
                Class res = R.drawable.class;
                Field field = res.getField(DbAdapter.getInstance().getPhotosByStation(idCinema+1).get(i).getName());
                int drawableId = field.getInt(null);
                ImageView imageView = new ImageView(ViewCinema.this);
                imageView.setImageResource(drawableId);
                vf.addView(imageView);

            } catch (Exception e) {}
        }


        vf.setAutoStart(true);
        vf.setFlipInterval(4000);
        vf.setInAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
        vf.setOutAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_out));
        vf.startFlipping();

        dialog = new AlertDialog.Builder(this);

    }

    View.OnClickListener ShareButtonOnClickListener  = new View.OnClickListener() {

        @Override
        public void onClick(View view) {



            layout1.setBackgroundColor(Color.parseColor("#ff8f0101"));
            share.setVisibility(View.INVISIBLE);
            goAheadButton.setVisibility(View.INVISIBLE);
            showInMap.setVisibility(View.INVISIBLE);
            facebookButton.setVisibility(View.VISIBLE);
            twitterButton.setVisibility(View.VISIBLE);
            instagramButton.setVisibility(View.VISIBLE);
            pinterestButton.setVisibility(View.VISIBLE);
            general_layout.setOnClickListener(layoutOnClickListener);

        }
    };

    View.OnClickListener layoutOnClickListener  = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            layout1.setBackgroundColor(Color.parseColor("#ff8f0101"));
            share.setVisibility(View.VISIBLE);
            goAheadButton.setVisibility(View.VISIBLE);
            showInMap.setVisibility(View.VISIBLE);
            facebookButton.setVisibility(View.INVISIBLE);
            twitterButton.setVisibility(View.INVISIBLE);
            instagramButton.setVisibility(View.INVISIBLE);
            pinterestButton.setVisibility(View.INVISIBLE);


        }
    };
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
        String shareBody = "#CineMetro#" + DbAdapter.getInstance().getStations().get(idCinema).getName();
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "CineMetro");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    View.OnClickListener showInMapButtonOnClickListener = new View.OnClickListener(){

        @Override
        public void onClick(View view) {

            MapActivity.showInMap(1, idCinema+1);

            Intent intent = new Intent(ViewCinema.this, MapActivity.class);
            ViewCinema.this.startActivity(intent);
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
            else if (DbAdapter.getInstance().getUserRatingForStation(idCinema, DbAdapter.getInstance().getActiveUser().getId()) !=0){
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
                Intent intent = new Intent(ViewCinema.this, RateActivity.class);
                intent.putExtra("line", 1);
                intent.putExtra("button_id", idCinema+1);
                ViewCinema.this.startActivity(intent);
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
            shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, "#CineMetro#" + DbAdapter.getInstance().getStations().get(idCinema).getName());
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
        }
    }


}
