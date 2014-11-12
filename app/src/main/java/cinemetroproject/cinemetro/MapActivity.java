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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MapActivity extends Activity implements LocationListener {


    private ListView lv;
    private LinearLayout Ll1;
    private GoogleMap mΜap;
    private LatLng current;
    private Marker mCurrent;
    private float colour;
    private int nLine;
    private List<MyPoint> line = new ArrayList<MyPoint>();
    private LocationManager lm;
    private Location currentLocation;
    private static int idLine = 0;
    private static int idStation = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_map);

        setUpMap();

        lv = (ListView) this.findViewById(R.id.lv);

        nLine = idLine;
        setLine();

        Ll1 = (LinearLayout) this.findViewById(R.id.Ll1);
        Ll1.setBackgroundColor(Color.WHITE);
        Ll1.setAlpha((float) 0.5);

        LinearLayout Ll2 = (LinearLayout) findViewById(R.id.Ll2);

        ArrayList<Route> rt = DbAdapter.getInstance().getRoutes();
        for (int i = 0; i < rt.size(); i++) {
            MyButton bt = new MyButton(this, rt.get(i).getId());

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

        MyButton bt = new MyButton(this, -1);
        bt.setText("Route 3");
        bt.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT));
        bt.setTextSize(12);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RouteButtonClicked(v);
            }


        });
        Ll2.addView(bt);


        MyButton bt2 = new MyButton(this, 0);
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


        nLine = 0;
        setLine();

        currentLocation = new Location("");
        if (this.mΜap != null) {
            mΜap.setMyLocationEnabled(true);
            mΜap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(40.633257,22.944343),8));
        }
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        currentLocation=lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 180000, 3, this);

        idLine = 0;
        idStation = 0;

    }

    private void RouteButtonClicked(View v) {
        MyButton bt = (MyButton) v;
        nLine = bt.getLine();
        setLine();

    }

    public void setUpMap() {
        mΜap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        if (mΜap != null) {
            mΜap.clear();

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
        if(nLine!=0)this.drawLines();
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
            case 10:
                colour = (float) 300.0;
                break;
            case -1:
                colour = (float) 120.0;
                break;
        }
    }

    public static void showInMap(int idLine, int idStation){

        MapActivity.idLine = idLine;
        MapActivity.idStation = idStation;



    }


    private void AddMarkers() {

        setUpMap();
        if (this.mΜap == null) return;
        ArrayList<Route> rt = DbAdapter.getInstance().getRoutes();
        for (int i = 0; i < rt.size(); i++) {
            setColour(i + 1);
            if (rt.get(i).getId() == nLine || nLine == 0) {
                ArrayList<Station> st = DbAdapter.getInstance().getStationByRoute(rt.get(i).getId());
                for (int j = 0; j < st.size(); j++) {
                    MyPoint point = st.get(j).getMyPoint();
                    line.add(point);
                    mΜap.addMarker(new MarkerOptions()
                            .position(point.getLng())
                            .title(point.getName())
                            .icon(BitmapDescriptorFactory.defaultMarker(colour)));

                }
            }
        }
        if (nLine == -1 || nLine == 0) {
            setColour(-1);
            ArrayList<TimelineStation> ts = DbAdapter.getInstance().getTimelineStations();
            for (int i = 0; i < ts.size(); i++) {
                MyPoint point = ts.get(i).getMyPoint();
                line.add(point);
                mΜap.addMarker(new MarkerOptions()
                        .position(point.getLng())
                        .title(point.getName())
                        .icon(BitmapDescriptorFactory.defaultMarker(colour)));
            }
        }
    }

    private void drawLines() {

        for (int i = 0; i < line.size() - 1; i++) {
            findDirections(line.get(i).getLng().latitude, line.get(i).getLng().longitude,
                    line.get(i + 1).getLng().latitude, line.get(i + 1).getLng().longitude,
                    GMapV2Direction.MODE_WALKING);

        }

    }

    private void showList() {
        ArrayAdapter<MyPoint> adapter = new myArrayAdapter();
        lv.setAdapter(adapter);
    }


    @Override
    public void onLocationChanged(Location location) {
        currentLocation=new Location("");
        currentLocation = location;

        if (this.mΜap == null) return;

        mΜap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 11.0f));

        for (int i = 0; i < line.size(); i++) {
            Location loc = new Location(line.get(i).getName());
            loc.setLatitude(line.get(i).getLng().latitude);
            loc.setLongitude(line.get(i).getLng().longitude);

            line.get(i).setDistance(loc.distanceTo(currentLocation));
            if (line.get(i).getDistance() < 100) {
                Unlock(line.get(i));
            }
        }
        //showList();
    }

    private void Unlock(MyPoint myPoint) {
        if (nLine == 0) return;

            DbAdapter.getInstance().Unlock(nLine, myPoint.getId());


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

    public void handleGetDirectionsResult(ArrayList<LatLng> directionPoints) {
        Polyline newPolyline;
        PolylineOptions rectLine = new PolylineOptions().width(3).color(Color.BLUE);
        for (int i = 0; i < directionPoints.size(); i++) {
            rectLine.add(directionPoints.get(i));
        }
        newPolyline = this.mΜap.addPolyline(rectLine);

    }

    public void findDirections(double fromPositionDoubleLat, double fromPositionDoubleLong, double toPositionDoubleLat, double toPositionDoubleLong, String mode) {
        Map<String, String> map = new HashMap<String, String>();
        map.put(GetDirectionsAsyncTask.USER_CURRENT_LAT, String.valueOf(fromPositionDoubleLat));
        map.put(GetDirectionsAsyncTask.USER_CURRENT_LONG, String.valueOf(fromPositionDoubleLong));
        map.put(GetDirectionsAsyncTask.DESTINATION_LAT, String.valueOf(toPositionDoubleLat));
        map.put(GetDirectionsAsyncTask.DESTINATION_LONG, String.valueOf(toPositionDoubleLong));
        map.put(GetDirectionsAsyncTask.DIRECTIONS_MODE, mode);

        GetDirectionsAsyncTask asyncTask = new GetDirectionsAsyncTask(this);
        asyncTask.execute(map);
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

            TextView text1 = (TextView) itemView.findViewById(R.id.station);
            switch(nLine){
                case 1:
                    text1.setTextColor(Color.rgb(227, 30, 25));
                    break;
                case 2:
                    text1.setTextColor(Color.rgb(9, 0, 160));
                    break;
                case 3:
                    text1.setTextColor(Color.rgb(63, 129, 42));
                    break;
                default:
                    text1.setTextColor(Color.rgb(9, 0, 160));
                    break;
            }

            text1.setText("Station " + (pos + 1));
            TextView text2 = (TextView) itemView.findViewById(R.id.stationInfo);
            text2.setText(line.get(pos).getName());
            TextView text3 = (TextView) itemView.findViewById(R.id.distance);
            text3.setText(line.get(pos).getDistance2());

            return itemView;
        }
    }

    private class MyButton extends Button {
        private int line;

        public MyButton(Context context, int line) {
            super(context);
            this.setLine(line);
        }

        public int getLine() {
            return line;
        }

        public void setLine(int line) {
            this.line = line;
        }
    }
}
