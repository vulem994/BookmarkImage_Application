package com.example.bookmarkimage.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.example.bookmarkimage.Dialogs.BookmarkImageGroupDialog;
import com.example.bookmarkimage.GroupGallery;
import com.example.bookmarkimage.R;
import com.example.bookmarkimage.RegisterActivity;
import com.example.bookmarkimage.UserDashboard;
import com.example.bookmarkimage.ui.gallery.GalleryFragment;

import java.io.Serializable;
import java.util.ArrayList;

import Database.DatabaseHelper;
import Models.BookmarkImage;
import Models.BookmarkImageGroup;

public class BookmarkImageGroupAdapter extends ArrayAdapter<BookmarkImageGroup> implements Serializable, BookmarkImageGroupDialog.BookmarkImageGroupDialogListener {

    private Context context;
    private int resource;


    BookmarkImageGroup clickedItem;
    AlertDialog.Builder builder;

    //Helper props

    public BookmarkImageGroupAdapter(@NonNull Context context, int resource, @NonNull ArrayList<BookmarkImageGroup> groups) {
        super(context, resource, groups);

        this.context = context;
        this.resource = resource;


        builder = new AlertDialog.Builder(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        convertView = layoutInflater.inflate(resource, parent, false);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, "Debug: Click on list view item", Toast.LENGTH_SHORT).show();
                clickedItem = getItem(position);
                OpenGroup(clickedItem);
            }
        });

        ImageView lockedGroupImg = convertView.findViewById(R.id.imageView_lock);
        if (!getItem(position).getPrivateGroup()) {
            lockedGroupImg.setVisibility(View.INVISIBLE);
        }

        ImageView removeGroupImg = convertView.findViewById(R.id.imageView_removeGroup);
        removeGroupImg.setOnClickListener(v -> {
            //deleteBookmark(getItem(position));
            bookmarkImageGroupForDelete = getItem(position);
            AlertDialog dialog = builder.setTitle("Delete Bookmark image group")
                    .setMessage("Are you sure to delete " + bookmarkImageGroupForDelete.getTitle() + "?")
                    .setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener)
                    .show();
        });
        TextView groupNameText = convertView.findViewById(R.id.textView_groupName);
        TextView groupDescText = convertView.findViewById(R.id.textView_groupDescription);

        groupNameText.setText(getItem(position).getTitle());
        groupDescText.setText(getItem(position).getDescription());

        return convertView;
    }


    //Add & delete bookmark
    private BookmarkImageGroup bookmarkImageGroupForDelete;

    public void deleteBookmark(BookmarkImageGroup item) {
        DatabaseHelper db = new DatabaseHelper(getContext());
        if (db != null) {
            boolean result = db.DeleteBookmarkImageGroup(item);
            if (result) {
                remove(item);
                notifyDataSetChanged();
                bookmarkImageGroupForDelete = null;
            }
        }
    }

    public void addBookmark(BookmarkImageGroup item) {
        DatabaseHelper db = new DatabaseHelper(getContext());
        if (db != null) {
            boolean result = db.AddBookmarkImageGroup(item);
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
                if (bookmarkImageGroupForDelete != null) {
                    deleteBookmark(bookmarkImageGroupForDelete);
                }
                break;

            case DialogInterface.BUTTON_NEGATIVE:
                //Toast.makeText(context, "Debug: Click no onDialog", Toast.LENGTH_SHORT).show();
                break;
        }
    };

    @Override
    public void applyText(String pin) {
        if (clickedItem != null && clickedItem.getPrivateGroupPassword() != null && clickedItem.getPrivateGroupPassword().equals(pin)) {
            OpenGroup(clickedItem);
        } else {
            Toast.makeText(context, "Pin se ne poklapa", Toast.LENGTH_SHORT).show();
        }
    }

    private void OpenGroup(BookmarkImageGroup clickedItem) {
        Intent intent = new Intent(getContext(), GroupGallery.class);
        //intent.putExtra("UserObj", currentUser);
        intent.putExtra("GroupObj",clickedItem);
        getContext().startActivity(intent);
        //Toast.makeText(context, "Debug: Group oppened", Toast.LENGTH_SHORT).show();
    }
}//[Class]
