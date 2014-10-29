package cinemetroproject.cinemetro;

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
    Button logOut;
    TextView email, points, red ,blue, green;
    static User connectedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.profile);

        logo=(ImageButton)findViewById(R.id.logo);
        logo.setBackgroundResource(R.drawable.logo_background);

        email=(TextView) findViewById(R.id.email);
        points=(TextView) findViewById(R.id.total_points);
        red=(TextView) findViewById(R.id.red_line);
        blue=(TextView) findViewById(R.id.blue_line);
        green=(TextView) findViewById(R.id.green_line);

        email.setText("Email: " + connectedUser.getUsername());
        points.setText("Total Points: " + "3");
        red.setText("    Red Line:       " + "2");
        blue.setText("    Blue Line:      " + "");
        green.setText("    Green Line:   " + "1");

        logOut = (Button) findViewById(R.id.log_out);

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOutClicked();
            }
        });

        /*ScrollView sv = (ScrollView) this.findViewById(R.id.scrollView);
        LinearLayout ll=new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));

        String str;
        ArrayList<Route> routes = DbAdapter.getInstance().getRoutes();

        for (int i = 0; i < routes.size(); i++) {

            str = routes.get(i).getName() + "\n";
            TextView tv = new TextView(this);
            tv.setText(str);
            ll.addView(tv);

            ArrayList<Station> stations = DbAdapter.getInstance().getStationByRoute(routes.get(i).getId());

            for (int j = 0; j < stations.size(); j++) {
                str = stations.get(j).getName();
                CheckBox cb = new CheckBox(this);
                cb.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
                cb.setClickable(false);
                cb.setText(str);
                cb.setSelected(true);
               //ll.addView(cb);
            }
        }
        sv.addView(ll);

        Button bt = (Button) findViewById(R.id.bt_logIn_test);
        bt.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent;
                        intent = new Intent(ProfileActivity.this, LogIn.class);
                        ProfileActivity.this.startActivity(intent);
                    }
                }
        );*/
    }

    public void logOutClicked() {

        connectedUser=new User();
        Intent intent;
        intent = new Intent(ProfileActivity.this, LogIn.class);
        startActivity(intent);
    }

    public static void getConnectedUser(User user){
        connectedUser=user;
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
