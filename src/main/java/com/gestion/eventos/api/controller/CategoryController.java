package com.gestion.eventos.api.controller;

import com.gestion.eventos.api.dto.CategoryDTO;
import com.gestion.eventos.api.mapper.CategoryMapper;
import com.gestion.eventos.api.service.implementation.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriBuilder;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
@Slf4j
public class CategoryController {
    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;


    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<List<CategoryDTO>> getAllCategories(){
        log.debug("GET /api/v1/categories - obteniendo todas las categorías");
        var categories= categoryService.findAll();
        log.debug("Categorías encontradas: {}", categories.size());
        return ResponseEntity.ok(categories
                .stream()
                .map(categoryMapper::toDTO)
                .collect(Collectors.toList()));


    }
    @GetMapping("{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long id){
        log.debug("GET /api/v1/categories/{} - buscando categoría", id);
        var category = categoryService.findById(id);
        var categoryDTO = categoryMapper.toDTO(category);
        log.debug("Categoría encontrada: {}", categoryDTO);
        return ResponseEntity.ok(categoryDTO);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CategoryDTO categoryDTO){
        log.debug("POST /api/v1/categories - creando categoría: {}", categoryDTO);
        var categoryToCreate = categoryMapper.toEntity(categoryDTO);
        var createdCategory = categoryService.create(categoryToCreate);
        log.debug("Categoría creada con id={}", createdCategory.getId());
        return  new ResponseEntity<>(categoryMapper.toDTO(createdCategory), HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Long id,@Valid @RequestBody CategoryDTO categoryDTO){
        log.debug("PUT /api/v1/categories/{} - actualizando categoría con: {}", id, categoryDTO);
        var categoryToUpdate = categoryMapper.toEntity(categoryDTO);
        var updatedCategory = categoryService.update(id, categoryToUpdate);
        log.debug("Categoría {} actualizada", id);
        return new ResponseEntity<>(categoryMapper.toDTO(updatedCategory), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id){
        log.debug("DELETE /api/v1/categories/{} - eliminando categoría", id);
        categoryService.deleteById(id);
        log.debug("Categoría {} eliminada", id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
