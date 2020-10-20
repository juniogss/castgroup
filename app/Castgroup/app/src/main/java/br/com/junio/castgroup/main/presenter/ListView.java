package br.com.junio.castgroup.main.presenter;

import java.util.List;

import br.com.junio.castgroup.common.adapter.CategoryItem;
import br.com.junio.castgroup.common.adapter.CourseItem;
import br.com.junio.castgroup.common.model.Category;
import br.com.junio.castgroup.common.model.Course;
import br.com.junio.castgroup.common.view.View;
import br.com.junio.castgroup.main.presenter.filter.FilterListener;

public interface ListView extends View {

    void showCourses();

    void showCourses(List<CourseItem> courses);

    void showFilterDialog(FilterListener listener);

    void editCourse(Course course);

    void editCategory(Category category);

    void showCategories();

    void onBackPressed();

    void onFailure(String message);

    interface CourseInterface {
        void onFailureForm(Course course);

        void onFailureInsert(String message);
    }

    interface CategoryInterface {
        void onFailureForm(Category category);

        void onFailureInsert(String message);
    }

    interface CategoriesInterface {
        void showCategories(List<CategoryItem> courses);
    }
}
