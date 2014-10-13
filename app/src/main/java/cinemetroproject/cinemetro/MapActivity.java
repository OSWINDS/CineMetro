package cinemetroproject.cinemetro;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
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
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;


public class MapActivity extends Activity implements LocationListener {


    private ListView lv;
    private LinearLayout Ll1;
    private GoogleMap mΜap;
    private Marker mCurrent;
    private float colour;
    private PolylineOptions options;
    private int nLine;
    private List<MyPoint> line = new ArrayList<MyPoint>();
    private float distances[];
    private LocationManager lm;
    private Location currentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_map);

        setUpMap();



        Ll1 = (LinearLayout) this.findViewById(R.id.Ll1);
        Ll1.setBackgroundColor(Color.WHITE);
        Ll1.setAlpha((float) 0.5);

        LinearLayout Ll2 = (LinearLayout) findViewById(R.id.Ll2);

        ArrayList<Route> rt = DbAdapter.getInstance().getRoutes();
        for (int i = 0; i < rt.size(); i++) {
            Button bt = new Button(this);
            bt.setText("Route " + rt.get(i).getId());
            bt.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT));
            bt.setTextSize(12);
            bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RouteButtonClicked(v);
                }


            });
            Ll2.addView(bt);
        }

        Button bt = new Button(this);
        bt.setText("Timeline Route");
        bt.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT));
        bt.setTextSize(12);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RouteButtonClicked(v);
            }


        });
        Ll2.addView(bt);


        Button bt2 = new Button(this);
        bt2.setText("NO Route");
        bt2.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT));
        bt2.setTextSize(12);
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RouteButtonClicked(v);
            }


        });
        Ll2.addView(bt2);


        lv = (ListView) this.findViewById(R.id.lv);

        nLine = 0;
        setLine();


        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 600000, 3, this);
    }

    private void RouteButtonClicked(View v) {
        Button bt = (Button) v;
        String str = bt.getText().toString();
        if (str.contains("NO")) {
            nLine = 0;
            setLine();
            return;
        }

        if (str.contains("Timeline")) {
            nLine = -1;
            setLine();
            return;
        }

        String[] strs = str.split(" ");

        nLine = Integer.getInteger(strs[1]);
        setLine();

    }

    public void setUpMap() {
        mΜap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        if (mΜap != null) {
            mΜap.clear();
            //mΜap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(40.633257,22.944343),8));
        }
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

        switch (id) {

            case R.id.Lines:
                if (Ll1.getVisibility() == View.VISIBLE)
                    Ll1.setVisibility(View.INVISIBLE);
                else
                    Ll1.setVisibility(View.VISIBLE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void setLine() {
        if (!line.isEmpty()) {
            line.clear();
        }
        AddMarkers();
        distances = new float[line.size()];
        showList();
    }

    private void setColour(int k) {
        switch (k) {
            case 1:
                colour = (float) 0.0;
                break;
            case 2:
                colour = (float) 240.0;
                break;
            case 3:
                colour = (float) 120.0;
                break;
        }
    }

    private void AddMarkers() {

        setUpMap();
        ArrayList<Route> rt = DbAdapter.getInstance().getRoutes();
        for (int i = 0; i < rt.size(); i++) {
            setColour(i + 1);
            if (rt.get(i).getId() == nLine || nLine == 0) {
                ArrayList<Station> st = DbAdapter.getInstance().getStationByRoute(rt.get(i).getId());
                for (int j = 0; j < st.size(); j++) {
                    MyPoint point = st.get(j).getMyPoint();
                    line.add(point);
                    if (mΜap != null){
                        mΜap.addMarker(new MarkerOptions()
                                .position(point.getLng())
                                .title(point.getName())
                                .icon(BitmapDescriptorFactory.defaultMarker(colour)));
                    }

                }
            }
        }
        if (nLine == -1 || nLine == 0) {
            ArrayList<TimelineStation> ts = DbAdapter.getInstance().getTimelineStations();
            for (int i = 0; i < ts.size(); i++) {
                MyPoint point = ts.get(i).getMyPoint();
                line.add(point);
                if (mΜap != null){
                    mΜap.addMarker(new MarkerOptions()
                            .position(point.getLng())
                            .title(point.getName())
                            .icon(BitmapDescriptorFactory.defaultMarker(colour)));
                }

            }
        }
    }

    private void showList() {
        ArrayAdapter<MyPoint> adapter = new myArrayAdapter();

        lv.setAdapter(adapter);
    }


    @Override
    public void onLocationChanged(Location location) {
        currentLocation=new Location("");
        currentLocation.setLatitude(location.getLatitude());
        currentLocation.setLongitude(location.getLongitude());
        LatLng current=new LatLng(location.getLatitude(),location.getLongitude());

        if(mΜap!=null) {
            //if(mCurrent!=null){
               // mCurrent.remove();
            //}
            mCurrent = mΜap.addMarker(new MarkerOptions()
                    .position(current)
                    .title("You are here!")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
        }

        for(int i=0;i<line.size();i++){
            Location loc=new Location("");
            loc.setLatitude(line.get(i).getLat());
            loc.setLongitude(line.get(i).getLon());

            distances[i]=currentLocation.distanceTo(loc);
        }
        showList();
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {
        Toast.makeText(MapActivity.this, "GPS is enabled.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String s) {
        Toast.makeText(MapActivity.this, "GPS is not enabled.", Toast.LENGTH_SHORT).show();
    }

    private class myArrayAdapter extends ArrayAdapter<MyPoint> {

        public myArrayAdapter() {
            super(MapActivity.this, R.layout.maplistitem, line);
        }

        @Override
        public View getView(int pos, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.maplistitem, parent, false);
            }

            String s1, s2, s3;

            s1 = (pos + 1) + "η Στάση";
            s2 = line.get(pos).getName();

            if (distances[pos] != 0.0) {
                s3 = "Distance: " + distances[pos] + " m";
            } else {
                s3 = "";
            }

            TextView text1 = (TextView) itemView.findViewById(R.id.station);
            text1.setText(s1);
            TextView text2 = (TextView) itemView.findViewById(R.id.stationInfo);
            text2.setText(s2);
            TextView text3 = (TextView) itemView.findViewById(R.id.distance);
            text3.setText(s3);

            return itemView;
        }
    }
}
