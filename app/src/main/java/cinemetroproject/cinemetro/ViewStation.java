package cinemetroproject.cinemetro;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by kiki__000 on 20-Jul-14.
 * Se ayto to activity anaparistatai to ViewStation
 * Dimiourgeitai otan epilegetai mia stasi apo LinesActivity
 */
public class ViewStation extends ActionBarActivity {

    private ImageView imageMovie;
    private TextView textViewTitle;
    private LinearLayout inHorizontalScrollView;
    private Button imageActor;
    private TextView textViewDirector;
    private TextView textViewInfo;
    private Button goAheadButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //setTheme(android.R.style.Theme_Light_Panel);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_station);
        inHorizontalScrollView = (LinearLayout)findViewById(R.id.actorsHsw);

        imageMovie =(ImageView)findViewById(R.id.imageMovie);
        imageMovie.setImageResource(R.drawable.image_08);

        textViewTitle =(TextView)findViewById(R.id.titleYear);
        // ------------->>>>>>>>>>>>>>>>>>> edwwwwwwwwwwwwwwwwwwwwwww
        textViewTitle.setText(dbAdapter.getInstance().getMovieByStation(1).getTitle());

        textViewDirector = (TextView)findViewById(R.id.director);
        textViewDirector.setText("Σκηνοθεσία: Γιάννης Δαλιανίδης \n");

        for (int i=0; i<15; i++) {
            Button imageActor = new Button(this);
            imageActor.setBackgroundResource(R.drawable.zervos);
            imageActor.setText("Ζερβος Παντελής");
            imageActor.setGravity(Gravity.BOTTOM | Gravity.CENTER);
            imageActor.setMaxHeight(5);
            imageActor.setMaxWidth(10);

            inHorizontalScrollView.addView(imageActor);
        }

        textViewInfo = (TextView)findViewById(R.id.info);
        textViewInfo.setText("Δύο αδέρφια προσπαθούν να ισορροπήσουν ανάμεσα στις αισθηματικές τους σχέσεις και στις συντηρητικές αρχές της πατρικής οικίας τους. \n" +
                "Εξ ολοκλήρου γυρισμένη στην Θεσσαλονίκη: Πανόραμα, περιοχή Ανθέων, Κέντρο Θεσσαλονίκης");

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
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    View.OnClickListener goAheadButtonOnClickListener = new View.OnClickListener(){

        @Override
        public void onClick(View view) {

            Intent intent = new Intent(ViewStation.this, RateActivity.class);
            ViewStation.this.startActivity(intent);
            finish();
        }};

}
