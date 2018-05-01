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

import junit.framework.TestCase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/**
 * Created by Duy on 27-Aug-17.
 */

public class GetEmoticons extends TestCase {

    private File getDataDir() {
        String path = "C:\\github\\ascii_generate\\app\\src\\main\\assets\\emoticons";
        return new File(path);
    }

    public void testGetAll() {
        String baseLocation = "http://japaneseemoticons.me/all-japanese-emoticons/";
        try {
            getDataFromUrl(baseLocation);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int pageIndex = 1; pageIndex < 20; pageIndex++) {
            try {
                getDataFromUrl(baseLocation + pageIndex);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private void getDataFromUrl(String url) throws Exception {
        Document document = Jsoup.connect(url).get();
        Element content = document.getElementsByAttributeValue("class", "entry-content").get(0);
        String title = null;
        StringBuilder description = new StringBuilder();

        ArrayList<String> list = new ArrayList<>();
        for (Node node : content.childNodes()) {
            if (node instanceof Element) {
                if (((Element) node).tagName().equals("h3")) {
                    //write last file
                    writeFile2(title, description.toString(), list);
                    //reduce list
                    list.clear();
                    title = node.childNode(0).toString();
                    description = new StringBuilder();

                    node = node.nextSibling();
                    while (!(node instanceof Comment)) {
                        if (node instanceof Element) {
                            description.append(((Element) node).text());
                        } else if (node instanceof TextNode) {
                            description.append(((TextNode) node).text());
                        } else {
                            description.append(node.toString());
                        }
                        node = node.nextSibling();
                    }
                } else if (((Element) node).tagName().equals("table")) {
                    //print table
                    Elements tr = ((Element) node).getElementsByTag("tr");
                    for (Element element : tr) {
                        Elements td = element.getElementsByTag("td");
                        for (Element value : td) {
                            if (value.childNodeSize() > 0) {
                                if (!(value.childNode(0) instanceof Comment)) {
                                    TextNode emoticonNode = (TextNode) value.childNode(0);
                                    String text = emoticonNode.text();
                                    list.add(text);
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    private void writeFile2(String title, String description, ArrayList<String> content) throws IOException, ParserConfigurationException, TransformerException, JSONException {
        System.out.println("title = " + title);
        System.out.println("description = " + description);
        System.out.println("list = " + content);

        if (title == null || content == null || content.isEmpty()) return;

        String fileName = title.replaceAll("[^a-zA-Z0-9-_\\.]", "") + ".json";
        File file = new File(getDataDir(), fileName);

        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }

        JSONObject root = new JSONObject();
        root.put("title", title);
        root.put("description", description);
        JSONArray data = new JSONArray();
        for (String item : content) {
            data.put(item);
        }
        root.put("data", data);
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(root.toString(2).getBytes());
        fos.flush();
        fos.close();
        System.out.println("GetEmoticons.writeFile2");
    }


}
