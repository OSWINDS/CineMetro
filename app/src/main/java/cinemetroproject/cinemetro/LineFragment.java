package cinemetroproject.cinemetro;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Chris on 12/19/2014.
 */
public class LineFragment extends Fragment {
    private ArrayList<MyPoint> stations;
    int mCurrentPage;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /** Getting the arguments to the Bundle object */
        Bundle data = getArguments();

        /** Getting integer data of the key current_page from the bundle */
        mCurrentPage = data.getInt("current_page", 0);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tabed_lines, container, false);
        LinearLayout ll = (LinearLayout) v.findViewById(R.id.linear_layout_lines);
        ll.setBackgroundColor((v.getResources().getColor(TabedLinesActivity.routes.get(mCurrentPage - 1).getColor())));

        TextView tv = (TextView) v.findViewById(R.id.lines_textView);
        tv.setText(v.getResources().getString(TabedLinesActivity.routes.get(mCurrentPage - 1).getName()));
        addLineData(v, TabedLinesActivity.routes.get(mCurrentPage - 1));
        return v;
    }

    private void addLineData(View v, MyRoute myRoute) {
        stations = DbAdapter.getInstance().getAllStationsbyRoute(TabedLinesActivity.routes.get(mCurrentPage - 1).getId());
        ListView lv = (ListView) v.findViewById(R.id.lines_listview);

        ArrayAdapter<MyPoint> adapter = new MyArrayAdapter();

        lv.setAdapter(adapter);


    }

    private class MyArrayAdapter extends ArrayAdapter<MyPoint> {
        public MyArrayAdapter() {
            super(LineFragment.this.getActivity(), R.layout.lines_item2, stations);
        }

        @Override
        public View getView(int pos, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null) {
                itemView = LineFragment.this.getActivity().getLayoutInflater().inflate(R.layout.lines_item2, parent, false);
            }

            LinearLayout ll = (LinearLayout) itemView.findViewById(R.id.lines_item_ll);
            ll.setBackgroundResource(getImage(TabedLinesActivity.routes.get(mCurrentPage - 1).getId(), stations.get(pos).getId()));

            TextView station = (TextView) itemView.findViewById(R.id.station_number);
            station.setTextColor((getResources().getColor(TabedLinesActivity.routes.get(mCurrentPage - 1).getColor())));
            station.setText(R.string.station_text);
            station.append(" " + (pos + 1));

            TextView title = (TextView) itemView.findViewById(R.id.station_title);
            title.setTextColor((getResources().getColor(TabedLinesActivity.routes.get(mCurrentPage - 1).getColor())));
            title.setText(stations.get(pos).getName());

            itemView.setTag(stations.get(pos));
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    stationClicked(v);
                }
            });
            return itemView;
        }

    }

    private int getImage(int i, int id) {
        Log.i("line,station", " " + i + ", " + id);
        if (i == MapActivity.LINE1 || i == MapActivity.LINE2) {
            return DbAdapter.getInstance().getPhotoDrawableID(DbAdapter.getInstance().getPhotosByStation(id).get(0).getName());
        }
        return DbAdapter.getInstance().getPhotoDrawableID(DbAdapter.getInstance().getTimelineStationMilestones(id).get(0).getPhotoName());
    }

    private void stationClicked(View v) {
        MyPoint mp = (MyPoint) v.getTag();

        Intent intent = new Intent(LineFragment.this.getActivity(), TabedLinesActivity.routes.get(mCurrentPage - 1).getRouteClass());
        intent.putExtra("button_id", mp.getId());
        LineFragment.this.startActivity(intent);
    }
}
