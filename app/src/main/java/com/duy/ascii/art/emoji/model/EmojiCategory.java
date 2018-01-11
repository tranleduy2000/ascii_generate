/*
 * Copyright (c) 2018 by Tran Le Duy
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

package com.duy.ascii.art.emoji.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Duy on 1/11/2018.
 */

public class EmojiCategory implements Serializable {
    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";
    public static final String DATA = "data";
    private String name;
    private String description;
    private ArrayList<EmojiItem> items = new ArrayList<>();

    public EmojiCategory(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<EmojiItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<EmojiItem> items) {
        this.items = items;
    }

    public void add(EmojiItem item) {
        items.add(item);

    }

    public EmojiItem get(int position) {
        return items.get(position);
    }

    public int size() {
        return items.size();
    }
}
