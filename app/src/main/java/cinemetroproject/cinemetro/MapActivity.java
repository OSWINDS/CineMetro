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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
    private float colour;
    private int nLine;
    private List<Station> line=new ArrayList<Station>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_map);

        setUpMap();

        nLine=0;
        setLine();

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
                    nLine=1;
                    setLine();
                    return true;
                case R.id.line2:
                    nLine=2;
                    setLine();
                    return true;
                case R.id.line3:
                    nLine=3;
                    setLine();
                    return true;
                case R.id.noLine:
                    /*if(mΜap!=null){
                    //setUpMap();
                    AddMarkers(0);
                    }*/
                    nLine=0;
                    setLine();
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
            //return true;
        //}
      //  return true;
    }

    private void setLine(){
        if(!line.isEmpty()){
            line.clear();
        }
        if(nLine!=0){
            line=DbAdapter.getInstance().getStationByRoute(nLine);
            setColour();
            /*if(mΜap!=null){
                mΜap.clear();
                for(int i=0;i<line.size();i++){
                    mΜap.clear();
                    addMarkers2(i);
                }
            }*/
        }else{
            line.add(new Station());
            //if(mΜap!=null){
                //mΜap.clear();
            //}
        }
        showList();
        stationClk();
    }

    private void setColour(){
        switch(nLine){
            case 1:
                colour= (float) 0.0;
                break;
            case 2:
                colour= (float) 240.0;
                break;
            case 3:
                colour= (float) 120.0;
                break;
        }
    }

    private void addMarkers2(int pos){

        double lat=line.get(pos).getLat();
        double lon=line.get(pos).getLng();

        Toast.makeText(MapActivity.this, "("+lat+","+lon+")", Toast.LENGTH_SHORT).show();
        /*mΜap.addMarker(new MarkerOptions()
                .position(line.get(pos).getLatpoint())
                .title(line.get(pos).getName())
                .icon(BitmapDescriptorFactory.defaultMarker(colour)));*/
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

    private void showList(){
        ArrayAdapter<Station> adapter=new myArrayAdapter();
        ListView list=(ListView)findViewById(R.id.lv);
        list.setAdapter(adapter);
    }

    private void stationClk(){
        ListView list=(ListView)findViewById(R.id.lv);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                //TextView text=(TextView)view;
                if(nLine!=0){
                    addMarkers2(pos);
                    //map.clear();
                    //if(mMap!=null){addMarkers2}
                }else{
                    Toast.makeText(MapActivity.this,"Nothing to show.", Toast.LENGTH_SHORT).show();
                    //map.clear
                }
            }
        });
    }

    private class myArrayAdapter extends ArrayAdapter<Station>{

        public myArrayAdapter(){
            super(MapActivity.this, R.layout.maplistitem, line);
        }

        @Override
        public View getView(int pos, View convertView, ViewGroup parent) {
            View itemView=convertView;
            if(itemView==null){
                itemView=getLayoutInflater().inflate(R.layout.maplistitem, parent, false);
            }

            String s1,s2;
            if(nLine!=0){
                s1=(pos+1)+"η Στάση";
                s2=line.get(pos).getName();
            }else{
                s1="No line was selected.";
                s2="You can select a line from settings.";
            }

            TextView text1=(TextView)itemView.findViewById(R.id.station);
            text1.setText(s1);
            TextView text2=(TextView)itemView.findViewById(R.id.stationInfo);
            text2.setText(s2);

            return itemView;
        }
    }

    class myLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {

            if (location != null) {
                //double pLong=location.getLongitude();
                //double pLat=location.getLatitude();

                current = new LatLng(location.getLatitude(), location.getLongitude());
                //setUpMap();
                if (mΜap != null) {
                    mΜap.moveCamera(CameraUpdateFactory.newLatLngZoom(current, 10));

                    // mCurrent = mΜap.addMarker(new MarkerOptions().position(current).title("You are here!"));
                    //icon
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
