package com.example.moiz_ihs.bucksapp.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Environment;
import android.os.FileObserver;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Moiz-IHS on 7/28/2018.
 */

public class CommonUtils {

    public static final String SD_CARD = "sdCard";
    public static final String EXTERNAL_SD_CARD = "externalSdCard";
    private static final String ENV_SECONDARY_STORAGE = "SECONDARY_STORAGE";

    @SuppressLint("MissingPermission")
    public static String getIMEI(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }



    public static ArrayList<File> getAllFiles(File root,String[] extentions) {
        ArrayList<File> inFiles = new ArrayList<>();
        File[] files = root.listFiles();
        if(files != null) {
            for (File file : files) {
                if (file.isDirectory() && !file.isHidden()) {
                    inFiles.addAll(getAllFiles(file, extentions));
                } else {
                    for (String extension : extentions) {
                        if (file.getName().toLowerCase().endsWith(extension)) {
                            inFiles.add(file);
                        }
                    }
                }
            }
        }
        return inFiles;
    }


    public  static boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public static boolean isEmpty(EditText editText)
    {
        if(editText.getText().toString().equals(""))
            return true;
        else
            return false;
    }


    public static boolean isInternetOn(Context context) {

        // get Connectivity Manager object to check connection
        ConnectivityManager connec =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        // Check for network connections
        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {


            return true;

        } else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {


            return false;
        }
        return false;
    }




    public static void startFileListener(final Context context)
    {
        FileObserver observer = new FileObserver(android.os.Environment.getExternalStorageDirectory().toString()) { // set up a file observer to watch this directory on sd card
            @Override
            public void onEvent(int event, String file) {
                if(event == FileObserver.CREATE && !file.equals(".probe")){
                    Toast.makeText(context, "file :"+ file, Toast.LENGTH_SHORT).show();
                }
            }
        };
        observer.startWatching();
    }
}
