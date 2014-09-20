package cinemetroproject.cinemetro;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by vivi dimitris on 8/9/2014.
 */
public class Line3 extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        TextView textView= new TextView(this);
        textView.setText("Line 3");
        setContentView(R.layout.line3);
    }
}
