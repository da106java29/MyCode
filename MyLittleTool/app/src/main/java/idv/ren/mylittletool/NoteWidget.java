package idv.ren.mylittletool;

import android.app.ActivityManager;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.sql.Time;
import java.util.List;

/**
 * Implementation of App Widget functionality.
 */
public class NoteWidget extends AppWidgetProvider {
    public static final String TAG = "NoteWidget";
    public ImagesUtils imgUtil = new ImagesUtils();

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.d(TAG, "onReceive : " + intent.getAction());

        switch (intent.getAction()){
            case "android.appwidget.action.APPWIDGET_UPDATE":
            Boolean isRun = isServiceRun(context);

            Log.d(TAG, "onReceive has Service Run ? => " + isRun);

        }
    }

    private Boolean isServiceRun (Context context){
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        List<ActivityManager.RunningServiceInfo> list = activityManager.getRunningServices(Integer.MAX_VALUE);

        for(ActivityManager.RunningServiceInfo info : list){
            if(TimeService.class.getName().equals(info.service.getClassName())){
                return true;
            }
        }

        return false;
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.note_widget);
        //views.setTextViewText(R.id.appwidget_text, widgetText);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.note_widget);
        Bitmap bim = imgUtil.makeRoundCorner(BitmapFactory.decodeResource(context.getResources(), R.drawable.start_sky), 20);

        Toast.makeText(context, context.getPackageName(), Toast.LENGTH_SHORT).show();
        //remoteViews.setImageViewResource(R.id.imgv_BackStarTree, R.color.white);
        //remoteViews.setInt(R.id.imgv_BackStarTree, "setBackgroundColor", R.color.white);

        remoteViews.setImageViewBitmap(R.id.imgv_starSky, bim);

    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }



}