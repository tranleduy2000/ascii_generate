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

package com.duy.ascii.sharedcode.emojiart;

import android.os.Bundle;

import com.duy.ascii.sharedcode.SimpleFragment;

/**
 * Created by Duy on 9/27/2017.
 */

public class EmojiArtFragment extends SimpleFragment {
    public static EmojiArtFragment newInstance() {

        Bundle args = new Bundle();

        EmojiArtFragment fragment = new EmojiArtFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    protected int getRootLayout() {
        return 0;
    }
}
