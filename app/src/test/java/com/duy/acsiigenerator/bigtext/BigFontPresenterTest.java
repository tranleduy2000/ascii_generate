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

package com.duy.acsiigenerator.bigtext;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;

/**
 * Created by Duy on 07-Jul-17.
 */
public class BigFontPresenterTest {
    @Test
    public void convert() throws Exception {
        File file = new File("C:\\github\\AsciiGenerator\\app\\src\\main\\assets\\bigtext");
        File[] files = file.listFiles();
        System.out.println(Arrays.toString(files));
        FileInputStream[] inputStreams = new FileInputStream[files.length];
        for (int i = 0; i < files.length; i++) {
            inputStreams[i] = new FileInputStream(files[i]);
        }
        Reader cache = new Reader();
        cache.loadAndClose(inputStreams);
        int size = cache.getSize();
        for (int i = 0; i < size; i++) {
            String convert = cache.convert("ASCII ART", i);
            System.out.println(convert);
        }
        for (int i = 0; i < size; i++) {
            for (char j = 'a'; j <= 'z'; j++) {
                String convert = cache.convert(String.valueOf(j), i);
                System.out.println(convert);
            }
        }
    }

    @Test
    public void convert2() throws Exception {
        for (char c = 'a'; c <= 'z'; c++) {
            System.out.print(c);
        }
        for (char c = 'A'; c <= 'Z'; c++) {
            System.out.print(c);
        }
    }

}