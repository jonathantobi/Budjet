package sr.unasat.financeapp.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout; // add dependency com.android.support:design
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import sr.unasat.financeapp.R;
import sr.unasat.financeapp.dao.ComfiDbHelper;
import sr.unasat.financeapp.dao.UserDao;
import sr.unasat.financeapp.entities.User;

public class RegisterActivity extends AppCompatActivity {

    //Declaration EditTexts
    private EditText editTextFullName, editTextEmail, editTextPassword;

    //Declaration TextInputLayout
    private TextInputLayout textInputLayoutUserName, textInputLayoutEmail, textInputLayoutPassword;

    //Declaration Button
    private Button buttonRegister;

    private UserDao userDao;

    // TO-DO rest service url plaatsen
//    private String URL = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initTextViewLogin();
        initViews();

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(userDao == null){
                    userDao = new UserDao(RegisterActivity.this);
                }
                if (validate()) {
                    String userName = editTextFullName.getText().toString();
                    String email = editTextEmail.getText().toString();
                    String password = editTextPassword.getText().toString();

                    //Check in the database is there any user associated with  this email
                    if (!userDao.isEmailExists(email)) {

                        //Email does not exist now add new user to database
                        boolean status = userDao.registerUser(new User(0, userName, email, password));

                        if(status){
                            Snackbar.make(buttonRegister, "User created successfully! Please Login ", Snackbar.LENGTH_LONG).show();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    finish();
                                }
                            }, Snackbar.LENGTH_LONG);
                        }

                    }else {

                        //Email exists with email input provided so show error user already exist
                        Snackbar.make(buttonRegister, "User already exists, login or choose another email ", Snackbar.LENGTH_LONG).show();
                    }

//                    StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
//                        @Override
//                        public void onResponse(String s) {
//                            if (s.equals("true")) {
//                                Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_LONG).show();
//                            } else {
//                                Toast.makeText(RegisterActivity.this, "Can't Register", Toast.LENGTH_LONG).show();
//                            }
//                        }
//                    }, new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError volleyError) {
//                            Toast.makeText(RegisterActivity.this, "Some error occurred -> " + volleyError, Toast.LENGTH_LONG).show();
//                        }
//                    }) {
//                        @Override
//                        protected Map<String, String> getParams() throws AuthFailureError {
//                            Map<String, String> parameters = new HashMap<String, String>();
//                            parameters.put("fullname", editTextFullName.getText().toString());
//                            parameters.put("email", editTextEmail.getText().toString());
//                            parameters.put("password", editTextPassword.getText().toString());
//                            return parameters;
//                        }
//                    };
//
//                    RequestQueue rQueue = Volley.newRequestQueue(RegisterActivity.this);
//                    rQueue.add(request);
                }
            }
        });
    }

    //this method used to set Login TextView click event
    private void initTextViewLogin() {
        TextView textViewLogin = findViewById(R.id.textViewLogin);
        textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    //this method is used to connect XML views to its Objects
    private void initViews() {
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextFullName = (EditText) findViewById(R.id.editTextFullName);
        textInputLayoutEmail = (TextInputLayout) findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPassword = (TextInputLayout) findViewById(R.id.textInputLayoutPassword);
        textInputLayoutUserName = (TextInputLayout) findViewById(R.id.textInputLayoutFullName);
        buttonRegister = (Button) findViewById(R.id.buttonRegister);

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