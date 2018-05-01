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
