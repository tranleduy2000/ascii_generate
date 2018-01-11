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

package com.duy.ascii.art.utils;

import android.view.View;

/**
 * Created by Duy on 1/11/2018.
 */

public class TooltipUtil {

    public static void bottomToolTipDialogBox(View view, String description) {
        it.sephiroth.android.library.tooltip.Tooltip.Builder builder = new it.sephiroth.android.library.tooltip.Tooltip.Builder(101)
                .anchor(view, it.sephiroth.android.library.tooltip.Tooltip.Gravity.BOTTOM)
                .closePolicy(new it.sephiroth.android.library.tooltip.Tooltip.ClosePolicy()
                        .insidePolicy(true, false)
                        .outsidePolicy(true, false), 4000)
                .activateDelay(900)
                .showDelay(400)
                .text(description)
                .maxWidth(600)
                .withArrow(true)
                .withOverlay(true);
        it.sephiroth.android.library.tooltip.Tooltip.make(view.getContext(), builder.build()).show();
    }

}
