package cinemetroproject.cinemetro;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class MapActivity extends ActionBarActivity implements LocationListener {


    public static final int LINE1 = 1;
    public static final int LINE3 = 3;
    public static final int LINE2 = 2;
    private static final int NOLINE = 0;
    private static final int SELECTED_STATION = 10;
    public static final float MARKER_LINE2 = (float) 240.0;
    public static final float MARKER_LINE1 = (float) 0.0;
    public static final float MARKER_LINE3 = (float) 120.0;
    private static float colour;
    private static int idLine = 0;
    private static int idStation = 0;
    private List<MyButton> buttons;
    private ListView lv;
    private GoogleMap mΜap;
    private int nLine;
    private List<MyPoint> line = new ArrayList<MyPoint>();
    private LocationManager lm;
    private Location currentLocation;
    private DrawerLayout dLayout;
    private int resourseColour;
    private Button back_bt;

    public static void showInMap(int idLine, int idStation) {

        MapActivity.idLine = idLine;
        MapActivity.idStation = idStation;


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        String lang = LanguageActivity.language;

        if (lang.equals("el")) {
            Configuration c = new Configuration(getResources().getConfiguration());
            c.locale = new Locale("el", "EL");
            getResources().updateConfiguration(c, getResources().getDisplayMetrics());
            DbAdapter.getInstance().changeLanguage(Language.GREEK);
        } else {
            Configuration c = new Configuration(getResources().getConfiguration());
            c.locale = new Locale("en", "EN");
            getResources().updateConfiguration(c, getResources().getDisplayMetrics());
            DbAdapter.getInstance().changeLanguage(Language.ENGLISH);
        }

        setContentView(R.layout.activity_map);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().setTitle(getResources().getString(R.string.title_activity_map));


        setUpMap();


        dLayout = (DrawerLayout) this.findViewById(R.id.drawer_layout);
        lv = (ListView) this.findViewById(R.id.lv);

        buttons = new ArrayList<MyButton>();
        buttons.add(new MyButton(LINE1, getResources().getString(R.string.tab1)));
        buttons.add(new MyButton(LINE2, getResources().getString(R.string.tab2)));
        buttons.add(new MyButton(LINE3, getResources().getString(R.string.tab3)));
        buttons.add(new MyButton(NOLINE, getResources().getString(R.string.no_Line)));
        // buttons.add(new MyButton(LINE1,getResources().getString(R.string.line1_title)));
        this.showButtonsList();


        back_bt = (Button) findViewById(R.id.backButton);
        back_bt.setVisibility(Button.INVISIBLE);
        back_bt.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {
                                           showButtonsList();
                                           back_bt.setVisibility(Button.INVISIBLE);
                                       }
                                   }
        );


        nLine = idLine;

        setLine();


        currentLocation = new Location("");
        if (this.mΜap != null) {
            mΜap.setMyLocationEnabled(true);
            mΜap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(40.633257, 22.944343), 12));
        }
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        currentLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 180000, 3, this);

        idLine = 0;
        idStation = 0;

    }


    private void sortLine(int lineid) {
        if (lineid != LINE2) return;

        Collections.sort(line, new Comparator<MyPoint>() {
            @Override
            public int compare(MyPoint ob1, MyPoint ob2) {

                return ob1.compareTo(ob2);
            }
        });
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
        // Take appropriate action for each action item click
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        //return super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.Lines:
                if (dLayout.isDrawerOpen(Gravity.LEFT))
                    dLayout.closeDrawers();
                else
                    dLayout.openDrawer(Gravity.LEFT);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void RouteButtonClicked(View v) {
        Button bt = (Button) v;
        nLine = (int) ((Integer) bt.getTag()).intValue();
        back_bt.setVisibility(Button.VISIBLE);
        setLine();

    }


    private void setLine() {
        if (!line.isEmpty()) {
            line.clear();
        }
        AddMarkers();
        sortLine(nLine);
        if (nLine != 0) this.drawLines();
        back_bt.setVisibility(Button.VISIBLE);
        showList();
    }

    public void showButtonsList() {
        ArrayAdapter<MyButton> adapter = new myArrayAdapterButton();
        lv.setAdapter(adapter);
    }

    public void setUpMap() {
        mΜap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        if (mΜap != null) {
            mΜap.clear();

        }
    }

    public void setColour(int k) {
        switch (k) {
            case LINE1:
                colour = (float) MapActivity.MARKER_LINE1;
                resourseColour = R.color.line1;
                break;
            case LINE2:
                colour = (float) MapActivity.MARKER_LINE2;
                resourseColour = R.color.line2;
                break;
            case LINE3:
                colour = (float) MapActivity.MARKER_LINE3;
                resourseColour = R.color.line3;
                break;
            case NOLINE:
                colour = (float) MapActivity.MARKER_LINE3;
                resourseColour = R.color.my_blue;
                break;
            case SELECTED_STATION:
                colour = (float) 165.55;
                break;
        }
    }

    private void AddMarkers() {

        setUpMap();
        if (this.mΜap == null) return;
        ArrayList<Route> rt = DbAdapter.getInstance().getRoutes();
        for (int i = 0; i < rt.size(); i++) {
            setColour(rt.get(i).getId());
            if (rt.get(i).getId() == nLine || nLine == 0) {
                ArrayList<Station> st = DbAdapter.getInstance().getStationByRoute(rt.get(i).getId());
                for (int j = 0; j < st.size(); j++) {
                    MyPoint point = st.get(j).getMyPoint();
                    line.add(point);
                    setColour(rt.get(i).getId());
                    if (idStation == point.getId()) {
                        setColour(SELECTED_STATION);
                    }
                    mΜap.addMarker(new MarkerOptions()
                            .position(point.getLng())
                            .title(point.getName())
                            .icon(BitmapDescriptorFactory.defaultMarker(colour)));

                }
            }
        }
        if (nLine == LINE3 || nLine == 0) {

            ArrayList<TimelineStation> ts = DbAdapter.getInstance().getTimelineStations();
            for (int i = 0; i < ts.size(); i++) {
                MyPoint point = ts.get(i).getMyPoint();
                line.add(point);
                setColour(LINE3);
                if (idStation == point.getId()) {
                    setColour(SELECTED_STATION);
                }
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

    private void showList() {
        ArrayAdapter<MyPoint> adapter = new myArrayAdapterPoint();
        lv.setAdapter(adapter);

    }


    @Override
    public void onLocationChanged(Location location) {
        currentLocation = new Location("");
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

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(MapActivity.this, getResources().getString(R.string.GPS_Enabled), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(MapActivity.this, getResources().getString(R.string.GPS_Disabled), Toast.LENGTH_SHORT).show();
    }


    private void Unlock(MyPoint myPoint) {
        if (nLine == 0) return;

        DbAdapter.getInstance().Unlock(nLine, myPoint.getId());


    }

    public void handleGetDirectionsResult(ArrayList<LatLng> directionPoints) {
        Polyline newPolyline;
        PolylineOptions rectLine = new PolylineOptions().width(3).color(getResources().getColor(resourseColour));
        for (int i = 0; i < directionPoints.size(); i++) {
            rectLine.add(directionPoints.get(i));
        }
        newPolyline = this.mΜap.addPolyline(rectLine);
    }

    private class myArrayAdapterPoint extends ArrayAdapter<MyPoint> {

        public myArrayAdapterPoint() {
            super(MapActivity.this, R.layout.maplistitem, line);
        }

        @Override
        public View getView(int pos, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.maplistitem, parent, false);
            }

            itemView.setTag(line.get(pos));
            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    onStationClicked(v);

                }
            });

            TextView text1 = (TextView) itemView.findViewById(R.id.station);
            setColour(nLine);

            text1.setTextColor(getResources().getColor(resourseColour));


            text1.setText(getResources().getString(R.string.station_text) + " " + (pos + 1));
            TextView text2 = (TextView) itemView.findViewById(R.id.stationInfo);
            text2.setText(line.get(pos).getName());
            TextView text3 = (TextView) itemView.findViewById(R.id.distance);
            text3.setText(line.get(pos).getDistance2());

            return itemView;
        }
    }

    private void onStationClicked(View v) {
        MyPoint p = (MyPoint) v.getTag();
        if (nLine == NOLINE) return;

        Intent intent = new Intent(MapActivity.this, TabedLinesActivity.routes.get(nLine - 1).getRouteClass());
        intent.putExtra("button_id", p.getId());
        MapActivity.this.startActivity(intent);


    }

    private class myArrayAdapterButton extends ArrayAdapter<MyButton> {

        public myArrayAdapterButton() {
            super(MapActivity.this, R.layout.maplistbutton, buttons);

        }

        @Override
        public View getView(int pos, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.maplistbutton, parent, false);
            }

            Button bt = (Button) itemView.findViewById(R.id.mapbutton);
            setColour(buttons.get(pos).getLine());

            // bt.setTextColor(getResources().getColor(resourseColour));
            bt.setBackgroundColor(getResources().getColor(resourseColour));

            bt.setText(buttons.get(pos).getStr());
            bt.setTag(new Integer(buttons.get(pos).getLine()));

            bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RouteButtonClicked(v);
                }
            });
            return itemView;
        }
    }


    private class MyButton {
        private int line;
        private String str;

        public MyButton(int line, String str) {


            this.setStr(str);
            this.setLine(line);
        }

        public int getLine() {
            return line;
        }

        public void setLine(int line) {
            this.line = line;
        }

        public String getStr() {
            return str;
        }

        public void setStr(String str) {
            this.str = str;
        }
    }
}
