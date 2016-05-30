package com.auratech.theme.utils;

import android.content.Context;
import com.lidroid.xutils.BitmapUtils;

/**
 * Author: wyouflf
 * Date: 13-11-12
 */
public class BitmapHelp {
    private BitmapHelp() {
    }

    private static BitmapUtils bitmapUtils;

    /**
     * BitmapUtils���ǵ����� ������Ҫ���ض����ȡʵ���ķ���
     * @param appContext application context
     * @return
     */
    public static BitmapUtils getBitmapUtils(Context appContext) {
        if (bitmapUtils == null) {
            bitmapUtils = new BitmapUtils(appContext);
        }
        return bitmapUtils;
    }
}
