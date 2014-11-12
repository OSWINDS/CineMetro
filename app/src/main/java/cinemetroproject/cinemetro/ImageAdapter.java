package cinemetroproject.cinemetro;

/**
 * Created by kiki__000 on 11-Nov-14.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ImageAdapter extends FragmentPagerAdapter{

    int PAGE_COUNT1;

    /** Constructor of the class */
    public ImageAdapter(FragmentManager fm, int count) {

        super(fm);
        PAGE_COUNT1 = count;
    }

    /** This method will be invoked when a page is requested to create */
    @Override
    public Fragment getItem(int arg0) {

        ImageFragment myFragment = new ImageFragment();
        Bundle data = new Bundle();
        data.putInt("current_page", arg0+1);
        myFragment.setArguments(data);
        return myFragment;
    }

    /** Returns the number of pages */
    @Override
    public int getCount() {
        return PAGE_COUNT1;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        String temp=(position+1) + "/" + PAGE_COUNT1;

        return temp;
    }
}
