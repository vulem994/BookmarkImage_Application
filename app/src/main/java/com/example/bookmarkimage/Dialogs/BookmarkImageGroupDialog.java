package com.example.bookmarkimage.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.bookmarkimage.R;

public class BookmarkImageGroupDialog extends AppCompatDialogFragment {
    private EditText pin;
    BookmarkImageGroupDialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_bookmark_image_group_dialog, null);

        builder.setView(view)
                .setPositiveButton("Ok", (dialog, which) -> {
                    listener.applyText(pin.getText().toString());
                })
                .setNegativeButton("Cancel", (dialog, which) -> {

                });

        pin = view.findViewById(R.id.dialogGroup_pin);
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener = (BookmarkImageGroupDialogListener) context;
    }

    public interface BookmarkImageGroupDialogListener {
        void applyText(String pin);
    }
}//[Class]
