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

package com.duy.ascii.art.emoji.model;

import java.io.Serializable;

/**
 * Created by Duy on 1/11/2018.
 */

public class EmojiItem implements Serializable {
    public static final String CHARACTER = "emoji";
    public static final String DESCRIPTION = "desc";
    private String emojiChar;
    private String desc;

    public EmojiItem(String emojiChar, String desc) {

        this.emojiChar = emojiChar;
        this.desc = desc;
    }

    public String getEmojiChar() {
        return emojiChar;
    }

    public void setEmojiChar(String emojiChar) {
        this.emojiChar = emojiChar;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
