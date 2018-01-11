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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Duy on 1/11/2018.
 */

public class GetEmoji extends TestCase {
    public void testGetSmileysAndPeople() throws IOException, JSONException {
        extractEmojiFromUrl("https://emojipedia.org/people/");
    }

    public void testGetAnimalAndNature() throws IOException, JSONException {
        extractEmojiFromUrl("https://emojipedia.org/nature/");
    }

    public void testGetFoodAndDrink() throws IOException, JSONException {
        extractEmojiFromUrl("https://emojipedia.org/food-drink/");
    }

    public void testGetActivity() throws IOException, JSONException {
        extractEmojiFromUrl("https://emojipedia.org/activity/");
    }

    public void testGetTravelAndPlaces() throws IOException, JSONException {
        extractEmojiFromUrl("https://emojipedia.org/travel-places/");
    }

    public void testGetObjects() throws IOException, JSONException {
        extractEmojiFromUrl("https://emojipedia.org/objects/");
    }

    public void testGetSymbols() throws IOException, JSONException {
        extractEmojiFromUrl("https://emojipedia.org/symbols/");
    }

    public void testGetFlags() throws IOException, JSONException {
        extractEmojiFromUrl("https://emojipedia.org/flags/");
    }

    private File getDataDir() {
        String path = System.getProperty("user.dir") + File.separator + "data" + File.separator + "emoji";
        return new File(path);
    }

    private void extractEmojiFromUrl(String url) throws IOException, JSONException {
        Document document = Jsoup.connect(url).get();

        Elements header = document.body().getElementsByTag("h1");
        String title = header.get(0).text();
        String categoryDescription = header.next().text();
        ArrayList<Emoji> emojis = new ArrayList<>();
        for (Node node : document.getElementsByClass("emoji-list").get(0).childNodes()) {
            if (node instanceof Element) {
                Element emojiNode = (Element) node;
                String emojiChar = emojiNode.getElementsByClass("emoji").get(0).text();
                String description = ((TextNode) emojiNode.childNode(0).childNode(1)).text();
                emojis.add(new Emoji(emojiChar, description));
            }
        }
        writeToFile(title, categoryDescription, emojis);
    }

    private void writeToFile(String title, String categoryDescription, ArrayList<Emoji> emojis) throws JSONException, IOException {
        String fileName = title.replaceAll("[^a-zA-Z0-9-_\\.]", "") + ".json";
        File file = new File(getDataDir(), fileName);
        JSONObject root = new JSONObject();
        root.put("title", title);
        root.put("description", categoryDescription);

        JSONArray data = new JSONArray();
        for (Emoji emoji : emojis) {
            JSONObject object = new JSONObject();
            object.put("emoji", emoji.emoji);
            object.put("desc", emoji.desc);
            data.put(object);
        }
        root.put("data", data);

        FileOutputStream fos = new FileOutputStream(file);
        fos.write(root.toString(2).getBytes());
        fos.flush();
        fos.close();
        System.out.println("GetEmoji.writeToFile");
    }

    static class Emoji {
        String emoji, desc;

        Emoji(String emoji, String desc) {
            this.emoji = emoji;
            this.desc = desc;
        }
    }
}
