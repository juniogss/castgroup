package br.com.junio.castgroup.main.datasource;

import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.List;

import br.com.junio.castgroup.common.datasource.remote.HTTPClient;
import br.com.junio.castgroup.common.datasource.remote.LocalAPI;
import br.com.junio.castgroup.common.model.CategoriesGET;
import br.com.junio.castgroup.common.model.Category;
import br.com.junio.castgroup.common.model.CategoryGET;
import br.com.junio.castgroup.common.model.Course;
import br.com.junio.castgroup.common.model.CourseGET;
import br.com.junio.castgroup.common.model.CourseObj;
import br.com.junio.castgroup.common.model.CoursesGET;
import br.com.junio.castgroup.common.model.ResponseObj;
import br.com.junio.castgroup.common.presenter.Presenter;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;

public class ListRemoteDataSource implements ListDataSource.Remote {

    private static final String TAG = "ListRemoteDataSource";

    @Override
    public void insertCategory(Category category, Presenter<Category> presenter) {
        HTTPClient.retrofit().create(LocalAPI.class)
                .insertCategory(category)
                .enqueue(new Callback<CategoryGET>() {
                    @Override
                    public void onResponse(@NotNull Call<CategoryGET> call, @NotNull Response<CategoryGET> response) {

                        if (response.isSuccessful()) {
                            CategoryGET body = response.body();
                            if (body != null) {
                                presenter.onSuccess(category);
                            } else
                                presenter.onError("Falha ao inserir categoria no servidor");
                        } else if (response.code() == 400) {
                            Converter<ResponseBody, ResponseObj> converter =
                                    HTTPClient.retrofit().responseBodyConverter(ResponseObj.class, new Annotation[0]);

                            ResponseObj error;

                            try {
                                if (response.errorBody() != null) {
                                    error = converter.convert(response.errorBody());
                                    if (error != null)
                                        presenter.onError(error.getData().get(0).getMsg());

                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        presenter.onComplete();
                    }

                    @Override
                    public void onFailure(@NotNull Call<CategoryGET> call, @NotNull Throwable t) {
                        Log.i(TAG, "onFailure insert category: " + t.getMessage());
                        presenter.onError(t.getMessage());
                        presenter.onComplete();
                    }
                });
    }

    @Override
    public void updateCategory(Category category, Presenter<Category> presenter) {
        HTTPClient.retrofit().create(LocalAPI.class)
                .updateCategory(category.getCode(), category)
                .enqueue(new Callback<CategoriesGET>() {
                    @Override
                    public void onResponse(@NotNull Call<CategoriesGET> call, @NotNull Response<CategoriesGET> response) {
                        if (response.body() != null)
                            presenter.onSuccess(category);
                        else
                            presenter.onError("Falha ao atualizar categoria no servidor");
                        presenter.onComplete();
                    }

                    @Override
                    public void onFailure(@NotNull Call<CategoriesGET> call, @NotNull Throwable t) {
                        Log.i(TAG, "onFailure update category: " + t.getMessage());
                        presenter.onError(t.getMessage());
                        presenter.onComplete();
                    }
                });
    }

    @Override
    public void getCategories(Presenter<List<Category>> presenter) {
        HTTPClient.retrofit().create(LocalAPI.class)
                .getCategories()
                .enqueue(new Callback<CategoriesGET>() {
                    @Override
                    public void onResponse(@NotNull Call<CategoriesGET> call, @NotNull Response<CategoriesGET> response) {
                        Log.i(TAG, "onResponse: " + response.body());
                        if (response.body() != null)
                            presenter.onSuccess(response.body().getData());
                        else
                            presenter.onError("Falha ao buscar categorias no servidor");
                        presenter.onComplete();
                    }

                    @Override
                    public void onFailure(@NotNull Call<CategoriesGET> call, @NotNull Throwable t) {
                        Log.i(TAG, "onFailure get categories: " + t.getMessage());
                        presenter.onError(t.getMessage());
                        presenter.onComplete();
                    }
                });
    }

    @Override
    public void deleteCategory(@NotNull Category category, Presenter<Category> presenter) {
        HTTPClient.retrofit().create(LocalAPI.class)
                .deleteCategory(category.getCode())
                .enqueue(new Callback<Category>() {
                    @Override
                    public void onResponse(@NotNull Call<Category> call, @NotNull Response<Category> response) {
                        if (response.body() != null)
                            presenter.onSuccess(response.body());
                        else
                            presenter.onError("Falha ao deletar categoria no servidor");
                        presenter.onComplete();
                    }

                    @Override
                    public void onFailure(@NotNull Call<Category> call, @NotNull Throwable t) {
                        Log.i(TAG, "onFailure delete category: " + t.getMessage());
                        presenter.onError(t.getMessage());
                        presenter.onComplete();
                    }
                });
    }

    @Override
    public void insertCourse(@NotNull Course course, Presenter<Course> presenter) {
        HTTPClient.retrofit().create(LocalAPI.class)
                .insertCourse(course.getHash())
                .enqueue(new Callback<CourseGET>() {
                    @Override
                    public void onResponse(@NotNull Call<CourseGET> call, @NotNull Response<CourseGET> response) {

                        if (response.isSuccessful()) {
                            CourseGET body = response.body();
                            if (body != null) {
                                presenter.onSuccess(course);
                            } else
                                presenter.onError("Falha ao inserir curso no servidor");
                        } else {
                            Converter<ResponseBody, ResponseObj> converter =
                                    HTTPClient.retrofit().responseBodyConverter(ResponseObj.class, new Annotation[0]);

                            ResponseObj error;

                            try {
                                if (response.errorBody() != null) {
                                    error = converter.convert(response.errorBody());
                                    if (error != null)
                                        presenter.onError(error.getData().get(0).getMsg());

                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        presenter.onComplete();
                    }

                    @Override
                    public void onFailure(@NotNull Call<CourseGET> call, @NotNull Throwable t) {
                        Log.i(TAG, "onFailure insert course: " + t.getMessage());
                        presenter.onError(t.getMessage());
                        presenter.onComplete();
                    }
                });
    }

    @Override
    public void updateCourse(@NotNull Course course, Presenter<Course> presenter) {
        HTTPClient.retrofit().create(LocalAPI.class)
                .updateCourse(course.get_id(), course.getHash())
                .enqueue(new Callback<Course>() {
                    @Override
                    public void onResponse(@NotNull Call<Course> call, @NotNull Response<Course> response) {
                        if (response.body() != null)
                            presenter.onSuccess(response.body());
                        else
                            presenter.onError("Falha ao atualizar curso no servidor");
                        presenter.onComplete();
                    }

                    @Override
                    public void onFailure(@NotNull Call<Course> call, @NotNull Throwable t) {
                        Log.i(TAG, "onFailure update course: " + t.getMessage());
                        presenter.onError(t.getMessage());
                        presenter.onComplete();
                    }
                });
    }

    @Override
    public void deleteCourse(@NotNull Course course, Presenter<Course> presenter) {
        HTTPClient.retrofit().create(LocalAPI.class)
                .deleteCourse(course.get_id())
                .enqueue(new Callback<Course>() {
                    @Override
                    public void onResponse(@NotNull Call<Course> call, @NotNull Response<Course> response) {
                        if (response.body() != null)
                            presenter.onSuccess(response.body());
                        else
                            presenter.onError("Falha ao deletar curso no servidor");
                        presenter.onComplete();
                    }

                    @Override
                    public void onFailure(@NotNull Call<Course> call, @NotNull Throwable t) {
                        Log.i(TAG, "onFailure delete course: " + t.getMessage());
                        presenter.onError(t.getMessage());
                        presenter.onComplete();
                    }
                });
    }

    @Override
    public void getCourses(Presenter<List<CourseObj>> presenter) {
        HTTPClient.retrofit().create(LocalAPI.class)
                .getCourses()
                .enqueue(new Callback<CoursesGET>() {
                    @Override
                    public void onResponse(@NotNull Call<CoursesGET> call, @NotNull Response<CoursesGET> response) {
                        if (response.body() != null)
                            presenter.onSuccess(response.body().getData());
                        else
                            presenter.onError("Falha ao buscar cursos no servidor");
                        presenter.onComplete();
                    }

                    @Override
                    public void onFailure(@NotNull Call<CoursesGET> call, @NotNull Throwable t) {
                        Log.i(TAG, "onFailure get courses: " + t.getMessage());
                        presenter.onError(t.getMessage());
                        presenter.onComplete();
                    }
                });
    }

}
