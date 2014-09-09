package cinemetroproject.cinemetro;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
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
    private TextView textViewInfo;
    private ListView listView ;
    private int idCinema;
    private int countP;
    private ArrayList<Photo> photos;
    private ArrayList<String> photo_names;

    protected void onCreate(Bundle savedInstanceState) {
        //setTheme(android.R.style.Theme_Light_Panel);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_cinema);

        Intent intent = getIntent();
        idCinema = intent.getIntExtra("button_id", 0);

        scrollView = (LinearLayout)findViewById(R.id.cinemaSV);

        photos = dbAdapter.getInstance().getPhotosByStation(idCinema+1);
        if (!photos.isEmpty()) {
            photo_names = new ArrayList<String>();
            for (Photo p : photos) {
                photo_names.add(p.getName());
            }
        }
        countP = photo_names.size();
        for (int i=0; i<photo_names.size(); i++) {
            Button imageActor = new Button(this);
            try {
                Class res = R.drawable.class;
                Field field = res.getField(photo_names.get(i));
                int drawableId = field.getInt(null);
                imageActor.setBackgroundResource(drawableId);
            } catch (Exception e) {

            }
            imageActor.setGravity(Gravity.BOTTOM | Gravity.CENTER);
            imageActor.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            scrollView.addView(imageActor);
        }

        listView = (ListView) findViewById(R.id.list);

        String[] temp = new String[1];
        temp[0] = dbAdapter.getInstance().getStations().get(idCinema).getDescription();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, temp);

        listView.setAdapter(adapter);

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
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
