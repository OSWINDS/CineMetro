package cinemetroproject.cinemetro;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

/**
 * Created by vivi dimitris on 22/9/2014.
 */
public class Timeline extends ActionBarActivity {

    final RadioButton[] rb = new RadioButton[3];


    private LinearLayout scrollView;
    private TextView description;
    private RadioGroup rg;
    private RadioButton radioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        rg = (RadioGroup) findViewById(R.id.radiogroup);
        rg.setOrientation(RadioGroup.HORIZONTAL);
        scrollView = (LinearLayout)findViewById(R.id.scrollView);


        for (int i=0; i<3; i++) {
            rb[i]  = new RadioButton(this);
            rb[i].setText("Στάση " + (i + 1) + "\n");
         // rb[i].setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            rb[i].setId(i);
            rb[i].setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    // get selected radio button from radioGroup
                    int selectedId = rg.getCheckedRadioButtonId();

                    // find the radiobutton by returned id
                    radioButton = (RadioButton) findViewById(selectedId);
                    description = (TextView)findViewById(R.id.description);
                    description.setText("Ο παραγωγός Τζεϊμς Παρις, εμβληματική μορφή του Φεστιβάλ κατά την περίοδο 1966-1972, «κατεβαζει» " +
                                    "ενα τανκ στην πλατεια του Λευκου Πυργου για διαφημιστικους σκοπους" +
                            " της ταινίας «Ξεχασμενοι ηρωες», σε δική του παραγωγή (7η Φεστιβάλ Ελληνικού Κινηματογράφου, 1966)." );
                }
            });;
            rg.addView(rb[i]);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.lines, menu);
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




}
