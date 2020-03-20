package sr.unasat.financeapp.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import sr.unasat.financeapp.R;
import sr.unasat.financeapp.activities.MainActivity;
import sr.unasat.financeapp.dao.TransactionDao;
import sr.unasat.financeapp.entities.Transaction;
import sr.unasat.financeapp.helpers.CurrencyHelper;
import sr.unasat.financeapp.helpers.DateHelper;
import sr.unasat.financeapp.helpers.TransactionAdapter;
import sr.unasat.financeapp.interfaces.Updateable;

/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment implements Updateable{
    TransactionDao transactionDao;
    View view;
    TextView balanceText, incomeText, expenseText;

    TransactionAdapter listAdapter;
    ListView recentsListView;

    Button recentAccordionButton, viewAllRecents;

    ViewPager viewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // do something such as init data
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        final ArrayList<Transaction> recipeList = Recipe.getRecipesFromFile("recipes.json", this);
        view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        balanceText = view.findViewById(R.id.balans);
        incomeText = view.findViewById(R.id.total_income);
        expenseText = view.findViewById(R.id.total_expense);
        recentsListView = view.findViewById(R.id.listview_recents);
        recentAccordionButton = view.findViewById(R.id.button_recents);
        viewAllRecents = view.findViewById(R.id.button_view_all_recents);

        viewPager = getActivity().findViewById(R.id.pager);

        recentAccordionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout accordionRecentContent = view.findViewById(R.id.accordion_expense_content);

                if(accordionRecentContent.getVisibility() != view.VISIBLE){
                    accordionRecentContent.setVisibility(View.VISIBLE);
                    return;
                }

                accordionRecentContent.setVisibility(View.GONE);
            }
        });

        viewAllRecents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(1);
            }
        });
        update(DateHelper.dateToMiliseconds(MainActivity.selectedDate));
        return view;
    }

    @Override
    public void update(long date){

        if(transactionDao == null){
            transactionDao = new TransactionDao(getActivity());
        }

        //update balance view
        double totalIncome = transactionDao.getTotalIncome(date);
        double totalExpense = transactionDao.getTotalExpense(date);
        double balance = totalIncome - totalExpense;

        incomeText.setText(CurrencyHelper.returnStringCurrency(totalIncome, getActivity()));
        expenseText.setText(CurrencyHelper.returnStringCurrency(totalExpense, getActivity()));
        balanceText.setText(CurrencyHelper.returnStringCurrency(balance, getActivity()));


        //update recent expenses view
        ArrayList<Transaction> recentExpenses = transactionDao.getRecents(date);
        listAdapter = new TransactionAdapter(getActivity(), recentExpenses);
        recentsListView.setAdapter(listAdapter);

        TextView no_recent = view.findViewById(R.id.no_recents);

        if(!(recentExpenses.size() > 0)){
            no_recent.setVisibility(View.VISIBLE);
        }else{
            no_recent.setVisibility(View.GONE);
        }
    }


}
