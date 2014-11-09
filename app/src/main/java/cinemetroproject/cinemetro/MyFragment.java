package cinemetroproject.cinemetro;

/**
 * Created by kiki__000 on 09-Nov-14.
 */
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MyFragment extends Fragment{

    int mCurrentPage;
    private int[] texts = new int[]{R.string.about_text1, R.string.about_text2, R.string.about_text3};

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
        View v = inflater.inflate(R.layout.about_screen, container,false);
        TextView text = (TextView ) v.findViewById(R.id.aboutText);
        text.setText(texts[mCurrentPage-1]);
        return v;
    }

}
