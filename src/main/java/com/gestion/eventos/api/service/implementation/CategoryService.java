package com.gestion.eventos.api.service.implementation;

import com.gestion.eventos.api.domain.Category;
import com.gestion.eventos.api.exception.ResouceNotFoundException;
import com.gestion.eventos.api.repository.ICategoryRepository;
import com.gestion.eventos.api.service.interfaces.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {
    private final ICategoryRepository categoryRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Category findById(Long id) {
        return find(id);
    }

    @Override
    @Transactional()
    public Category create(Category category) {
        category.setId(null);
        return categoryRepository.save(category);
    }

    @Override
    @Transactional()
    public Category update(Long id, Category category) {
        Category existingCategory = find(id);
        existingCategory.setDescription(category.getDescription());
        existingCategory.setName(category.getName());
        return categoryRepository.save(existingCategory);
    }



    @Override
    @Transactional()
    public void deleteById(Long id) {
        Category category = find(id);
        categoryRepository.delete(category);
    }



    private Category find(Long id) {
        return categoryRepository.findById(id).orElseThrow(
                ()->new ResouceNotFoundException("Categoria no encontrada con id: "+id)
        );
    }
}
