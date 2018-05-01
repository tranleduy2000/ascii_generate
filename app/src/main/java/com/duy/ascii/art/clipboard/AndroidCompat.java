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

package com.duy.ascii.art.clipboard;

/**
 * The classes in this package take advantage of the fact that the VM does
 * not attempt to loadAndClose a class until it's accessed, and the verifier
 * does not run until a class is loaded.  By keeping the methods which
 * are unavailable on older platforms in subclasses which are only ever
 * accessed on platforms where they are available, we can preserve
 * compatibility with older platforms without resorting to reflection.
 * <p>
 * See http://developer.android.com/resources/articles/backward-compatibility.html
 * and http://android-developers.blogspot.com/2010/07/how-to-have-your-cupcake-and-eat-it-too.html
 * for further discussion of this technique.
 */

public class AndroidCompat {
    public final static int SDK = getSDK();

    private final static int getSDK() {
        int result;
        try {
            result = AndroidLevel4PlusCompat.getSDKInt();
        } catch (VerifyError e) {
            // We must be at an SDK level less than 4.
            try {
                result = Integer.valueOf(android.os.Build.VERSION.SDK);
            } catch (NumberFormatException e2) {
                // Couldn't parse string, assume the worst.
                result = 1;
            }
        }
        return result;
    }
}

