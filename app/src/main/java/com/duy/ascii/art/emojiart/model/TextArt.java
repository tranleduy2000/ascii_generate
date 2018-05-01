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

package com.duy.ascii.art.emojiart.model;

import java.io.Serializable;

/**
 * Created by Duy on 9/27/2017.
 */

public class TextArt implements Serializable, Cloneable {
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
