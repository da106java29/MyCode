package idv.ren.batteryapp;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.view.WindowId;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class FloatService extends Service {
    private static final String TAG = "FloatService";

    //用於在執行緒中建立或移除懸浮窗
    private Handler handler = new Handler();

    //定時器，定時檢測當前應該創建或是移除懸浮窗
    private Timer timer;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(timer == null){
            timer = new Timer();
            timer.scheduleAtFixedRate(new RefreshTask(), 0, 500);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
        timer = null;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    class RefreshTask extends TimerTask{
        @Override
        public void run() {
            //當前是桌面，且沒有懸浮窗顯示，則創建。
            if(isHome() && !FloatWindowManager.isWindowsShowing()){
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        FloatWindowManager.createSmallWindow(getApplicationContext());
                    }
                });
            }
            //當前不是桌面但有懸浮窗，則移除
            else if(!isHome() && FloatWindowManager.isWindowsShowing()){
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        FloatWindowManager.removeSmallWindow(getApplicationContext());
                    }
                });
            }
            //當前是桌面，且有懸浮窗，則更新數據
            else if(isHome() && FloatWindowManager.isWindowsShowing()){
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        FloatWindowManager.updateUsedPercent(getApplicationContext());
                    }
                });
            }
        }
    }

    /**
     * 判斷當前是否是桌面
     * @return
     */
    private boolean isHome(){
        long ts = System.currentTimeMillis();
        UsageStatsManager usageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
        List<UsageStats> usList = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_BEST, ts-1000, ts);

        if(usList.isEmpty()){
            return true;
        }
        //ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        //List<ActivityManager.RunningTaskInfo> rti = activityManager.getRunningTasks(1);
        return getHome().contains(usList.get(0).getPackageName());
    }

    private List<String> getHome(){
        List<String> names = new ArrayList<String>();
        PackageManager packageManager = this.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        List<ResolveInfo> resolveInfos = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for(ResolveInfo ri : resolveInfos){
            names.add(ri.activityInfo.packageName);
        }
        return names;
    }


}
