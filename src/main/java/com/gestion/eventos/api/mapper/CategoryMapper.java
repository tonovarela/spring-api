package com.gestion.eventos.api.mapper;

import com.gestion.eventos.api.domain.Category;
import com.gestion.eventos.api.dto.CategoryDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryDTO toDTO(Category category);
    Category toEntity(CategoryDTO categoryDTO);




}
