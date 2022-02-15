package idv.ren.batteryapp;

import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.BatteryManager;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.view.WindowId;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class FloatService extends Service {
    private static final String TAG = "FloatService";

    private WindowManager windowManager;
    private WindowManager.LayoutParams layoutParams;

    private Button button;

    private static String betteryPercent;

    public static boolean isOpen = false;

    @Override
    public void onCreate() {
        super.onCreate();

        isOpen = true;
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        layoutParams = new WindowManager.LayoutParams();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        }else{
            layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }

        layoutParams.format = PixelFormat.RGBA_8888;
        layoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        layoutParams.width = 500;
        layoutParams.height = 500;
        layoutParams.x = 300;
        layoutParams.y = 300;

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        showFloatWindow();
        return super.onStartCommand(intent, flags, startId);
    }

    private void showFloatWindow(){
        BatteryManager bttm = (BatteryManager) getSystemService(BATTERY_SERVICE);
        betteryPercent =  String.valueOf(bttm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)).concat("%");
        Toast.makeText(this, betteryPercent, Toast.LENGTH_SHORT).show();

        if(Settings.canDrawOverlays(this)){
            button = new Button(getApplicationContext());
            button.setText("電池懸浮窗");
            button.setBackgroundColor(Color.TRANSPARENT);

            windowManager.addView(button, layoutParams);

        }
    }

    public static String getBetteryPercent(){
        return betteryPercent;
    }

    public void setBetteryPercent(String str){
        this.betteryPercent = str;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
