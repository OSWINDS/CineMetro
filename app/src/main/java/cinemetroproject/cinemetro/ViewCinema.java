package cinemetroproject.cinemetro;

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

/**
 * Created by kiki__000 on 28-Aug-14.
 *
 *
 * ViewCinema shows the features of a cinema
 */
public class ViewCinema extends ActionBarActivity {

    private LinearLayout scrollView;
    private TextView textViewInfo;
    private ListView listView ;

    protected void onCreate(Bundle savedInstanceState) {
        //setTheme(android.R.style.Theme_Light_Panel);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_cinema);

        scrollView = (LinearLayout)findViewById(R.id.cinemaSV);

        for (int i=0; i<5; i++) {
            Button imageActor = new Button(this);
            imageActor.setBackgroundResource(R.drawable.image_08);
            imageActor.setGravity(Gravity.BOTTOM | Gravity.CENTER);
            imageActor.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            scrollView.addView(imageActor);
        }

        listView = (ListView) findViewById(R.id.list);

        String[] values = new String[] {"It's a \n beautiful day\n Sky falls, \n you feel like\n  It's a \n beautiful day\n Don't let it \n get away"};



        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values);


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
