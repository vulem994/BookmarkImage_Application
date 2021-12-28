package com.example.bookmarkimage.ui.gallery;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.bookmarkimage.Adapters.BookmarkImageGroupAdapter;
import com.example.bookmarkimage.AddGroup;
import com.example.bookmarkimage.Common.HelperModel;
import com.example.bookmarkimage.R;
import com.example.bookmarkimage.RegisterActivity;
import com.example.bookmarkimage.UserDashboard;
import com.example.bookmarkimage.databinding.FragmentGalleryBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.lang.reflect.Array;
import java.util.ArrayList;

import Database.DatabaseHelper;
import Models.BookmarkImageGroup;
import Models.User;

public class GalleryFragment extends Fragment {

    private GalleryViewModel galleryViewModel;
    private FragmentGalleryBinding binding;

    private User currentUser;

    //controls
    FloatingActionButton floatButtonAddGroup;
    ListView groupsListView;

    //models
    ArrayList<BookmarkImageGroup> groupsList = new ArrayList<>();
    BookmarkImageGroupAdapter groupAdapter;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        galleryViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });

        UserDashboard activity = (UserDashboard) getActivity();
        if (activity != null) {
            currentUser = activity.getCurrentUser();
            if (currentUser != null) {
                //Toast.makeText(getActivity(), "Debug: Current user dobijen " + currentUser.GetFullname(), Toast.LENGTH_SHORT).show();
            }
        }

        floatButtonAddGroup = binding.floatingActionButtonAddGroup;
        if (floatButtonAddGroup != null && currentUser != null) {
            floatButtonAddGroup.setOnClickListener(v -> {
                //Toast.makeText(getActivity(), "Debug: Click float button add group", Toast.LENGTH_SHORT).show();
                BookmarkImageGroup group = new BookmarkImageGroup();
                group.setParentUserId(currentUser.GetId());
                Intent intent = new Intent(getContext(), AddGroup.class);
                intent.putExtra("groupObj", group);
                //someActivityResultLauncher.launch(intent);
                //startActivity(intent);
                startActivityForResult(intent, HelperModel.Helpers_Activities_NewGroupResultRegister);
            });
        }

        LoadGroups();
        groupsListView = binding.listViewGroups;
        //mockup
        //groupsList.add(new BookmarkImageGroup(1,0,"Grupa1","Grupa1",false,""));
        groupAdapter = new BookmarkImageGroupAdapter(getContext(), R.layout.group_list_item_row, groupsList);
        groupsListView.setAdapter(groupAdapter);

        return root;
    }


    //Old way (Using)
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == HelperModel.Helpers_Activities_NewGroupResultRegister) {
            if (resultCode == Activity.RESULT_OK) {
                BookmarkImageGroup group = (BookmarkImageGroup) data.getSerializableExtra("returnObj");
                if (group != null) {
                    //Toast.makeText(getActivity(), "Debug: Dobijena grupa " + group.getTitle(), Toast.LENGTH_SHORT).show();
                    if (groupAdapter != null) {
                        groupAdapter.addBookmark(group);
                    }
                }

            }
        }
    }


    //New way (Not using)
    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        BookmarkImageGroup group = (BookmarkImageGroup) data.getSerializableExtra("returnObj");
                        if (group != null) {
                            //Toast.makeText(getActivity(), "Debug: Dobijena grupa " + group.getTitle(), Toast.LENGTH_SHORT).show();
                        } else {
                            //Toast.makeText(getActivity(), "Debug: Grupa nije dobijena", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        //Toast.makeText(getActivity(), "Debug: Intent nije dobijen", Toast.LENGTH_SHORT).show();
                    }
                }
            });


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void LoadGroups() {
        DatabaseHelper db = new DatabaseHelper(getContext());
        if (db != null && currentUser != null) {
            ArrayList<BookmarkImageGroup> tmpGroups = db.GetGroupsByUserId(currentUser.GetId());
            if (tmpGroups != null && tmpGroups.size() > 0) {
                groupsList.addAll(tmpGroups);
            }else{
                //Toast.makeText(getActivity(), "Debug: Grupe nisu ucitane", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getActivity(), "Debug: Db nije kreiran ili user ne postoji", Toast.LENGTH_SHORT).show();
        }
    }


}