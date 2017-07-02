package com.duy.acsiigenerator.image;

import android.net.Uri;

/**
 * Created by Duy on 02-Jul-17.
 */

public class ImageContract {
    public interface View {
        public void display(Uri uri);

        void onFailed(Exception e);
    }

    public interface Presenter {

    }
}
