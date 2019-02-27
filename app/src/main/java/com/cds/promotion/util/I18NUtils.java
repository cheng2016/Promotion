package com.cds.promotion.util;

import android.content.Context;

import java.util.Locale;
import java.util.TimeZone;

/**
 * @Author: chengzj
 * @CreateDate: 2019/2/26 14:49
 * @Version: 3.0.0
 */
public class I18NUtils {

    /**
     * 获取当前时区
     * @return
     */
    public static String getCurrentTimeZone() {
        TimeZone tz = TimeZone.getDefault();
        String strTz = tz.getDisplayName(false, TimeZone.SHORT);
        return strTz;

    }


    /**
     * 获取当前系统语言格式
     * @param mContext
     * @return
     */
    public static String getCurrentLanguage(Context mContext){
        Locale locale =mContext.getResources().getConfiguration().locale;
        String language=locale.getLanguage();
        String country = locale.getCountry();
        String lc=language+"_"+country;
        return lc;
    }
}
