package br.com.junio.castgroup.main.datasource;

import java.util.List;

import br.com.junio.castgroup.common.model.Category;
import br.com.junio.castgroup.common.model.Course;
import br.com.junio.castgroup.common.model.CourseObj;
import br.com.junio.castgroup.common.presenter.Presenter;
import br.com.junio.castgroup.main.presenter.filter.Sort;

public interface ListDataSource {

    interface Local {

        void addCourses(List<Course> courses, Presenter<List<Course>> presenter);

        void addCategories(List<Category> categories, Presenter<List<Category>> presenter);

        void getCourses(Presenter<List<Course>> presenter, String name, String date, Sort sort);

        String getCategoryBy(long code);

        List<Category> getCategories();
    }

    interface Remote {
        void insertCategory(Category category, Presenter<Category> presenter);

        void updateCategory(Category category, Presenter<Category> presenter);

        void getCategories(Presenter<List<Category>> presenter);

        void deleteCategory(Category category, Presenter<Category> presenter);

        void insertCourse(Course course, Presenter<Course> presenter);

        void updateCourse(Course course, Presenter<Course> presenter);

        void deleteCourse(Course course, Presenter<Course> presenter);

        void getCourses(Presenter<List<CourseObj>> presenter);

    }
}
