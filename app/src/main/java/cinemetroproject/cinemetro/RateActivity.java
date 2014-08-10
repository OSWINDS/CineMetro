package cinemetroproject.cinemetro;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.app.AlertDialog;

/**
 * Created by kiki__000 on 03-Aug-14.
 */
public class RateActivity extends ActionBarActivity {

    private Button skipButton;
    private Button goButton;
    private RatingBar ratingBar;
    private AlertDialog.Builder dialog;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //setTheme(android.R.style.Theme_Light_Panel);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);


        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.layout);
        linearLayout.setBackgroundResource(R.drawable.image_08);
        linearLayout.getBackground().setAlpha(128); //opacity gia to Background

        skipButton = (Button) findViewById(R.id.skipButton);
        skipButton.setOnClickListener(skipButtonOnClickListener);

        ratingBar=(RatingBar)findViewById(R.id.ratingBar);
        ratingBar.setOnRatingBarChangeListener(barRatingOnClickListener);

        goButton = (Button) findViewById(R.id.goButton);
        goButton.setOnClickListener(goButtonOnClickListener);
        goButton.setEnabled(false);

        dialog = new AlertDialog.Builder(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.rate, menu);
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

    RatingBar.OnRatingBarChangeListener barRatingOnClickListener = new RatingBar.OnRatingBarChangeListener(){

        public void onRatingChanged(RatingBar ratingBar, float rating,boolean fromUser) {


                goButton.setEnabled(true);



        }};


    View.OnClickListener skipButtonOnClickListener = new View.OnClickListener(){

        @Override
        public void onClick(View view) {

            //kanonika paei sto MapActivity alla epeidi ekei skaei to evala sto LinesActivity gia na to testarw
            Intent intent = new Intent(RateActivity.this, LinesActivity.class);
            RateActivity.this.startActivity(intent);
            finish();
        }};

    View.OnClickListener goButtonOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {

            dialog.setTitle("Points");
            dialog.setMessage("You gave " + ratingBar.getRating() + " points");
            dialog.setPositiveButton("OK",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //kanonika paei sto MapActivity alla epeidi ekei skaei to evala sto LinesActivity gia na to testarw
                    Intent intent = new Intent(RateActivity.this, LinesActivity.class);
                    RateActivity.this.startActivity(intent);
                    finish();

                }
            });
            dialog.setIcon(R.drawable.pressed);
            AlertDialog alert = dialog.create();
            alert.show();


        }};
}
