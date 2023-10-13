package com.example.online_farm.Controller;


import com.example.online_farm.DTO.CategoryDTO;
import com.example.online_farm.DTO.CategoryResponse;
import com.example.online_farm.Entity.Category;
import com.example.online_farm.Entity.ResponseObject;
import com.example.online_farm.Service.CategoryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome this endpoint is not secure";
    }
    @GetMapping("/categories")
    @CrossOrigin
    public CategoryResponse getAll(){
        List<Category> categories = categoryService.getAll();
        List<CategoryDTO> categoryDTOs = categories.stream()
                .map(category -> {
                    CategoryDTO categoryDTO = new CategoryDTO();
                    categoryDTO.set_id(String.valueOf(category.getId()));
                    categoryDTO.setName(category.getName());
                    return categoryDTO;
                })
                .collect(Collectors.toList());

        CategoryResponse response = new CategoryResponse();
        response.setMessage("Lấy categories thành công");
        response.setData(categoryDTOs);

        return response;
    }

    @GetMapping("/all")
    @CrossOrigin
    public Page<Category> getAllCategory(@RequestParam(defaultValue = "1") int page) {
        return categoryService.getAllForCategoryPageable(page-1,6);
    }

    @GetMapping("/allForReal")
    @CrossOrigin
    public List<Category> getRealAllCategory() {
        return categoryService.getAllCategory();
    }

    @GetMapping("/{id}")
    @CrossOrigin
    public Category getCategoryById(@PathVariable int id) {
        return categoryService.getCategoryById(id);
    }

    @PostMapping(value = "/add-category")
    @CrossOrigin
    public ResponseEntity<ResponseObject> addCategory(@RequestBody @Valid CategoryDTO categoryDTO, BindingResult result) {
        ResponseObject ro = new ResponseObject();

        if (result.hasErrors()) {
            Map<String, String> errors = result.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
            ro.setErrorMessages(errors);
            ro.setStatus("fail");
        } else {
            // Tạo đối tượng Category từ CategoryDTO
            Category newCategory = new Category();
            newCategory.setName(categoryDTO.getName());

            // Lưu đối tượng Category vào cơ sở dữ liệu
            Category savedCategory = categoryService.save(newCategory);

            ro.setData(savedCategory);
            ro.setStatus("success");
        }

        return ResponseEntity.ok(ro);
    }



    @PutMapping(value = "/update")
    @CrossOrigin
    public ResponseObject updateCategory(@RequestBody @Valid Category editCategory, BindingResult result, HttpServletRequest request) {

        ResponseObject ro = new ResponseObject();
        if (result.hasErrors()) {

            Map<String, String> errors = result.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
            ro.setErrorMessages(errors);
            ro.setStatus("fail");
            errors = null;

        } else {
            categoryService.update(editCategory);
            ro.setData(editCategory);
            ro.setStatus("success");
        }
        return ro;
    }

    @DeleteMapping("/delete-category/{id}")
    @CrossOrigin
    public String deleteCategory(@PathVariable int id, HttpServletRequest request) {
        categoryService.deleteById(id);
        request.getSession().setAttribute("listCategory", categoryService.getAllCategory());;
        return "Xóa danh mục thành công!";
    }
}
