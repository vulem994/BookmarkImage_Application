package com.example.bookmarkimage;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bookmarkimage.databinding.ActivityUserDashboardBinding;

import Models.User;

public class UserDashboard extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityUserDashboardBinding binding;

    NavigationView navigationView;
    DrawerLayout drawer;
    View headerView;

    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SetUser((User) getIntent().getSerializableExtra("UserObj"));

        binding = ActivityUserDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarUserDashboard.toolbar);
       // binding.appBarUserDashboard.fab.setOnClickListener(new View.OnClickListener() {
       //     @Override
       //     public void onClick(View view) {
       //         Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
       //                 .setAction("Action", null).show();
       //     }
       // });
        drawer = binding.drawerLayout;
        navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_user_dashboard);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        headerView = navigationView.getHeaderView(0);
        SetUserInfo();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_Logout:
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
                return  true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_user_dashboard);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }



    private void SetUser(User user) {
        currentUser = user;
        //Toast.makeText(UserDashboard.this, "Debug: User " + currentUser.GetFullname(), Toast.LENGTH_SHORT).show();
    }

    private void SetUserInfo() {
        if(headerView != null && currentUser != null){
            TextView textName = (TextView) headerView.findViewById(R.id.nav_header_name);
            if(textName != null){
                textName.setText(currentUser.GetFullname());
            }
            TextView textMail = (TextView) headerView.findViewById(R.id.nav_header_mail);
            if(textMail != null){
                textMail.setText(currentUser.GetEmail());
            }
        }
    }

    public User getCurrentUser(){return currentUser;}
}