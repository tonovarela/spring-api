package com.gestion.eventos.api.controller;

import com.gestion.eventos.api.dto.CategoryDTO;
import com.gestion.eventos.api.mapper.CategoryMapper;
import com.gestion.eventos.api.service.implementation.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
public class CategoryController {
    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;


    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<List<CategoryDTO>> getAllCategories(){

        var categories= categoryService.findAll();
        return ResponseEntity.ok(categories
                .stream()
                .map(categoryMapper::toDTO)
                .collect(Collectors.toList()));


    }
    @GetMapping("{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long id){

        var category = categoryService.findById(id);
        return ResponseEntity.ok(categoryMapper.toDTO(category));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CategoryDTO categoryDTO){
        var categoryToCreate = categoryMapper.toEntity(categoryDTO);
        var createdCategory = categoryService.create(categoryToCreate);
        return  new ResponseEntity<>(categoryMapper.toDTO(createdCategory), HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Long id,@Valid @RequestBody CategoryDTO categoryDTO){
        var categoryToUpdate = categoryMapper.toEntity(categoryDTO);
        var updatedCategory = categoryService.update(id, categoryToUpdate);
        return new ResponseEntity<>(categoryMapper.toDTO(updatedCategory), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id){
        categoryService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
