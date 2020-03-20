package sr.unasat.financeapp.activities;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Date;

import sr.unasat.financeapp.R;
import sr.unasat.financeapp.dao.TransactionDao;
import sr.unasat.financeapp.entities.Transaction;

public class TransactionActivity extends AppCompatActivity {

    private TransactionDao transactionDao;
    private boolean edit = false;
    private boolean status = false;
    private Button saveTransaction;
    private Spinner transactionType;
    private EditText transactionTitle, transactionAmount;
    private String transactionID;
    String type = "created";
    private Transaction transaction = new Transaction();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();

        if(intent.getStringExtra("transactionID") != null){
            edit = true;
            transactionID = intent.getStringExtra("transactionID");
            Log.d(this.getClass().getSimpleName(), transactionID);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        Toolbar toolbar = (Toolbar) findViewById(R.id.transaction_toolbar);
        if(edit){
            toolbar.setTitle("Edit Transaction");
        }else{
            toolbar.setTitle("Create Transaction");
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        transactionType = findViewById(R.id.spinnerTransactionType);
        transactionTitle = findViewById(R.id.editTextTransactionTitle);
        transactionAmount = findViewById(R.id.editTextTransactionAmount);
        saveTransaction = findViewById(R.id.buttonSaveTransaction);

        if(transactionDao == null){
            transactionDao = new TransactionDao(this);
        }

        if(edit){ //update alle velden als het een update is
            transaction = transactionDao.getTransaction(Integer.parseInt(transactionID)); //haal de te bewerken transactie

            String[] transactionTypes = getResources().getStringArray(R.array.transactionType); //haal de verschillende transactie types
            for(int i = 0;  i < transactionTypes.length ; i++){ //loop over de transacties om de positie in de array te halen
                if(transactionTypes[i].contains(transaction.getType())){
                    transactionType.setSelection(i); //verander de spinner selectie naar de transactie type
                }
            }

            transactionTitle.setText(transaction.getTitle());
            transactionAmount.setText(Double.toString(transaction.getAmount()));
        }

        saveTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SaveTransaction().execute();
            }
        });

    }

    private class SaveTransaction extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            saveTransaction();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if(status){
                Toast.makeText(getApplicationContext(), "Succesfully " + type + " transaction", Toast.LENGTH_LONG).show();
                Intent intent = new Intent();
                intent.putExtra("status",status);
                setResult(RESULT_OK, intent);
                finish();
            }else{
                Toast.makeText(getApplicationContext(), "Something wrong", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }

    private void saveTransaction(){
        String typeValue = transactionType.getSelectedItem().toString();
        String titleValue = transactionTitle.getText().toString();
        double amountValue = Double.parseDouble(transactionAmount.getText().toString());

        transaction.setType(typeValue);
        transaction.setTitle(titleValue);
        transaction.setAmount(amountValue);

        if(edit){
            type = "edited";
        }

        if(edit){
            //edit transaction
            if(transactionDao == null){
                transactionDao = new TransactionDao(getApplicationContext());
            }
            status = transactionDao.editTransaction(transaction);
        }else{
            //add transaction
            if(transactionDao == null){
                transactionDao = new TransactionDao(getApplicationContext());
            }
            status = transactionDao.addTransaction(transaction);
        }
    }

}
