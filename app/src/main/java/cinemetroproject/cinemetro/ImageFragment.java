package cinemetroproject.cinemetro;

/**
 * Created by kiki__000 on 11-Nov-14.
 */
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import java.lang.reflect.Field;

public class ImageFragment extends Fragment{

    int mCurrentPage;
    static int id;
    static int line;

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
        View v = inflater.inflate(R.layout.image_screen, container,false);
        ImageView image = (ImageView) v.findViewById(R.id.image);

        if (line == 1) {
            return v;
        }
        else{
            try {
                Class res = R.drawable.class;
                Field field = res.getField((DbAdapter.getInstance().getMainPhotosOfMovie(id - 6).get(mCurrentPage - 1).getName()));
                int drawableId = field.getInt(null);
                image.setBackgroundResource(drawableId);
            } catch (Exception e) {}

            return v;
        }
    }
}
