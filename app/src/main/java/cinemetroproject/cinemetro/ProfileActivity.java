package cinemetroproject.cinemetro;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Instrumentation;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.widget.Toast;

import java.util.ArrayList;


public class ProfileActivity extends ActionBarActivity {
    ImageButton logo;
    Button logOut, updatebt;
    private TextView email, points, red ,blue, green;
    private boolean readyToLogOut;

    //private int update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().setTitle(getResources().getString(R.string.title_activity_profile));
        setContentView(R.layout.activity_profile);



        email=(TextView) findViewById(R.id.email2);
        points=(TextView) findViewById(R.id.total_points2);
        red=(TextView) findViewById(R.id.red_line);
        blue=(TextView) findViewById(R.id.blue_line);
        green=(TextView) findViewById(R.id.green_line);


        email.setText(DbAdapter.getInstance().getActiveUser().getUsername());


        float rating = DbAdapter.getInstance().getSumForRouteRatings(MapActivity.LINE1, DbAdapter.getInstance().getActiveUser().getUsername());
        float sum = rating;
        red.setText(getResources().getString(R.string.red) + rating);
        rating = DbAdapter.getInstance().getSumForRouteRatings(MapActivity.LINE2, DbAdapter.getInstance().getActiveUser().getUsername());
        sum += rating;
        blue.setText(getResources().getString(R.string.blue) + rating);
        rating = DbAdapter.getInstance().getSumForRouteRatings(MapActivity.LINE3, DbAdapter.getInstance().getActiveUser().getUsername());
        sum += rating;
        green.setText(getResources().getString(R.string.green) + rating);


        points.setText(" "+sum);

        readyToLogOut=false;


        updatebt = (Button) findViewById(R.id.update_button);


        updatebt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateClicked();
            }
        });
    }

    public void updateClicked(){
        ConnectivityManager connec = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connec != null &&
                (connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED) ||
                (connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED)) {
            //You are connected
            DbAdapter.getInstance().updateUserToParse(DbAdapter.getInstance().getActiveUser());
            Toast.makeText(ProfileActivity.this, R.string.database_update, Toast.LENGTH_SHORT).show();
            readyToLogOut=true;
        } else if (connec.getNetworkInfo(0).getState() == NetworkInfo.State.DISCONNECTED ||
                connec.getNetworkInfo(1).getState() == NetworkInfo.State.DISCONNECTED) {
            //Not connected.
            Toast.makeText(ProfileActivity.this, R.string.no_connection, Toast.LENGTH_SHORT).show();
            readyToLogOut = false;
            //return;
        }
    }



    public void signOut(){
        if(!readyToLogOut){
            return;
        }

        DbAdapter.getInstance().setActiveUser(null);
        //update=0;
        Toast.makeText(ProfileActivity.this, R.string.bye, Toast.LENGTH_SHORT).show();

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
        if (id == R.id.sign_out) {
            new AlertDialog.Builder(this).setTitle(R.string.warning)
                    .setMessage(R.string.logout_message)
                    .setPositiveButton(R.string.positive_button, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            updateClicked();
                            signOut();
                        }
                    }).setNegativeButton(R.string.negative_button, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    readyToLogOut=true;
                    signOut();
                }
            }).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}