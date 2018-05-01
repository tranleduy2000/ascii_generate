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
import android.text.ClipboardManager;

@SuppressWarnings("deprecation")
public class ClipboardManagerCompatV1 implements ClipboardManagerCompat {
    private final ClipboardManager clip;

    public ClipboardManagerCompatV1(Context context) {
        clip = (ClipboardManager) context.getApplicationContext()
                .getSystemService(Context.CLIPBOARD_SERVICE);
    }

    @Override
    public CharSequence getText() {
        return clip.getText();
    }

    @Override
    public void setText(CharSequence text) {
        clip.setText(text);
    }

    @Override
    public boolean hasText() {
        return clip.hasText();
    }
}
