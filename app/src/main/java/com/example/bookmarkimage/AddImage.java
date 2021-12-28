package com.example.bookmarkimage;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.bookmarkimage.Common.HelperModel;
import com.example.bookmarkimage.Common.ImageFilePath;

import java.io.InputStream;
import java.util.ArrayList;

import Models.BookmarkImage;

public class AddImage extends AppCompatActivity {

    Spinner spinnerSource;
    EditText path;
    Button addImage, selectLocalImagePath, clearPath;
    ImageView previewImage;

    BookmarkImage currentImage;

    //helpers
    ArrayAdapter<CharSequence> sourceTypeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_image);

        currentImage = (BookmarkImage) getIntent().getSerializableExtra("bookmarkImageObj");

        spinnerSource = (Spinner) findViewById(R.id.spinner_imgSource);
        path = (EditText) findViewById(R.id.editText_imagePath);
        path.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (currentImage != null) {
                    currentImage.setLocalImagePath(path.getText().toString());
                    currentImage.setWebImageUrl(path.getText().toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!path.getText().toString().isEmpty()) {
                    clearPath.setVisibility(View.VISIBLE);
                }
                //SetPreviewImage();
            }
        });

        clearPath = (Button) findViewById(R.id.button_clearPath);
        if (clearPath != null) {
            clearPath.setOnClickListener(v -> {
                path.setText("");
                clearPath.setVisibility(View.INVISIBLE);
            });
        }

        addImage = (Button) findViewById(R.id.button_addImage);
        if (addImage != null) {
            addImage.setOnClickListener(v -> {
                boolean valid = CheckValidity();
                if (valid) {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("returnImgObj", currentImage);
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                }
            });
        }
        selectLocalImagePath = (Button) findViewById(R.id.button_selectLocalImage);
        if (selectLocalImagePath != null) {
            selectLocalImagePath.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), HelperModel.Helpers_Activities_PickImage);
            });
        }
        previewImage = (ImageView) findViewById(R.id.imageView_previewImage);

        sourceTypeAdapter = ArrayAdapter.createFromResource(AddImage.this, R.array.imageSourceTypes, android.R.layout.simple_spinner_item);
        sourceTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSource.setAdapter(sourceTypeAdapter);
        spinnerSource.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (sourceTypeAdapter.getItem(position).equals("Web image")) {
                    SetSelectedSourceType(true);
                    //Toast.makeText(AddImage.this, "Debug: Selected Web image", Toast.LENGTH_LONG).show();
                } else if (sourceTypeAdapter.getItem(position).equals("Local image")) {
                    SetSelectedSourceType(false);
                    //Toast.makeText(AddImage.this, "Debug: Selected Local image", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private boolean CheckValidity() {
        if (path.getText() == null || path.getText().toString().isEmpty()) {
            path.setError("Image is not selected");
            return false;
        }
        return true;
    }

    private void SetPreviewImage() {
        if (currentImage != null && previewImage != null) {
            if (currentImage.getFromWebImage() && !currentImage.getWebImageUrl().isEmpty()) {
                try {
                    Glide.with(this).load(currentImage.getWebImageUrl()).into(previewImage);
                } catch (Exception e) {
                    Toast.makeText(this, "Exception: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }
    }

    private void SetSelectedSourceType(boolean webImage) {
        if (webImage) {
            selectLocalImagePath.setVisibility(View.INVISIBLE);
            path.setEnabled(true);
        } else {
            selectLocalImagePath.setVisibility(View.VISIBLE);
            path.setEnabled(false);
        }

        if (currentImage != null) {
            currentImage.setFromWebImage(webImage);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == HelperModel.Helpers_Activities_PickImage) {
            if (data == null) {
                Toast.makeText(AddImage.this, "Debug:  Data is null", Toast.LENGTH_LONG).show();
            }
            try {
                Uri uri = data.getData();

                String realPath = ImageFilePath.getPath(AddImage.this, data.getData());
                path.setText(realPath);

                Bitmap myBitmap = BitmapFactory.decodeFile(realPath);
                previewImage.setImageBitmap(myBitmap);




                Toast.makeText(AddImage.this, "Debug:  image selected", Toast.LENGTH_LONG).show();

            } catch (Exception e) {
                //Toast.makeText(this, "Exception: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }

        }
    }


}//[Class]