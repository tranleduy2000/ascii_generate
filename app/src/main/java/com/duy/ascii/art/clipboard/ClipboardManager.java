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

import android.content.Context;
import android.widget.Toast;

/**
 * Created by DUy on 04-Nov-16.
 * Uses {@link ClipboardManagerCompat}
 */
@Deprecated
public class ClipboardManager {
    private Context context;

    public ClipboardManager(Context context) {
        this.context = context;
    }

    // copy text to clipboard
    public static void setClipboard(Context context, String text) {
        android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    } // copy text to clipboard

    /**
     * get text from clipboard
     *
     * @param context
     * @return
     */
    public static String getClipboard(Context context) {
        String res = "";
        android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboard.getText() != null) {
            res = clipboard.getText().toString();
        } else res = "";
        Toast.makeText(context, res, Toast.LENGTH_SHORT).show();
        return res;
    }


    public String getClipboard() {
        String res = "";
        android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboard.getPrimaryClip().getItemAt(0).getText() != null) {
            res = clipboard.getPrimaryClip().getItemAt(0).getText().toString();
        } else
            res = "";
        Toast.makeText(context, res, Toast.LENGTH_SHORT).show();
        return res;
    }

    public void setClipboard(CharSequence text) {
        android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
}
