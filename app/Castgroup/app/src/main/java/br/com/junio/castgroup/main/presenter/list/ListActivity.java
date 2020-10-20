package br.com.junio.castgroup.main.presenter.list;

import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.GroupieViewHolder;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import br.com.junio.castgroup.R;
import br.com.junio.castgroup.common.adapter.CourseItem;
import br.com.junio.castgroup.common.datasource.local.CategorySQL;
import br.com.junio.castgroup.common.datasource.local.CourseSQL;
import br.com.junio.castgroup.common.model.Category;
import br.com.junio.castgroup.common.model.Course;
import br.com.junio.castgroup.common.services.InternetConnection;
import br.com.junio.castgroup.common.view.AbstractActivity;
import br.com.junio.castgroup.databinding.ActivityListBinding;
import br.com.junio.castgroup.main.datasource.ListDataSource;
import br.com.junio.castgroup.main.datasource.ListLocalDataSource;
import br.com.junio.castgroup.main.datasource.ListRemoteDataSource;
import br.com.junio.castgroup.main.presenter.ListView;
import br.com.junio.castgroup.main.presenter.categories.CategoriesFragment;
import br.com.junio.castgroup.main.presenter.categories.CategoriesPresenter;
import br.com.junio.castgroup.main.presenter.category.CategoryFragment;
import br.com.junio.castgroup.main.presenter.category.CategoryPresenter;
import br.com.junio.castgroup.main.presenter.course.CourseFragment;
import br.com.junio.castgroup.main.presenter.course.CoursePresenter;
import br.com.junio.castgroup.main.presenter.filter.CourseFilterDialog;
import br.com.junio.castgroup.main.presenter.filter.FilterListener;

public class ListActivity extends AbstractActivity implements ListView {

    private ListPresenter presenter;
    private MenuItem btFilter;
    private MenuItem btAddCourse;
    private MenuItem btAddCategory;
    private MenuItem btListCategories;
    private List<CourseItem> items = new ArrayList<>();
    private GroupAdapter<GroupieViewHolder> adapter;
    private TextView emptyCourses;
    private CoursePresenter coursePresenter;
    private CategoryPresenter categoryPresenter;
    private CategoriesPresenter categoriesPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityListBinding binding = ActivityListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getSupportActionBar() != null)
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);

        emptyCourses = binding.emptyCourses;

        adapter = new GroupAdapter<>();
        binding.rvCourses.setAdapter(adapter);
        binding.rvCourses.setLayoutManager(new LinearLayoutManager(getContext()));

        presenter.addCategories();
        presenter.getLocalCourses();
    }

    @Override
    protected void onInject() {
        CategorySQL categorySQL = new CategorySQL(this);
        CourseSQL courseSQL = new CourseSQL(this);

        ListDataSource.Local localDataSource = new ListLocalDataSource(categorySQL, courseSQL);
        ListDataSource.Remote remoteDataSource = new ListRemoteDataSource();
        presenter = new ListPresenter(localDataSource, remoteDataSource);
        categoryPresenter = new CategoryPresenter(localDataSource, remoteDataSource);
        categoriesPresenter = new CategoriesPresenter(remoteDataSource);
        coursePresenter = new CoursePresenter(localDataSource, remoteDataSource);
        presenter.setView(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (InternetConnection.isConnected(this))
            presenter.getRemoteCategories();
        else presenter.getLocalCourses();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        else if (item.getItemId() == R.id.addCategory) {
            editCategory(null);
        } else if (item.getItemId() == R.id.addCourse) {
            editCourse(null);
        } else if (item.getItemId() == R.id.listCategories) {
            showCategories();
        } else if (item.getItemId() == R.id.filter) {
            presenter.openFilter();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        btFilter = menu.findItem(R.id.filter);
        btAddCategory = menu.findItem(R.id.addCategory);
        btListCategories = menu.findItem(R.id.listCategories);
        btAddCourse = menu.findItem(R.id.addCourse);

        if (items.size() > 0) btFilter.setVisible(true);
        return true;
    }

    @Override
    public void onBackPressed() {
        setTitle(getString(R.string.courses));
        btFilter.setVisible(true);
        btAddCourse.setVisible(true);
        btAddCategory.setVisible(true);
        btListCategories.setVisible(true);

        presenter.getRemoteCategories();

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        /*hide keyboard*/
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        super.onBackPressed();
    }

    @Override
    public void onFailure(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showCourses() {
        presenter.getRemoteCategories();
    }

    @Override
    public void showCourses(@NotNull List<CourseItem> items) {
        this.items.clear();
        this.items = items;
        adapter.clear();
        adapter.addAll(this.items);
        adapter.notifyDataSetChanged();

        if (items.size() == 0) {
            emptyCourses.setVisibility(View.VISIBLE);
            if (btFilter != null) btFilter.setVisible(false);
        } else {
            emptyCourses.setVisibility(View.GONE);
            if (btFilter != null) btFilter.setVisible(true);
        }
    }

    @Override
    public void showFilterDialog(FilterListener listener) {
        FragmentManager fm = getSupportFragmentManager();
        CourseFilterDialog dialog = CourseFilterDialog.newInstance(listener);
        dialog.show(fm, "course_filter");
    }

    @Override
    public void editCourse(Course course) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        String TAG = getString(R.string.fragment);
        Fragment fragment = CourseFragment.newInstance(this, coursePresenter, course);

        setFragment(manager, transaction, TAG, fragment);

        setTitle(course == null ? getString(R.string.add) : getString(R.string.edit));
    }

    @Override
    public void editCategory(Category category) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        String TAG = getString(R.string.fragment);
        Fragment fragment = CategoryFragment.newInstance(this, categoryPresenter, category);

        setFragment(manager, transaction, TAG, fragment);

        setTitle(category == null ? getString(R.string.add) : getString(R.string.edit));
    }

    @Override
    public void showCategories() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        String TAG = getString(R.string.fragment);
        Fragment fragment = CategoriesFragment.newInstance(this, categoriesPresenter);

        setFragment(manager, transaction, TAG, fragment);

        setTitle("Categorias");
    }

    private void setFragment(FragmentManager manager, @NotNull FragmentTransaction transaction, String TAG, Fragment fragment) {
        transaction.replace(R.id.flCourses, fragment, TAG);
        transaction.addToBackStack(TAG);

        btFilter.setVisible(false);
        btAddCourse.setVisible(false);
        btAddCategory.setVisible(false);
        btListCategories.setVisible(false);

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (!manager.isDestroyed())
            transaction.commit();
    }
}