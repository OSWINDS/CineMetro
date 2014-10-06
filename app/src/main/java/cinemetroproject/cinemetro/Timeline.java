package cinemetroproject.cinemetro;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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
    private LinearLayout mylayout;
    private TextView description;
    private TextView description2;
    private RadioGroup rg;
    private RadioButton radioButton;
    private int j=0;
    private ImageView image;
private View view;
    private Button nextstasion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
       // this.num = num;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        rg = (RadioGroup) findViewById(R.id.radiogroup);
        rg.setOrientation(RadioGroup.VERTICAL);
        scrollView = (LinearLayout)findViewById(R.id.scrollView);
        mylayout = (LinearLayout)findViewById(R.id.mylayout);
        nextstasion=(Button)findViewById(R.id.nextstation);




        for (int i=0; i<3; i++) {
            rb[i]  = new RadioButton(this);
            //rb[i].setText("1964");
          //  rb[i].setTextGravity(Gravity.BOTTOM);


            rb[i].setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            rb[i].setText("1964");
            rb[i].setId(i);

            description = (TextView) findViewById(R.id.description);
            description.setText("hfojgoigtjeiogjdsklfnlsreuhbdsjvhbdskjvhbksdjgtiewufhbsdjknfbskjhfreiwuhfsiufkjdjkhfeuirhfsifjsiufhsdkjhsdkjhgdskkdjghlewurghfskdjvnsilguhreioufjnsdlkfjvnsdjvkgsdklgjhweugheiurgj");
            image=(ImageView)findViewById(R.id.image);
            image.setBackgroundResource(R.drawable.red1);
            rb[i].setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {


                    // get selected radio button from radioGroup
                    int selectedId = rg.getCheckedRadioButtonId();

                    // find the radiobutton by returned id
                    radioButton = (RadioButton) findViewById(selectedId);
                    description = (TextView) findViewById(R.id.description);
                    description.setText("hfojgoigtjeiogjdsklfnlsreuhbdsjvhbdskjvhbksdjgtiewufhbsdjknfbskjhfreiwuhfsiufkjdjkhfeuirhfsifjsiufhsdkjhsdkjhgdskkdjghlewurghfskdjvnsilguhreioufjnsdlkfjvnsdjvkgsdklgjhweugheiurgj"+selectedId);
                    image=(ImageView)findViewById(R.id.image);
                    image.setBackgroundResource(R.drawable.red1);



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
