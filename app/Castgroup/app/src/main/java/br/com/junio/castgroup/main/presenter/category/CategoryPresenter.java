package br.com.junio.castgroup.main.presenter.category;

import br.com.junio.castgroup.common.model.Category;
import br.com.junio.castgroup.common.presenter.Presenter;
import br.com.junio.castgroup.main.datasource.ListDataSource;
import br.com.junio.castgroup.main.presenter.ListView;

public class CategoryPresenter implements Presenter<Category> {

    private final ListDataSource.Local localDataSource;
    private final ListDataSource.Remote remoteDataSource;
    private ListView listView;
    private ListView.CategoryInterface view;

    public CategoryPresenter(ListDataSource.Local localDataSource, ListDataSource.Remote remoteDataSource) {
        this.localDataSource = localDataSource;
        this.remoteDataSource = remoteDataSource;
    }

    public void setView(ListView listView, ListView.CategoryInterface view) {
        this.listView = listView;
        this.view = view;
    }

    public void add(Category category) {
        listView.showProgressBar();
        remoteDataSource.insertCategory(category, this);
    }

    public void update(Category category) {
        remoteDataSource.updateCategory(category, this);
    }

    @Override
    public void onSuccess(Category response) {
        listView.showCourses();
        listView.onBackPressed();
    }

    @Override
    public void onError(String message) {
        view.onFailureInsert(message);
    }

    @Override
    public void onComplete() {
        listView.hideProgressBar();
    }
}
