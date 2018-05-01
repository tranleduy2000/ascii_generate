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
