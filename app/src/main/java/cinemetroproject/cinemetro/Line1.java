package cinemetroproject.cinemetro;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by vivi  on 10/9/2014.
 */
public class Line1 extends ActionBarActivity {

    private LinearLayout scrollView;
    private ArrayList<String> titles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lines);

        LinearLayout ll=(LinearLayout)findViewById(R.id.linear_layout_lines);
        ll.setBackgroundColor(Color.parseColor("#ff8f0101"));

        TextView tv=(TextView)findViewById(R.id.lines_textView);
        tv.setText(R.string.line1_title);

        titles=new ArrayList<String>();

        for(int i=0; i<7; i++){
            titles.add(DbAdapter.getInstance().getStations().get(i).getName());
        }

        showList();
    }

    private void showList(){
        ArrayAdapter<String> adapter=new MyArrayAdapter();
        ListView list=(ListView) findViewById(R.id.lines_listview);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //TextView textView = (TextView) view;
                Intent intent = new Intent(Line1.this, ViewCinema.class);
                intent.putExtra("button_id", i);
                Line1.this.startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.lines, menu);
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

    View.OnClickListener cinemaButtonOnClickListener = new View.OnClickListener(){

        @Override
        public void onClick(View view) {

            Intent intent = new Intent(Line1.this, ViewCinema.class);
            intent.putExtra("button_id", view.getId());
            Line1.this.startActivity(intent);
        }};

    private class MyArrayAdapter extends ArrayAdapter<String> {
        public MyArrayAdapter() {
            super(Line1.this, R.layout.lines_item, titles);
        }

        @Override
        public View getView(int pos, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.lines_item, parent, false);
            }

            ImageView image = (ImageView) itemView.findViewById(R.id.thumbnail);
            image.setBackgroundResource(DbAdapter.getInstance().getPhotoDrawableID(DbAdapter.getInstance().getPhotosByStation(pos+1).get(0).getName()));

            TextView station = (TextView) itemView.findViewById(R.id.station_number);
            station.setTextColor(Color.parseColor("#ff8f0101"));
            station.setText(R.string.station_text);
            station.append(" " + (pos+1));

            TextView title = (TextView) itemView.findViewById(R.id.station_title);
            title.setText(titles.get(pos));

            return itemView;
        }
    }
}


