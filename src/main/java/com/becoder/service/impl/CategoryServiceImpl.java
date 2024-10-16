package com.becoder.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.becoder.dto.CategoryDto;
import com.becoder.dto.CategoryReponse;
import com.becoder.entity.Category;
import com.becoder.exception.ResourceNotFoundException;
import com.becoder.repository.CategoryRepository;
import com.becoder.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryRepository categoryRepo;

	@Autowired
	private ModelMapper mapper;

	@Override
	public Boolean saveCategory(CategoryDto categoryDto) {

//		Category category = new Category();
//		category.setName(categoryDto.getName());
//		category.setDescription(categoryDto.getDescription());
//		category.setIsActive(categoryDto.getIsActive());

		Category category = mapper.map(categoryDto, Category.class);

		if (ObjectUtils.isEmpty(category.getId())) 
		{
			category.setIsDeleted(false);
			category.setCreatedBy(1);
			category.setCreatedOn(new Date());
		} else {
			updateCategory(category);
		}

		Category saveCategory = categoryRepo.save(category);
		if (ObjectUtils.isEmpty(saveCategory)) 
		{
			return false;
		}
		return true;
	}
	
	private void updateCategory(Category category) {
		Optional<Category> findById = categoryRepo.findById(category.getId());
		
		if (findById.isPresent()) 
		{
			Category existCategory = findById.get();
			category.setIsDeleted(existCategory.getIsDeleted());
			category.setCreatedBy(existCategory.getCreatedBy());
			category.setCreatedOn(existCategory.getCreatedOn());
			
			
			category.setUpdatedBy(1);
			category.setUpdatedOn(new Date());
		}
	}

	@Override
	public List<CategoryDto> getAllCategory() {
		List<Category> categories = categoryRepo.findAll();

		List<CategoryDto> categoryDtoList = categories.stream().map(cat -> mapper.map(cat, CategoryDto.class)).toList();

		return categoryDtoList;
	}

	@Override
	public List<CategoryReponse> getActiveCategory() {

		List<Category> categories = categoryRepo.findByIsActiveTrueAndIsDeletedFalse();
		List<CategoryReponse> categoryList = categories.stream().map(cat -> mapper.map(cat, CategoryReponse.class))
				.toList();
		return categoryList;
	}
	
	

//	@Override
//	public CategoryDto getCategoryById(Integer id) {
//
//		Optional<Category> findByCatgeory = categoryRepo.findByIdAndIsDeletedFalse(id);
//
//		if (findByCatgeory.isPresent()) {
//			Category category = findByCatgeory.get();
//			return mapper.map(category, CategoryDto.class);
//		}
//		return null;
//	}
	
	@Override
	public CategoryDto getCategoryById(Integer id) throws Exception {

		Category category = categoryRepo.findByIdAndIsDeletedFalse(id)
				.orElseThrow(() -> new ResourceNotFoundException("Category not found with id=" + id));

		if (!ObjectUtils.isEmpty(category)) {
			category.getName().toUpperCase();
			return mapper.map(category, CategoryDto.class);
		}
		return null;
	}

	@Override
	public Boolean deleteCategory(Integer id) {
		Optional<Category> findByCatgeory = categoryRepo.findById(id);

		if (findByCatgeory.isPresent()) {
			Category category = findByCatgeory.get();
			category.setIsDeleted(true);
			categoryRepo.save(category);
			return true;
		}
		return false;
	}
	
	

	@Override
	public List<CategoryReponse> getInactiveCategory() {

	    List<Category> categories = categoryRepo.findByIsActiveFalseAndIsDeletedFalse();
	    List<CategoryReponse> categoryList = categories.stream().map(cat -> mapper.map(cat, CategoryReponse.class))
	            .toList();
	    return categoryList;
	}

	
}