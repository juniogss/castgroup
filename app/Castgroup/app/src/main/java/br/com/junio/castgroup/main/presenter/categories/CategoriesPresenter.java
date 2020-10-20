package br.com.junio.castgroup.main.presenter.categories;

import android.content.Context;

import androidx.appcompat.app.AlertDialog;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import br.com.junio.castgroup.common.adapter.CategoryItem;
import br.com.junio.castgroup.common.model.Category;
import br.com.junio.castgroup.common.presenter.Presenter;
import br.com.junio.castgroup.main.datasource.ListDataSource;
import br.com.junio.castgroup.main.presenter.ListView;

public class CategoriesPresenter implements Presenter<Category>, CategoryListener {

    private final ListDataSource.Remote remoteDataSource;
    private ListView listView;
    private ListView.CategoriesInterface view;

    public CategoriesPresenter(ListDataSource.Remote remoteDataSource) {
        this.remoteDataSource = remoteDataSource;
    }

    public void setView(ListView listView, ListView.CategoriesInterface view) {
        this.listView = listView;
        this.view = view;
    }

    public void add(Category category) {
        listView.showProgressBar();
        remoteDataSource.insertCategory(category, this);
    }

    @Override
    public void onSuccess(Category response) {
        listView.showCourses();
        listView.onBackPressed();
    }

    @Override
    public void onError(String message) {
    }

    @Override
    public void onComplete() {
        listView.hideProgressBar();
    }

    public void getRemoteCategories() {
        listView.showProgressBar();
        remoteDataSource.getCategories(new RemoteCategoriesCallback());
    }

    @Override
    public void onEditClick(Category category) {
        listView.editCategory(category);
    }

    @Override
    public void onDeleteClick(Category category) {
        new AlertDialog.Builder((Context) view)
                .setTitle("Excluir")
                .setMessage("Tem certeza que deseja excluir?")
                .setPositiveButton("Sim", (dialogInterface, i) -> remoteDataSource.deleteCategory(category, new RemoteDeleteCallback()))
                .setNegativeButton("Não", null)
                .show();
    }

    private class RemoteCategoriesCallback implements Presenter<List<Category>> {

        @Override
        public void onSuccess(List<Category> categories) {
            showCategories(categories);
        }

        @Override
        public void onError(String message) {
        }

        @Override
        public void onComplete() {

        }
    }

    private class RemoteDeleteCallback implements Presenter<Category> {

        @Override
        public void onSuccess(Category category) {
            getRemoteCategories();
        }

        @Override
        public void onError(String message) {
        }

        @Override
        public void onComplete() {

        }
    }

    private void showCategories(@NotNull List<Category> categories) {
        listView.hideProgressBar();
        List<CategoryItem> items = new ArrayList<>();

        for (Category category : categories)
            items.add(new CategoryItem(category, this));

        view.showCategories(items);
    }

}
