package br.com.junio.castgroup.main.presenter.filter;

public interface FilterListener {
    void onUpdateFilter(String desc, String dateStart, Sort sort);

}