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

package com.duy.ascii.art.purchase;

import android.content.Context;

import com.duy.common.purchase.Premium;

/**
 * Created by Duy on 28-Dec-17.
 */

public class AsciiPremium {
    public static boolean isPremiumUser(Context context) {
        return isProPackage(context) || isPurchase(context);
    }

    private static boolean isProPackage(Context context) {
        return context.getPackageName().equals("com.duy.asciigenerator.pro");
    }

    private static boolean isPurchase(Context context) {
        return Premium.isPremiumUser(context);
    }

    public static boolean isFreeUser(Context context) {
        return !isPremiumUser(context);
    }
}
