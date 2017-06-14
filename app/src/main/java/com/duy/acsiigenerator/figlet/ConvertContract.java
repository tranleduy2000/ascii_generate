package com.duy.acsiigenerator.figlet;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;

/**
 * Created by Duy on 15-Jun-17.
 */

public class ConvertContract {
    public interface View {
        void showResult(@NonNull ArrayList<String> result);

        void clearResult();

        void addResult(String text);

        void setPresenter(@Nullable Presenter presenter);

        void setProgress(int process);

        int getMaxProgress();
    }

    public interface Presenter {
        void onTextChanged(@NonNull String text);
    }
}
