package com.flexfare.flextools;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ArrayAdapter;

import com.flexfare.android.R;

import java.util.Arrays;
import java.util.List;

import static com.flexfare.flextools.FormValidator.INVALID_VALUE;

/**
 * Created by kodenerd on 8/19/17.
 */

public class AutoCompleteTextView extends AppCompatAutoCompleteTextView implements View.OnFocusChangeListener{

    private FormValidator validator;
    private FormListener formListener;
    private List<String> items;
    private String errorMessage;

    private FormTextWatcher textWatcher = new FormTextWatcher(this) {
        @Override
        protected void renderError() {
            setError(errorMessage);
        }

        @Override
        protected void clearError() {
            setError(null);
        }
    };

    public AutoCompleteTextView(Context context) {
        super(context);
    }

    public AutoCompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (!isInEditMode()) {
            setPropertyFromAttributes(attrs);
        }
    }

    public AutoCompleteTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        if (!isInEditMode()) {
            setPropertyFromAttributes(attrs);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        if (items != null) {
            setItems(items);
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
            validate();
        }
    }

    public ErrorType getErrorType() {
        return validator.getErrorType();
    }

    public void setErrorType(ErrorType errorType) {
        validator.setErrorType(errorType);
    }

    public void setRegexPattern(String regexPattern) {
        validator.setRegexPattern(regexPattern);
    }

    public void setMinChars(int minChars) {
        validator.setMinChars(minChars);
    }

    public void setMaxChars(int maxChars) {
        validator.setMaxChars(maxChars);
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void setItems(String[] items) {
        this.items = Arrays.asList(items);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, items);
        setAdapter(adapter);
    }

    public void setItems(List<String> items) {
        this.items = items;
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, items);
        setAdapter(adapter);
    }

    void setShowErrorOn(ShowErrorOn showErrorOn) {
        if (validator.getErrorType() != ErrorType.NONE) {
            if (showErrorOn == ShowErrorOn.CHANGE) {
                addTextChangedListener(textWatcher);
                setOnFocusChangeListener(null);
            } else {
                removeTextChangedListener(textWatcher);
                setOnFocusChangeListener(this);
            }
        }
    }

    void setAutoFormListener(FormListener easyFormEditTextListener) {
        this.formListener = easyFormEditTextListener;
        textWatcher.setFormwatcherListener(easyFormEditTextListener);
    }

    void validate() {
        boolean isValid = validator.isValid(getText().toString());
        setError(isValid ? null : errorMessage);

        if (isValid) {
            formListener.onFilled(this);
        } else {
            formListener.onError(this);
        }
    }

    private void setPropertyFromAttributes(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.EasyAutoCompleteTextView);

        if (typedArray != null) {
            int type = typedArray.getInt(R.styleable.EasyAutoCompleteTextView_errorType, -1);
            ErrorType errorType = ErrorType.valueOf(type);
            errorMessage = typedArray.getString(R.styleable.EasyAutoCompleteTextView_errorMessage);
            String regexPattern = typedArray.getString(R.styleable.EasyAutoCompleteTextView_regexPattern);
            int minChars = typedArray.getInt(R.styleable.EasyAutoCompleteTextView_minChars, INVALID_VALUE);
            int maxChars = typedArray.getInt(R.styleable.EasyAutoCompleteTextView_maxChars, INVALID_VALUE);

            if (errorType.equals(ErrorType.VALUE)) {
                errorType = ErrorType.NONE;
            }

            if (errorMessage == null) {
                errorMessage = "Error";
            }

            int itemResId = typedArray.getResourceId(R.styleable.EasyAutoCompleteTextView_items, 0);

            if (itemResId > 0) {
                items = Arrays.asList(getResources().getStringArray(itemResId));
            }

            validator = new FormValidator(errorType, regexPattern, INVALID_VALUE, INVALID_VALUE, minChars, maxChars);

            textWatcher.setValidator(validator);

            typedArray.recycle();
        }
    }
}
