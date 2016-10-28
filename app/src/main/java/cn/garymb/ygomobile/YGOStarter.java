package cn.garymb.ygomobile;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

import java.util.HashMap;

import cn.garymb.ygodata.YGOGameOptions;

public class YGOStarter {

    private static void setFullScreen(Activity activity, ActivityShowInfo activityShowInfo) {
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (activity instanceof AppCompatActivity) {
            ActionBar actionBar = ((AppCompatActivity) activity).getSupportActionBar();
            if (actionBar != null) {
                actionBar.hide();
            }
        } else {
            android.app.ActionBar actionBar = activity.getActionBar();
            if (actionBar != null) {
                actionBar.hide();
            }
        }
    }

    private static void quitFullScreen(Activity activity, ActivityShowInfo activityShowInfo) {
        if (activity instanceof AppCompatActivity) {
            ActionBar actionBar = ((AppCompatActivity) activity).getSupportActionBar();
            if (activityShowInfo.hasSupperbar && actionBar != null) {
                actionBar.show();
            }
        } else {
            android.app.ActionBar actionBar = activity.getActionBar();
            if (activityShowInfo.hasBar && actionBar != null) {
                actionBar.show();
            }
        }
        final WindowManager.LayoutParams attrs = activity.getWindow().getAttributes();
        attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
        activity.getWindow().setAttributes(attrs);
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    private static void showLoadingBg(Activity activity) {
        ActivityShowInfo activityShowInfo = Infos.get(activity);
        if (activityShowInfo == null) {
            return;
        }
        activityShowInfo.oldRequestedOrientation = activity.getRequestedOrientation();
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//强制为横屏
        activityShowInfo.rootOld = activityShowInfo.mRoot.getBackground();
        activityShowInfo.mContentView.setVisibility(View.INVISIBLE);
        activityShowInfo.mRoot.setBackgroundResource(R.drawable.bg);
        setFullScreen(activity, activityShowInfo);
    }

    private static void hideLoadingBg(Activity activity, ActivityShowInfo activityShowInfo) {
        activity.setRequestedOrientation(activityShowInfo.oldRequestedOrientation);
        activityShowInfo.mContentView.setVisibility(View.VISIBLE);
        if (Build.VERSION.SDK_INT >= 16) {
            activityShowInfo.mRoot.setBackground(activityShowInfo.rootOld);
        } else {
            activityShowInfo.mRoot.setBackgroundDrawable(activityShowInfo.rootOld);
        }
        quitFullScreen(activity, activityShowInfo);
    }

    public static ActivityShowInfo initInfo(Activity activity) {
        ActivityShowInfo activityShowInfo = Infos.get(activity);
        if (activityShowInfo == null) {
            activityShowInfo = new ActivityShowInfo();
            Infos.put(activity, activityShowInfo);
        }
        activityShowInfo.oldRequestedOrientation = activity.getRequestedOrientation();
        activityShowInfo.mRoot = activity.getWindow().getDecorView();
        activityShowInfo.mContentView = activityShowInfo.mRoot.findViewById(android.R.id.content);
        if (activity instanceof AppCompatActivity) {
            ActionBar actionBar = ((AppCompatActivity) activity).getSupportActionBar();
            if (actionBar != null) {
                activityShowInfo.hasSupperbar = actionBar.isShowing();
            }
        } else {
            android.app.ActionBar actionBar = activity.getActionBar();
            if (actionBar != null) {
                activityShowInfo.hasBar = true;
            }
        }
        return activityShowInfo;
    }

    public static void onResume(Activity activity) {
        ActivityShowInfo activityShowInfo = Infos.get(activity);
        if (activityShowInfo == null) {
            return;
        }
        if (!activityShowInfo.isFirst) {
            hideLoadingBg(activity, activityShowInfo);
        }
        activityShowInfo.isFirst = false;
    }

    public static void startGame(Activity activity, YGOGameOptions options) {
        showLoadingBg(activity);
        Intent intent = new Intent(activity, YGOMobileActivity.class);
        if (options != null) {
            intent.putExtra(YGOGameOptions.YGO_GAME_OPTIONS_BUNDLE_KEY, options);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    private static HashMap<Activity, ActivityShowInfo> Infos = new HashMap<>();

    private static class ActivityShowInfo {
        View mRoot;
        boolean hasSupperbar;
        boolean hasBar;
        View mContentView;
        Drawable rootOld;
        boolean isFirst = true;
        int oldRequestedOrientation;
    }
}
