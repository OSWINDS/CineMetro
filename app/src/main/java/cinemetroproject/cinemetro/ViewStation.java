package cinemetroproject.cinemetro;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by kiki__000 on 20-Jul-14.
 * Se ayto to activity anaparistatai to ViewStation
 * Dimiourgeitai otan epilegetai mia stasi apo LinesActivity
 */
public class ViewStation extends ActionBarActivity {

    private ImageView image;
    private TextView textViewTitle;
    private TextView textViewDirector;
    private TextView textViewInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //setTheme(android.R.style.Theme_Light_Panel);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_station);

        image =(ImageView)findViewById(R.id.imageView);
        image.setImageResource(R.drawable.image_08);

        textViewTitle =(TextView)findViewById(R.id.titleYear);
        textViewTitle.setText("Ατσίδας 1961 \n");

        textViewDirector = (TextView)findViewById(R.id.director);
        textViewDirector.setText("Σκηνοθεσία: Γιάννης Δαλιανίδης \n");

        textViewInfo = (TextView)findViewById(R.id.info);
        textViewInfo.setText("Δύο αδέρφια προσπαθούν να ισορροπήσουν ανάμεσα στις αισθηματικές τους σχέσεις και στις συντηρητικές αρχές της πατρικής οικίας τους. \n" +
                "Εξ ολοκλήρου γυρισμένη στην Θεσσαλονίκη: Πανόραμα, περιοχή Ανθέων, Κέντρο Θεσσαλονίκης");



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




}