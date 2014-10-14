package cinemetroproject.cinemetro;

import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TabHost;

public class LinesActivity  extends TabActivity {

    private TabHost mTabHost;

    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_lines);

        mTabHost=getTabHost();
        TabHost.TabSpec spec;
        Intent intent;

        //line1
        intent =new Intent(this,Line1.class);
        spec =mTabHost.newTabSpec("")
                .setContent(intent)
                .setIndicator("");
        mTabHost.addTab(spec);

        //line2
        intent =new Intent(this,Line2.class);
        spec =mTabHost.newTabSpec("")
                .setContent(intent)
                .setIndicator("");
        mTabHost.addTab(spec);

        //line 3
        intent =new Intent(this,Line3.class);
        spec =mTabHost.newTabSpec("")
                .setContent(intent)
                .setIndicator("");
        mTabHost.addTab(spec);

        mTabHost.setCurrentTab(2);
        for(int i=0;i<mTabHost.getTabWidget().getChildCount();i++)
        {
            mTabHost.getTabWidget().getChildAt(0).setBackgroundColor(Color.parseColor("#E41C26"));
            mTabHost.getTabWidget().getChildAt(1).setBackgroundColor(Color.parseColor("#2389BE"));
            mTabHost.getTabWidget().getChildAt(2).setBackgroundColor(Color.parseColor("#1B9344"));
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
        if (id == R.id.home) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



}
