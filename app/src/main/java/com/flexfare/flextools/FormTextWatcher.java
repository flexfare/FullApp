package com.flexfare.flextools;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

/**
 * Created by kodenerd on 8/19/17.
 */

abstract class FormTextWatcher implements TextWatcher{

    private View delegateView;
    private FormValidator validator;
    private FormListener formTextListener;

    FormTextWatcher(View delegateView) {
        this.delegateView = delegateView;
    }

    void setFormwatcherListener(FormListener easyFormTextListener) {
        this.formTextListener = easyFormTextListener;
    }

    void setValidator(FormValidator validator) {
        this.validator = validator;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        boolean isValid = validator.isValid(s);

        if (isValid) {
            clearError();

            if (formTextListener != null) {
                formTextListener.onFilled(delegateView);
            }
        } else {
            renderError();

            if (formTextListener != null) {
                formTextListener.onError(delegateView);
            }
        }
    }

    protected abstract void renderError();
    protected abstract void clearError();
}
