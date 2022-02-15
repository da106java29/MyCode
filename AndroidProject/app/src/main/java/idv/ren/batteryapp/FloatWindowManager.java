package idv.ren.batteryapp;

import android.content.Context;
import android.os.BatteryManager;
import android.view.WindowManager;

public class FloatWindowManager {

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
}
