package br.com.junio.castgroup.main.presenter.categories;

import br.com.junio.castgroup.common.model.Category;

public interface CategoryListener {
    void onEditClick(Category category);

    void onDeleteClick(Category category);
}