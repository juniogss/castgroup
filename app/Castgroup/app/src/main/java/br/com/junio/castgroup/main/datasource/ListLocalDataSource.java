package br.com.junio.castgroup.main.datasource;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import br.com.junio.castgroup.common.datasource.local.CategorySQL;
import br.com.junio.castgroup.common.datasource.local.CourseSQL;
import br.com.junio.castgroup.common.model.Category;
import br.com.junio.castgroup.common.model.Course;
import br.com.junio.castgroup.common.presenter.Presenter;
import br.com.junio.castgroup.main.presenter.filter.Sort;

public class ListLocalDataSource implements ListDataSource.Local {

    private final CourseSQL courseSQL;
    private final CategorySQL categorySQL;

    public ListLocalDataSource(CategorySQL categorySQL, CourseSQL courseSQL) {
        this.categorySQL = categorySQL;
        this.courseSQL = courseSQL;
    }

    @Override
    public void addCourses(List<Course> courses, Presenter<List<Course>> presenter) {
        boolean insert = courseSQL.insert(courses);
        if (insert) presenter.onSuccess(courses);
        else presenter.onError("Falha ao inserir cursos");
        presenter.onComplete();
    }

    @Override
    public void addCategories(List<Category> categories, Presenter<List<Category>> presenter) {
        courseSQL.delete();
        boolean insert = categorySQL.insert(categories);
        if (insert) presenter.onSuccess(null);
        else presenter.onError("Falha ao inserir categorias");
        presenter.onComplete();
    }

    @Override
    public void getCourses(@NotNull Presenter<List<Course>> presenter, String name, String date, Sort sort) {
        List<Course> list = courseSQL.getListWith(name, date, sort);
        presenter.onSuccess(list);
        presenter.onComplete();
    }

    @Override
    public String getCategoryBy(long code) {
        return categorySQL.getBy(code).getDesc();
    }

    @Override
    public List<Category> getCategories() {
        return categorySQL.getList();
    }
}
