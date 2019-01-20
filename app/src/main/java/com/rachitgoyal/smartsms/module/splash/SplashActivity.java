package com.rachitgoyal.smartsms.module.splash;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.rachitgoyal.smartsms.R;
import com.rachitgoyal.smartsms.module.main.MainActivity;

public class SplashActivity extends AppCompatActivity {

    private static final int SMS_PERMISSION_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (!arePermissionsGranted()) {
            getPermissions();
        } else {
            launchMainActivity();
        }
    }

    private void getPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.READ_SMS,
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.RECEIVE_SMS},
                SMS_PERMISSION_CODE);
    }

    public boolean arePermissionsGranted() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == SMS_PERMISSION_CODE) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                launchMainActivity();
            } else {
                showDialogForPermission();
            }
        }
    }

    private void showDialogForPermission() {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }

        builder.setTitle(R.string.sms_permission_required);
        builder.setMessage(R.string.app_cannot_function_without_sms_permission);
        builder.setPositiveButton(R.string.give_access, (dialog, which) -> getPermissions());
        builder.setNegativeButton(R.string.quit_app, (dialog, which) -> finish());
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.show();
    }

    private void launchMainActivity() {
        startActivity(MainActivity.getActivityIntent(this));
        finish();
    }
}
