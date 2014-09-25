package cinemetroproject.cinemetro;

import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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
            mTabHost.getTabWidget().getChildAt(2).setBackgroundColor(Color.parseColor("#71B278"));
        }
    }
}
