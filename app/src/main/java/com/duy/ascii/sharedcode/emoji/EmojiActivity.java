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

package com.duy.ascii.sharedcode.emoji;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.MenuItem;

import com.duy.ascii.sharedcode.AdBannerActivity;
import com.duy.ascii.sharedcode.R;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by Duy on 09-Aug-17.
 */

public class EmojiActivity extends AdBannerActivity implements HeaderAdapter.EmojiClickListener {
    private static final String TAG = "EmojiActivity";
    private final Handler mHandler = new Handler();
    private HashMap<String, ArrayList<String>> emojis = new HashMap<>();
    private SlidingUpPanelLayout mSlidingUpPanelLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emoji);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        setTitle(R.string.emoji);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                parseData();
                updateData();
            }
        });
    }

    private void updateData() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycle_view);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        HeaderAdapter headerAdapter = new HeaderAdapter(this, getHeaderData());
        recyclerView.setAdapter(headerAdapter);
        headerAdapter.setListener(this);
    }

    private ArrayList<Pair<String, String>> getHeaderData() {
        Set<Map.Entry<String, ArrayList<String>>> entries = emojis.entrySet();
        ArrayList<Pair<String, String>> header = new ArrayList<>();
        for (Map.Entry<String, ArrayList<String>> entry : entries) {
            ArrayList<String> data = entry.getValue();
            String preview = "";
            for (int i = 0; i < (Math.min(5, data.size())); i++) {
                preview += data.get(i) + " ";
            }
            header.add(new Pair<>(entry.getKey(), preview));
        }
        return header;
    }

    private void parseData() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(getAssets().open("emoji/emoji.xml"));
            Element root = document.getDocumentElement();
            NodeList items = root.getElementsByTagName("item");
            for (int i = 0; i < items.getLength(); i++) {
                Element item = (Element) items.item(i);
                String name = item.getElementsByTagName("name").item(0).getChildNodes().item(0).getNodeValue();
                String data = item.getElementsByTagName("data").item(0).getChildNodes().item(0).getNodeValue();
                String[] split = data.split("\\n");
                ArrayList<String> list = new ArrayList<>();
                for (String s : split) {
                    s = s.trim();
                    if (!s.isEmpty() && s.contains(" ")) {
                        list.add(s.substring(0, s.indexOf(" ")));
                    }
                }
                emojis.put(name, list);
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(String name) {
        EmojiFragment emojiFragment = EmojiFragment.newInstance(emojis.get(name));
        emojiFragment.show(getSupportFragmentManager(), EmojiFragment.TAG);
    }
}
