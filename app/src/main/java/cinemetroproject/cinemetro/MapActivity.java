package cinemetroproject.cinemetro;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapActivity extends Activity {


    private GoogleMap mΜap;
    private LatLng current;
    private Marker mCurrent;
    //TextView Longitude;
    //TextView Latitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_map);

        LocationManager lm=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
        LocationListener ll=new myLocationListener();
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, ll);

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
                    break;
                default:
                    return super.onOptionsItemSelected(item);
            }
            return true;
        }
        return true;
    }

    class myLocationListener implements LocationListener{

        @Override
        public void onLocationChanged(Location location) {

            if(location!=null){
                //double pLong=location.getLongitude();
                //double pLat=location.getLatitude();
                //Longitude.setText(Double.toString(pLong));
                //Latitude.setText(Double.toString(pLat));

                current= new LatLng(location.getLatitude(),location.getLongitude());
                //setUpMap();
                if(mΜap!=null){
                    mΜap.moveCamera(CameraUpdateFactory.newLatLngZoom(current, 10));
                    mCurrent = mΜap.addMarker(new MarkerOptions().position(current).title("You are here!"));
                }
            }
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    }
}
