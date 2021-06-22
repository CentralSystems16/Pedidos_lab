package com.laboratorio.pedidos_lab.conections;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class preferenceUtils {

        // this is for version code
        private  final String APP_VERSION_CODE = "APP_VERSION_CODE";
        private SharedPreferences sharedPreferencesAppVersionCode;
        private SharedPreferences.Editor editorAppVersionCode;
        private static Context mContext;

        public preferenceUtils(Context context)
        {
            this.mContext=context;
            // this is for app versioncode
            sharedPreferencesAppVersionCode=mContext.getSharedPreferences(APP_VERSION_CODE,MODE_PRIVATE);
            editorAppVersionCode=sharedPreferencesAppVersionCode.edit();
        }

        public void createAppVersionCode(int versionCode) {

            editorAppVersionCode.putInt(APP_VERSION_CODE, versionCode);
            editorAppVersionCode.apply();
        }

        public int getAppVersionCode()
        {
            return sharedPreferencesAppVersionCode.getInt(APP_VERSION_CODE,0); // as default version code is 0
        }

}
