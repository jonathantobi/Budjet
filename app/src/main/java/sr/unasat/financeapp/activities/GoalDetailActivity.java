package sr.unasat.financeapp.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import sr.unasat.financeapp.R;
import sr.unasat.financeapp.dao.GoalDao;
import sr.unasat.financeapp.entities.Goal;
import sr.unasat.financeapp.helpers.DateHelper;

public class GoalDetailActivity extends AppCompatActivity {
    GoalDao goalDao;

    TextView goalAction, goalMonth;

    EditText limitAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_detail);

        Toolbar toolbar = findViewById(R.id.goal_detail_toolbar);
        toolbar.setTitle("Create Goal");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        goalAction = findViewById(R.id.goal_action);
        goalMonth = findViewById(R.id.goal_month);

        goalAction.setText(R.string.creating_goal_for);
        Calendar c = Calendar.getInstance();
        goalMonth.setText(new DateHelper().monthIntToString(c.get(Calendar.MONTH))+" "+c.get(Calendar.YEAR));

        Button goalCreate = findViewById(R.id.button_goaldetail_create);

        limitAmount = findViewById(R.id.goaldetail_limit_amount);

        goalCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addGoal(v);
            }
        });
    }

    public void addGoal(View view){
        double amountValue = Double.parseDouble(limitAmount.getText().toString());

        Goal goal = new Goal(amountValue);

        if(goalDao == null){
            goalDao = new GoalDao(this);
        }

        boolean status = goalDao.addGoal(goal);

        if(status){
            Toast.makeText(this, "Succesfully created goal", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.putExtra("status",status);
            setResult(RESULT_OK, intent);
            finish();
        }else{
            Toast.makeText(this, "Something wrong", Toast.LENGTH_SHORT).show();
        }


    }
}
