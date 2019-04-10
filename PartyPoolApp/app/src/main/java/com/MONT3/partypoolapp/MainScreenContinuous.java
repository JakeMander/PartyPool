package com.MONT3.partypoolapp;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import android.support.v4.view.ViewPager;

public class MainScreenContinuous extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen_continuous);


        ViewPager pager = findViewById(R.id.viewPagerContinuous);

        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));

        pager.setCurrentItem(1);

    }

    private class MyPagerAdapter extends FragmentPagerAdapter{
        public MyPagerAdapter(FragmentManager fm)
        {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            if(getIntent().getExtras().getString("LoginType").equals("Admin"))
            {
                switch(i)
                {
                    case 1 : return new mid_continuous_admin();
                    case 2 : return new SocialContinuousFrag();
                    default : return new mid_continuous_admin();
                }
            }
            else {
                switch (i) {
                    case 0:
                        return new VoteContinuousFrag();
                    case 1:
                        return new MidContinuousFrag();
                    case 2:
                        return new SocialContinuousFrag();
                    default:
                        return new MidContinuousFrag();
                }
            }
        }

        @Override
        public int getCount()
        {
            return 3;
        }
    }

}
