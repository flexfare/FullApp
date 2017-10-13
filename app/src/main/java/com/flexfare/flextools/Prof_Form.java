package com.flexfare.flextools;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.flexfare.android.R;

/**
 * Created by kodenerd on 8/19/17.
 */

public class Prof_Form extends RelativeLayout implements FormListener {

    private Button submitButton;
    private ShowErrorOn showErrorOn = ShowErrorOn.CHANGE;

    private SparseArray<FormInputs> fieldCheckList;
    private int submitButtonId;

    private static class FormInputs {
        private View view;
        boolean isValid;

        FormInputs(View view, boolean isValid) {
            this.view = view;
            this.isValid = isValid;
        }

        public View getView() {
            return view;
        }
    }
        public Prof_Form(Context context) {
            super(context);
        }
        public Prof_Form(Context context, AttributeSet attrs) {
            super(context, attrs);
            setPropertyFromAttributes(attrs);
        }
        public Prof_Form(Context context, AttributeSet attrs, int defStyleAttrs) {
            super(context, attrs, defStyleAttrs);
            setPropertyFromAttributes(attrs);
    }
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        submitButton = (Button) findViewById(submitButtonId);
        fieldCheckList = new SparseArray<>(getChildCount());
        initializeFieldCheckList(this);
        enableSubmitButton(isValid());
    }
    @Override
    public void onFilled(View view) {
        fieldCheckList.get(view.getId()).isValid = true;

        if (showErrorOn == ShowErrorOn.CHANGE) {
            if (isValid()){
                enableSubmitButton(true);
            }
        } else {
            if (isLastFieldToFill()) {
                enableSubmitButton(true);
            }
        }
    }
    @Override
    public void onError(View view) {
        fieldCheckList.get(view.getId()).isValid = false;

        if (showErrorOn == ShowErrorOn.CHANGE || !isLastFieldToFill()) {
            enableSubmitButton(false);
        }
    }

    public void validate() {
        if (showErrorOn == ShowErrorOn.UNFOCUS) {
            for (int i = 0; i < fieldCheckList.size(); i++) {
                FormInputs formInputs = fieldCheckList.get(fieldCheckList.keyAt(i));
                View view = formInputs.getView();
                if (view instanceof Prof_Form) {
                    FormEditText editText = (FormEditText) view;
                    editText.validate();
                } else if (view instanceof AutoCompleteTextView) {
                    AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) view;
                    autoCompleteTextView.validate();
                } else {
                    UpdateForm updateForm = (UpdateForm) view;
                    updateForm.validate();
                }
            }
        }
    }

    public boolean isValid() {
        for (int i = 0; i < fieldCheckList.size(); i++) {
            FormInputs formInputs = fieldCheckList.get(fieldCheckList.keyAt(i));
            if (!formInputs.isValid) {
                return false;
            }
        }

        return true;
    }
    private void initializeFieldCheckList(ViewGroup viewGroup) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View view = viewGroup.getChildAt(i);
            if (view instanceof UpdateForm) {
                UpdateForm updateForm = (UpdateForm) view;
                if (updateForm.getErrorType() != ErrorType.NONE) {
                    updateForm.setFormListener(this);
                    updateForm.setShowErrorOn(showErrorOn);
                    fieldCheckList.put(updateForm.getId(), new FormInputs(updateForm, false));
                }

            } else if (view instanceof ViewGroup) {
                initializeFieldCheckList((ViewGroup) view);

            } else if (view instanceof FormEditText) {
                FormEditText easyFormEditText = (FormEditText) view;
                if (easyFormEditText.getErrorType() != ErrorType.NONE) {
                    easyFormEditText.formEditTextListener(this);
                    easyFormEditText.setShowErrorOn(showErrorOn);
                    fieldCheckList.put(easyFormEditText.getId(), new FormInputs(easyFormEditText, false));
                }

            } else if (view instanceof AutoCompleteTextView) {
                AutoCompleteTextView easyAutoCompleteTextView = (AutoCompleteTextView) view;
                if (easyAutoCompleteTextView.getErrorType() != ErrorType.NONE) {
                    easyAutoCompleteTextView.setAutoFormListener(this);
                    easyAutoCompleteTextView.setShowErrorOn(showErrorOn);
                    fieldCheckList.put(easyAutoCompleteTextView.getId(), new FormInputs(easyAutoCompleteTextView, false));
                }
            }
        }
    }

    private void setPropertyFromAttributes(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.EasyForm);

        if (typedArray != null) {
            submitButtonId = typedArray.getResourceId(R.styleable.EasyForm_submitButton, -1);
            int type = typedArray.getInt(R.styleable.EasyForm_showErrorOn, -1);
            showErrorOn = ShowErrorOn.valueOf(type);

            typedArray.recycle();
        }
    }

    private boolean isLastFieldToFill() {
        int filled = 0;

        for (int i = 0; i < fieldCheckList.size(); i++) {
            FormInputs formInputs = fieldCheckList.get(fieldCheckList.keyAt(i));
            if (formInputs.isValid) {
                filled++;
            }
        }

        return filled == fieldCheckList.size() - 1;
    }

    private void enableSubmitButton(boolean enable) {
        if (submitButton != null) {
            if (enable) {
                submitButton.setEnabled(true);
                submitButton.setAlpha(1f);
            } else {
                submitButton.setEnabled(false);
                submitButton.setAlpha(0.5f);
            }
        }
    }
}
