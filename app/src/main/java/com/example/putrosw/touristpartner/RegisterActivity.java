package com.example.putrosw.touristpartner;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

import com.eyro.mesosfer.MesosferException;
import com.eyro.mesosfer.MesosferUser;
import com.eyro.mesosfer.RegisterCallback;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Putro SW on 21-Nov-16.
 */
public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText textEmail, textPassword, textFirstname, textLastname, textDateOfBirth,
            textNickname;
    //private Switch switchIsMarried;

    private String email, password, firstname, lastname, dateOfBirthString, nickname;
    private Date dateOfBirth;
    //private boolean isMarried;

    private ProgressDialog loading;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Mesosfer Registration");
        }

        // initialize input form view
        textEmail = (TextInputEditText) findViewById(R.id.text_email);
        textPassword = (TextInputEditText) findViewById(R.id.text_password);
        textFirstname = (TextInputEditText) findViewById(R.id.text_firstname);
        textLastname = (TextInputEditText) findViewById(R.id.text_lastname);
        /*textHeight = (TextInputEditText) findViewById(R.id.text_height);
        textWeight = (TextInputEditText) findViewById(R.id.text_weight);
        switchIsMarried = (Switch) findViewById(R.id.switch_is_married);*/
        textNickname = (TextInputEditText) findViewById(R.id.text_nickname);

        loading = new ProgressDialog(this);
        loading.setIndeterminate(true);
        loading.setCancelable(false);
        loading.setCanceledOnTouchOutside(false);
    }

    public void handleRegister(View view) {
        // get all value from input
        email = textEmail.getText().toString();
        password = textPassword.getText().toString();
        firstname = textFirstname.getText().toString();
        lastname = textLastname.getText().toString();
        nickname = textNickname.getText().toString();
        /*height = textHeight.getText().toString();
        weight = textWeight.getText().toString();
        isMarried = switchIsMarried.isChecked();*/

        // validating input values
        if (!isInputValid()) {
            // return if there is an invalid input
            return;
        }

        registerUser();
    }

    private boolean isInputValid() {
        // validating all input values if it is empty
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Email is empty", Toast.LENGTH_LONG).show();
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Password is empty", Toast.LENGTH_LONG).show();
            return false;
        }
        if (TextUtils.isEmpty(firstname)) {
            Toast.makeText(this, "First name is empty", Toast.LENGTH_LONG).show();
            return false;
        }
        if (TextUtils.isEmpty(lastname)) {
            Toast.makeText(this, "Last name is empty", Toast.LENGTH_LONG).show();
            return false;
        }
        /*if (TextUtils.isEmpty(height)) {
            Toast.makeText(this, "Height is empty", Toast.LENGTH_LONG).show();
            return false;
        }
        if (TextUtils.isEmpty(weight)) {
            Toast.makeText(this, "Weight is empty", Toast.LENGTH_LONG).show();
            return false;
        }*/
        if (TextUtils.isEmpty(nickname)) {
            Toast.makeText(this, "Nick Name is empty", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void registerUser() {
        // showing a progress dialog loading
        loading.setMessage("Registering new user...");
        loading.show();

        // create new instance of Mesosfer User
        MesosferUser newUser = MesosferUser.createUser();
        // set default field
        newUser.setEmail(email);
        newUser.setPassword(password);
        newUser.setFirstName(firstname);
        newUser.setLastName(lastname);
        // set custom field
        /*newUser.setData("height", Double.parseDouble(height));
        newUser.setData("weight", Integer.parseInt(weight));
        newUser.setData("isMarried", isMarried);*/
        newUser.setData("nickname", nickname);
        // execute register user asynchronous
        newUser.registerAsync(new RegisterCallback() {
            @Override
            public void done(MesosferException e) {
                // hide progress dialog loading
                loading.dismiss();

                // setup alert dialog builder
                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);

                // check if there is an exception happen
                if (e != null) {
                    builder.setNegativeButton(android.R.string.ok, null);
                    builder.setTitle("Error Happen");
                    builder.setMessage(
                            String.format(Locale.getDefault(), "Error code: %d\nDescription: %s",
                                    e.getCode(), e.getMessage())
                    );
                    dialog = builder.show();
                    return;
                }
                builder.setNegativeButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setResult(Activity.RESULT_OK, new Intent());
                        finish();
                    }
                });
                builder.setTitle("Register Succeeded");
                builder.setMessage("Thank you for registering.");
                dialog = builder.show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        return true;
    }

    @Override
    protected void onDestroy() {
        // dismiss any resource showing
        if (loading != null && loading.isShowing()) {
            loading.dismiss();
        }

        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

        super.onDestroy();
    }
}

