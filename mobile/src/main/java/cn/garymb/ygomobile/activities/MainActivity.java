package cn.garymb.ygomobile.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import cn.garymb.ygomobile.lite.R;
import cn.garymb.ygomobile.task.ResCheckTask;
import cn.garymb.ygomobile.utils.VUiKit;
import cn.garymb.ygomobile.utils.YGOStarter;


public class MainActivity extends AppCompatActivity {
    private boolean enableStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        setContentView(layout);
        //资源复制
        checkResourceDownload((error) -> {
            if (error < 0) {
                enableStart = false;
            } else {
                enableStart = true;
            }
        });
        YGOStarter.initInfo(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        YGOStarter.onResume(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_about:

                break;
            case R.id.action_game:
                if (enableStart) {
                    YGOStarter.startGame(this, null);
                } else {
                    VUiKit.show(this, R.string.dont_start_game);
                }
                break;
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void checkResourceDownload(ResCheckTask.ResCheckListener listener) {
        ResCheckTask task = new ResCheckTask(this, listener);
        if (Build.VERSION.SDK_INT >= 11) {
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            task.execute();
        }
    }
}
