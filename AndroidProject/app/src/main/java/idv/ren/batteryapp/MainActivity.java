package idv.ren.batteryapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private Button btn_open;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 0) {
            if(Settings.canDrawOverlays(this)){
                Toast.makeText(this, "授權成功", Toast.LENGTH_SHORT).show();
                startService(new Intent(MainActivity.this, FloatService.class));
            }else{
                Toast.makeText(this, "授權失敗", Toast.LENGTH_SHORT).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void startFloatService(View view){
        btn_open = findViewById(R.id.btn_open);

        //if(FloatService.isOpen){
        //    return;
        //}

        if(Settings.canDrawOverlays(this)){
            startService(new Intent(MainActivity.this, FloatService.class));
        }else{
            Toast.makeText(this, "當前未授予權限，請先開啟相關權限。", Toast.LENGTH_SHORT).show();
            startActivityForResult(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName())), 0);
        }

    }
}