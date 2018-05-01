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

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;

@SuppressLint("NewApi")
public class ClipboardManagerCompatV11 implements ClipboardManagerCompat {
    private final ClipboardManager clip;

    public ClipboardManagerCompatV11(Context context) {
        clip = (ClipboardManager) context.getApplicationContext()
                .getSystemService(Context.CLIPBOARD_SERVICE);
    }

    @Override
    public CharSequence getText() {
        if (clip.getPrimaryClip().getItemCount() >= 1) {
            ClipData.Item item = clip.getPrimaryClip().getItemAt(0);
            return item.getText();
        }
        return "";
    }

    @Override
    public void setText(CharSequence text) {
        ClipData clipData = ClipData.newPlainText("simple text", text);
        clip.setPrimaryClip(clipData);
    }

    @Override
    public boolean hasText() {
        return (clip.hasPrimaryClip() && clip.getPrimaryClipDescription()
                .hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN));
    }
}
