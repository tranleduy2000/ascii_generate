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

import junit.framework.TestCase;

import org.json.JSONException;

import java.io.IOException;

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
}
