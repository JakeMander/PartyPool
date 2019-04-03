package com.MONT3.partypoolapp;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.view.ViewPager;

public class MainScreenParty extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen_party);


        ViewPager pager = findViewById(R.id.viewPagerParty);

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
                    case 1 : return new mid_party_admin();
                    default : return new mid_party_admin();
                }
            }
            else {
                switch (i) {
                    case 0:
                        return new VotePartyFrag();
                    case 1:
                        return new MidPartyFrag();
                    case 2:
                        return new SocialPartyFrag();
                    default:
                        return new MidPartyFrag();
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
