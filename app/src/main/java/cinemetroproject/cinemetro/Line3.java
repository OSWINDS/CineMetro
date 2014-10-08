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
 * Created by vivi dimitris on 8/9/2014.
 */
public class Line3 extends ActionBarActivity {


    private LinearLayout scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.line3);
        scrollView = (LinearLayout)findViewById(R.id.scrollView);

        for (int i=0; i<5; i++) {
            Button stationButton = new Button(this);
            stationButton.setText("Στάση "+(i+1)+"\n"+ DbAdapter.getInstance().getTimelineStations().get(i).getName().toLowerCase());
            stationButton.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            stationButton.setId(i + 15);
            stationButton.setWidth(100);
            stationButton.setHeight(150);
            //stationButton.setBackgroundColor(Color.parseColor("#dee6ef"));
            stationButton.setCompoundDrawablesWithIntrinsicBounds( +DbAdapter.getInstance().getPhotoDrawableID("prasino"+Integer.toString(i+1)), 0, 0, 0);

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


           Intent intent = new Intent(Line3.this, Timeline.class);
           intent.putExtra("button_id", view.getId());
           Line3.this.startActivity(intent);
        }};
}
