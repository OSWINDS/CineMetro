package cinemetroproject.cinemetro;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;


public class MapActivity extends Activity {


    private GoogleMap mΜap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_map);
        //mΜap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        setUpMap();

    }

    public void setUpMap(){
        mΜap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.map, menu);

        //return true;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.map, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.Options) {
            switch (item.getItemId()) {
                case R.id.line1:
                    // add line 1
                    return true;
                case R.id.line2:
                    // add line 2
                    return true;
                case R.id.line3:
                    // add line3
                    return true;
                case R.id.noLine:
                    setUpMap();
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
            //return true;
        }
        return true;
        /*
        switch (item.getItemId()) {
            case R.id.red:
                // Red item was selected
                return true;
            case R.id.blue:
                // Green item was selected
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }*/
        //return super.onOptionsItemSelected(item);
    }
}
