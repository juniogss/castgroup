package br.com.junio.castgroup.main.presenter.course;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;

import br.com.junio.castgroup.R;
import br.com.junio.castgroup.common.component.LoadingButton;
import br.com.junio.castgroup.common.events.CustomFocusChange;
import br.com.junio.castgroup.common.mask.DateInputMask;
import br.com.junio.castgroup.common.model.Category;
import br.com.junio.castgroup.common.model.Course;
import br.com.junio.castgroup.common.services.DateUtil;
import br.com.junio.castgroup.common.view.AbstractFragment;
import br.com.junio.castgroup.databinding.FragmentCourseBinding;
import br.com.junio.castgroup.main.presenter.ListView;
import lombok.Setter;

public class CourseFragment extends AbstractFragment<CoursePresenter> implements ListView.CourseInterface {

    @Setter
    private Course course;
    private AutoCompleteTextView acCategory;
    private TextInputEditText etStudents;
    private TextInputEditText etDateStart;
    private TextInputEditText etDateEnd;
    private TextInputEditText etDesc;
    private LoadingButton btAdd;
    private TextInputLayout lyDesc;
    private TextInputLayout lyDateStart;
    private TextInputLayout lyDateEnd;
    private TextInputLayout lyStudents;
    private TextInputLayout lyCategory;
    private long category;

    @NotNull
    public static Fragment newInstance(ListView listView, CoursePresenter presenter, Course course) {
        CourseFragment fragment = new CourseFragment();
        fragment.setPresenter(presenter);
        fragment.setCourse(course);
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
        FragmentCourseBinding binding = FragmentCourseBinding.inflate(inflater, container, false);

        btAdd = binding.btAdd;

        acCategory = binding.acCategory;
        etDesc = binding.etDesc;
        etDateStart = binding.etDateStart;
        etDateEnd = binding.etDateEnd;
        etStudents = binding.etStudents;

        lyDesc = binding.lyDesc;
        lyDateStart = binding.lyDateStart;
        lyDateEnd = binding.lyDateEnd;
        lyStudents = binding.lyStudents;
        lyCategory = binding.lyCategory;

        etDesc.addTextChangedListener(new CustomTextWatcher(lyDesc));
        etDateStart.addTextChangedListener(new CustomTextWatcher(lyDateStart));
        etDateEnd.addTextChangedListener(new CustomTextWatcher(lyDateEnd));
        acCategory.addTextChangedListener(new CustomTextWatcher(lyCategory));

        etDateStart.addTextChangedListener(new DateInputMask());
        etDateEnd.addTextChangedListener(new DateInputMask());

        etDesc.setOnFocusChangeListener(new CustomFocusChange(etDesc, lyDesc));
        etDateStart.setOnFocusChangeListener(new CustomFocusChange(etDateStart, lyDateStart));
        etDateEnd.setOnFocusChangeListener(new CustomFocusChange(etDateEnd, lyDateEnd));
        acCategory.setOnFocusChangeListener(new CustomFocusChange(acCategory, lyCategory));

        acCategory.setOnItemClickListener((adapterView, view, i, l) -> {
            Category category = (Category) adapterView.getAdapter().getItem(i);
            this.category = category.getCode();
        });

        btAdd.setOnClickListener(view -> {
            if (course != null) presenter.update(getCourse());
            else presenter.add(getCourse());
        });

        if (course != null) {
            btAdd.setText(getString(R.string.edit));
            setFields();
        }

        return binding.getRoot();
    }

    private void setFields() {
        etDesc.setText(course.getSubjectDesc());
        etDateStart.setText(DateUtil.toString(course.getStartDate()));
        etDateEnd.setText(DateUtil.toString(course.getEndDate()));
        etStudents.setText(course.getStudents() != null ? String.valueOf(course.getStudents()) : "");

        category = course.getFkCategory();
        acCategory.setText(presenter.getCategory(course.getFkCategory()));
    }

    @NotNull
    private Course getCourse() {
        Course course = new Course();
        if (this.course != null)
            course.set_id(this.course.get_id());
        course.setSubjectDesc(getText(etDesc));
        course.setStartDate(DateUtil.toLong(getText(etDateStart)));
        course.setEndDate(DateUtil.toLong(getText(etDateEnd)));
        course.setStudents(getText(etStudents).isEmpty() ? null : Integer.valueOf(getText(etStudents)));
        course.setFkCategory(category);
        return course;
    }

    @Override
    public void onResume() {
        super.onResume();

        presenter.getCategories();
        acCategory.setAdapter(getAdapterOf(presenter.getCategories()));
        validateForm();
    }

    private void validateForm() {
        btAdd.setEnabled(!getText(etDesc).isEmpty() && !getText(etDateStart).isEmpty()
                && !getText(etDateEnd).isEmpty() && !getText(acCategory).isEmpty());
    }

    @Override
    public void onFailureForm(@NotNull Course course) {
        if (course.getStartDate() != 0) lyDateStart.setError(getString(R.string.invalid_date));
        if (course.getEndDate() != 0) lyDateEnd.setError(getString(R.string.invalid_date));
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
        return R.layout.fragment_course;
    }
}