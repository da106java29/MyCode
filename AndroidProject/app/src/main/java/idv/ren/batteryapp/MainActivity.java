package idv.ren.batteryapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;

import android.app.Activity;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private static final int MY_PERMISSIONS_REQUEST_PACKAGE_USAGE_STATS = 1101;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        doBtnListener();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 0) {
            if(Settings.canDrawOverlays(this)){
                Toast.makeText(this, "授權成功", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "授權失敗", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == MY_PERMISSIONS_REQUEST_PACKAGE_USAGE_STATS) {
            if (!hasPermission()) {
                //若用户未开启权限，则引导用户开启“Apps with usage access”权限
                startActivityForResult(
                        new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS),
                        MY_PERMISSIONS_REQUEST_PACKAGE_USAGE_STATS);
            }
        }

        startService(new Intent(MainActivity.this, FloatService.class));

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void doBtnListener(){
        Button btn_open = (Button) findViewById(R.id.btn_open);
        btn_open.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startFloatService();
                finish();
            }
        });

        Button btn_close = (Button) findViewById(R.id.btn_close);
        btn_close.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                closeFloatService();
            }
        });
    }

    public void startFloatService() {
        /*
        if (Settings.canDrawOverlays(this)) {
            startService(new Intent(MainActivity.this, FloatService.class));
            //finish();
        }else if(!hasPermission()){
            Toast.makeText(this, "當前未授予權限，請先開啟相關權限。",Toast.LENGTH_SHORT).show();
            startActivityForResult(
                    new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS),
                    MY_PERMISSIONS_REQUEST_PACKAGE_USAGE_STATS);
        }else{
            Toast.makeText(this, "當前未授予權限，請先開啟相關權限。", Toast.LENGTH_SHORT).show();
            startActivityForResult(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName())), 0);
        }
        */

        if(!Settings.canDrawOverlays(this)){
            Toast.makeText(this, "當前未授予權限，請先開啟相關權限。", Toast.LENGTH_SHORT).show();
            startActivityForResult(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName())), 0);
        }else if(!hasPermission()){
            Toast.makeText(this, "當前未授予權限，請先開啟相關權限。",Toast.LENGTH_SHORT).show();
            startActivityForResult(
                    new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS),
                    MY_PERMISSIONS_REQUEST_PACKAGE_USAGE_STATS);
        }else{
            startService(new Intent(MainActivity.this, FloatService.class));
            //finish();
        }
    }

    //判斷用戶是否開啟 Usage access權限
    private boolean hasPermission() {
        AppOpsManager appOps = (AppOpsManager)
                getSystemService(Context.APP_OPS_SERVICE);
        int mode = 0;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                    android.os.Process.myUid(), getPackageName());
        }
        return mode == AppOpsManager.MODE_ALLOWED;
    }

    public void closeFloatService(){
        if(FloatWindowManager.isCreate) {
            FloatWindowManager.removeSmallWindow(getApplicationContext());
            Intent intent = new Intent(getApplicationContext(), FloatService.class);
            getApplicationContext().stopService(intent);
        }else{
            Toast.makeText(this, R.string.closeError, Toast.LENGTH_SHORT).show();
        }
    }
}