package cinemetroproject.cinemetro;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Created by kiki__000 on 28-Aug-14.
 *
 *
 * ViewCinema shows the features (photo and text) of a cinema
 */
public class ViewCinema extends ActionBarActivity {

    private LinearLayout scrollView;
    private TextView description;
    private int idCinema;
    private int countP;
    private ArrayList<Photo> photos;
    private ArrayList<String> photo_names;

    protected void onCreate(Bundle savedInstanceState) {
        //setTheme(android.R.style.Theme_Light_Panel);
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_view_cinema);

        Intent intent = getIntent();
        idCinema = intent.getIntExtra("button_id", 0);

        scrollView = (LinearLayout)findViewById(R.id.cinemaHsV);

        photos = dbAdapter.getInstance().getPhotosByStation(idCinema+1);
        if (!photos.isEmpty()) {
            photo_names = new ArrayList<String>();
            for (Photo p : photos) {
                photo_names.add(p.getName());
            }
        }
        countP = photo_names.size();
        for (int i=0; i<photo_names.size(); i++) {
            Button imageCinema = new Button(this);
            try {
                Class res = R.drawable.class;
                Field field = res.getField(photo_names.get(i));
                int drawableId = field.getInt(null);
                imageCinema.setBackgroundResource(drawableId);
            } catch (Exception e) {

            }
            imageCinema.setGravity(Gravity.BOTTOM | Gravity.CENTER);
            imageCinema.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            scrollView.addView(imageCinema);
        }

        description = (TextView)findViewById(R.id.description);
        description.setText(dbAdapter.getInstance().getStations().get(idCinema).getDescription());
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
}
