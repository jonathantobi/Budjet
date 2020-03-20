package sr.unasat.financeapp.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import sr.unasat.financeapp.R;
import sr.unasat.financeapp.activities.GoalDetailActivity;
import sr.unasat.financeapp.activities.MainActivity;
import sr.unasat.financeapp.dao.GoalDao;
import sr.unasat.financeapp.entities.Goal;
import sr.unasat.financeapp.helpers.DateHelper;
import sr.unasat.financeapp.interfaces.Updateable;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class GoalFragment extends Fragment implements Updateable{
    View view;
    GoalDao goalDao;
    Goal goal;

    LinearLayout layoutNoGoal, layoutYesGoal;

    Button createGoal;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // do something such as init data
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_goal, container, false);

        layoutNoGoal = view.findViewById(R.id.no_goal_found_content);
        layoutYesGoal = view.findViewById(R.id.goal_exist_content);
        createGoal = view.findViewById(R.id.button_create_goal);

        createGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GoalDetailActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        update(DateHelper.dateToMiliseconds(MainActivity.selectedDate));
        return view;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                System.out.println("Goal made, update fragment now");
                update(DateHelper.dateToMiliseconds(MainActivity.selectedDate));
            }
        }
    }

    @Override
    public void update(long date) {
        //System.out.println("updating goal fragment");
        if(goalDao == null){
            goalDao = new GoalDao(getActivity());
        }

        goal = goalDao.getGoal(date);

        if(goal != null){
            //do goal view
            layoutYesGoal.setVisibility(View.VISIBLE);
            layoutNoGoal.setVisibility(View.GONE);
        }else{
            //do nothing view
            layoutYesGoal.setVisibility(View.GONE);
            layoutNoGoal.setVisibility(View.VISIBLE);


        }

    }
}
