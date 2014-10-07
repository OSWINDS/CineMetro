package cinemetroproject.cinemetro;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * Created by vivi  on 10/9/2014.
 */
public class Line1 extends ActionBarActivity {

    private LinearLayout scrollView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.line1);


        scrollView = (LinearLayout)findViewById(R.id.scrollView);

        for (int i=0; i<7; i++) {
            Button cinemaButton = new Button(this);
            cinemaButton.setText("Στάση "+(i+1)+"\n"+ DbAdapter.getInstance().getStations().get(i).getName());
            cinemaButton.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            cinemaButton.setId(i);
            cinemaButton.setWidth(100);
            cinemaButton.setHeight(150);
            cinemaButton.setCompoundDrawablesWithIntrinsicBounds( +DbAdapter.getInstance().getPhotoDrawableID("red"+Integer.toString(i+1)), 0, 0, 0);

          //  cinemaButton.setBackgroundColor(Color.parseColor("#debebb"));



            cinemaButton.setOnClickListener(cinemaButtonOnClickListener);

            scrollView.addView(cinemaButton);
           // scrollView.setBackgroundColor(Color.parseColor("060606"));
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

    View.OnClickListener cinemaButtonOnClickListener = new View.OnClickListener(){

        @Override
        public void onClick(View view) {

            Intent intent = new Intent(Line1.this, ViewCinema.class);
            intent.putExtra("button_id", view.getId());
            Line1.this.startActivity(intent);
        }};
}


