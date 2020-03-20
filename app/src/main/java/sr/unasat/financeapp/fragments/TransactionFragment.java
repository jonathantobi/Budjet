package sr.unasat.financeapp.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import sr.unasat.financeapp.R;
import sr.unasat.financeapp.activities.MainActivity;
import sr.unasat.financeapp.dao.TransactionDao;
import sr.unasat.financeapp.helpers.DateHelper;
import sr.unasat.financeapp.interfaces.Updateable;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class TransactionFragment extends Fragment implements Updateable{
    static final int NUM_FRAGMENTS = 2;
    ViewPager viewPager;
    TabLayout tabs;
    Adapter adapter;


    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_transaction, container, false);

        // Setting ViewPager for each Tabs
        viewPager = view.findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        // Set Tabs inside Toolbar
        tabs = view.findViewById(R.id.result_tabs);
        tabs.setupWithViewPager(viewPager);

        return view;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(getActivity(), "Edit success", Toast.LENGTH_SHORT).show();
                update(DateHelper.dateToMiliseconds(MainActivity.selectedDate));
            }
        }
    }

    private void setupViewPager(ViewPager viewPager){
        adapter = new Adapter(getChildFragmentManager());
        adapter.addFragment(new IncomeTransactionFragment(),"Income");
        adapter.addFragment(new ExpenseTransactionFragment(),"Expense");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void update(long date) {
        adapter.notifyDataSetChanged();
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager manager) {
            super(manager);
        }
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

        @Override
        // To update fragment in ViewPager, we should override getItemPosition() method,
        // in this method, we call the fragment's public updating method.
        public int getItemPosition(Object object) {
            if (object instanceof IncomeTransactionFragment) {
                ((IncomeTransactionFragment) object).update(DateHelper.dateToMiliseconds(MainActivity.selectedDate));
            } else if (object instanceof ExpenseTransactionFragment) {
                ((ExpenseTransactionFragment) object).update(DateHelper.dateToMiliseconds(MainActivity.selectedDate));
            }

            return super.getItemPosition(object);
        };
    }




}
