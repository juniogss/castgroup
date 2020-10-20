package br.com.junio.castgroup.main.presenter.filter;

import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import br.com.junio.castgroup.R;
import br.com.junio.castgroup.common.component.LoadingButton;
import br.com.junio.castgroup.common.model.Month;
import br.com.junio.castgroup.common.services.DateUtil;
import br.com.junio.castgroup.databinding.FragmentCourseFilterDialogBinding;
import lombok.Setter;

public class CourseFilterDialog extends DialogFragment {

    @Setter
    FilterListener listener;

    private TextInputLayout lyMonth;
    private TextInputLayout lyDay;
    private TextInputLayout lyYear;
    private TextInputEditText etName;
    private AutoCompleteTextView acDay;
    private AutoCompleteTextView acMonth;
    private AutoCompleteTextView acYear;
    private AutoCompleteTextView acSort;

    private int day;
    private String year;
    private String month = "";
    private int sort;
    private boolean dateEnabled;

    Integer[] DAYS = new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15,
            16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31};
    Integer[] YEARS = new Integer[]{2020, 2021, 2022, 2023, 2024, 2025, 2026, 2027};
    String[] SORT = new String[]{"Mais recente", "Mais antigo", "Nomes de A-Z", "Nomes de Z-A"};
    List<Month> months = new ArrayList<>();

    @NotNull
    public static CourseFilterDialog newInstance(FilterListener listener) {
        CourseFilterDialog dialog = new CourseFilterDialog();
        dialog.setListener(listener);
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentCourseFilterDialogBinding binding = FragmentCourseFilterDialogBinding.inflate(inflater, container, false);

        months.add(new Month("01", "Janeiro"));
        months.add(new Month("02", "Fevereiro"));
        months.add(new Month("03", "Março"));
        months.add(new Month("04", "Abril"));
        months.add(new Month("05", "Maio"));
        months.add(new Month("06", "Junho"));
        months.add(new Month("07", "Julho"));
        months.add(new Month("08", "Agosto"));
        months.add(new Month("09", "Setembro"));
        months.add(new Month("10", "Outubro"));
        months.add(new Month("11", "Novembro"));
        months.add(new Month("12", "Dezembro"));

        etName = binding.etName;
        LoadingButton btConfirm = binding.btConfirm;

        acDay = binding.acDay;
        acMonth = binding.acMonth;
        acYear = binding.acYear;
        acSort = binding.acSort;
        lyMonth = binding.lyMonth;
        lyDay = binding.lyDay;
        lyYear = binding.lyYear;
        CheckBox dateCheck = binding.dateCheck;

        acMonth.setOnItemClickListener((adapterView, view, i, l) -> {
            Month item = (Month) adapterView.getAdapter().getItem(i);
            month = item.getValue();
            etName.setText("");
        });

        acDay.setOnItemClickListener((adapterView, view, i, l) -> {
            day = (int) adapterView.getAdapter().getItem(i);
            etName.setText("");
        });

        acYear.setOnItemClickListener((adapterView, view, i, l) -> {
            year = adapterView.getAdapter().getItem(i).toString();
            etName.setText("");
        });

        dateCheck.setChecked(true);
        changeDateState(true);
        dateCheck.setOnCheckedChangeListener((compoundButton, b) -> changeDateState(b));

        acSort.setOnItemClickListener((adapterView, view, i, l) -> sort = i);

        btConfirm.setEnabled(true);
        btConfirm.setOnClickListener(view -> filter());
        binding.btClean.setOnClickListener(view -> clear());

        return binding.getRoot();
    }

    private void changeDateState(boolean state) {
        acDay.setEnabled(!state);
        acMonth.setEnabled(!state);
        acYear.setEnabled(!state);
        lyDay.setClickable(!state);
        lyMonth.setClickable(!state);
        lyYear.setClickable(!state);
        dateEnabled = !state;
    }

    @Override
    public void onResume() {
        super.onResume();

        sort = 0;

        acSort.setText(SORT[0]);

        acDay.setAdapter(getAdapterOf(DAYS));
        acMonth.setAdapter(getAdapterOf(months));
        acYear.setAdapter(getAdapterOf(YEARS));
        acSort.setAdapter(getAdapterOf(SORT));
    }

    public ArrayAdapter<Integer> getAdapterOf(@NotNull Integer[] objects) {
        return new ArrayAdapter<>(requireContext(), R.layout.dropdown_item, objects);
    }

    public ArrayAdapter<String> getAdapterOf(@NotNull String[] objects) {
        return new ArrayAdapter<>(requireContext(), R.layout.dropdown_item, objects);
    }

    @NotNull
    public <T> ArrayAdapter<T> getAdapterOf(List<T> list) {
        return new ArrayAdapter<>(requireContext(), R.layout.dropdown_item, list);
    }

    private void filter() {

        String name = getText(etName).toString();
        String date = String.format(Locale.getDefault(), "%02d", day) + "/" + month + "/" + year;
        date = String.valueOf(DateUtil.toLong(date));

        if (!dateEnabled) date = "";
        else {
            if (day == 0) {
                lyDay.setError("Informe o dia");
                return;
            }
            if (month.equals("")) {
                lyMonth.setError("Informe o mês");
                return;
            }
            if (year.equals("")) {
                lyYear.setError("Informe o ano");
                return;
            }
        }

        listener.onUpdateFilter(name, date, Sort.values()[sort]);

        dismiss();
    }

    private void clear() {
        listener.onUpdateFilter("", "", Sort.values()[sort]);
        dismiss();
    }

    private Editable getText(@NotNull EditText editText) {
        return editText.getText();
    }

}