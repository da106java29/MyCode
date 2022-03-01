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
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowId;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
            /**
             * scheduleAtFixedRate:
             * 該method，在一個任務因某些事件造成Delay時，若先前有未完成任務，他會儘快將其完成。
             * schedule:
             * 反之，該method，在一個任務因某些事件造成Delay時，先前未完成的任務就會讓他直接lose掉。
             */
            //timer.scheduleAtFixedRate(new RefreshTask(), 0, 500);
            timer.schedule(new RefreshTask(), 0, 10000);
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

        String packageName = getTopApp(this);

        return getHome().contains(packageName);

        /*
        long ts = System.currentTimeMillis();
        UsageStatsManager usageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
        List<UsageStats> usList = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_BEST, ts-1000, ts);

        if(usList == null || usList.size() == 0){
            return true;
        }

        //ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        //List<ActivityManager.RunningTaskInfo> rti = activityManager.getRunningTasks(1);
        return getHome().contains(usList.get(0).getPackageName());
        */
    }

    private String getTopApp(Context context){
        //Android 5.0 只能使用該方法
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            UsageStatsManager m = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);

            if(m != null){
                long now = System.currentTimeMillis();

                //獲取一小時內的應用數據
                List<UsageStats> stats = m.queryUsageStats(UsageStatsManager.INTERVAL_BEST, now - 1000 * 10, now);
                String topActivity = "";

                //取得最近運行的APP，即當前APP
                if(stats != null && !stats.isEmpty()){
                    int j = 0;
                    for(int i = 0; i < stats.size(); i++){
                        if(stats.get(i).getLastTimeUsed() > stats.get(j).getLastTimeUsed()){
                            j = i;
                        }
                    }
                    topActivity = stats.get(j).getPackageName();
                }
                return topActivity;
            }
        //Android 5.0以下可以使用該方法
        }else{
            ActivityManager mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> rti = mActivityManager.getRunningTasks(1);
            return rti.get(0).topActivity.getPackageName();
        }
        return null;
    }


    private List<String> getHome(){

        List<String> names = new ArrayList<String>();
        PackageManager packageManager = this.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for(ResolveInfo resolveInfo : list){
            names.add(resolveInfo.activityInfo.packageName);
        }

        return names;
    }
}
