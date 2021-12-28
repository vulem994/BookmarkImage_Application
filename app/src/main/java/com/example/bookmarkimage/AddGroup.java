package com.example.bookmarkimage;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bookmarkimage.Adapters.BookmarkImageGroupAdapter;

import Models.BookmarkImageGroup;
import Models.User;

public class AddGroup extends AppCompatActivity {

    BookmarkImageGroup currentGroup;
    BookmarkImageGroupAdapter adapter;
    //Controls
    EditText groupTitle, groupDescription, groupPin, groupRepeatPin;
    CheckBox privateGroupCheckBox;
    Button addGroupButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group);

        try {
            groupTitle = findViewById(R.id.addGroup_title);
            groupTitle.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (currentGroup != null) {
                        currentGroup.setTitle(groupTitle.getText().toString());
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            groupDescription = findViewById(R.id.addGroup_description);
            groupDescription.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (currentGroup != null) {
                        currentGroup.setDescription(groupDescription.getText().toString());
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            groupPin = findViewById(R.id.addGroup_password);
            groupPin.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (currentGroup != null) {
                        currentGroup.setPrivateGroupPassword(groupPin.getText().toString());
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            groupRepeatPin = findViewById(R.id.addGroup_repeatPassword);

            privateGroupCheckBox = findViewById(R.id.addGroup_checkBox_private);
            if (privateGroupCheckBox != null) {
                privateGroupCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    SetEnableDisablePin(privateGroupCheckBox.isChecked());
                });
            }

            addGroupButton = findViewById(R.id.addGroup_add_button);
            if (addGroupButton != null) {
                addGroupButton.setOnClickListener(v -> {
                    boolean valid = CheckValidity();
                    if (valid) {
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("returnObj", currentGroup);
                        setResult(Activity.RESULT_OK,resultIntent);
                        finish();
                    }

                });
            }

            SetGroup((BookmarkImageGroup) getIntent().getSerializableExtra("groupObj"));
            //adapter = (BookmarkImageGroupAdapter) getIntent().getSerializableExtra("adapterObj");
        } catch (Exception e) {
            Toast.makeText(this, "Exception: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private boolean CheckValidity() {
        if (groupTitle.getText() == null || groupTitle.getText().toString().isEmpty()) {
            groupTitle.setError("Enter Title");
            return false;
        }
        if(privateGroupCheckBox.isChecked()){
            if(groupPin.getText() == null || groupPin.getText().toString().isEmpty()){
                groupPin.setError("You must set pin");
            }
            if(groupRepeatPin.getText() == null || groupRepeatPin.getText().toString().isEmpty()){
                groupRepeatPin.setError("Please repeat pin");
                return false;
            }
            if(!groupPin.getText().toString().equals(groupRepeatPin.getText().toString())){
                groupPin.setError("Pin does not match");
                return false;
            }
        }
        return true;
    }

    private void SetEnableDisablePin(boolean enabled) {
        try {
            groupPin.setEnabled(enabled);
            groupRepeatPin.setEnabled(enabled);
            if(currentGroup != null){
                currentGroup.setPrivateGroup(enabled);
            }
        } catch (Exception e) {
            Toast.makeText(this, "Exception: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    private void SetGroup(BookmarkImageGroup group) {
        try {
            currentGroup = group;
            if (currentGroup != null) {
                groupTitle.setText(group.getTitle());
                groupDescription.setText(group.getDescription());
                privateGroupCheckBox.setChecked(group.getPrivateGroup());
            }

        } catch (Exception e) {
            Toast.makeText(this, "Exception: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}//[Class]