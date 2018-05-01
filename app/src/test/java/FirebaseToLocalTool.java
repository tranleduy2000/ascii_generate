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

import com.duy.ascii.art.asciiart.model.TextArt;
import com.duy.ascii.art.utils.FileUtil;

import junit.framework.TestCase;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;

/**
 * Created by Duy on 01-May-18.
 */

public class FirebaseToLocalTool extends TestCase {

    public void test() throws IOException, JSONException {
//        File file = new File("./app/src/main/assets/asciiart.json");
//        String content = IOUtils.toString(new FileInputStream(file));
//        JSONObject oldData = new JSONObject(content).getJSONObject("ascii_art");
//        Iterator<String> keys = oldData.keys();
//
//
//        JSONObject newData = new JSONObject();
//        JSONArray array = new JSONArray();
//        newData.put("ascii_art", array);
//        while (keys.hasNext()) {
//            String next = keys.next();
//            JSONObject jsonObject = oldData.getJSONObject(next);
//            array.put(jsonObject);
//        }
//        content = newData.toString(2);
//        FileOutputStream fileOutputStream = new FileOutputStream(new File(file.getParent(), "new_ascii_art.json"));
//        fileOutputStream.write(content.getBytes());
//        System.out.println();
    }

    public void testMerge() throws IOException, JSONException {
        final File file = new File("./app/src/main/assets/asciiart.json");
        final FileInputStream in = new FileInputStream(new File(file.getParent(), "new_ascii_art.json"));
        JSONObject jsonObject = new JSONObject(IOUtils.toString(in));
        JSONArray array = jsonObject.getJSONArray(TextArt.KEY_ROOT);

        Matcher matcher = FileUtil.PATTERN_SPLIT.matcher(IOUtils.toString(new FileInputStream("./app/src/main/assets/image.txt")));
        ArrayList<TextArt> result = new ArrayList<>();
        while (matcher.find()) {
            String value = matcher.group(2);
            JSONObject item = new JSONObject();
            item.put("category", 0);
            item.put("time", 0L);
            item.put("content", value);
            item.put("name", "");
            item.put("star", 0);
            array.put(item);
        }

        FileOutputStream fileOutputStream = new FileOutputStream(new File(file.getParent(), "new_ascii_art2.json"));
        String content = jsonObject.toString(1);
        fileOutputStream.write(content.getBytes());
    }

    public void testClean() throws IOException, JSONException {
        final File file = new File("./app/src/main/assets/asciiart.json");
        final FileInputStream in = new FileInputStream(new File(file.getParent(), "new_ascii_art.json"));
        JSONObject jsonObject = new JSONObject(IOUtils.toString(in));
        JSONArray array = jsonObject.getJSONArray(TextArt.KEY_ROOT);
        ArrayList<String> contents = new ArrayList<>();
        int i = 0;
        while (i < array.length()) {
            JSONObject item = array.getJSONObject(i);
            if (item.getString("content").matches("[\\w]{1,20}")) {
                array.remove(i);
            } else if (item.getString("content").length() <= 5) {
                array.remove(i);
            } else if (contents.contains(item.getString("content").trim())) {
                array.remove(i);
            } else {
                contents.add(item.getString("content").trim());
            }
            i++;
        }

        FileOutputStream fileOutputStream = new FileOutputStream(new File(file.getParent(), "new_ascii_art4.json"));
        String content = jsonObject.toString(1);
        fileOutputStream.write(content.getBytes());
    }
}
