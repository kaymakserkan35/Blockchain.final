package com.betelgeuse.blockchain;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class H {
    // app yayınladığında kaldırılacak methodlar!!....
    public static <T extends Object> T isNull (T obj, Context context) {
        if (obj == null) {
            Toast.makeText(context, obj.getClass().getSimpleName().toString() + "is Null!!", Toast.LENGTH_SHORT).show();
            return obj;
        }
        return obj;
    }
    public static <T extends Object> T isNull (T obj,String msg) {
        if (obj == null) {
            Log.e(obj.getClass().getSimpleName(), msg +  " --> returnedNull!!" );
            return obj;
        }
        return obj;
    }
    public static <T extends Object> T isNull (String className, String methodName, T obj) {
        if (obj == null) Log.e(className, methodName + obj.getClass().getSimpleName() + "is null!");
        return obj;
    }
    public static void errorLog (String className, String methodName, String message) {
        Log.e(className, methodName + "-->" + message);
    }
    public static void  debugLog(String className, String methodName, String message) {
        Log.d(className, methodName + "-->" + message);
    }
    public static void  debugLog(String className , String message) {
        Log.d(className, message);
    }
}
