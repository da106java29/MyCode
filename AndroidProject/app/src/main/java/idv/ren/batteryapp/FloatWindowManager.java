package idv.ren.batteryapp;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.BatteryManager;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

public class FloatWindowManager {

    public static boolean isCreate = false;

    //小窗View實體
    private static FloatWindowSmallView smallWindow;

    //小窗View參數
    private static WindowManager.LayoutParams smallWindowParams;

    //用以控制 開啟或關閉懸浮窗
    private static WindowManager mWindowManager;

    //用以取得電量
    private static BatteryManager batteryManager;

    //建立小窗的方法
    public static void createSmallWindow(Context context){
        WindowManager windowManager = getWindowManaget(context);
        int screenWidth = windowManager.getDefaultDisplay().getWidth();
        int screenHeight = windowManager.getDefaultDisplay().getHeight();

        if(smallWindow == null){
            smallWindow = new FloatWindowSmallView(context);
            if(smallWindowParams == null){
                smallWindowParams = new WindowManager.LayoutParams();
                //smallWindowParams.type = WindowManager.LayoutParams.TYPE_PHONE;
                smallWindowParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY ;
                smallWindowParams.format = PixelFormat.RGBA_8888;
                smallWindowParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                        | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
                smallWindowParams.gravity = Gravity.LEFT | Gravity.TOP;
                smallWindowParams.width = FloatWindowSmallView.viewWidth;
                smallWindowParams.height = FloatWindowSmallView.viewHeight;
                smallWindowParams.x = screenWidth;
                smallWindowParams.y = screenHeight / 2;
            }

            smallWindow.setParams(smallWindowParams);
            windowManager.addView(smallWindow, smallWindowParams);
            isCreate = true;
        }
    }

    /**
     * 移除小懸浮窗
     * @param context
     * @return
     */
    public static void removeSmallWindow(Context context){
        if(smallWindow != null){
            WindowManager windowManager = getWindowManaget(context);
            windowManager.removeView(smallWindow);
            smallWindow = null;
            isCreate = false;
        }
    }

    /**
     * 更新小懸浮窗的文字
     * @param context
     * @return
     */
    public static void updateUsedPercent(Context context){
        if(smallWindow != null){
            int betteryPercent = getNowBatteryPercent(context);
            TextView tv_percent = (TextView) smallWindow.findViewById(R.id.percent);
            tv_percent.setText(String.valueOf(betteryPercent).concat("%"));

            setPercentImg(betteryPercent, tv_percent);
        }
    }

    public static void setPercentImg(int betteryPercent, TextView tv_percent){
        if(betteryPercent >= 90){
            tv_percent.setBackgroundResource(R.drawable.battery_100);
        }else if(betteryPercent < 90 && betteryPercent >= 75){
            tv_percent.setBackgroundResource(R.drawable.battery_85);
        }else if(betteryPercent < 75 && betteryPercent >= 60){
            tv_percent.setBackgroundResource(R.drawable.battery_70);
        }else if(betteryPercent < 60 && betteryPercent >= 45){
            tv_percent.setBackgroundResource(R.drawable.battery_55);
        }else if(betteryPercent < 45 && betteryPercent >= 30){
            tv_percent.setBackgroundResource(R.drawable.battery_40);
        }else if(betteryPercent < 30 && betteryPercent >= 20) {
            tv_percent.setBackgroundResource(R.drawable.battery_30);
        }else if(betteryPercent <= 20){
            tv_percent.setBackgroundResource(R.drawable.battery_20);
        }else{
            tv_percent.setBackgroundResource(R.drawable.battery_100);
        }
    }
    /*
     *  如果WindowManager尚未創建，則建立一個返回給他。否則返回當前Manager。
     */
    private static WindowManager getWindowManaget(Context context){
        if(mWindowManager == null){
            mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }
        return mWindowManager;
    }

    /**
     * 是否有懸浮窗在螢幕上
     */
    public static boolean isWindowsShowing(){
        return smallWindow != null;
    }

    /**
     * 如果WindowManager尚未創建，則建立一個新的；若有責返回當前WindowManager。
     */
    private static WindowManager getmWindowManager(Context context){
        if(mWindowManager == null){
            mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }
        return mWindowManager;
    }

    /**
     * 取得電池電量
     */
    public static int getNowBatteryPercent(Context context){
        return getbatteryManager(context).getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
    }

    /**
     * 如果BatteryManager尚未創建，則建立一個新的；若有責返回當前BatteryManager
     */
    private static BatteryManager getbatteryManager(Context context){
        if(batteryManager == null){
            batteryManager = (BatteryManager) context.getSystemService(Context.BATTERY_SERVICE);
        }
        return batteryManager;
    }

    public boolean getIsCreate(){
        return this.isCreate;
    }

    public void setIsCreate(boolean type){
        this.isCreate = type;
    }
}
