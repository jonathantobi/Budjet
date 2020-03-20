package sr.unasat.financeapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout; // add dependency com.android.support:design
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class LoginActivity extends AppCompatActivity {

    // Declaration EditTexts
    private EditText editTextEmail, editTextPassword;

    // Declaration TextInputLayout
    TextInputLayout textInputLayoutEmail, textInputLayoutPassword;

    // Declaration Buttons
    Button buttonLogin;

    UserDao userDao;

    //String URL = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //initCreateAccountTextView();
        initViews();

        // Set click event of login button
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Check user input is correct or not
                if (validate()) {

                    //Get values from EditText fields
                    String Email = editTextEmail.getText().toString();
                    String Password = editTextPassword.getText().toString();

                    if(userDao == null){
                        userDao = new UserDao(LoginActivity.this);
                    }

                    //Authenticate user
                    User currentUser = userDao.Authenticate(new User(0, null, Email, Password));

                    //Check Authentication is successful or not
                    if (currentUser != null) {
                        Snackbar.make(buttonLogin, "Successfully Logged in!", Snackbar.LENGTH_LONG).show();

                        //User Logged in Successfully Launch You home screen activity
                        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {

                        //User Logged in Failed
                        Snackbar.make(buttonLogin, "Failed to log in , please try again", Snackbar.LENGTH_LONG).show();

                    }

//                    StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>(){
//                        @Override
//                        public void onResponse(String s) {
//                            if(s.equals("true")){
//                                Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_LONG).show();
//                                startActivity(new Intent(LoginActivity.this,MainActivity.class));
//                            }
//                            else{
//                                Toast.makeText(LoginActivity.this, "Incorrect Details", Toast.LENGTH_LONG).show();
//                            }
//                        }
//                    },new Response.ErrorListener(){
//                        @Override
//                        public void onErrorResponse(VolleyError volleyError) {
//                            Toast.makeText(LoginActivity.this, "Some error occurred -> "+volleyError, Toast.LENGTH_LONG).show();;
//                        }
//                    }) {
//                        @Override
//                        protected Map<String, String> getParams() throws AuthFailureError {
//                            Map<String, String> parameters = new HashMap<String, String>();
//                            parameters.put("email", editTextEmail.getText().toString());
//                            parameters.put("password", editTextPassword.getText().toString());
//                            return parameters;
//                        }
//                    };
//
//                    RequestQueue rQueue = Volley.newRequestQueue(LoginActivity.this);
//                    rQueue.add(request);
                }
            }
        });

    }

    // When click on Create Account go to register activity
    public void onButtonCreate(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    //this method is used to connect XML views to its Objects
    private void initViews() {
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        textInputLayoutEmail = (TextInputLayout) findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPassword = (TextInputLayout) findViewById(R.id.textInputLayoutPassword);
        buttonLogin = (Button) findViewById(R.id.buttonLogin);
    }

    //This method is used to validate input given by user
    public boolean validate() {
        boolean valid = false;

        //Get values from EditText fields
        String Email = editTextEmail.getText().toString();
        String Password = editTextPassword.getText().toString();

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