package com.johnhellbom.dysseappen;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Button;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by John Hellbom on 2016-02-28.
 */
public final class DroidUtils {

    public static void scaleButtonDrawables(Button btn, double fitFactor) {
        Drawable[] drawables = btn.getCompoundDrawables();

        for (int i = 0; i < drawables.length; i++) {
            if (drawables[i] != null) {
                int imgWidth = drawables[i].getIntrinsicWidth();
                int imgHeight = drawables[i].getIntrinsicHeight();
                if ((imgHeight > 0) && (imgWidth > 0)) {
                    float scale;
                    if ((i == 0) || (i == 2)) {
                        scale = (float) (btn.getHeight() * fitFactor) / imgHeight;
                    } else {
                        scale = (float) (btn.getWidth() * fitFactor) / imgWidth;
                    }
                    if (scale < 1.0) {
                        Rect rect = drawables[i].getBounds();
                        int newWidth = (int)(imgWidth * scale);
                        int newHeight = (int)(imgHeight * scale);
                        rect.left = rect.left + (int)(0.5 * (imgWidth - newWidth));
                        rect.top = rect.top + (int)(0.5 * (imgHeight - newHeight));
                        rect.right = rect.left + newWidth;
                        rect.bottom = rect.top + newHeight;
                        drawables[i].setBounds(rect);
                    }
                }
            }
        }
    }

    public static int pxToDp(Context context, int px) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static String upperCaseFirst(String value) {
        char[] array = value.toCharArray();
        array[0] = Character.toUpperCase(array[0]);
        return new String(array);
    }

    public static void SaveString(Activity activity, String key, String value)
    {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String ReadString(String key)
    {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(DysseAppenApplication.getAppContext());
        return sharedPref.getString(key, null);
    }

    public static void SaveBool(Activity activity, String key, Boolean value)
    {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static void SaveStrings(String key, HashSet<String> values)
    {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(DysseAppenApplication.getAppContext());
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putStringSet(key, values);
        editor.commit();
    }

    public static Set<String> ReadStrings(String key)
    {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(DysseAppenApplication.getAppContext());
        return sharedPref.getStringSet(key, new HashSet<String>());
    }

    public static boolean ContainsString(String key, String val)
    {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(DysseAppenApplication.getAppContext());
        Set<String> vals = sharedPref.getStringSet(key, new HashSet<String>());
        return vals.contains(val);
    }

    public static Boolean ReadBool(Activity activity, String key)
    {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());
        return sharedPref.getBoolean(key, false);
    }

    public static void SaveInteger(String key, int value) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(DysseAppenApplication.getAppContext());
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static Integer ReadInteger(String key)
    {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(DysseAppenApplication.getAppContext());
        return sharedPref.getInt(key, -1);
    }
}
