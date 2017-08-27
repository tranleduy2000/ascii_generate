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

import junit.framework.TestCase;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * Created by Duy on 27-Aug-17.
 */

public class GetEmoticons extends TestCase {

    public void test1() throws IOException {
        Document document = Jsoup.connect("http://japaneseemoticons.me/all-japanese-emoticons/").get();
        System.out.println();
    }

    public void testGetAll() throws Exception {
        String baseLocation = "http://japaneseemoticons.me/all-japanese-emoticons/";
        print2(baseLocation);
        for (int i = 1; i < 20; i++) {
            print2(baseLocation + i);
        }
    }


    private void print2(String baseLocation) throws IOException, TransformerException, ParserConfigurationException {
        Document document = Jsoup.connect(baseLocation).get();
        Elements content = document.getElementsByAttributeValue("class", "entry-content");
        String title = null;
        ArrayList<String> list = new ArrayList<>();
        for (Element div : content) {
            List<Node> nodes = div.childNodes();
            for (Node node : nodes) {
                if (node instanceof Element) {
                    if (((Element) node).tagName().equals("h3")) {
                        writeFile(title, list);
                        System.out.println("Title: " + node.childNode(0));
                        title = node.childNode(0).toString();
                    } else if (((Element) node).tagName().equals("table")) {
                        //print table
                        Elements tr = ((Element) node).getElementsByTag("tr");
                        for (Element element : tr) {
                            Elements td = element.getElementsByTag("td");
                            for (Element value : td) {
                                if (value.childNodeSize() > 0) {
                                    if (!(value.childNode(0) instanceof Comment)) {
//                                        System.out.println("Emoticon: " + value.childNode(0) + " " + value.childNode(0).getClass().getSimpleName());
                                        list.add(value.childNode(0).toString());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void writeFile(String title, ArrayList<String> list) throws IOException, ParserConfigurationException, TransformerException {
        System.out.println("GetEmoticons.writeFile");
        System.out.println("title = " + title);
        System.out.println("list = " + list);
        if (title == null || list == null || list.isEmpty()) return;
        String fileName = title.replaceAll("\\W", "") + ".xml";
        File file = new File("C:\\github\\AsciiGenerator\\app\\src\\main\\assets\\emoticons\\" + fileName);
        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        org.w3c.dom.Document document = documentBuilder.newDocument();
        org.w3c.dom.Element root = document.createElement("root");
        document.appendChild(root);

        org.w3c.dom.Element name = document.createElement("name");
        name.appendChild(document.createTextNode(title));
        root.appendChild(name);

        org.w3c.dom.Element data = document.createElement("data");

        for (String s : list) {
            org.w3c.dom.Element item = document.createElement("item");
            item.appendChild(document.createTextNode(s));
            data.appendChild(item);
        }
        root.appendChild(data);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource domSource = new DOMSource(document);
        StreamResult streamResult = new StreamResult(file);
        transformer.transform(domSource, streamResult);
    }

    public void test2() throws IOException, TransformerException, ParserConfigurationException {
        String baseLocation = "http://japaneseemoticons.me/all-japanese-emoticons/";
        print2(baseLocation);
    }

}
