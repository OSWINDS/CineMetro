package cinemetroproject.cinemetro;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.lang.reflect.Field;

/**
 * Created by kiki__000 on 20-Jul-14.
 * Se ayto to activity anaparistatai to ViewStation
 * Dimiourgeitai otan epilegetai mia stasi apo LinesActivity
 */
public class ViewStation extends ActionBarActivity  {

    private int idStation;
    private int actors; //count of actors
    private ImageView imageMovie;
    private TextView textViewTitle;
    private LinearLayout inHorizontalScrollView;
    private TextView textViewDirector;
    private TextView textViewInfo;
    private Button goAheadButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //setTheme(android.R.style.Theme_Light_Panel);
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_view_station);

        Intent intent = getIntent();
        idStation = intent.getIntExtra("button_id", 0);

        actors = dbAdapter.getInstance().getMovieByStation(idStation).getActors().size();

        inHorizontalScrollView = (LinearLayout)findViewById(R.id.actorsHsw);

        imageMovie =(ImageView)findViewById(R.id.imageMovie);
        try {
            Class res = R.drawable.class;
            Field field = res.getField((dbAdapter.getInstance().getPhotosByStation(idStation).get(0).getName()));
            int drawableId = field.getInt(null);
            imageMovie.setImageResource(drawableId);
            imageMovie.getLayoutParams().height = 250;
        } catch (Exception e) {}

        textViewTitle =(TextView)findViewById(R.id.titleYear);
        textViewTitle.setText(dbAdapter.getInstance().getMovieByStation(idStation).getTitle() + " " + dbAdapter.getInstance().getMovieByStation(idStation).getYear() + "\n");

        textViewDirector = (TextView)findViewById(R.id.director);
        textViewDirector.setText("Σκηνοθεσία: " + dbAdapter.getInstance().getMovieByStation(idStation).getDirector() + "\n");

        for (int i=0; i<actors; i++) {
            Button imageActor = new Button(this);
            try {
                Class res = R.drawable.class;
                Field field = res.getField(dbAdapter.getInstance().getActorPhotosOfMovie(idStation-6).get(i).getName());
                int drawableId = field.getInt(null);
                imageActor.setBackgroundResource(drawableId);
                imageActor.setLayoutParams(new ViewGroup.LayoutParams(160,150));
            } catch (Exception e) {}
            String string = dbAdapter.getInstance().getMovieByStation(idStation).getActors().get(i);
            String[] parts = string.split(" ");
            imageActor.setText(parts[0] + "\n" + parts[1]);
            imageActor.setTextSize(16);
            imageActor.setTextColor(Color.BLACK);
            imageActor.setGravity(Gravity.BOTTOM | Gravity.CENTER);

            inHorizontalScrollView.addView(imageActor);

        }

        textViewInfo = (TextView)findViewById(R.id.info);
        textViewInfo.setText(dbAdapter.getInstance().getMovieByStation(idStation).getDescription());

        goAheadButton = (Button) findViewById(R.id.go_ahead_button);
        goAheadButton.setOnClickListener(goAheadButtonOnClickListener);
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

            Intent intent = new Intent(ViewStation.this, RateActivity.class);
            intent.putExtra("button_id", idStation);
            ViewStation.this.startActivity(intent);
        }};


}
