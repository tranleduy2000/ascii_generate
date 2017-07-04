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

package com.duy.acsiigenerator.image.converter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/** 
 * Represents a directory where the PNG and HTML versions of saved pictures are stored.
 */
public class ImageDirectory {
    
    String baseDirectory;
    List<File> imageFiles = new ArrayList<File>();

    public ImageDirectory(String basedir) {
        this.baseDirectory = basedir;
        updateImageList();
    }
    
    public void updateImageList() {
        imageFiles.clear();
        File[] basedirContents = (new File(baseDirectory)).listFiles();
        for(File f : basedirContents) {
            if (f.isDirectory()) {
                File imageFile = new File(f.getAbsolutePath() + File.separator + f.getName() + ".html");
                if (imageFile.isFile()) {
                    imageFiles.add(imageFile);
                }
            }
        }
    }
    
    public int getFileCount() {
        return imageFiles.size();
    }
    
    public File getFileForIndex(int index) {
        return imageFiles.get(index);
    }
    
    public String getFilePathForIndex(int index) {
        return imageFiles.get(index).getAbsolutePath();
    }
}
