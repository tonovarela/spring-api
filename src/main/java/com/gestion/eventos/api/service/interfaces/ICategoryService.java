package com.gestion.eventos.api.service.interfaces;


import com.gestion.eventos.api.domain.Category;

import java.util.List;

public interface ICategoryService {
    List<Category> findAll();
    Category findById(Long id);
    Category create(Category category);
    Category update(Long id, Category category);
    void deleteById(Long id);

}



