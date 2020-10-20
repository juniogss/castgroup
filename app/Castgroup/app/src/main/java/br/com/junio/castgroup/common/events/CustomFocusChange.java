package br.com.junio.castgroup.common.events;

import android.view.View;
import android.widget.AutoCompleteTextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;

public class CustomFocusChange implements View.OnFocusChangeListener {

    private final TextInputEditText editText;
    private final TextInputLayout inputLayout;
    private final AutoCompleteTextView completeTextView;

    public CustomFocusChange(@NotNull TextInputEditText editText, @NotNull TextInputLayout inputLayout) {
        this.editText = editText;
        this.inputLayout = inputLayout;
        this.completeTextView = null;
    }

    public CustomFocusChange(@NotNull AutoCompleteTextView completeTextView, @NotNull TextInputLayout inputLayout) {
        this.completeTextView = completeTextView;
        this.editText = null;
        this.inputLayout = inputLayout;
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        if (!hasFocus)
            if (editText != null) {
                if (editText.getText() != null && editText.getText().toString().isEmpty())
                    inputLayout.setError("Campo vazio");
            } else if (completeTextView != null)
                if (completeTextView.getText() != null && completeTextView.getText().toString().isEmpty())
                    inputLayout.setError("Campo vazio");
    }
}