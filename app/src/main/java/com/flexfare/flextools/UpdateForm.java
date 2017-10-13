package com.flexfare.flextools;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.flexfare.android.R;

import static com.flexfare.flextools.FormValidator.INVALID_VALUE;

/**
 * Created by kodenerd on 8/19/17.
 */

public class UpdateForm extends TextInputLayout implements View.OnFocusChangeListener{

    private static final String ANDROID_RES_NAMESPACE = "http://schemas.android.com/apk/res/android";

    private EditText formEdit;
    private FormValidator validator;
    private FormListener formListener;

    private int editInputType;
    private float editTextSize;
    private int editColor;
    private String errorMsg;

    private FormTextWatcher textWatcher = new FormTextWatcher(this) {
        @Override
        protected void renderError() {
            setError(errorMsg);
            setErrorEnabled(true);
        }

        @Override
        protected void clearError() {
            setError(null);
            setErrorEnabled(false);
        }
    };

    public UpdateForm(Context context) {
        super(context);
    }
    public UpdateForm(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (!isInEditMode()) {
            setPropertyFromAttributes(attrs);
        }
    }
    public UpdateForm(Context context, AttributeSet attrs, int defStyleAttrs) {
        super(context, attrs, defStyleAttrs);

        if (!isInEditMode()) {
            setPropertyFromAttributes(attrs);
        }
    }
    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        if (!hasFocus){
            validate();
        }
    }
    void validate() {
        boolean isValid = validator.isValid(formEdit.getText());
        setError(isValid ? null : errorMsg);
        setErrorEnabled(!isValid);

        if (isValid) {
            formListener.onFilled(this);
        } else {
            formListener.onError(this);
        }
    }
    @NonNull
    @Override
    public EditText getEditText() {
        return formEdit;
    }

    public ErrorType getErrorType() {
        return validator.getErrorType();
    }

    public void setRegexPattern(String regexPattern) {
        validator.setRegexPattern(regexPattern);
    }

    public void setMinValue(int minValue) {
        validator.setMinValue(minValue);
    }

    public void setMaxValue(int maxValue) {
        validator.setMaxValue(maxValue);
    }

    public void setMinChars(int minChars) {
        validator.setMinChars(minChars);
    }

    public void setMaxChars(int maxChars) {
        validator.setMaxChars(maxChars);
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMsg = errorMessage;
    }

    void setShowErrorOn(ShowErrorOn showErrorOn) {
        if (validator.getErrorType() != ErrorType.NONE) {
            if (showErrorOn == ShowErrorOn.CHANGE) {
                formEdit.addTextChangedListener(textWatcher);
                formEdit.setOnFocusChangeListener(null);
            } else {
                formEdit.removeTextChangedListener(textWatcher);
                formEdit.setOnFocusChangeListener(this);
            }
        }
    }

    void setFormListener(FormListener easyFormEditTextListener) {
        this.formListener = easyFormEditTextListener;
        textWatcher.setFormwatcherListener(easyFormEditTextListener);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        addEasyEditText();
    }

    private void setPropertyFromAttributes(AttributeSet attrs) {
        editInputType = attrs.getAttributeIntValue(ANDROID_RES_NAMESPACE, "inputType", -1);

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.EasyFormEditText);

        if (typedArray != null) {
            int type = typedArray.getInt(R.styleable.EasyFormEditText_errorType, INVALID_VALUE);
            ErrorType errorType = ErrorType.valueOf(type);
            errorMsg = typedArray.getString(R.styleable.EasyFormEditText_errorMessage);
            String regexPattern = typedArray.getString(R.styleable.EasyFormEditText_regexPattern);
            float minValue = typedArray.getFloat(R.styleable.EasyFormEditText_minValue, INVALID_VALUE);
            float maxValue = typedArray.getFloat(R.styleable.EasyFormEditText_maxValue, INVALID_VALUE);
            int minChars = typedArray.getInt(R.styleable.EasyFormEditText_minChars, INVALID_VALUE);
            int maxChars = typedArray.getInt(R.styleable.EasyFormEditText_maxChars, INVALID_VALUE);

            editTextSize = typedArray.getDimensionPixelSize(R.styleable.EasyFormEditText_textSize, 0);
            editColor = typedArray.getColor(R.styleable.EasyFormEditText_textColor, 0);

            if (errorMsg == null) {
                errorMsg = "Error";
            }

            validator = new FormValidator(errorType, regexPattern, minValue, maxValue, minChars, maxChars);
            textWatcher.setValidator(validator);

            typedArray.recycle();
        }
    }

    private void addEasyEditText() {
        formEdit = new EditText(getContext());
        formEdit.setSingleLine();

        if (editInputType != -1) {
            formEdit.setInputType(editInputType);
        }

        if (editTextSize > 0) {
            formEdit.setTextSize(TypedValue.COMPLEX_UNIT_PX, editTextSize);
        }

        if (editColor != 0) {
            formEdit.setTextColor(editColor);
        }
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        formEdit.setLayoutParams(params);

        addView(formEdit);
    }
}
