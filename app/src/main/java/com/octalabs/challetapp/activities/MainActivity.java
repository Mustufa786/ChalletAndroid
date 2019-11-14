package com.octalabs.challetapp.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.octalabs.challetapp.R;
import com.octalabs.challetapp.adapter.MyPagerAdapter;
import com.octalabs.challetapp.custome.NonSwipeableViewPager;
import com.octalabs.challetapp.fragments.FragmentBookingHistory;
import com.octalabs.challetapp.fragments.FragmentMore;
import com.octalabs.challetapp.fragments.FragmentSearch;
import com.octalabs.challetapp.fragments.HomeFragment;
import com.octalabs.challetapp.fragments.LoginFragment;
import com.octalabs.challetapp.fragments.UserProfileFragment;
import com.octalabs.challetapp.retrofit.RetrofitInstance;
import com.octalabs.challetapp.utils.Constants;
import com.octalabs.challetapp.utils.CustomDialog;

public class MainActivity extends AppCompatActivity {
    public static ActionBar getactionbar;
    private MyPagerAdapter mPagerAdapter;
    private NonSwipeableViewPager mMainViewPager;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mMainViewPager.setCurrentItem(1);
                    setTextAction(getactionbar, getResources().getString(R.string.home));
                    return true;
                case R.id.navigation_search:
                    mMainViewPager.setCurrentItem(0);
                    setTextAction(getactionbar, getResources().getString(R.string.title_search));

                    return true;
                case R.id.navigation_booking:
                    mMainViewPager.setCurrentItem(2);
                    setTextAction(getactionbar, getResources().getString(R.string.booking_history));
                    return true;
                case R.id.navigation_profile:
                    mMainViewPager.setCurrentItem(3);
                    setTextAction(getactionbar, getResources().getString(R.string.user_profile));
                    return true;
                case R.id.navigation_more:
                    mMainViewPager.setCurrentItem(4);
                    setTextAction(getactionbar, getResources().getString(R.string.more));
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
        setTextAction(actionbar, getResources().getString(R.string.title_search));
        getactionbar = actionbar;
        Init();
    }

    private void Init() {
        mPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        mPagerAdapter.addFragmeent(new FragmentSearch(), getResources().getString(R.string.title_search));
        if (getSharedPreferences("main", MODE_PRIVATE).getBoolean(Constants.IS_USER_LOGGED_IN, false)) {
            mPagerAdapter.addFragmeent(new HomeFragment(), getResources().getString(R.string.home));
            mPagerAdapter.addFragmeent(new FragmentBookingHistory(), getResources().getString(R.string.booking_history));
            mPagerAdapter.addFragmeent(new UserProfileFragment(), getResources().getString(R.string.user_profile));
            mPagerAdapter.addFragmeent(new FragmentMore(), getResources().getString(R.string.more));
        } else {
            mPagerAdapter.addFragmeent(new LoginFragment(), getResources().getString(R.string.login));
            mPagerAdapter.addFragmeent(new LoginFragment(), getResources().getString(R.string.login));
            mPagerAdapter.addFragmeent(new LoginFragment(), getResources().getString(R.string.login));
            mPagerAdapter.addFragmeent(new LoginFragment(), getResources().getString(R.string.login));
        }


        mMainViewPager = findViewById(R.id.main_pager);
        mMainViewPager.setAdapter(mPagerAdapter);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private void setTextAction(ActionBar actionbar, String title) {
        TextView textview = new TextView(MainActivity.this);
        RelativeLayout.LayoutParams layoutparams = new RelativeLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        textview.setLayoutParams(layoutparams);
        textview.setText(title);
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.ADD) {
            startActivity(new Intent(this, ActivityCart.class));
        } else if (item.getItemId() == R.id.options) {
            CustomDialog cd = new CustomDialog(MainActivity.this);
            cd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            cd.show();

        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mMainViewPager.getCurrentItem() == 3) {
            ((UserProfileFragment) mPagerAdapter.getItem(3)).onActivityResultHandle(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (RetrofitInstance.service == null) {
            RetrofitInstance.createRetrofitInstance();
        }
    }
}
