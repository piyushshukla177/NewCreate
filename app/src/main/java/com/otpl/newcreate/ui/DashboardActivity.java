package com.otpl.newcreate.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.otpl.newcreate.R;
import com.otpl.newcreate.databinding.ActivityDashboardBinding;
import com.otpl.newcreate.utils.AppConstants;


public class DashboardActivity extends BaseActivity {

    private static final String TAG = DashboardActivity.class.getSimpleName();
    private Context mContext = DashboardActivity.this;
    private ActivityDashboardBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = putContentView(R.layout.activity_dashboard);
        binding.setDashboardActivity(this);
        setToolbarTitle("डैशबोर्ड");

    }

    public void onClickForSkill(View view) {
        Intent i = new Intent(mContext, MainActivity.class);
        i.putExtra(AppConstants.NAV_POSITION, 0);
        startActivity(i);
    }

    public void onClickServices(View view) {
        Intent i = new Intent(mContext, MainActivity.class);
        i.putExtra(AppConstants.NAV_POSITION, 1);
        startActivity(i);
    }

    public void onClickVideos(View view) {
        Intent i = new Intent(mContext, MainActivity.class);
        i.putExtra(AppConstants.NAV_POSITION, 2);
        startActivity(i);
    }

    public void onClickSkills(View view) {
        Intent i = new Intent(mContext, MainActivity.class);
        i.putExtra(AppConstants.NAV_POSITION, 3);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        if (item.getItemId() == R.id.action_profile) {
            Intent i = new Intent(mContext, MainActivity.class);
            i.putExtra(AppConstants.NAV_POSITION, 4);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
