package com.example.test1706;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class User_HoaDon_Activity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_hoa_don);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));


    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_admin__hoa_don, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_admin__hoa_don, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    User_HoaDon_Fragment fragment1 = new User_HoaDon_Fragment();
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("Status", "Chờ xác nhận");
                    
                    fragment1.setArguments(bundle1);
                    return fragment1;
                case 1:
                    User_HoaDon_Fragment fragment2 = new User_HoaDon_Fragment();
                    Bundle bundle2 = new Bundle();
                    bundle2.putString("Status", "Chờ lấy hàng");
                    fragment2.setArguments(bundle2);
                    return fragment2;
                case 2:
                    User_HoaDon_Fragment fragment3 = new User_HoaDon_Fragment();
                    Bundle bundle3 = new Bundle();
                    bundle3.putString("Status", "Đang giao");
                    fragment3.setArguments(bundle3);
                    return fragment3;
                case 3:
                    User_HoaDon_Fragment fragment4 = new User_HoaDon_Fragment();
                    Bundle bundle4 = new Bundle();
                    bundle4.putString("Status", "Đã giao");
                    fragment4.setArguments(bundle4);
                    return fragment4;
                case 4:
                    User_HoaDon_Fragment fragment5 = new User_HoaDon_Fragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("Status", "Khách hủy");
                    fragment5.setArguments(bundle);
                    return fragment5;
                case 5:
                    User_HoaDon_Fragment fragment6 = new User_HoaDon_Fragment();
                    Bundle bundle5 = new Bundle();
                    bundle5.putString("Status", "Admin hủy");
                    fragment6.setArguments(bundle5);
                    return fragment6;

            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 6;
        }
    }
}
