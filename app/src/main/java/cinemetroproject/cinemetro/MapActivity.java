package cinemetroproject.cinemetro;

import android.app.Activity;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;


public class MapActivity extends Activity {


    private GoogleMap mΜap;
    private LatLng current;
    private Marker mCurrent;
    //TextView Longitude;
    //TextView Latitude;
    private List<String> line=new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_map);
        setUpMap();
        createList(0);
        showList();
        //AddMarkers(0);
        // LocationManager lm=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
        // LocationListener ll=new myLocationListener();
        // lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, ll);


    }

    public void setUpMap() {
        mΜap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        if(mΜap!=null){
            mΜap.clear();
        }
        //mΜap.setMyLocationEnabled(true);
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
       // if (id == R.id.Options) {
            switch (item.getItemId()) {
                case R.id.line1:
                    if(mΜap!=null){
                        Draw_Line(1);
                    }
                    createList(1);
                    showList();
                    return true;
                case R.id.line2:
                    if(mΜap!=null) {
                        Draw_Line(2);
                    }
                    createList(2);
                    showList();
                    return true;
                case R.id.line3:
                    if(mΜap!=null) {
                        Draw_Line(3);
                    }
                    createList(3);
                    showList();
                    return true;
                case R.id.noLine:
                    /*if(mΜap!=null){
                    //setUpMap();
                    AddMarkers(0);
                    }*/
                    createList(0);
                    showList();
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
            //return true;
        //}
      //  return true;
    }


    private void Draw_Line(int nLine) {

     mΜap.clear();
     AddMarkers(nLine);

        PolylineOptions options = new PolylineOptions().width(5).color(Color.BLUE);
        ArrayList<Route> rt = DbAdapter.getInstance().getRoutes();
        for (int i = 0; i < rt.size(); i++) {
            if (rt.get(i).getId() == nLine||nLine==0) {
                ArrayList<Station> st = DbAdapter.getInstance().getStationByRoute(rt.get(i).getId());
                for (int j = 0; j < st.size(); j++) {

                    LatLng point = st.get(j).getLatpoint();
                    options.add(point);
                }
            }
        }
       mΜap.addPolyline(options);


    }


    private void AddMarkers(int nLine) {

        mΜap.clear();
        ArrayList<Route> rt = DbAdapter.getInstance().getRoutes();
        for (int i = 0; i < rt.size(); i++){
            if(rt.get(i).getId()==nLine||nLine==0) {
                ArrayList<Station> st = DbAdapter.getInstance().getStationByRoute(rt.get(i).getId());
                for (int j = 0; j < st.size(); j++) {
                    Station point = st.get(j);
                    mΜap.addMarker(new MarkerOptions()
                            .position(point.getLatpoint())
                            .title(point.getName())
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

                }
            }
        }
    }

    private void createList(int nLine){
        if(!line.isEmpty()){
            line.clear();
        }

        switch(nLine){
            case 0:
                line.add("No line was selected.");
                break;
            case 1:
                line.add("1η Στάση");
                line.add("2η Στάση");
                line.add("3η Στάση");
                line.add("4η Στάση");
                line.add("5η Στάση");
                line.add("6η Στάση");
                break;
            default:
                line.add("Under construction");
        }
    }

    private void showList(){
        ArrayAdapter<String> adapter=new myArrayAdapter();
        ListView list=(ListView)findViewById(R.id.lv);
        list.setAdapter(adapter);
    }

    class myLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {

            if (location != null) {
                //double pLong=location.getLongitude();
                //double pLat=location.getLatitude();
                //Longitude.setText(Double.toString(pLong));
                //Latitude.setText(Double.toString(pLat));

                current = new LatLng(location.getLatitude(), location.getLongitude());
                //setUpMap();
                if (mΜap != null) {
                    mΜap.moveCamera(CameraUpdateFactory.newLatLngZoom(current, 10));

                    // mCurrent = mΜap.addMarker(new MarkerOptions().position(current).title("You are here!"));
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

    private class myArrayAdapter extends ArrayAdapter<String>{

        public myArrayAdapter(){
            super(MapActivity.this, R.layout.maplistitem, line);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView=convertView;
            if(itemView==null){
                itemView=getLayoutInflater().inflate(R.layout.maplistitem, parent, false);
            }

            String s1=line.get(position);

            TextView text1=(TextView)itemView.findViewById(R.id.station);
            text1.setText(s1);

            return itemView;
        }
    }


}
