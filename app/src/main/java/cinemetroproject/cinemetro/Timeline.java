package cinemetroproject.cinemetro;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.lang.reflect.Field;

/**
 * Created by vivi dimitris on 22/9/2014.
 */
public class Timeline extends ActionBarActivity {

    private RadioButton[] rb = new RadioButton[10];//for now


    private LinearLayout scrollView;
    private LinearLayout mylayout;
    private TextView description;
    private RadioGroup rg;
    private RadioButton radioButton;
    private ImageView image;
    private View view;
  //  private Button nextstasion;
    private Button prevstation;
    private int idCinema;
    private int selectedId;
    private TextView title;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
       // this.num = num;
        super.onCreate(savedInstanceState);

        //full screen to timeline
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_timeline);

        rg = (RadioGroup) findViewById(R.id.radiogroup);
        rg.setOrientation(RadioGroup.VERTICAL);
        scrollView = (LinearLayout)findViewById(R.id.scrollView);
        mylayout = (LinearLayout)findViewById(R.id.mylayout);
        //nextstasion=(Button)findViewById(R.id.nextstation);



        Intent intent = getIntent();
        idCinema = intent.getIntExtra("button_id", 0);
        title=(TextView)findViewById(R.id.title);
        title.setText(DbAdapter.getInstance().getTimelineStations().get(idCinema-15).getName());
        description = (TextView) findViewById(R.id.description);
        description.setText(DbAdapter.getInstance().getTimelineStationMilestones(idCinema-14).get(1).getDescription());
        image=(ImageView)findViewById(R.id.image);
       try {
            Class res = R.drawable.class;
            Field field = res.getField(DbAdapter.getInstance().getTimelineStationMilestones(idCinema-14).get(0).getPhotoName());
            int drawableId = field.getInt(null);
            image.setBackgroundResource(drawableId);
            //  imageCinema.setGravity(Gravity.BOTTOM | Gravity.CENTER);
            //  imageCinema.setLayoutParams(new ViewGroup.LayoutParams(320,300));
        } catch (Exception e) {}
        int millestones= DbAdapter.getInstance().getTimelineStationMilestones(idCinema-14).size();

        for (int i=0; i<millestones; i++) {
            rb[i]  = new RadioButton(this);
            rb[i].setText(DbAdapter.getInstance().getTimelineStationMilestones(idCinema-14).get(i).getYear());
            rb[i].setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            rb[i].setId(i);
            rb[i].setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {


                    // get selected radio button from radioGroup
                     selectedId = rg.getCheckedRadioButtonId();

                    // find the radiobutton by returned id
                    radioButton = (RadioButton) findViewById(selectedId);
                    description = (TextView) findViewById(R.id.description);
                    description.setText(DbAdapter.getInstance().getTimelineStationMilestones(idCinema-14).get(selectedId).getDescription());
                    image=(ImageView)findViewById(R.id.image);

                    try {
                        Class res = R.drawable.class;
                        Field field = res.getField(DbAdapter.getInstance().getTimelineStationMilestones(idCinema-14).get(selectedId).getPhotoName());
                        int drawableId = field.getInt(null);
                        image.setBackgroundResource(drawableId);

                    } catch (Exception e) {}
                   /* nextstasion.setOnClickListener(new View.OnClickListener() {

                        public void onClick(View v) {
                            description.setText(DbAdapter.getInstance().getTimelineStationMilestones(idCinema-14).get(selectedId+1).getDescription());
                            image=(ImageView)findViewById(R.id.image);

                            try {
                                Class res = R.drawable.class;
                                Field field = res.getField(DbAdapter.getInstance().getTimelineStationMilestones(idCinema-14).get(selectedId+1).getPhotoName());
                                int drawableId = field.getInt(null);
                                image.setBackgroundResource(drawableId);

                            } catch (Exception e) {}


                        }
                    }*/



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
