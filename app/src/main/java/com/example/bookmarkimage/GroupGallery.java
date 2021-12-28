package com.example.bookmarkimage;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookmarkimage.Adapters.ImageItemAdapter;
import com.example.bookmarkimage.Common.HelperModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import Database.DatabaseHelper;
import Models.BookmarkImage;
import Models.BookmarkImageGroup;
import Models.User;

public class GroupGallery extends AppCompatActivity {

    ListView imagesListView;

    ArrayList<BookmarkImage> imagesList = new ArrayList<>();
    ImageItemAdapter imagesAdapter;

    User currentUser;
    BookmarkImageGroup currentGroup;

    FloatingActionButton addImageFloat;

    ImageView maskImg;
    TextView maskText;
    EditText pinText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_gallery);

        imagesListView = findViewById(R.id.listView_images);

        maskImg = (ImageView)findViewById(R.id.pg_mask_img);
        maskText = (TextView)findViewById(R.id.pg_mask_text);
        pinText = (EditText)findViewById(R.id.pg_mask_pin);


        currentGroup = (BookmarkImageGroup) getIntent().getSerializableExtra("GroupObj");
        if(currentGroup != null && currentGroup.getPrivateGroup()){
            SetPrivateMask(true);
            if(pinText != null){
                pinText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if(currentGroup != null && !currentGroup.getPrivateGroupPassword().isEmpty()){
                            if(currentGroup.getPrivateGroupPassword().equals(pinText.getText().toString())){
                                SetPrivateMask(false);
                                pinText.removeTextChangedListener(this);
                            }
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
            }
        }

        LoadImages();
        imagesAdapter = new ImageItemAdapter(GroupGallery.this, R.layout.image_list_item_row, imagesList);

        imagesListView.setAdapter(imagesAdapter);

        addImageFloat = findViewById(R.id.floatingActionButton_addImage);
        addImageFloat.setOnClickListener(v -> {
            BookmarkImage tmpImg = new BookmarkImage();
            tmpImg.setParentGroupId(currentGroup.getId());
            Intent intent = new Intent(this, AddImage.class);
            intent.putExtra("bookmarkImageObj", tmpImg);
            startActivityForResult(intent,  HelperModel.Helpers_Activities_NewImageResultRegister);
        });

    }

    private void SetPrivateMask(boolean maskOn) {
        if(maskOn){
            maskImg.setVisibility(View.VISIBLE);
            maskText.setVisibility(View.VISIBLE);
            pinText.setVisibility(View.VISIBLE);
        }
        else{
            maskImg.setVisibility(View.INVISIBLE);
            maskText.setVisibility(View.INVISIBLE);
            pinText.setVisibility(View.INVISIBLE);
        }
    }


    private void LoadImages() {
        if (currentGroup != null) {
            DatabaseHelper db = new DatabaseHelper(GroupGallery.this);
            if (db != null && currentGroup != null) {
                ArrayList<BookmarkImage> tmpImages = db.GetImagesByGroupId(currentGroup.getId());
                if (tmpImages != null && tmpImages.size() > 0) {
                    imagesList.addAll(tmpImages);
                }else{
                    //Toast.makeText(getActivity(), "Debug: Grupe nisu ucitane", Toast.LENGTH_SHORT).show();
                }
            }else{
                //Toast.makeText(GroupGallery.this, "Debug: Db nije kreiran ili user ne postoji", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == HelperModel.Helpers_Activities_NewImageResultRegister) {
            if (resultCode == Activity.RESULT_OK) {
                BookmarkImage image = (BookmarkImage) data.getSerializableExtra("returnImgObj");
                if (image != null) {
                    //Toast.makeText(getActivity(), "Debug: Dobijena grupa " + group.getTitle(), Toast.LENGTH_SHORT).show();
                    if (imagesAdapter != null) {
                        imagesAdapter.addImage(image);
                    }
                }
            }
        }
    }
}//[Class]