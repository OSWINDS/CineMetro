package cinemetroproject.cinemetro;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class Line2 extends ActionBarActivity {


    private LinearLayout scrollView;
    private ImageView image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.line2);



        scrollView = (LinearLayout)findViewById(R.id.scrollView);

        for (int i=0; i<8; i++) {
            Button stationButton = new Button(this);

            stationButton.setText("Στάση"+(i+1)+"\n"+dbAdapter.getInstance().getMovies().get(i).getTitle());
            stationButton.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            stationButton.setId(i + 7);
            stationButton.setWidth(100);
            stationButton.setHeight(150);
            //stationButton.setBackgroundColor(Color.parseColor("#dee6ef"));
            if(i==0){

                stationButton.setCompoundDrawablesWithIntrinsicBounds( R.drawable.green1, 0, 0, 0);}
            if(i==1){
                stationButton.setCompoundDrawablesWithIntrinsicBounds( R.drawable.green2 , 0, 0, 0);}
            if(i==2){
                stationButton.setCompoundDrawablesWithIntrinsicBounds( R.drawable.green3 , 0, 0, 0);}
            if(i==3){
                stationButton.setCompoundDrawablesWithIntrinsicBounds( R.drawable.green4 , 0, 0, 0);}
            if(i==4){
                stationButton.setCompoundDrawablesWithIntrinsicBounds( R.drawable.green5 , 0, 0, 0);}
            if(i==5){
                stationButton.setCompoundDrawablesWithIntrinsicBounds( R.drawable.green6, 0, 0, 0);}
            if(i==6){
                stationButton.setCompoundDrawablesWithIntrinsicBounds( R.drawable.green7, 0, 0, 0);}
            if(i==7){
                stationButton.setCompoundDrawablesWithIntrinsicBounds( R.drawable.green8 , 0, 0, 0);}









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
            finish();
        }};
}
