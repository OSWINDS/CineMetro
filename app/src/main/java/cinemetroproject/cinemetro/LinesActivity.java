package cinemetroproject.cinemetro;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import cinemetroproject.cinemetro.R;

public class LinesActivity extends ActionBarActivity {


    private LinearLayout scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_lines);

        scrollView = (LinearLayout)findViewById(R.id.scrollView);

        //cinema
        for (int i=0; i<6; i++) {
            Button cinemaButton = new Button(this);
            cinemaButton.setText(dbAdapter.getInstance().getStations().get(i).getName());
            cinemaButton.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            cinemaButton.setId(i);
            cinemaButton.setOnClickListener(cinemaButtonOnClickListener);
            scrollView.addView(cinemaButton);
        }

        //movies
        for (int i=0; i<8; i++) {
            Button stationButton = new Button(this);
            stationButton.setText(dbAdapter.getInstance().getMovies().get(i).getTitle());
            stationButton.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            stationButton.setId(i+7);
            stationButton.setOnClickListener(stationButtonOnClickListener);
            scrollView.addView(stationButton);
        }



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

    View.OnClickListener stationButtonOnClickListener = new View.OnClickListener(){

        @Override
        public void onClick(View view) {


            Intent intent = new Intent(LinesActivity.this, ViewStation.class);
            intent.putExtra("button_id", view.getId());
            LinesActivity.this.startActivity(intent);
        }};

    View.OnClickListener cinemaButtonOnClickListener = new View.OnClickListener(){

        @Override
        public void onClick(View view) {


            Intent intent = new Intent(LinesActivity.this, ViewCinema.class);
            intent.putExtra("button_id", view.getId());
            LinesActivity.this.startActivity(intent);
        }};
}
