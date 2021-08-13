package com.xuandanh.springbootshop.restapi;

import com.xuandanh.springbootshop.domain.Category;
import com.xuandanh.springbootshop.dto.CategoryDTO;
import com.xuandanh.springbootshop.exception.MyResourceNotFoundException;
import com.xuandanh.springbootshop.repository.CategoryRepository;
import com.xuandanh.springbootshop.service.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class CategoryResources {
    private final CategoryRepository categoryRepository;
    private final CategoryService categoryService;
    private final Logger log = LoggerFactory.getLogger(CategoryResources.class);
    public CategoryRepository getCategoryRepository() {
        return categoryRepository;
    }

    public CategoryService getCategoryService() {
        return categoryService;
    }

    public CategoryResources(CategoryRepository categoryRepository, CategoryService categoryService){
        this.categoryRepository = categoryRepository;
        this.categoryService = categoryService;
    }

    @GetMapping("/category/findOne/{categoriesId}")
    public ResponseEntity<?> getOne(@PathVariable("categoriesId")int categoriesId){
        Optional<CategoryDTO>categoryDTO = categoryService.findOne(categoriesId);
        try{
            if(categoryDTO.isPresent()){
                log.info("show information of a category"+categoryDTO.get());
                return ResponseEntity.ok(categoryDTO);
            }
        }catch (Exception e){
            log.error("Category with id:"+categoriesId + " not exist in system");
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new CategoryDTO());
    }

    @GetMapping("/category/findAll")
    public ResponseEntity<?> getAll(){
        Optional<List<CategoryDTO>>categoryDTOList = categoryService.findAll();
        if(categoryDTOList.isEmpty()){
            log.error("List Category is not exist");
            return ResponseEntity.notFound().build();
        }
        log.info("show list Category"+categoryDTOList);
        return ResponseEntity.ok(categoryDTOList);
    }

    @PostMapping("/category/createCategory")
    public ResponseEntity<?> create(@Valid @RequestBody Category category){
        return ResponseEntity.ok(categoryService.createCategory(category));
    }

    @PutMapping("/category/updateCategory/{categoriesId}")
    public ResponseEntity<?>update(@PathVariable("categoriesId")int categoriesId, @Valid @RequestBody Category category){
        Optional<Category>category1 = categoryRepository.findById(categoriesId);
        try{
            if(category1.isPresent()){
                log.info("Category with id:"+categoriesId+" was update successfully");
                return ResponseEntity.ok(categoryService.updateCategory(category));
            }
        }catch (MyResourceNotFoundException exception){
            log.error("Category with id:"+categoriesId+" not exist in system");
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Category with id:"+categoriesId+" not exist in system", exception);
        }
        return ResponseEntity.ok(new CategoryDTO());
    }

    @DeleteMapping("/category/deleteCategory/{categoriesId}")
    public ResponseEntity<?>delete(@PathVariable("categoriesId")int categoriesId){
        Optional<CategoryDTO>category = categoryService.findOne(categoriesId);
        Map<String,Boolean>response = new HashMap<>();
        try{
            if(category.isPresent()){
                categoryService.deleteCategory(category.get());
                log.info("category with id:"+categoriesId+" was deleted successfully");
                response.put("category with id:"+categoriesId+" was deleted successfully",Boolean.TRUE);
                return ResponseEntity.ok(response);
            }
        }catch (MyResourceNotFoundException exception){
            log.error("Category with id:"+categoriesId+" not exist in system");
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Category with id:"+categoriesId+" not exist in system", exception);
        }
        return ResponseEntity.ok(response);
    }
}
