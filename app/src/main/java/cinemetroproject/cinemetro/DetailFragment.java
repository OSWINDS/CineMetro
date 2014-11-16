package cinemetroproject.cinemetro;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by kiki__000 on 15-Nov-14.
 */
public class DetailFragment extends Fragment {

    TextView text;
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle args) {
        View view = inflater.inflate(R.layout.menu_detail_fragment, container, false);

        String menu = getArguments().getString("Menu");
        if (Integer.parseInt(menu) == 0) {
            Intent intent = new Intent(getActivity(), LinesActivity.class);
            startActivity(intent);
        }
        if (Integer.parseInt(menu) == 1) {
            Intent intent = new Intent(getActivity(), LanguageActivity.class);
            startActivity(intent);
        }

        return view;
    }
}
