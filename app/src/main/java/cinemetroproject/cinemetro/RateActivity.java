package cinemetroproject.cinemetro;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.content.Context;
import android.app.AlertDialog;

/**
 * Created by kiki__000 on 03-Aug-14.
 */
public class RateActivity extends ActionBarActivity {

    private Button skipButton;
    private Button goButton;
    private ToggleButton star1;
    private ToggleButton star2;
    private ToggleButton star3;
    private ToggleButton star4;
    private ToggleButton star5;
    private AlertDialog.Builder dialog;
    private int points=0; //oi pontoi pou dinei o xristis

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //setTheme(android.R.style.Theme_Light_Panel);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);

        skipButton = (Button) findViewById(R.id.skipButton);
        skipButton.setOnClickListener(skipButtonOnClickListener);

        goButton = (Button) findViewById(R.id.goButton);
        goButton.setOnClickListener(goButtonOnClickListener);
        goButton.setEnabled(false);

        star1 = (ToggleButton)findViewById(R.id.star1);
        star1.setOnClickListener(starsOnClickListener);
        star2 = (ToggleButton)findViewById(R.id.star2);
        star2.setOnClickListener(starsOnClickListener);
        star3 = (ToggleButton)findViewById(R.id.star3);
        star3.setOnClickListener(starsOnClickListener);
        star4 = (ToggleButton)findViewById(R.id.star4);
        star4.setOnClickListener(starsOnClickListener);
        star5 = (ToggleButton)findViewById(R.id.star5);
        star5.setOnClickListener(starsOnClickListener);

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

    public int getPoints() {return points;}

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

            if (star1.isChecked())
                points++;
            if (star2.isChecked())
                points++;
            if (star3.isChecked())
                points++;
            if (star4.isChecked())
                points++;
            if (star5.isChecked())
                points++;

            dialog.setTitle("Points");
            dialog.setMessage("You gave" + points + " points");
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
            //dialog.show();
            AlertDialog alert = dialog.create();
            alert.show();


        }};

    View.OnClickListener starsOnClickListener = new View.OnClickListener(){

        @Override
        public void onClick(View view) {

            if (star1.isChecked() || star2.isChecked() || star3.isChecked() || star4.isChecked() || star5.isChecked()){
                goButton.setClickable(true);
                goButton.setEnabled(true);
            }
            else{
                goButton.setEnabled(false);
            }
        }};

    View.OnClickListener okOnClickListener = new View.OnClickListener(){

        @Override
        public void onClick(View view) {

             //kanonika paei sto MapActivity alla epeidi ekei skaei to evala sto LinesActivity gia na to testarw
             Intent intent = new Intent(RateActivity.this, LinesActivity.class);
             RateActivity.this.startActivity(intent);
             finish();
        }};



}
