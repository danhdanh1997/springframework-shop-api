package com.xuandanh.springbootshop.service;

import com.xuandanh.springbootshop.domain.Category;
import com.xuandanh.springbootshop.dto.CategoryDTO;
import com.xuandanh.springbootshop.exception.ResourceNotFoundException;
import com.xuandanh.springbootshop.mapper.CategoryMapper;
import com.xuandanh.springbootshop.repository.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    private final CategoryMapper categoryMapper;
    private final CategoryRepository categoryRepository;
    private final Logger log = LoggerFactory.getLogger(CategoryService.class);
    public CategoryMapper getCategoryMapper() {
        return categoryMapper;
    }

    public CategoryRepository getCategoryRepository() {
        return categoryRepository;
    }

    public CategoryService(CategoryMapper categoryMapper, CategoryRepository categoryRepository){
        this.categoryMapper = categoryMapper;
        this.categoryRepository = categoryRepository;
    }

    public Optional<CategoryDTO>findOne(int categoriesId){
        return Optional.ofNullable(categoryMapper
                .categoryToCategoryDTO(categoryRepository
                        .findById(categoriesId)
                        .orElseThrow(()->new ResourceNotFoundException("category with id:"+categoriesId+" not exist"))));
    }

    public Optional<List<CategoryDTO>>findAll(){
        return Optional.ofNullable(categoryMapper.categoryToCategoryDTO(categoryRepository.findAll()));
    }

    public Optional<CategoryDTO>createCategory(Category category){
        Optional<Category>category1 = categoryRepository.findByCategoryName(category);
        try{
            if(category1.isPresent()){
                return Optional.empty();
            }
            return Optional.ofNullable(categoryMapper.categoryToCategoryDTO(categoryRepository.save(category)));
        }catch (Exception e){
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public Optional<CategoryDTO>updateCategory(Category category){
        return Optional.of(categoryRepository
                        .findById(category.getCategoriesId()))
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .map(category1 -> {
                           category1.setCategoriesId(category.getCategoriesId());
                           category1.setCategoriesName(category.getCategoriesName());
                           category1.setLastUpdate(category.getLastUpdate());
                           return categoryMapper.categoryToCategoryDTO(categoryRepository.save(category));
                        });
    }

    public void deleteCategory(CategoryDTO category){
        categoryRepository.findById(category.getCategoriesId())
                        .ifPresent(category1 -> {
                            categoryRepository.delete(category1);
                            log.info("Category was delete successfully");
                        });
    }
}
