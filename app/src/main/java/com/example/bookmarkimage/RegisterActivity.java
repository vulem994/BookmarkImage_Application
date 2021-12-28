package com.example.bookmarkimage;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Database.DatabaseHelper;
import Models.User;

public class RegisterActivity extends AppCompatActivity {

    private User CurrentUser;

    private EditText textBox_fullname, textBox_email, textBox_password, textbox_passwordRepeat, textBox_date, textBox_username;
    private RadioButton radioButton_male, radioButton_female;
    private RadioGroup radioGroup_gender;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        InitializeComponents();
        SetUser((User) getIntent().getSerializableExtra("UserObj"));

    }

    private void InitializeComponents() {
        this.textBox_fullname = (EditText) findViewById(R.id.reg_textBox_fullName);
        this.textBox_date = (EditText) findViewById(R.id.reg_textBox_dateOfBirth);
        this.textBox_email = (EditText) findViewById(R.id.reg_textBox_email);
        this.textBox_password = (EditText) findViewById(R.id.reg_passwordBox_password);
        this.textbox_passwordRepeat = (EditText) findViewById(R.id.reg_passwordBox_repeatPassword);
        this.textBox_username = (EditText) findViewById(R.id.reg_textBox_username);

        this.radioButton_male = (RadioButton)findViewById(R.id.reg_radioButton_male);
        this.radioButton_female = (RadioButton)findViewById(R.id.reg_radioButton_female);
        this.radioGroup_gender = (RadioGroup)findViewById(R.id.reg_radioButtonGroup_gender);


        //region Add listeners to text boxs
        radioGroup_gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(radioButton_male.isChecked()){
                    if (CurrentUser != null) {
                        //Toast.makeText(RegisterActivity.this, "Debug: Male checked", Toast.LENGTH_SHORT).show();
                        CurrentUser.setGender("Male");
                    }
                }
                else if(radioButton_female.isChecked()){
                    if (CurrentUser != null) {
                        //Toast.makeText(RegisterActivity.this, "Debug: Female checked", Toast.LENGTH_SHORT).show();
                        CurrentUser.setGender("Female");
                    }
                }
            }
        });

        textBox_fullname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (CurrentUser != null) {
                    CurrentUser.SetFullname(textBox_fullname.getText().toString());
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        textBox_username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (CurrentUser != null) {
                    CurrentUser.SetUsername(textBox_username.getText().toString());
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        textBox_date.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (CurrentUser != null) {
                    CurrentUser.SetDateOfBirth(textBox_date.getText().toString());
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        textBox_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (CurrentUser != null) {
                    CurrentUser.SetEmail(textBox_email.getText().toString());
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        textBox_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SetWarningText("");
                if (CurrentUser != null) {
                    CurrentUser.SetPassword(textBox_password.getText().toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        textbox_passwordRepeat.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //endregion
    }

    private void SetUser(User user) {
        CurrentUser = user;
        if (CurrentUser != null) {
            textBox_fullname.setText(CurrentUser.GetFullname());
            textBox_email.setText(CurrentUser.GetEmail());
            textBox_date.setText(CurrentUser.GetEmail());
            textBox_email.setText(CurrentUser.GetEmail());
        }
    }

    public void ButtonRegister_Click(View view) {
        if (CurrentUser != null && CheckRegisterValuesValidation()) {
            DatabaseHelper db = new DatabaseHelper(RegisterActivity.this);
            if (db != null)
                db.AddUser(CurrentUser);
            finish();
        }
    }

    private boolean CheckRegisterValuesValidation() {
        if (CurrentUser != null) {
            if (CurrentUser.GetFullname() == null || CurrentUser.GetFullname().isEmpty()) {
                SetWarningText("Niste uneli ime i prezime");
                return false;
            } else if (CurrentUser.GetDateOfBirth() == null || CurrentUser.GetDateOfBirth().isEmpty()) {
                SetWarningText("Niste uneli datum rođenja");
                return false;
            }else if(!radioButton_male.isChecked() && !radioButton_female.isChecked()){
                SetWarningText("Selektujte pol");
                return false;
            }
            else if(CurrentUser.GetUsername() == null || CurrentUser.GetUsername().isEmpty()){
                SetWarningText("Niste uneli korisničko ime");
                return false;
            } else if (CurrentUser.GetEmail() == null || CurrentUser.GetEmail().isEmpty() || !CheckEmail(CurrentUser.GetEmail())) {
                SetWarningText("Niste uneli E-mail ili nije u dobrom formatu");
                return false;
            } else if (CurrentUser.GetPassword() == null || CurrentUser.GetPassword().isEmpty()) {
                SetWarningText("Niste uneli lozinku");
                return false;
            } else if (!CurrentUser.GetPassword().equals(textbox_passwordRepeat.getText().toString())) {
                SetWarningText("Lozinke se ne poklapaju");
                return false;
            }
            return true;
        }
        return false;
    }

    //region Check if e-mail is vaild function
    private boolean CheckEmail(String inEmail) {
        Pattern pattern = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}");
        Matcher mat = pattern.matcher(inEmail);
        return mat.matches();
    }
    //endregion

    //region Set warning text function
    private void SetWarningText(String inText) {
        if (!inText.isEmpty())
            Toast.makeText(RegisterActivity.this, inText, Toast.LENGTH_SHORT).show();
    }
    //endregion
}//[Class]