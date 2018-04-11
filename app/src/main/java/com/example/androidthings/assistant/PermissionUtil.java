package com.example.androidthings.assistant;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import java.util.ArrayList;
import java.util.List;

/*
*  ****************************************************************************
*  * Created by : Azizul Islam process 18-Oct-17 at 4:58 PM.
*  * Email : azizul@w3engineers.com
*  *
*  * Last edited by : Mohd. Asfaq-E-Azam Rifat on 04-Jan-18.
*  *
*  * Last Reviewed by : <Reviewer Name> process <mm/dd/yy>
*  ****************************************************************************
*/


public class PermissionUtil {

    public static final int PERMISSIONS_REQUEST = 1;
    public static final int REQUEST_CODE_CONTACTS_PERMISSION_FROM_MESSAGE_HISTORY = 2;
    public static final int CAMERA_PERMISSION = 3;
    public static final int FOOTER_GALLERY_PERMISSION = 4;

    private static PermissionUtil invokePermission;


    public static synchronized PermissionUtil on() {
        if (invokePermission == null) {
            invokePermission = new PermissionUtil();
        }
        return invokePermission;
    }

    public boolean requestPermission(Context context, String... str) {

        if(context == null) return false;

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }

        List<String> finalArgs = new ArrayList<>();
        for (int i = 0; i < str.length; i++) {
            if (context.checkSelfPermission(str[i]) != PackageManager.PERMISSION_GRANTED) {
                finalArgs.add(str[i]);
            }
        }

        if (finalArgs.isEmpty()) {
            return true;
        }

        ((Activity) context).requestPermissions(finalArgs.toArray(new String[finalArgs.size()]), PERMISSIONS_REQUEST);

        return false;
    }

    public boolean requestPermission(Context context, int requestCode, String... str) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }

        List<String> finalArgs = new ArrayList<>();
        for (int i = 0; i < str.length; i++) {
            if (context.checkSelfPermission(str[i]) != PackageManager.PERMISSION_GRANTED) {
                finalArgs.add(str[i]);
            }
        }

        if (finalArgs.isEmpty()) {
            return true;
        }

        ((Activity) context).requestPermissions(finalArgs.toArray(new String[finalArgs.size()]), requestCode);

        return false;
    }

    public boolean isAllowed(Context context, String str) {

        if (context == null) return false;

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (context.checkSelfPermission(str) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }

        return false;
    }
}