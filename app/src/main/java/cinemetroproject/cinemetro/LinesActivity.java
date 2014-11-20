package cinemetroproject.cinemetro;

import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TabHost;
import android.widget.TextView;

public class LinesActivity  extends TabActivity implements TabHost.OnTabChangeListener {

    private TabHost mTabHost;
    private static final int ANIMATION_TIME = 240;
    private View previousView;
    private View currentView;
    private GestureDetector gestureDetector;
    private int currentTab;


    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_lines);

        mTabHost=getTabHost();
        TabHost.TabSpec spec;
        Intent intent;

        //line1
        intent =new Intent(this,Line1.class);
        spec =mTabHost.newTabSpec("Cinemas")
                .setContent(intent)
                .setIndicator("Cinemas");


        mTabHost.addTab(spec);

        //line2
        intent =new Intent(this,Line2.class);
        spec =mTabHost.newTabSpec("Movies")
                .setContent(intent)
                .setIndicator("Movies");
        mTabHost.addTab(spec);

        //line 3
        intent =new Intent(this,Line3.class);
        spec =mTabHost.newTabSpec("Timeline")
                .setContent(intent)
                .setIndicator("Timeline");
        mTabHost.addTab(spec);

        //mTabHost.getTabWidget().setStripEnabled(false);

        mTabHost.setCurrentTab(0);
        TextView tv;
        for(int i=0;i<mTabHost.getTabWidget().getChildCount();i++)
        {
            mTabHost.getTabWidget().getChildAt(0).setBackgroundColor(Color.parseColor("#ff8f0101"));
            tv = (TextView) mTabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
            tv.setTextColor(Color.WHITE);
            mTabHost.getTabWidget().getChildAt(1).setBackgroundColor(Color.parseColor("#ff0b3f64"));
            mTabHost.getTabWidget().getChildAt(2).setBackgroundColor(Color.parseColor("#115533"));

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

    public void AnimatedTabHostListener(TabHost tabHost)
    {
        this.mTabHost = tabHost;
        this.previousView = tabHost.getCurrentView();
    }

    @Override
    public void onTabChanged(String tabId)
    {

        currentView = mTabHost.getCurrentView();
        if (mTabHost.getCurrentTab() > currentTab)
        {
            previousView.setAnimation(outToLeftAnimation());
            currentView.setAnimation(inFromRightAnimation());
        }
        else
        {
            previousView.setAnimation(outToRightAnimation());
            currentView.setAnimation(inFromLeftAnimation());
        }
        previousView = currentView;
        currentTab = mTabHost.getCurrentTab();

    }

    /**
     * Custom animation that animates in from right
     *
     * @return Animation the Animation object
     */
    private Animation inFromRightAnimation()
    {
        Animation inFromRight = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 1.0f, Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f);
        return setProperties(inFromRight);
    }

    /**
     * Custom animation that animates out to the right
     *
     * @return Animation the Animation object
     */
    private Animation outToRightAnimation()
    {
        Animation outToRight = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f);
        return setProperties(outToRight);
    }

    /**
     * Custom animation that animates in from left
     *
     * @return Animation the Animation object
     */
    private Animation inFromLeftAnimation()
    {
        Animation inFromLeft = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, -1.0f, Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f);
        return setProperties(inFromLeft);
    }

    /**
     * Custom animation that animates out to the left
     *
     * @return Animation the Animation object
     */
    private Animation outToLeftAnimation()
    {
        Animation outtoLeft = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, -1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f);
        return setProperties(outtoLeft);
    }

    /**
     * Helper method that sets some common properties
     * @param animation the animation to give common properties
     * @return the animation with common properties
     */
    private Animation setProperties(Animation animation)
    {
        animation.setDuration(ANIMATION_TIME);
        animation.setInterpolator(new AccelerateInterpolator());
        return animation;
    }

}
