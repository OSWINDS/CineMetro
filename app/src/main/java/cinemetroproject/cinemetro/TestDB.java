package cinemetroproject.cinemetro;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import java.util.ArrayList;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.view.*;

import cinemetroproject.cinemetro.R;

public class TestDB extends ActionBarActivity {

    private ArrayList<Route> routes;
    private ArrayList<Station> stations;
    ArrayList<String> names;
    ArrayList<String> station_names;
    Spinner route_spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_db);

        this.initializeData();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.test_db, menu);
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

    public void initializeData()
    {
        //get the routes
        this.routes = dbAdapter.getInstance().getRoutes();

        //set spinner for routes
        route_spinner = (Spinner) findViewById(R.id.spinner);
        //create array with the names of each route
        names = new ArrayList<String>();
        for(Route r : routes)
        {
            names.add(r.getName());
        }
        ArrayAdapter<String> route_spinnerArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, names);
        route_spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        route_spinner.setAdapter(route_spinnerArrayAdapter);

        updateStations();

        route_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                updateStations();
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void updateStations()
    {
        //get the stations of the route currectly displayed in the spinner
        stations = dbAdapter.getInstance().getStationByRoute(routes.get(route_spinner.getSelectedItemPosition()).getId());
        //create array with the names of these stations
        station_names = new ArrayList<String>();
        for(Station s : stations)
        {
            station_names.add(s.getName());
        }
        Spinner station_spinner = (Spinner) findViewById(R.id.spinner2);
        ArrayAdapter<String> station_spinnerArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, station_names);
        station_spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        station_spinner.setAdapter(station_spinnerArrayAdapter);
    }
}
