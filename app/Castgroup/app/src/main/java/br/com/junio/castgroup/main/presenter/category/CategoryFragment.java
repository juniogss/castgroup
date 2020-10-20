package br.com.junio.castgroup.main.presenter.category;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;

import br.com.junio.castgroup.R;
import br.com.junio.castgroup.common.component.LoadingButton;
import br.com.junio.castgroup.common.events.CustomFocusChange;
import br.com.junio.castgroup.common.model.Category;
import br.com.junio.castgroup.common.view.AbstractFragment;
import br.com.junio.castgroup.databinding.FragmentCategoryBinding;
import br.com.junio.castgroup.main.presenter.ListView;
import lombok.Setter;

public class CategoryFragment extends AbstractFragment<CategoryPresenter> implements ListView.CategoryInterface {

    @Setter
    private Category category;
    private TextInputEditText etCode;
    private TextInputEditText etDesc;
    private LoadingButton btAdd;
    private TextInputLayout lyDesc;
    private TextInputLayout lyCode;

    @NotNull
    public static Fragment newInstance(ListView listView, CategoryPresenter presenter, Category category) {
        CategoryFragment fragment = new CategoryFragment();
        fragment.setPresenter(presenter);
        fragment.setCategory(category);
        presenter.setView(listView, fragment);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentCategoryBinding binding = FragmentCategoryBinding.inflate(inflater, container, false);

        btAdd = binding.btAdd;

        etCode = binding.etCode;
        etDesc = binding.etDesc;

        lyDesc = binding.lyDesc;
        lyCode = binding.lyCode;

        etDesc.addTextChangedListener(new CustomTextWatcher(lyDesc));
        etCode.addTextChangedListener(new CustomTextWatcher(lyCode));

        etDesc.setOnFocusChangeListener(new CustomFocusChange(etDesc, lyDesc));
        etCode.setOnFocusChangeListener(new CustomFocusChange(etCode, lyCode));

        btAdd.setOnClickListener(view -> {
            if (category != null) presenter.update(getCategory());
            else presenter.add(getCategory());
        });

        if (category != null) {
            btAdd.setText(getString(R.string.edit));
            setFields();
        }

        return binding.getRoot();
    }

    private void setFields() {
        etCode.setText(String.valueOf(category.getCode()));
        etDesc.setText(category.getDesc());
    }

    @NotNull
    private Category getCategory() {
        Category category = new Category();
        if (this.category != null) category.setCode(this.category.getCode());
        category.setCode(Integer.parseInt(getText(etCode)));
        category.setDesc(getText(etDesc));
        return category;
    }

    @Override
    public void onResume() {
        super.onResume();
        validateForm();
    }

    private void validateForm() {
        btAdd.setEnabled(!getText(etDesc).isEmpty() && !getText(etCode).isEmpty());
    }

    @Override
    public void onFailureForm(@NotNull Category category) {
        if (category.getCode() != 0) lyCode.setError(getString(R.string.invalid_code));
        if (category.getDesc() != null) lyDesc.setError(category.getDesc());
    }

    @Override
    public void onFailureInsert(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    private class CustomTextWatcher implements TextWatcher {
        private TextInputLayout textInputLayout;

        public CustomTextWatcher(TextInputLayout e) {
            textInputLayout = e;
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            textInputLayout.setError(null);
            validateForm();
        }

        public void afterTextChanged(Editable s) {
        }
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_category;
    }
}