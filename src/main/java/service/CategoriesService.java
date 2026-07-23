package service;

import dto.response.CategoriesResponse;
import dto.response.Category;

import java.util.List;

public class CategoriesService extends BaseService {
    private static final String BASE_URL = "https://quizapi.io/api/v1/categories";
    private List<Category> categoriesResponseData;

    private static final CategoriesService INSTANCE = new CategoriesService();

    private CategoriesService() {
        super();
    }

    public static CategoriesService getInstance() {
        return INSTANCE;
    }

    public List<Category> getCategories() {
        if (categoriesResponseData == null || categoriesResponseData.isEmpty()){
            this.categoriesResponseData = get(BASE_URL, CategoriesResponse.class).data().data();
        }

        return this.categoriesResponseData;
    }
}