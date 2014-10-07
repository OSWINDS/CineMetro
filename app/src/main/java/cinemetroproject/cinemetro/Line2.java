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

public class Line2 extends ActionBarActivity {


    private LinearLayout scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.line2);
        scrollView = (LinearLayout)findViewById(R.id.scrollView);

        for (int i=0; i<8; i++) {
            Button stationButton = new Button(this);
            stationButton.setText("Στάση "+(i+1)+"\n"+ DbAdapter.getInstance().getMovies().get(i).getTitle());
            stationButton.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            stationButton.setId(i + 8);
            stationButton.setWidth(100);
            stationButton.setHeight(150);
            //stationButton.setBackgroundColor(Color.parseColor("#dee6ef"));
           stationButton.setCompoundDrawablesWithIntrinsicBounds( +DbAdapter.getInstance().getPhotoDrawableID("green"+Integer.toString(i+1)), 0, 0, 0);
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


            Intent intent = new Intent(Line2.this, ViewStation.class);
            intent.putExtra("button_id", view.getId());
            Line2.this.startActivity(intent);
        }};
}
