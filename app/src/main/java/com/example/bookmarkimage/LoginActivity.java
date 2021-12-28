package com.example.bookmarkimage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bookmarkimage.Dialogs.BookmarkImageGroupDialog;

import Database.DatabaseHelper;
import Models.User;

public class LoginActivity extends AppCompatActivity {

    private EditText textBox_email, textBox_password;
    DatabaseHelper db;
    private int iterator ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        InitializeComponents();

    }

    private void InitializeComponents() {
        this.textBox_email = (EditText) findViewById(R.id.main_textBox_email);
        this.textBox_password = (EditText) findViewById(R.id.main_passwordBox_password);
    }

    public void ButtonLogin_Click(View view) {
        User tmpUser;
        DatabaseHelper db = new DatabaseHelper(LoginActivity.this);
        try {
            if (db != null) {
                if (!textBox_email.getText().toString().isEmpty()) {
                    tmpUser = db.GetUser(textBox_email.getText().toString());
                    if (tmpUser != null) {
                        if (!textBox_password.getText().toString().isEmpty()) {
                            //Toast.makeText(LoginActivity.this, "E-mail postoji " + textBox_password.getText().toString(), Toast.LENGTH_LONG).show();
                            if (tmpUser.GetPassword().equals(textBox_password.getText().toString())) {
                                goToUserDashboard(tmpUser);
                            } else {
                                Toast.makeText(LoginActivity.this, "Korisnicko ime i lozinka se ne poklapaju", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, "Unesite lozinku", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "Ne postoji nalog sa unetom E-mail adresom ili korisničkim imenom", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Unesite korisničko ime ili lozinku", Toast.LENGTH_LONG).show();
                }
            }
        } catch (Exception e) {
            Toast.makeText(LoginActivity.this, "Exception: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void LabelRegister_Click(View view) {
        User usr = new User();
        Intent intent = new Intent(this, RegisterActivity.class);
        intent.putExtra("UserObj", usr);
        startActivity(intent);
    }

    private void goToUserDashboard(User inUser) {
        //Toast.makeText(LoginActivity.this, "Korisnik " + tmpUser.GetFullname() + " se uspesno ulogovao", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, UserDashboard.class);
        intent.putExtra("UserObj", inUser);
        startActivity(intent);
        finish();
    }

    public void testButton_click(View view) {
        BookmarkImageGroupDialog dialogTest = new BookmarkImageGroupDialog();
        dialogTest.show(getSupportFragmentManager(), "test dialog");
    }
}//[Class]