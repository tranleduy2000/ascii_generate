/*
 *     Copyright (C) 2018 Tran Le Duy
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
