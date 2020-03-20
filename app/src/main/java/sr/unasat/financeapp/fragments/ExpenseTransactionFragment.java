package sr.unasat.financeapp.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import sr.unasat.financeapp.R;
import sr.unasat.financeapp.activities.MainActivity;
import sr.unasat.financeapp.dao.TransactionDao;
import sr.unasat.financeapp.entities.Transaction;
import sr.unasat.financeapp.helpers.DateHelper;
import sr.unasat.financeapp.helpers.TransactionAdapter;
import sr.unasat.financeapp.interfaces.Updateable;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExpenseTransactionFragment extends Fragment implements Updateable{
    View view;
    TransactionDao transactionDao;
    ArrayList<Transaction> expenses;
    TransactionAdapter listAdapter;
    ListView expensesListView;

    public ExpenseTransactionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_expense_transaction, container, false);

        expensesListView = view.findViewById(R.id.listview_expenses);

        update(DateHelper.dateToMiliseconds(MainActivity.selectedDate));
        return view;
    }


    @Override
    public void update(long date) {
        if(transactionDao == null){
            transactionDao = new TransactionDao(super.getActivity());
        }

        expenses = transactionDao.getAllExpense(date);
        listAdapter = new TransactionAdapter(getActivity(), expenses);
        expensesListView.setAdapter(listAdapter);
    }
}
