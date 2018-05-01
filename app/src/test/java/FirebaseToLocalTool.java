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
//        final File file = new File("./app/src/main/assets/asciiart.json");
//        final FileInputStream in = new FileInputStream(new File(file.getParent(), "new_ascii_art.json"));
//        JSONObject jsonObject = new JSONObject(IOUtils.toString(in));
//        JSONArray array = jsonObject.getJSONArray(TextArt.KEY_ROOT);
//        ArrayList<String> contents = new ArrayList<>();
//        int i = 0;
//        while (i < array.length()) {
//            JSONObject item = array.getJSONObject(i);
//            if (item.getString("content").matches("[\\w]{1,20}")) {
//                array.remove(i);
//            } else if (item.getString("content").length() <= 5) {
//                array.remove(i);
//            } else if (contents.contains(item.getString("content").trim())) {
//                array.remove(i);
//            } else {
//                contents.add(item.getString("content").trim());
//            }
//            i++;
//        }
//
//        FileOutputStream fileOutputStream = new FileOutputStream(new File(file.getParent(), "new_ascii_art4.json"));
//        String content = jsonObject.toString(1);
//        fileOutputStream.write(content.getBytes());
    }
}
