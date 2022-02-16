package idv.ren.batteryapp;

import android.content.Context;
import android.graphics.Typeface;
import android.os.BatteryManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.reflect.Field;

public class FloatWindowSmallView extends LinearLayout {
    private static final String TAG = "FloatWindowSmallView";

    //小懸浮窗的寬高
    public static int viewWidth;
    public static int viewHeight;

    //系統狀態欄高度
    private static int statusBarHeight;

    //使用於更新小懸浮窗的位置
    private WindowManager windowManager;

    //小懸浮窗參數
    private WindowManager.LayoutParams layoutParams;

    //紀錄當前手指座標
    private float xInScreen;
    private float yInScreen;

    //紀錄當前手指按下的座標
    private float xDownInScreen;
    private float yDownInScreen;

    //紀錄手指按在小懸浮窗時的座標
    private float xInView;
    private float yInView;

    public FloatWindowSmallView(Context context){
        super(context);
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        //如不使用layout.param去建立一個樣式，可以自訂一個layout載入，就需要使用到LayoutInflater。
        LayoutInflater.from(context).inflate(R.layout.small_float_view, this);

        View view = findViewById(R.id.small_window_layout);
        viewHeight = view.getLayoutParams().height;
        viewWidth = view.getLayoutParams().width;

        TextView percentView = findViewById(R.id.percent);
        percentView.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Monaco.ttf"));
        percentView.setText(FloatWindowManager.getNowBatteryPercent(context));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                //手指按下時，皆須紀錄必要數據，須記得扣除狀態欄高度等。
                xInView = event.getX();
                yInView = event.getY();

                xDownInScreen = event.getRawX();
                yDownInScreen = event.getRawY() - getStatusBarHeight();

                xInScreen = event.getRawX();
                yInScreen = event.getRawY() - getStatusBarHeight();
                break;
            case MotionEvent.ACTION_MOVE:
                xInScreen = event.getRawX();
                yInScreen = event.getRawY() - getStatusBarHeight();

                //手指移動時更新小窗的位置
                updateViewposition();
                break;
            case MotionEvent.ACTION_UP:
                //如果手指離開螢幕時，xDownInScreen 等於 xInScreen，且 yDownInScreen 等於 yInScreen，
                //則視為單擊事件
                if(xDownInScreen == xInScreen && yDownInScreen == yInScreen){
                    //我沒有要開大窗，先擱置。
                }
            default:break;
        }

        return true;
    }

    //更新小懸浮窗的參數
    public void setParams(WindowManager.LayoutParams param){
        this.layoutParams = param;
    }

    //更新小懸浮窗在螢幕中的位置
    public void updateViewposition(){
        layoutParams.x = (int) (xInScreen - xInView);
        layoutParams.y = (int) (yInScreen - yInView);

        windowManager.updateViewLayout(this, layoutParams);
    }

    //取得狀態欄高度
    private int getStatusBarHeight(){
        if(statusBarHeight == 0){
            try {


                int statusBarHeight1 = -1;
                //獲取status_bar_height資源的ID
                int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
                if (resourceId > 0) {
                    //根據資源ID獲取響應的尺寸值
                    statusBarHeight1 = getResources().getDimensionPixelSize(resourceId);
                }
                Log.e("statusBar_Height", "高度是 : " + statusBarHeight1);
                statusBarHeight = statusBarHeight1;
                /*
                Class<?> c = Class.forName("com.android.internal.R$dimen");
                Object o = c.newInstance();
                Field field = c.getField("status_bar_height");
                int x = (Integer) field.get(o);
                statusBarHeight = getResources().getDimensionPixelSize(x);
                */


            }catch (Exception e){
                e.printStackTrace();
                Log.e(TAG, "取得狀態欄高度時出現錯誤!!");
            }
        }
        return statusBarHeight;
    }

}
