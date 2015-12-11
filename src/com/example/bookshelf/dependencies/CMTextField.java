package com.example.bookshelf.dependencies;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.widget.EditText;

/**
 * Customized text field used for specific user-input
 */
public class CMTextField extends EditText {

    /**
     * Default constructor
     *
     * @param context activity identifier
     * @param id      text field id
     * @param hint    displayed input hint
     */
    public CMTextField(Context context, int id, String hint) {
        super(context);

        this.setSingleLine();
        this.setMaxLines(1);
        this.setTextColor(Color.WHITE);
        this.setHintTextColor(Color.rgb(224, 224, 209));
        this.setBackgroundResource(android.R.color.darker_gray);
        this.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        this.setHint(hint);
        this.setId(id);
    }

    /**
     * Default constructor with validation param
     *
     * @param context activity identifier
     * @param id      text field id
     * @param hint    displayed input hint
     */
    public CMTextField(Context context, int id, String hint, String validate) {
        super(context);

        this.setSingleLine();
        this.setMaxLines(1);
        this.setTextColor(Color.WHITE);
        this.setHintTextColor(Color.rgb(224, 224, 209));
        this.setBackgroundResource(android.R.color.darker_gray);
        this.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        this.setHint(hint);
        this.setId(id);

        onChangeListener(validate);
    }

    public CMTextField(Context context, int id, String hint, PasswordTransformationMethod method, String validate) {
        super(context);

        this.setSingleLine();
        this.setMaxLines(1);
        this.setTextColor(Color.WHITE);
        this.setBackgroundResource(android.R.color.darker_gray);
        this.setHintTextColor(Color.rgb(224, 224, 209));
        this.setHint(hint);
        this.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        this.setId(id);
        this.setTransformationMethod(method);

        onChangeListener(validate);
    }


    private void onChangeListener(String id) {
        this.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                switch(id) {
                    case "email":
                        Validation.checkEmailAddress(CMTextField.this, true);
                        break;
                    case "postcode":
                        Validation.checkPostalCode(CMTextField.this, true);
                        break;
                    case "isbn":
                        Validation.checkISBN(CMTextField.this, true);
                        break;
                    case "vereist":
                        Validation.hasText(CMTextField.this);
                        break;
                }
            }
        });
    }

}
