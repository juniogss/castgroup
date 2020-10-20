package br.com.junio.castgroup.main.presenter.list;

import br.com.junio.castgroup.common.model.Course;

public interface CourseListener {
    void onEditClick(Course course);

    void onDeleteClick(Course course);
}