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

package com.duy.common.ads.apps;

import java.io.Serializable;

class ApplicationItem implements Serializable, Cloneable {
    private String name;
    private String applicationId;
    private String iconUrl;
    private String wallpaperUrl;

    ApplicationItem(String name, String applicationId, String iconUrl, String wallpaperUrl) {
        this.name = name;
        this.applicationId = applicationId;
        this.iconUrl = iconUrl;
        this.wallpaperUrl = wallpaperUrl;
    }

    @Override
    public String toString() {
        return "ApplicationItem{" +
                "name='" + name + '\'' +
                ", applicationId='" + applicationId + '\'' +
                ", iconUrl='" + iconUrl + '\'' +
                ", wallpaperUrl='" + wallpaperUrl + '\'' +
                '}';
    }

    public String getWallpaperUrl() {
        return wallpaperUrl;
    }

    public void setWallpaperUrl(String wallpaperUrl) {
        this.wallpaperUrl = wallpaperUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }


}