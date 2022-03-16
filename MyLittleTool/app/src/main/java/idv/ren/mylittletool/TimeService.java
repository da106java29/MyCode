package idv.ren.mylittletool;

import android.annotation.SuppressLint;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeService extends Service implements Runnable{
    public static final String TAG = "TimeService";
    public static final String CLICK_EVENT = "android.appwidget.action.Click";

    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd, HH:mm:ss");

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1){
                update();
            }
        }
    };

    @Override
    public void run() {
        handler.sendEmptyMessage(1);
        handler.postDelayed(this, 1000);
    }

    private void update(){
        String time = sdf.format(new Date());

        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.note_widget);
        remoteViews.setTextViewText(R.id.tv_dateTime, time);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
        ComponentName componentName = new ComponentName(getApplicationContext(), NoteWidget.class);
        appWidgetManager.updateAppWidget(componentName, remoteViews);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate(Service) : " + TAG);
        handler.sendEmptyMessage(1);
        handler.post(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
