package br.com.junio.castgroup.common.datasource.remote;

import java.util.HashMap;

import br.com.junio.castgroup.common.model.CategoriesGET;
import br.com.junio.castgroup.common.model.Category;
import br.com.junio.castgroup.common.model.CategoryGET;
import br.com.junio.castgroup.common.model.Course;
import br.com.junio.castgroup.common.model.CourseGET;
import br.com.junio.castgroup.common.model.CoursesGET;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface LocalAPI {

    String BASE_URL = "http://192.168.1.184:3333/";

    @Headers("Content-Type: application/json")
    @POST("category")
    Call<CategoryGET> insertCategory(@Body Category category);

    @Headers("Content-Type: application/json")
    @PUT("category/{id}")
    Call<CategoriesGET> updateCategory(@Path("id") long id, @Body Category category);

    @Headers("Content-Type: application/json")
    @DELETE("category/{code}")
    Call<Category> deleteCategory(@Path("code") long code);

    @Headers("Content-Type: application/json")
    @GET("category")
    Call<CategoriesGET> getCategories();

    @Headers("Content-Type: application/json")
    @POST("course")
    Call<CourseGET> insertCourse(@Body HashMap<String, Object> course);

    @Headers("Content-Type: application/json")
    @PUT("course/{id}")
    Call<Course> updateCourse(@Path("id") String id, @Body HashMap<String, Object> course);

    @Headers("Content-Type: application/json")
    @DELETE("course/{id}")
    Call<Course> deleteCourse(@Path("id") String id);

    @Headers("Content-Type: application/json")
    @GET("course")
    Call<CoursesGET> getCourses();
}
