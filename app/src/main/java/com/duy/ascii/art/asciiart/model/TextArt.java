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

package com.duy.ascii.art.asciiart.model;

import java.io.Serializable;

/**
 * Created by Duy on 9/27/2017.
 */

public class TextArt implements Serializable, Cloneable {
    public static final String KEY_ROOT = "ascii_art";
    public int category;
    public long time;
    public String content;
    public String name;
    public int star;

    public TextArt(int category, long time, String content, String name, int star) {
        this.category = category;
        this.time = time;
        this.content = content;
        this.name = name;
        this.star = star;
    }

    public TextArt(String content, String name) {
        this.category = 0;
        this.time = System.currentTimeMillis();
        this.content = content;
        this.name = name;
        this.star = 0;
    }

    public TextArt() {

    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    @Override
    public String toString() {
        return "EmojiItem{" +
                "category=" + category +
                ", time=" + time +
                ", content='" + content + '\'' +
                ", name='" + name + '\'' +
                ", star=" + star +
                '}';
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
