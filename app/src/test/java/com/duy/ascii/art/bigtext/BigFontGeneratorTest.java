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

package com.duy.ascii.art.bigtext;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

/**
 * Created by Duy on 1/11/2018.
 */
public class BigFontGeneratorTest {
    private BigFontGenerator mBigFontGenerator = new BigFontGenerator();

    @Before
    public void loadData() throws IOException {
        System.out.println("BigFontGeneratorTest.loadData");
        File file = new File("C:\\github\\ascii_generate\\app\\src\\main\\assets\\bigtext_json");
        File[] files = file.listFiles();
        InputStream[] streams = new InputStream[files.length];
        for (int i = 0; i < files.length; i++) {
            streams[i] = new FileInputStream(files[i]);
        }
        mBigFontGenerator.load(streams);
        for (InputStream stream : streams) {
            stream.close();
        }
    }

    @Test
    public void convert() throws Exception {
        String path = "C:\\github\\ascii_generate\\app\\src\\test\\java\\com\\duy\\ascii\\art\\bigtext\\out.txt";
        PrintStream stream = new PrintStream(new FileOutputStream(path));
        for (int i = 0; i < mBigFontGenerator.getSize(); i++) {
            String convert = mBigFontGenerator.convert("hello", i);
            stream.println(convert);
            System.out.println(convert);
        }
        stream.flush();
        stream.close();
    }

}
