package sr.unasat.financeapp.activities;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Calendar;

import sr.unasat.financeapp.R;
import sr.unasat.financeapp.dao.UserDao;
import sr.unasat.financeapp.fragments.CurrenciesIntentService;
import sr.unasat.financeapp.fragments.DashboardFragment;
import sr.unasat.financeapp.fragments.DatePickerFragment;
import sr.unasat.financeapp.fragments.TransactionFragment;
import sr.unasat.financeapp.fragments.GoalFragment;
import sr.unasat.financeapp.fragments.ProfileFragment;
import sr.unasat.financeapp.helpers.DateHelper;

public class MainActivity extends AppCompatActivity {
    static final int NUM_FRAGMENTS = 3;
    SectionsPagerAdapter pagerAdapter;
    ViewPager viewPager;
    TabLayout tabLayout;

    Button dateButton, refreshButton;
    private DialogFragment dateDialogfragment;
    public static String selectedDate;

    private static int[] tabIcons = {
            R.drawable.ic_dashboard_white_24dp, //tab icon at postition 1
            R.drawable.ic_attach_money_white_24dp, //tab icon at postition 2
            R.drawable.ic_person_white_24dp //tab icon at postition 3
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //remove title from toolbar
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // get date button
        dateButton = findViewById(R.id.dateButton);
        // set onClick listener datebutton
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        refreshButton = findViewById(R.id.refreshButton);

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateActivity();
            }
        });



        Intent currencyIntent = new Intent(this, CurrenciesIntentService.class);
        startService(currencyIntent);

        // set selected date today
        Calendar c = Calendar.getInstance();
        selectedDate = formSelectedDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));

        //Attach the SectionsPagerAdapter to the ViewPager
        pagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        viewPager = findViewById(R.id.pager);
        viewPager.setAdapter(pagerAdapter);

        //Attach the ViewPager to the TabLayout
        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
    }

    private void setupTabIcons(){
        if(tabLayout != null){
            tabLayout.getTabAt(0).setIcon(tabIcons[0]);
            tabLayout.getTabAt(1).setIcon(tabIcons[1]);
            tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        }
    }

    private void updateActivity(){
        pagerAdapter.notifyDataSetChanged();
        setupTabIcons();
    }

    /**
     * Handle the new set date here.
     * @param year
     * @param month
     * @param day
     */
    public void onDateSet(int year, int month, int day){
        selectedDate = formSelectedDate(year, month, day);
        updateActivity();
        Calendar c = Calendar.getInstance();
        if(year == c.get(Calendar.YEAR) && month == c.get(Calendar.MONTH) && day == c.get(Calendar.DAY_OF_MONTH)) {
            dateButton.setText(R.string.today);
        }
        else {
            dateButton.setText(day + " " + new DateHelper().monthIntToString(month)+" "+year);
        }
    }

    private String formSelectedDate(int year, int month, int day){
        return year + "-" + String.format("%02d",month+1) + "-" + String.format("%02d",day);
    }

    private void showDatePickerDialog(){
        if(dateDialogfragment == null) {
            dateDialogfragment = new DatePickerFragment();
        }
        dateDialogfragment.show(getSupportFragmentManager(), "datePicker");
    }

    // Option menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Logout:
                logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logout() {
        UserDao userDao = new UserDao(this);
        if(userDao.logUserOut()){
            Intent intent = new Intent(MainActivity.this,LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }else{
            Toast.makeText(this, "Failed to logout", Toast.LENGTH_SHORT).show();
        }

    }

    public void onClickAdd(View view) {
        Intent intentTransaction = new Intent(this, TransactionActivity.class);
        startActivityForResult(intentTransaction, 1);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
//                (dashboardFragment).updateView(this, selectedDate);
                System.out.println("Transaction made, update fragments now");
                updateActivity();

            }
        }
    }


    public static class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return NUM_FRAGMENTS;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                        return new DashboardFragment();
                case 1:
                        return new TransactionFragment();
                case 2:
                        return new ProfileFragment();
            }
            return null;
        }

        @Override
        // To update fragment in ViewPager, we should override getItemPosition() method,
        // in this method, we call the fragment's public updating method.
        public int getItemPosition(Object object) {
            if (object instanceof DashboardFragment) {
                ((DashboardFragment) object).update(DateHelper.dateToMiliseconds(selectedDate));
            } else if (object instanceof TransactionFragment) {
                ((TransactionFragment) object).update(DateHelper.dateToMiliseconds(selectedDate));
            }

            return super.getItemPosition(object);
        };
    }


}