package com.otpl.newcreate.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.otpl.newcreate.R;
import com.otpl.newcreate.databinding.ActivityMainBinding;
import com.otpl.newcreate.network.ApiClient;
import com.otpl.newcreate.network.ApiHelper;
import com.otpl.newcreate.utils.AppConstants;


import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
    private Context mContext = MainActivity.this;
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private ApiHelper apiHelper;
    private View headerView;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setSupportActionBar(binding.toolbar);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_skills, R.id.nav_for_skills,
                R.id.nav_videos, R.id.nav_services, R.id.nav_profile)
                .setDrawerLayout(binding.drawerLayout)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
        getIntentData();

        apiHelper = ApiClient.getClient().create(ApiHelper.class);
        headerView = binding.navView.getHeaderView(0);

    }

    @Override
    protected void onStart() {
        super.onStart();
        //getUserProfile();
    }

    private void getIntentData() {
        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null && intent.hasExtra(AppConstants.NAV_POSITION)) {
            int navPosition = intent.getExtras().getInt(AppConstants.NAV_POSITION);
            if (navPosition == 0) {
                navController.navigate(R.id.nav_for_skills);
            } else if (navPosition == 1) {
                navController.navigate(R.id.nav_services);
            } else if (navPosition == 2) {
                navController.navigate(R.id.nav_videos);
            } else if (navPosition == 3) {
                navController.navigate(R.id.nav_skills);
            } else if (navPosition == 4) {
                navController.navigate(R.id.nav_profile);
            }
        }
    }

}