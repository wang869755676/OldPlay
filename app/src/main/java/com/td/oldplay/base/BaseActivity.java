package com.td.oldplay.base;

import android.support.v7.app.AppCompatActivity;

import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.ListCompositeDisposable;

public class BaseActivity extends AppCompatActivity {
    private ListCompositeDisposable listCompositeDisposable = new ListCompositeDisposable();


    protected void addDisposable(Disposable disposable) {
        if (disposable != null && !disposable.isDisposed()) {
            listCompositeDisposable.add(disposable);
        }
    }

    protected void reDisposable(Disposable disposable) {
        if (disposable != null) {
            listCompositeDisposable.remove(disposable);
        }
    }

    protected void clear() {
        if (!listCompositeDisposable.isDisposed()) {
            listCompositeDisposable.clear();
        }
    }

    @Override
    protected void onDestroy() {
        clear();
        super.onDestroy();
    }
}