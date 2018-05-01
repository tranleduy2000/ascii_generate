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

import com.duy.ascii.art.utils.FileUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.regex.Matcher;

/**
 * Created by Duy on 1/11/2018.
 */

public class JsonEncoder {

    @Test
    public void generateAndEncodeDataBigFont() throws FileNotFoundException {
        System.out.println("BigFontGeneratorTest.loadData");
        File file = new File("C:\\github\\ascii_generate\\data\\bigtext");
        File[] files = file.listFiles();
        InputStream[] inputStream = new InputStream[files.length];
        for (int i = 0; i < files.length; i++) {
            inputStream[i] = new FileInputStream(files[i]);
        }
        for (int i1 = 0; i1 < inputStream.length; i1++) {
            InputStream stream = inputStream[i1];
            try {
                Matcher matcher = FileUtil.PATTERN_SPLIT.matcher(FileUtil.streamToString(stream));
                HashMap<Character, String> font = new HashMap<>();
                for (int i = 'A'; i <= 'Z' && matcher.find(); i++) {
                    font.put((char) i, matcher.group(2));
                }
                if (matcher.find()) {
                    font.put(' ', matcher.group(2));
                }
                file = new File("C:\\github\\ascii_generate\\app\\src\\main\\assets\\bigtext_json\\" + "font" + i1 + ".json");
                file.createNewFile();
                JSONObject jsonObject = new JSONObject(font);
                FileOutputStream fos = new FileOutputStream(file);
                try {
                    fos.write(jsonObject.toString(2).getBytes());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
