package br.com.junio.castgroup.main.presenter.categories;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.GroupieViewHolder;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import br.com.junio.castgroup.R;
import br.com.junio.castgroup.common.adapter.CategoryItem;
import br.com.junio.castgroup.common.view.AbstractFragment;
import br.com.junio.castgroup.databinding.FragmentCategoriesBinding;
import br.com.junio.castgroup.main.presenter.ListView;

public class CategoriesFragment extends AbstractFragment<CategoriesPresenter> implements ListView.CategoriesInterface {

    private List<CategoryItem> items = new ArrayList<>();
    private GroupAdapter<GroupieViewHolder> adapter;
    private TextView emptyCourses;

    @NotNull
    public static Fragment newInstance(ListView listView, CategoriesPresenter presenter) {
        CategoriesFragment fragment = new CategoriesFragment();
        fragment.setPresenter(presenter);
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
        FragmentCategoriesBinding binding = FragmentCategoriesBinding.inflate(inflater, container, false);


        emptyCourses = binding.emptyCategories;

        adapter = new GroupAdapter<>();
        binding.rvCategories.setAdapter(adapter);
        binding.rvCategories.setLayoutManager(new LinearLayoutManager(getContext()));

        return binding.getRoot();
    }


    @Override
    public void onResume() {
        super.onResume();
        presenter.getRemoteCategories();
    }

    @Override
    public void showCategories(List<CategoryItem> courses) {
        this.items.clear();
        this.items = courses;
        adapter.clear();
        adapter.addAll(this.items);
        adapter.notifyDataSetChanged();

        if (items.size() == 0) {
            emptyCourses.setVisibility(View.VISIBLE);
        } else {
            emptyCourses.setVisibility(View.GONE);
        }
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_categories;
    }
}