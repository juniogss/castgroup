package br.com.junio.castgroup.main.presenter.course;

import java.util.List;

import br.com.junio.castgroup.common.model.Category;
import br.com.junio.castgroup.common.model.Course;
import br.com.junio.castgroup.common.presenter.Presenter;
import br.com.junio.castgroup.main.datasource.ListDataSource;
import br.com.junio.castgroup.main.presenter.ListView;

public class CoursePresenter implements Presenter<Course> {

    private final ListDataSource.Local localDataSource;
    private final ListDataSource.Remote remoteDataSource;
    private ListView listView;
    private ListView.CourseInterface view;

    public CoursePresenter(ListDataSource.Local localDataSource, ListDataSource.Remote remoteDataSource) {
        this.localDataSource = localDataSource;
        this.remoteDataSource = remoteDataSource;
    }

    public void setView(ListView listView, ListView.CourseInterface view) {
        this.listView = listView;
        this.view = view;
    }

    public void add(Course course) {
        remoteDataSource.insertCourse(course, this);
    }

    public void update(Course course) {
        remoteDataSource.updateCourse(course, this);
    }

    public List<Category> getCategories() {
        return localDataSource.getCategories();
    }

    public String getCategory(long code) {
        return localDataSource.getCategoryBy(code);
    }

    @Override
    public void onSuccess(Course response) {
        listView.showCourses();
        listView.onBackPressed();
    }

    @Override
    public void onError(String message) {
        view.onFailureInsert(message);
    }

    @Override
    public void onComplete() {

    }

}
