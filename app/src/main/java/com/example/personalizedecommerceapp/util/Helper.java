package com.example.personalizedecommerceapp.util;


import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.inputmethod.InputMethodManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Helper {

    public static void closeKeyboard(Context context, View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.
                getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void makeSnackBar(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
    }

    public static void makeSnackBarWithAction(View view, String message,
                                              String button, View.OnClickListener onClick) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).setAction(button, onClick).show();
    }

    public static void hideKeyBoard(Context context, View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static boolean emailValidation(CharSequence charSequence) {
        return (!TextUtils.isEmpty(charSequence) && Patterns.EMAIL_ADDRESS.matcher(charSequence).matches());
    }

    public static boolean phoneValidation(CharSequence charSequence) {
        return (!TextUtils.isEmpty(charSequence) && Patterns.PHONE.matcher(charSequence).matches());
    }

    public static void goTo(Context context, Class<?> activity) {
        Intent intent = new Intent(context, activity);
        context.startActivity(intent);
    }

    public static void goTo(Context context, Class<?> activity, String name, String value) {
        Intent intent = new Intent(context, activity);
        intent.putExtra(name, value);
        context.startActivity(intent);
    }

    public static void goTo(Context context, Class<?> activity, String name, Serializable value) {
        Intent intent = new Intent(context, activity);
        intent.putExtra(name, value);
        context.startActivity(intent);
    }

    public static void goTo(Context context, Class<?> activity, String name, List<? extends Serializable> values) {
        Intent intent = new Intent(context, activity);
        ArrayList<Serializable> serializableList = new ArrayList<>(values);
        intent.putExtra(name, serializableList);
        context.startActivity(intent);
    }


    public static void goToWithFlags(Context context, Class<?> activity, int flags) {
        Intent intent = new Intent(context, activity);
        intent.setFlags(flags);
        context.startActivity(intent);
    }

    public static LinearLayoutManager getVerticalManager(Context context) {
        return new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
    }


    public static void clearText(ViewGroup viewGroup) {

        for (int i = 0, count = viewGroup.getChildCount(); i < count; ++i) {
            View view = viewGroup.getChildAt(i);
            if (view instanceof TextInputEditText) {
                Objects.requireNonNull(((TextInputEditText) view).getText()).clear();
            }

            if (view instanceof RadioGroup) {
                ((RadioButton) ((RadioGroup) view).getChildAt(0)).setChecked(true);
            }

            if (view instanceof Spinner) {
                ((Spinner) view).setSelection(0);
            }

            if (view instanceof ViewGroup && (((ViewGroup) view).getChildCount() > 0))
                clearText((ViewGroup) view);
        }
    }

    public static boolean isValidDoubleFieldValidation(TextInputEditText editText, double minValue) {
        boolean isValidate = true;
        try {
            TextInputLayout textInputLayout = null;
            ViewParent parent = editText.getParent().getParent();
            if (parent instanceof TextInputLayout) {
                textInputLayout = (TextInputLayout) parent;
            }

            double stringToDouble;
            try {
                final String text = Objects.requireNonNull(editText.getText()).toString().trim();
                stringToDouble = Double.parseDouble(text);
                if (!(stringToDouble > minValue)) throw new RuntimeException("Not Valid");
                if (textInputLayout != null) {
                    textInputLayout.setErrorEnabled(false);
                } else {
                    editText.setError(null);
                }
            } catch (Exception e) {
                if (textInputLayout != null) {
                    textInputLayout.isHelperTextEnabled();
                    textInputLayout.setError("Please Enter Valid Value");
                    textInputLayout.setErrorEnabled(true);
                } else {
                    editText.setError("Invalid email");
                }
                isValidate = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            isValidate = false;
        }
        return isValidate;
    }

    public static boolean isValidEmailFieldValidation(TextInputEditText editText) {
        boolean isValidate = true;
        try {
            TextInputLayout textInputLayout = null;
            ViewParent parent = editText.getParent().getParent();
            if (parent instanceof TextInputLayout) {
                textInputLayout = (TextInputLayout) parent;
            }

            final String email = Objects.requireNonNull(editText.getText()).toString().trim();
            if (!(!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches())) {
                if (textInputLayout != null) {
                    textInputLayout.isHelperTextEnabled();
                    textInputLayout.setError("Please Enter Valid Email");
                    textInputLayout.setErrorEnabled(true);
                } else {
                    editText.setError("Invalid email");
                }
                isValidate = false;
            } else {
                if (textInputLayout != null) {
                    textInputLayout.setErrorEnabled(false);
                } else {
                    editText.setError(null);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            isValidate = false;
        }
        return isValidate;
    }

    public static boolean isValidContactFieldValidation(TextInputEditText editText) {
        boolean isValidate = true;
        try {
            TextInputLayout textInputLayout = null;
            ViewParent parent = editText.getParent().getParent();
            if (parent instanceof TextInputLayout) {
                textInputLayout = (TextInputLayout) parent;
            }

            final String phone = Objects.requireNonNull(editText.getText()).toString().trim();
            if (!(!TextUtils.isEmpty(phone) && Patterns.PHONE.matcher(phone).matches())) {
                if (textInputLayout != null) {
                    textInputLayout.isHelperTextEnabled();
                    textInputLayout.setError("Please Enter Valid Contact");
                    textInputLayout.setErrorEnabled(true);
                } else {
                    editText.setError("Invalid Contact");
                }
                isValidate = false;
            } else {
                if (textInputLayout != null) {
                    textInputLayout.setErrorEnabled(false);
                } else {
                    editText.setError(null);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            isValidate = false;
        }
        return isValidate;
    }


    public static boolean isEmptyFieldValidation(TextInputEditText editText) {
        boolean isValidate = true;
        try {
            TextInputLayout textInputLayout = null;
            ViewParent parent = editText.getParent().getParent();
            if (parent instanceof TextInputLayout) {
                textInputLayout = (TextInputLayout) parent;
            }
            if (editText.getText().toString().isEmpty()) {
                if (textInputLayout != null) {
                    textInputLayout.isHelperTextEnabled();
                    textInputLayout.setError("Please " + textInputLayout.getHint());
                    textInputLayout.setErrorEnabled(true);
                } else {
                    editText.setError("Empty");
                }
                isValidate = false;
            } else {
                if (textInputLayout != null) {
                    textInputLayout.setErrorEnabled(false);
                } else {
                    editText.setError(null);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            isValidate = false;
        }
        return isValidate;
    }

    public static boolean isEmptyFieldValidation(TextInputEditText[] editTexts) {
        boolean isValidate = true;
        try {
            for (TextInputEditText editText : editTexts) {
                TextInputLayout textInputLayout = null;
                ViewParent parent = editText.getParent().getParent();
                if (parent instanceof TextInputLayout) {
                    textInputLayout = (TextInputLayout) parent;
                }
                if (Objects.requireNonNull(editText.getText()).toString().isEmpty()) {
                    if (textInputLayout != null) {
                        textInputLayout.isHelperTextEnabled();
                        textInputLayout.setError("Please " + textInputLayout.getHint());
                        textInputLayout.setErrorEnabled(true);
                    } else {
                        editText.setError("Empty");
                    }
                    isValidate = false;
                } else {
                    if (textInputLayout != null) {
                        textInputLayout.setErrorEnabled(false);
                    } else {
                        editText.setError(null);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            isValidate = false;
        }
        return isValidate;
    }

    public static boolean isEmptyFieldValidation(MaterialAutoCompleteTextView[] autoCompleteTextViews) {
        boolean isValidate = true;
        try {
            for (MaterialAutoCompleteTextView autoCompleteTextView : autoCompleteTextViews) {
                TextInputLayout textInputLayout = null;
                ViewParent parent = autoCompleteTextView.getParent().getParent();
                if (parent instanceof TextInputLayout) {
                    textInputLayout = (TextInputLayout) parent;
                }
                if (autoCompleteTextView.getText().toString().isEmpty()) {
                    if (textInputLayout != null) {
                        textInputLayout.isHelperTextEnabled();
                        textInputLayout.setError("Please " + textInputLayout.getHint());
                        textInputLayout.setErrorEnabled(true);
                    } else {
                        autoCompleteTextView.setError("Empty");
                    }
                    isValidate = false;
                } else {
                    if (textInputLayout != null) {
                        textInputLayout.setErrorEnabled(false);
                    } else {
                        autoCompleteTextView.setError(null);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            isValidate = false;
        }
        return isValidate;
    }

    public static void setTextInputError(View view, String error) {
        ViewParent parent = view.getParent().getParent();
        if (parent instanceof TextInputLayout) {
            TextInputLayout textInputLayout = (TextInputLayout) parent;
            textInputLayout.isHelperTextEnabled();
            textInputLayout.setError(error);
            textInputLayout.setErrorEnabled(true);
        }
    }

    public static void clearError(ViewGroup viewGroup) {

        for (int i = 0, count = viewGroup.getChildCount(); i < count; ++i) {
            View view = viewGroup.getChildAt(i);
            if (view instanceof TextInputEditText) {
                ((TextInputEditText) view).setError(null);
            }

            if (view instanceof RadioGroup) {
                ((RadioButton) ((RadioGroup) view).getChildAt(0)).setError(null);
            }

            if (view instanceof TextInputLayout) {
                ((TextInputLayout) view).setError(null);
                ((TextInputLayout) view).setErrorEnabled(false);
            }

            if (view instanceof ViewGroup && (((ViewGroup) view).getChildCount() > 0))
                clearError((ViewGroup) view);
        }
    }

    public static void setAllTextInputEditTextsNonEditable(ViewGroup viewGroup) {

        for (int i = 0, count = viewGroup.getChildCount(); i < count; ++i) {
            View view = viewGroup.getChildAt(i);
            if (view instanceof TextInputEditText) {
                view.setClickable(false);
                view.setFocusable(false);
                view.setLongClickable(false);
            }
            if (view instanceof ViewGroup && (((ViewGroup) view).getChildCount() > 0))
                setAllTextInputEditTextsNonEditable((ViewGroup) view);
        }
    }

    public static void setTextInputEditTextNonEditable(TextInputEditText[] editTextArray) {
        for (TextInputEditText editText : editTextArray) {
            editText.setClickable(false);
            editText.setFocusable(false);
            editText.setLongClickable(false);
        }
    }

    public static void setTextInputEditTextEditable(TextInputEditText[] editTextArray) {
        for (int i = 0; i < editTextArray.length; i++) {
            TextInputEditText editText = editTextArray[i];
            editText.setClickable(true);
            editText.setFocusable(true);
            editText.setLongClickable(true);
            editText.setFocusableInTouchMode(true);
            if (i == 0) editText.requestFocus();
        }
    }
}
