package com.gestion.eventos.api.service.implementation;

import com.gestion.eventos.api.domain.Category;
import com.gestion.eventos.api.exception.ResouceNotFoundException;
import com.gestion.eventos.api.repository.ICategoryRepository;
import com.gestion.eventos.api.service.interfaces.ICategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService implements ICategoryService {
    private final ICategoryRepository categoryRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Category> findAll() {
        log.debug("findAll() - consultando todas las categorías");
        List<Category> categories = categoryRepository.findAll();
        log.debug("findAll() - categorías recuperadas: {}", categories.size());
        return categories;
    }

    @Override
    @Transactional(readOnly = true)
    public Category findById(Long id) {
        log.debug("findById() - buscando categoría con id={}", id);
        return find(id);
    }

    @Override
    @Transactional()
    public Category create(Category category) {
        log.debug("create() - creando categoría: {}", category);
        category.setId(null);
        Category createdCategory = categoryRepository.save(category);
        log.debug("create() - categoría creada con id={}", createdCategory.getId());
        return createdCategory;
    }

    @Override
    @Transactional()
    public Category update(Long id, Category category) {
        log.debug("update() - actualizando categoría id={} con: {}", id, category);
        Category existingCategory = find(id);
        existingCategory.setDescription(category.getDescription());
        existingCategory.setName(category.getName());
        Category updatedCategory = categoryRepository.save(existingCategory);
        log.debug("update() - categoría id={} actualizada", id);
        return updatedCategory;
    }



    @Override
    @Transactional()
    public void deleteById(Long id) {
        log.debug("deleteById() - eliminando categoría id={}", id);
        Category category = find(id);
        categoryRepository.delete(category);
        log.debug("deleteById() - categoría id={} eliminada", id);
    }



    private Category find(Long id) {
        return categoryRepository.findById(id).orElseThrow(
                ()-> {
                    log.debug("find() - categoría no encontrada con id={}", id);
                    return new ResouceNotFoundException("Categoria no encontrada con id: "+id);
                }
        );
    }
}
