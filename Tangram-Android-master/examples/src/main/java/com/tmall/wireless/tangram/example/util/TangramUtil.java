package com.tmall.wireless.tangram.example.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.util.DisplayMetrics;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public class TangramUtil{
    public static byte[] getAssertsFile(Context context, String fileName) {
        InputStream inputStream = null;
        AssetManager assetManager = context.getAssets();
        try {
            inputStream = assetManager.open(fileName);
            if (inputStream == null) {
                return null;
            }

            BufferedInputStream bis = null;
            int length;
            try {
                bis = new BufferedInputStream(inputStream);
                length = bis.available();
                byte[] data = new byte[length];
                bis.read(data);

                return data;
            } catch (IOException e) {

            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (Exception e) {

                    }
                }
            }

            return null;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
    //获取屏幕宽度
    public static int getDisplayWidth(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }
}
