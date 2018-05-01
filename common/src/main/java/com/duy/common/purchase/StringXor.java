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

package com.duy.common.purchase;

import android.support.annotation.NonNull;

import org.apache.commons.codec.binary.Base64;

/**
 * Created by Duy on 23-Jul-17.
 */

public class StringXor {
    @NonNull
    public static String encode(String s, String key) {
        return new String(Base64.encodeBase64(xor(s.getBytes(), key.getBytes())));
    }

    @NonNull
    public static String encode(String s) {
        return new String(Base64.encodeBase64((s.getBytes())));
    }

    @NonNull
    public static String decode(String s, String key) {
        return new String(xor(Base64.decodeBase64(s.getBytes()), key.getBytes()));
    }

    @NonNull
    public static String decode(String s) {
        return new String((Base64.decodeBase64(s.getBytes())));
    }

    private static byte[] xor(byte[] src, byte[] key) {
        byte[] out = new byte[src.length];
        for (int i = 0; i < src.length; i++) {
            out[i] = (byte) (src[i] ^ key[i % key.length]);
        }
        return out;
    }

}
