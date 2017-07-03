package com.duy.acsiigenerator.emoticons;

import java.util.ArrayList;

/**
 * Created by Duy on 03-Jul-17.
 */

public class EmoticonContract {
    public interface View {
        void showProgress();

        void hideProgress();

        void display(ArrayList<String> list);

        void setPresenter(Presenter presenter);

        void append(String value);
    }

    public interface Presenter {
        void start(int index);
    }

}
