package com.octalabs.challetapp.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.octalabs.challetapp.R;
import com.octalabs.challetapp.adapter.MyPagerAdapter;
import com.octalabs.challetapp.fragments.HomeFragment;

public class MainActivity extends AppCompatActivity {
    private MyPagerAdapter mPagerAdapter;
    private ViewPager mMainViewPager;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mMainViewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_search:
//                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_booking:
//                    mTextMessage.setText(R.string.title_notifications);
                    return true;
                case R.id.navigation_profile:
//                    mTextMessage.setText(R.string.title_notifications);
                    return true;
                case R.id.navigation_more:
//                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionbar = getSupportActionBar();
        setTextAction(actionbar);
        Init();
    }

    private void Init() {
        mPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        mPagerAdapter.addFragmeent(new HomeFragment(), "Home");
        mMainViewPager = findViewById(R.id.main_pager);
        mMainViewPager.setAdapter(mPagerAdapter);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private void setTextAction(ActionBar actionbar) {
        TextView textview = new TextView(MainActivity.this);
        RelativeLayout.LayoutParams layoutparams = new RelativeLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        textview.setLayoutParams(layoutparams);
        textview.setText(getResources().getString(R.string.home));
        textview.setTextColor(Color.WHITE);
        textview.setGravity(Gravity.CENTER);
        textview.setTextSize(16);
        actionbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionbar.setCustomView(textview);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_home_menu, menu);
        if (menu instanceof MenuBuilder) {
            MenuBuilder menuBuilder = (MenuBuilder) menu;
            menuBuilder.setOptionalIconsVisible(true);
        }
        return true;
    }
}
