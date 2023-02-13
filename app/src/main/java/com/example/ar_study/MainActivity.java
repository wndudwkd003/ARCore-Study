package com.example.ar_study;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import com.google.ar.core.ArCoreApk;
import com.google.ar.core.Session;
import com.google.ar.core.exceptions.UnavailableDeviceNotCompatibleException;
import com.google.ar.core.exceptions.UnavailableUserDeclinedInstallationException;

public class MainActivity extends AppCompatActivity {

    private Session session = null;
    private boolean installRequested;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        String openGLVersion = activityManager.getDeviceConfigurationInfo().getGlEsVersion();
        // 3.0 = MIN OpenGL Version
        if(Double.parseDouble(openGLVersion) < 3.0) {
            Log.d("__ar__", "OpenGL 버전 미충족");
            finish();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        if(session == null) {
            // ARCore 설치되어있는지 확인
            try {
                switch (ArCoreApk.getInstance().requestInstall(this, !installRequested)) {
                    case INSTALL_REQUESTED:
                        installRequested = true;
                        return;
                    case INSTALLED:
                        break;
                }
            } catch (UnavailableDeviceNotCompatibleException | UnavailableUserDeclinedInstallationException e) {
                e.printStackTrace();
            }
        }

        // 카메라 권한 체크
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, 0);
        }


    }
}