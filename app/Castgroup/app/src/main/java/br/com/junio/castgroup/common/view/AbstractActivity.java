package br.com.junio.castgroup.common.view;

import android.content.Context;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import br.com.junio.castgroup.R;

public abstract class AbstractActivity extends AppCompatActivity implements View {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        onInject();
    }

    protected abstract void onInject();

    @Override
    public Context getContext() {
        return getBaseContext();
    }

    @Override
    public void showProgressBar() {
    }

    @Override
    public void hideProgressBar() {
    }

    public ArrayAdapter<String> getAdapterOf(@NotNull String[] objects) {
        return new ArrayAdapter<>(this, R.layout.dropdown_item, objects);
    }

    @NotNull
    public <T> ArrayAdapter<T> getAdapterOf(List<T> list) {
        return new ArrayAdapter<>(this, R.layout.dropdown_item, list);
    }
}
