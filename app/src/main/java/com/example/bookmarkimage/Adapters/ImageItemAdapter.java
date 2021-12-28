package com.example.bookmarkimage.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.bumptech.glide.Glide;
import com.example.bookmarkimage.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import Database.DatabaseHelper;
import Models.BookmarkImage;
import Models.BookmarkImageGroup;

public class ImageItemAdapter extends ArrayAdapter<BookmarkImage> {
    private int resource;
    private Context context;


    AlertDialog.Builder builder;

    public ImageItemAdapter(@NonNull Context context, int resource, @NotNull ArrayList<BookmarkImage> images) {
        super(context, resource, images);
        this.context = context;
        this.resource = resource;

        builder = new AlertDialog.Builder(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        convertView = layoutInflater.inflate(resource, parent, false);

        ImageView removeImg = convertView.findViewById(R.id.imageView_removeImg);
        removeImg.setOnClickListener(v -> {
            //deleteBookmark(getItem(position));
            imageForDelete = getItem(position);
            AlertDialog dialog = builder.setTitle("Delete Bookmark image group")
                    .setMessage("Are you sure to delete image?")
                    .setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener)
                    .show();
        });

        ImageView imgView = convertView.findViewById(R.id.imageView_imageItem);
        BookmarkImage img = getItem(position);
        if (imgView != null && img != null) {
            imgView.setOnLongClickListener(v -> {
                imageForDelete = img;
                AlertDialog dialog = builder.setTitle("Delete Bookmark image group")
                        .setMessage("Are you sure to delete image?")
                        .setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener)
                        .show();
                return false;
            });
            Glide.with(context).load(img.getWebImageUrl()).into(imgView);
        }


        return convertView;
    }

    //Add & delete bookmark
    private BookmarkImage imageForDelete;
    public void deleteImage(BookmarkImage item) {
        DatabaseHelper db = new DatabaseHelper(getContext());
        if (db != null) {
            boolean result = db.DeleteBookmarkImage(item);
            if (result) {
                remove(item);
                notifyDataSetChanged();
                imageForDelete = null;
            }
        }
    }

    public void addImage(BookmarkImage item) {
        DatabaseHelper db = new DatabaseHelper(getContext());
        if (db != null) {
            boolean result = db.AddBookmarkImage(item);
            if (result) {
                add(item);
                notifyDataSetChanged();
            }
        }
    }


    DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {

        switch (which) {
            case DialogInterface.BUTTON_POSITIVE:
                //Toast.makeText(context, "Debug: Click yes on dialog", Toast.LENGTH_SHORT).show();
                if (imageForDelete != null) {
                    deleteImage(imageForDelete);
                }
                break;

            case DialogInterface.BUTTON_NEGATIVE:
                //Toast.makeText(context, "Debug: Click no onDialog", Toast.LENGTH_SHORT).show();
                break;
        }
    };
}
