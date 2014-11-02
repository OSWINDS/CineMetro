package cinemetroproject.cinemetro;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Instrumentation;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;


public class ProfileActivity extends ActionBarActivity {
    ImageButton logo;
    Button logOut, updatebt;
    private TextView email, points, red ,blue, green;
    private boolean readyToLogOut;
    private int update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_profile);

         logo=(ImageButton)findViewById(R.id.logo);
        logo.setBackgroundResource(R.drawable.logo_background);

        email=(TextView) findViewById(R.id.email2);
        points=(TextView) findViewById(R.id.total_points2);
        red=(TextView) findViewById(R.id.red_line);
        blue=(TextView) findViewById(R.id.blue_line);
        green=(TextView) findViewById(R.id.green_line);

        email.setText(DbAdapter.getInstance().getActiveUser().getUsername());
        points.setText("");
        red.setText("    Red Line:      ");
        blue.setText("    Blue Line:     ");
        green.setText("    Green Line:    ");

        update=0;
        readyToLogOut=false;

        logOut = (Button) findViewById(R.id.log_out);
        updatebt = (Button) findViewById(R.id.update_button);


        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOutClicked();
            }
        });
        updatebt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateClicked();
            }
        });
    }

    public void updateClicked(){
        update++;
        readyToLogOut=true;
        //DbAdapter.getInstance().updateUserToParse(DbAdapter.getInstance().getActiveUser());
    }

    public void logOutClicked() {

        if(update == 0){
            new AlertDialog.Builder(this).setTitle("Warning")
                    .setMessage("If you sign out without updating the database you will lose" +
                            " all the points you gave after your last login." +
                            "\n\nAre you sure you want to log out?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    signOut();
                }
            }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            }).show();
        }

        if(readyToLogOut){
            signOut();
        }
    }

    public void signOut(){
        DbAdapter.getInstance().setActiveUser(null);
        //update=0;

        Intent intent;
        intent = new Intent(ProfileActivity.this, LogIn.class);

        startActivity(intent);

        this.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.profile, menu);
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
