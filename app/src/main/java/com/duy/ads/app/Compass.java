/*
 * Copyright (c) 2017 by Tran Le Duy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.duy.ads.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.duy.ads.StoreUtil;
import com.duy.ascii.sharedcode.R;

import java.util.List;

/**
 * Created by Duy on 10/22/2017.
 */

public class Compass {
    private static final String KEY_HAS_SAW = "key_saw";

    private static boolean hasAccelerometer(Context context) {
        SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);

        List<Sensor> listSensors = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
        return !listSensors.isEmpty();
    }

    private static boolean hasRotationVector(Context context) {
        SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);

        List<Sensor> listSensors = sensorManager.getSensorList(Sensor.TYPE_ROTATION_VECTOR);
        return !listSensors.isEmpty();
    }

    public static boolean hasSensor(Context context) {
        return hasAccelerometer(context) && hasRotationVector(context);
    }

    @SuppressWarnings("ConstantConditions")
    public static boolean canShowDialog(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        boolean saw = pref.getBoolean(KEY_HAS_SAW, false);
        return hasSensor(context) && !saw && !isPremiumUser(context);
    }

    public static boolean isPremiumUser(Context context) {
        return context.getPackageName().equals("com.duy.asciigenerate.pro");
    }

    public static AlertDialog showGetAppDialog(final Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        pref.edit().putBoolean(KEY_HAS_SAW, true).apply();

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(R.layout.dialog_compass);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        ImageView imgHeader = alertDialog.findViewById(R.id.img_header);
        Glide.with(context).load(Uri.parse("file:///android_asset/images/compass_wall.png")).into(imgHeader);
        alertDialog.findViewById(R.id.btn_download).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StoreUtil.gotoPlayStore(context, "com.duy.compass");
                alertDialog.dismiss();
            }
        });
        alertDialog.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        return alertDialog;
    }
}
