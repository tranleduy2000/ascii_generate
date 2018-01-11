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
                Matcher matcher = FileUtil.PATTERN_SLIP.matcher(FileUtil.streamToString(stream));
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
