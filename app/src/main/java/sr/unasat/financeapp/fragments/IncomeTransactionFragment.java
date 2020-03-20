package sr.unasat.financeapp.fragments;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ser.impl.StringArraySerializer;

import java.util.ArrayList;

import sr.unasat.financeapp.R;
import sr.unasat.financeapp.activities.MainActivity;
import sr.unasat.financeapp.activities.TransactionActivity;
import sr.unasat.financeapp.dao.TransactionDao;
import sr.unasat.financeapp.entities.Transaction;
import sr.unasat.financeapp.helpers.DateHelper;
import sr.unasat.financeapp.helpers.TransactionAdapter;
import sr.unasat.financeapp.interfaces.Updateable;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class IncomeTransactionFragment extends Fragment implements Updateable{
    View view;
    TransactionDao transactionDao;
    ArrayList<Transaction> income;
    TransactionAdapter listAdapter;
    ListView incomeListView;
    String[] menuItems = {
            "Edit",
            "Delete"
    };

    public IncomeTransactionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_income_transaction, container, false);

        incomeListView = view.findViewById(R.id.listview_income);
        update(DateHelper.dateToMiliseconds(MainActivity.selectedDate));
        return view;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if(v.getId() == R.id.listview_income){
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            menu.setHeaderTitle(income.get(info.position).getTitle());

            for (int i = 0; i < menuItems.length; i++){
                menu.add(Menu.NONE, i, i, menuItems[i]);
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int menuItemIndex = item.getItemId();
        String menuItemName = menuItems[menuItemIndex];

        switch (menuItemName){
            case "Edit":
//                Toast.makeText(getActivity(), "Edit item " + income.get(info.position).getId(), Toast.LENGTH_SHORT).show();
                Intent intentTransaction = new Intent(getActivity(), TransactionActivity.class);
                intentTransaction.putExtra("transactionID", Integer.toString(income.get(info.position).getId()));
                startActivity(intentTransaction);
                break;
            case "Delete":
                final int transactionID = income.get(info.position).getId();
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                if(transactionDao.deleteTransaction(transactionID)){
                                    Toast.makeText(getActivity(), "Transaction deleted", Toast.LENGTH_SHORT).show();
                                    listAdapter.notifyDataSetChanged();
                                    update(DateHelper.dateToMiliseconds(MainActivity.selectedDate));
                                }

                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
                break;
            default:
                break;
        }

        return true;
    }

    @Override
    public void update(long date) {
        if(transactionDao == null){
            transactionDao = new TransactionDao(super.getActivity());
        }

        income = transactionDao.getAllIncome(date);
        listAdapter = new TransactionAdapter(getActivity(), income);
        incomeListView.setAdapter(listAdapter);
        registerForContextMenu(incomeListView);


//        incomeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getActivity(), "Clicked", Toast.LENGTH_SHORT).show();
//            }
//        });

    }
}
