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

import java.util.ArrayList;

/**
 * Created by vivi dimitris on 10/9/2014.
 */
public class Line1 extends ActionBarActivity {

    private LinearLayout scrollView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.line1);

        scrollView = (LinearLayout)findViewById(R.id.scrollView);

        for (int i=0; i<6; i++) {
            Button cinemaButton = new Button(this);
            cinemaButton.setText("Στάση "+(i+1)+"\n"+dbAdapter.getInstance().getStations().get(i).getName());
            cinemaButton.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            cinemaButton.setId(i);
            cinemaButton.setWidth(100);
            cinemaButton.setHeight(150);
            //cinemaButton.setBackgroundColor(Color.parseColor("#dee6ef"));
            if(i==0){
                cinemaButton.setCompoundDrawablesWithIntrinsicBounds( R.drawable.green1, 0, 0, 0);}
            if(i==1){
                cinemaButton.setCompoundDrawablesWithIntrinsicBounds( R.drawable.green2 , 0, 0, 0);}
            if(i==2){
                cinemaButton.setCompoundDrawablesWithIntrinsicBounds( R.drawable.green3 , 0, 0, 0);}
            if(i==3){
                cinemaButton.setCompoundDrawablesWithIntrinsicBounds( R.drawable.green4 , 0, 0, 0);}
            if(i==4){
                cinemaButton.setCompoundDrawablesWithIntrinsicBounds( R.drawable.green5 , 0, 0, 0);}
            if(i==5){
                cinemaButton.setCompoundDrawablesWithIntrinsicBounds( R.drawable.green6, 0, 0, 0);}
            if(i==6){
                cinemaButton.setCompoundDrawablesWithIntrinsicBounds( R.drawable.green7, 0, 0, 0);}
            if(i==7){
                cinemaButton.setCompoundDrawablesWithIntrinsicBounds( R.drawable.green8 , 0, 0, 0);}
            cinemaButton.setOnClickListener(cinemaButtonOnClickListener);
            scrollView.addView(cinemaButton);
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


