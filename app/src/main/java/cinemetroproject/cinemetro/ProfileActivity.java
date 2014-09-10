package cinemetroproject.cinemetro;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;



public class ProfileActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        String str="";
        ArrayList<Route> routes= dbAdapter.getInstance().getRoutes();
        for(int i=0;i<routes.size();i++){
           str+=routes.get(i).getName()+"\n";
            ArrayList<Station> stations=dbAdapter.getInstance().getStationByRoute(routes.get(i).getId());
            for(int j=0;j<stations.size();j++){
                str+="  # "+stations.get(j).getName()+ "  LOCKED\n";
            }
        }

        TextView tv=(TextView)this.findViewById(R.id.textViewProfile);
        tv.setText(str);


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
        );
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
