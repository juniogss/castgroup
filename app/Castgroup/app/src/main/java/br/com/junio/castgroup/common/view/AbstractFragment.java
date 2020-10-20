package br.com.junio.castgroup.common.view;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

import br.com.junio.castgroup.R;

public abstract class AbstractFragment<P> extends Fragment implements View {

    protected P presenter;

    @Override
    public Context getContext() {
        return super.getContext();
    }

    @Override
    public void showProgressBar() {
    }

    @Override
    public void hideProgressBar() {
    }

    public ArrayAdapter<String> getAdapterOf(@NotNull String[] objects) {
        return new ArrayAdapter<>(requireContext(), R.layout.dropdown_item, objects);
    }

    @NotNull
    public <T> ArrayAdapter<T> getAdapterOf(List<T> list) {
        return new ArrayAdapter<>(requireContext(), R.layout.dropdown_item, list);
    }

    public String getText(@NonNull TextInputEditText editText) {
        return Objects.requireNonNull(editText.getText()).toString();
    }

    public String getText(@NonNull AutoCompleteTextView autoCompleteTextView) {
        return autoCompleteTextView.getText().toString();
    }

    public void setPresenter(P presenter) {
        this.presenter = presenter;
    }

    protected abstract @LayoutRes
    int getLayout();
}
