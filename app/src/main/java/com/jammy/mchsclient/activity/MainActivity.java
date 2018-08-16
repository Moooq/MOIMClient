package com.jammy.mchsclient.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.google.gson.Gson;
import com.jammy.mchsclient.MyApplication;
import com.jammy.mchsclient.R;
import com.jammy.mchsclient.fragment.ChatFragment;
import com.jammy.mchsclient.fragment.ContactsFragment;
import com.jammy.mchsclient.fragment.DiscoverFragment;
import com.jammy.mchsclient.fragment.MeFragment;
import com.jammy.mchsclient.model.ReturnFriends;
import com.jammy.mchsclient.url.API;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;

import static com.jammy.mchsclient.MyApplication.activityMap;

public class MainActivity extends FragmentActivity implements BottomNavigationBar.OnTabSelectedListener {
    @BindView(R.id.viewPager)
    public ViewPager viewPager;

    BottomNavigationBar mBottomNavigationBar;

    private List<Fragment> fragmentList;
    ReturnFriends returnFriends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activityMap.put("MainActivity", this);
        ButterKnife.bind(this);
        initViewPager();
        initBottomBar();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Necessary to restore the BottomBar's state, otherwise we would
        // lose the current tab on orientation change.
    }


    public void initBottomBar() {
        mBottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
        mBottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        mBottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        mBottomNavigationBar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        mBottomNavigationBar.addItem(new BottomNavigationItem(R.drawable.chat_fill, getString(R.string.chat)).setInactiveIconResource(R.drawable.chat).setActiveColorResource(R.color.yellow).setInActiveColorResource(R.color.gray))
                .addItem(new BottomNavigationItem(R.drawable.contacts_fill, getString(R.string.contacts)).setInactiveIconResource(R.drawable.contacts).setActiveColorResource(R.color.yellow).setInActiveColorResource(R.color.gray))
                .addItem(new BottomNavigationItem(R.drawable.discover_fill, getString(R.string.discover)).setInactiveIconResource(R.drawable.discover).setActiveColorResource(R.color.yellow).setInActiveColorResource(R.color.gray))
                .addItem(new BottomNavigationItem(R.drawable.me_fill, getString(R.string.me)).setInactiveIconResource(R.drawable.me).setActiveColorResource(R.color.yellow).setInActiveColorResource(R.color.gray))
                .setFirstSelectedPosition(0)
                .initialise();

        mBottomNavigationBar.setTabSelectedListener(this);

    }

    private void initViewPager() {
        fragmentList = new ArrayList<>();
        fragmentList.add(new ChatFragment());
        fragmentList.add(new ContactsFragment());
        fragmentList.add(new DiscoverFragment());
        fragmentList.add(new MeFragment());
        viewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getCount() {
                return fragmentList.size();
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mBottomNavigationBar.selectTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * Called when a tab enters the selected state.
     *
     * @param position The position of the tab that was selected
     */
    @Override
    public void onTabSelected(int position) {
        switch (position) {
            case 0:
                viewPager.setCurrentItem(0);
                break;
            case 1:
                viewPager.setCurrentItem(1);
                break;
            case 2:
                viewPager.setCurrentItem(2);
                break;
            case 3:
                viewPager.setCurrentItem(3);
                break;
        }
    }

    /**
     * Called when a tab exits the selected state.
     *
     * @param position The position of the tab that was unselected
     */
    @Override
    public void onTabUnselected(int position) {

    }

    /**
     * Called when a tab that is already selected is chosen again by the user. Some applications
     * may use this action to return to the top level of a category.
     *
     * @param position The position of the tab that was reselected.
     */
    @Override
    public void onTabReselected(int position) {

    }
}