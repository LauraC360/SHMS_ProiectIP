package com.restservice.recipeAndMealPlanning.recipe;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Integer> {

    /**
     *
     * @param keyword : the keyword to look for in a recipes category or keywords; the search is case-insensitive, and it searches for the given keywords in the category string(thus if the category contains a substring of the keyword, it'll be found) and in the keywords list, in the same manner(if any keyword in the list contains the given keyword as a substring, it'll be found)
     * @param pageable: You may insert any available pageable in this repo call, but in the API call, you should use the PageDTO object to create this pageable; a PageDTO object has the following fields:
     *                       Integer pageNo, Integer pageSize, Sort.Direction sortDirection {Sort.Direction.ASC, Sort.Direction.DESC}, String sortByField <nameOfRecipePropriety>
     *                      defaults: pageNo = 0, pageSize = 10, sortDirection = Sort.Direction.ASC, sortByField = "recipeId"
     *                      if you only give some of the parameters, the rest will be set to the default values! <3
     *                      if you give invalid values for Sort.Direction or sortByField, it'll crash(for sortByField, you should get error code 500)
     * @return Page<Recipe>
     */
    @Query("SELECT DISTINCT r FROM Recipe r " +
            "LEFT JOIN r.keywords k " +
            "WHERE LOWER(r.category) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(k) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Recipe> findRecipeByCategoryOrKeywordsQuery(@Param("keyword") String keyword, Pageable pageable);


    Page<Recipe> findRecipeByAuthorId(Integer authorId, Pageable pageable);

}