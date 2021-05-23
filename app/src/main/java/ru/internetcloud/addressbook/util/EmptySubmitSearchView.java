package ru.internetcloud.addressbook.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;

//**********************************************
// если запрос пуст не срабатывает кнопка поиска: решение by Jens Klingenberg (jensklingenberg.de)
// https://jensklingenberg.de/how-to-use-a-searchview-with-an-empty-query-text-submit/
//**********************************************

public class EmptySubmitSearchView extends SearchView {

    private OnQueryTextListener listener;
    private SearchView.SearchAutoComplete searchSrcTextView;

    public EmptySubmitSearchView(@NonNull Context context) {
        super(context);
    }

    public EmptySubmitSearchView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public EmptySubmitSearchView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setOnQueryTextListener(final OnQueryTextListener listener) {
        super.setOnQueryTextListener(listener);

        // by Jens Klingenberg (jensklingenberg.de):
        this.listener = listener;
        searchSrcTextView = this.findViewById(androidx.appcompat.R.id.search_src_text);
        searchSrcTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (listener != null) {
                    listener.onQueryTextSubmit(getQuery().toString());
                }
                return true;
            }
        });
    }
}
