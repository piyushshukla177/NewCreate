package com.otpl.newcreate;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import com.otpl.newcreate.data.local.prefs.PrefsHelper;
import com.otpl.newcreate.data.model.api.LoggedInUser;
import com.otpl.newcreate.utils.AppConstants;
import com.otpl.newcreate.utils.JSONConverter;

public class MyApplication extends Application implements LifecycleObserver {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }
    public static final String TAG = MyApplication.class.getSimpleName();
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getAppContext() {
        return context;
    }

    public static boolean getAppOpenStatus() {
        return PrefsHelper.getBoolean(context, AppConstants.APP_FOREGROUNDED);
    }

    public static String getUserId() {
        String loggedInUserDetails = PrefsHelper.getString(context, AppConstants.LOGGED_IN_USER);
        if (loggedInUserDetails != null) {
            LoggedInUser loggedInUser = (LoggedInUser) JSONConverter.getInstance().getModel(
                    LoggedInUser.class, loggedInUserDetails);
            if (loggedInUser != null) {
                return loggedInUser.getId();
            }
        }
        return null;
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onAppBackgrounded() {
        // App in background
        PrefsHelper.putBoolean(context, AppConstants.APP_FOREGROUNDED, false);
    }

     @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onAppForegrounded() {
        // App in foreground
        PrefsHelper.putBoolean(context, AppConstants.APP_FOREGROUNDED, true);
    }
}
