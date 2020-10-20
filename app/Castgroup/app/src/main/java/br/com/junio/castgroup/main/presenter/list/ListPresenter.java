package br.com.junio.castgroup.main.presenter.list;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import br.com.junio.castgroup.common.adapter.CourseItem;
import br.com.junio.castgroup.common.model.Category;
import br.com.junio.castgroup.common.model.Course;
import br.com.junio.castgroup.common.model.CourseObj;
import br.com.junio.castgroup.common.presenter.Presenter;
import br.com.junio.castgroup.common.services.DateUtil;
import br.com.junio.castgroup.main.datasource.ListDataSource;
import br.com.junio.castgroup.main.presenter.ListView;
import br.com.junio.castgroup.main.presenter.filter.FilterListener;
import br.com.junio.castgroup.main.presenter.filter.Sort;

public class ListPresenter implements Presenter<List<Course>>, CourseListener, FilterListener {

    private ListView view;
    private final ListDataSource.Local localDataSource;
    private final ListDataSource.Remote remoteDataSource;

    public ListPresenter(ListDataSource.Local localDataSource, ListDataSource.Remote remoteDataSource) {
        this.localDataSource = localDataSource;
        this.remoteDataSource = remoteDataSource;
    }

    public void setView(ListView view) {
        this.view = view;
    }

    public String getCategoryBy(long code) {
        return localDataSource.getCategoryBy(code);
    }

    public void getLocalCourses() {
        view.showProgressBar();
        localDataSource.getCourses(this, "", "", Sort.LAST);
    }

    public void getRemoteCategories() {
        view.showProgressBar();
        remoteDataSource.getCategories(new RemoteCategoriesCallback());
    }

    public void getRemoteCourses() {
        remoteDataSource.getCourses(new RemoteCoursesCallback());
    }

    public void openFilter() {
        view.showFilterDialog(this);
    }

    @Override
    public void onSuccess(@NotNull List<Course> courses) {
        view.hideProgressBar();
        List<CourseItem> items = new ArrayList<>();

        for (Course course : courses)
            items.add(new CourseItem(course, this, this));

        view.showCourses(items);
    }

    @Override
    public void onError(String message) {

    }

    @Override
    public void onComplete() {
        view.hideProgressBar();
    }

    @Override
    public void onEditClick(Course course) {
        view.editCourse(course);
    }

    @Override
    public void onDeleteClick(Course course) {
        remoteDataSource.deleteCourse(course, new RemoteDeleteCallback());
    }

    @Override
    public void onUpdateFilter(String desc, String dateStart, Sort sort) {
        localDataSource.getCourses(this, desc, dateStart, sort);
    }

    public void addCategories() {
    }

    private class RemoteCategoriesCallback implements Presenter<List<Category>> {

        @Override
        public void onSuccess(List<Category> categories) {
            localDataSource.addCategories(categories, new LocalCategoriesCallback());
        }

        @Override
        public void onError(String message) {
            view.onFailure(message);
        }

        @Override
        public void onComplete() {

        }
    }

    private class LocalCategoriesCallback implements Presenter<List<Category>> {

        @Override
        public void onSuccess(List<Category> response) {
            getRemoteCourses();
        }

        @Override
        public void onError(String message) {
            view.onFailure(message);
        }

        @Override
        public void onComplete() {

        }
    }

    private class RemoteCoursesCallback implements Presenter<List<CourseObj>> {

        @Override
        public void onSuccess(@NotNull List<CourseObj> courses) {

            List<Course> courseList = new ArrayList<>();

            for (CourseObj course : courses) {
                Course newCourse = new Course();
                newCourse.set_id(course.get_id());
                newCourse.setSubjectDesc(course.getSubjectDesc());
                newCourse.setStudents(course.getStudents());
                newCourse.setFkCategory(course.getFkCategory());
                newCourse.setStartDate(DateUtil.toLongEnd(course.getStartDate()));
                newCourse.setEndDate(DateUtil.toLongEnd(course.getEndDate()));
                courseList.add(newCourse);
            }

            localDataSource.addCourses(courseList, ListPresenter.this);
        }

        @Override
        public void onError(String message) {
            view.onFailure(message);
        }

        @Override
        public void onComplete() {

        }
    }

    private class LocalDeleteCourse implements Presenter<Void> {

        @Override
        public void onSuccess(Void response) {
            getLocalCourses();
        }

        @Override
        public void onError(String message) {
            view.onFailure(message);
        }

        @Override
        public void onComplete() {

        }
    }

    private class RemoteDeleteCallback implements Presenter<Course> {

        @Override
        public void onSuccess(Course course) {
            getRemoteCategories();
        }

        @Override
        public void onError(String message) {
        }

        @Override
        public void onComplete() {

        }
    }
}
