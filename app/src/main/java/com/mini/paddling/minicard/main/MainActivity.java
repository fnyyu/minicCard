package com.mini.paddling.minicard.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.mini.paddling.minicard.R;

public class MainActivity extends AppCompatActivity {
    
    private Fragment[] fragments;
    private int lastFragmentIndex;

    private CardFragment homeFragment;
    private CardFragment cardFragment;
    private MineFragment mineFragment;

    private BottomNavigationView navigationView;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    if(lastFragmentIndex != 0) {
                        switchFragment(lastFragmentIndex, 0);
                        lastFragmentIndex = 0;

                    }

                    return true;
                case R.id.navigation_mine:
                    if(lastFragmentIndex != 2) {
                        switchFragment(lastFragmentIndex, 2);
                        lastFragmentIndex = 2;

                    }
                    return true;

                case R.id.navigation_card:
                    if(lastFragmentIndex != 1) {
                        switchFragment(lastFragmentIndex, 1);
                        lastFragmentIndex = 1;

                    }
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigationView = findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        initFragment();
    }

    private void initFragment() {

        Bundle bundle = new Bundle();
        bundle.putString("portId", "collect");

        homeFragment = new CardFragment();
        homeFragment.setArguments(bundle);

        Bundle bundle2 = new Bundle();
        bundle2.putString("portId", "mine");
        cardFragment = new CardFragment();
        cardFragment.setArguments(bundle2);

        mineFragment = new MineFragment();
        fragments = new Fragment[]{homeFragment, cardFragment, mineFragment};

        lastFragmentIndex=0;

        getSupportFragmentManager().beginTransaction().replace(R.id.fl_content, homeFragment).show(homeFragment).commit();
    }


    private void switchFragment(int lastFragmentIndex, int index) {

        FragmentTransaction transaction =getSupportFragmentManager().beginTransaction();
        transaction.hide(fragments[lastFragmentIndex]);

        if(!fragments[index].isAdded()) {
            transaction.add(R.id.fl_content,fragments[index]);

        }
        transaction.show(fragments[index]).commitAllowingStateLoss();

    }


}
