package sr.unasat.financeapp.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import sr.unasat.financeapp.R;
import sr.unasat.financeapp.activities.LoginActivity;
import sr.unasat.financeapp.activities.MainActivity;
import sr.unasat.financeapp.dao.UserDao;
import sr.unasat.financeapp.entities.Currency;
import sr.unasat.financeapp.entities.User;
import sr.unasat.financeapp.interfaces.Updateable;

/**
 * A simple {@link Fragment} subclass.
 */

public class ProfileFragment extends Fragment{

    public static String selectedCurrency = "SRD";

    private View view;
    private UserDao userDao;

    private LinearLayout profileView, noProfileView, editProfileView;

    private Button btnGoToLogin, btnEditProfile, btnDeleteProfile, btnSaveEdit;

    private TextView textViewUsername, textViewEmail, textViewSelectedCurrency;

    private EditText editTextFullName, editTextEmail, editTextPassword;
    private TextInputLayout textInputLayoutUserName, textInputLayoutEmail, textInputLayoutPassword;

    private Spinner currencySpinner;

    private User user;


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false);

        initViews();

        checkLoggedIn();

        return view;
    }

    private void checkLoggedIn(){
        if(userDao == null){
            userDao = new UserDao(getActivity());
        }

        user = userDao.isLoggedIn();

        if(user != null){
            System.out.println("user logged in");

            setupUser(user);

            //show profile info
            noProfileView.setVisibility(View.GONE);
            profileView.setVisibility(View.VISIBLE);
        }else{
            System.out.println("user not logged in");

            //show login button
            noProfileView.setVisibility(View.VISIBLE);
            profileView.setVisibility(View.GONE);
        }
    }

    private void initViews(){
        profileView = view.findViewById(R.id.profile_view);
        noProfileView = view.findViewById(R.id.no_profile_view);
        editProfileView = view.findViewById(R.id.profile_editprofile);
        initProfileView();
        initEditView();
    }

    private void setupUser(User user){
        if(userDao == null){
            userDao = new UserDao(getActivity());
        }

        textViewUsername.setText(user.getUsername());
        textViewEmail.setText(user.getEmail());
        textViewSelectedCurrency.setText(userDao.getSelectedCurrency());
    }

    private void initProfileView(){
        btnGoToLogin = view.findViewById(R.id.button_to_login);
        btnEditProfile = view.findViewById(R.id.button_edit_profile);
        btnDeleteProfile = view.findViewById(R.id.button_delete_profile);
        textViewUsername = view.findViewById(R.id.profile_username);
        textViewEmail = view.findViewById(R.id.profile_email);
        textViewSelectedCurrency = view.findViewById(R.id.profile_selected_currency);

        btnGoToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),LoginActivity.class);
                startActivity(intent);
            }
        });

        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileView.setVisibility(View.GONE);
                editProfileView.setVisibility(View.VISIBLE);
            }
        });

        btnDeleteProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = userDao.isLoggedIn();

                boolean status = userDao.deleteUser(user.getId());

                if(status){
                    Intent intent = new Intent(getActivity(),LoginActivity.class);
                    startActivity(intent);
                }

            }
        });
    }

    //this method is used to connect XML views to its Objects
    private void initEditView() {

        editTextEmail = view.findViewById(R.id.editprofile_editTextEmail);
        editTextPassword = view.findViewById(R.id.editprofile_editTextPassword);
        editTextFullName = view.findViewById(R.id.editprofile_editTextFullName);
        textInputLayoutEmail = view.findViewById(R.id.editprofile_textInputLayoutEmail);
        textInputLayoutPassword = view.findViewById(R.id.editprofile_textInputLayoutPassword);
        textInputLayoutUserName = view.findViewById(R.id.editprofile_textInputLayoutFullName);
        currencySpinner = view.findViewById(R.id.editprofile_currency_spinner);
        btnSaveEdit = view.findViewById(R.id.button_update_profile);

        final ArrayList<Currency> currencies = CurrenciesIntentService.currencies;

        if(currencies != null){
            ArrayAdapter<Currency> adapter = new ArrayAdapter<Currency>(getActivity(), R.layout.simple_spinner_dropdown_item, currencies );
            adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);

            currencySpinner.setAdapter(adapter);
        }

        currencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int selectedItem = currencySpinner.getSelectedItemPosition();
                userDao.updateSelectedCurrency(currencies.get(selectedItem).getCode());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        btnSaveEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                        int id = user.getId();
                        String userName = editTextFullName.getText().toString();
                        String email = editTextEmail.getText().toString();
                        String password = editTextPassword.getText().toString();

                        //Email does not exist now add new user to database
                        boolean status = userDao.updateUser(new User(id, userName, email, password));

                        if(status){
                            Toast.makeText(getActivity(), "Succesfully updated user", Toast.LENGTH_SHORT).show();
                            user = userDao.isLoggedIn();
                            setupUser(user);
                        }
                    }

                profileView.setVisibility(View.VISIBLE);
                editProfileView.setVisibility(View.GONE);
            }
        });
    }

    //This method is used to validate input given by user
    public boolean validate() {
        boolean valid = false;

        //Get values from EditText fields
        String UserName = editTextFullName.getText().toString();
        String Email = editTextEmail.getText().toString();
        String Password = editTextPassword.getText().toString();

        //Handling validation for UserName field
        if (UserName.isEmpty()) {
            valid = false;
            textInputLayoutUserName.setError("Please enter valid username!");
        } else {
            valid = true;
            textInputLayoutUserName.setError(null);
        }

        //Handling validation for Email field
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
            valid = false;
            textInputLayoutEmail.setError("Please enter valid email!");
        } else {
            valid = true;
            textInputLayoutEmail.setError(null);
        }

        //Handling validation for Password field
        if (Password.isEmpty()) {
            valid = false;
            textInputLayoutPassword.setError("Please enter valid password!");
        } else {
            valid = true;
            textInputLayoutPassword.setError(null);
        }

        return valid;
    }


}
