package com.tiendajava.service;

import java.util.List;

import com.google.gson.Gson;
import com.tiendajava.model.ApiResponse;
import com.tiendajava.model.Category;
import com.tiendajava.repository.CategoryRepository;

public class CategoryService {

    private final CategoryRepository repository = new CategoryRepository();
    private final Gson gson = new Gson();

    public ApiResponse<List<Category>> getAllCategories() {
        return repository.getAllCategories();
    }

    public ApiResponse<Category> getCategoryById(int id) {
        return repository.getCategoryById(id);
    }

    public ApiResponse<Category> createCategory(Category category) {
        String json = gson.toJson(category);
        return repository.createCategory(json);
    }

    public ApiResponse<Category> updateCategory(int id, Category category) {
        String json = gson.toJson(category);
        return repository.updateCategory(id, json);
    }

    public ApiResponse<String> deleteCategory(int id) {
        return repository.deleteCategory(id);
    }
}
