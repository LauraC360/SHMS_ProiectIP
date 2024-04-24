DROP TABLE db_example.recipe_image_list ;
DROP TABLE db_example.recipe_keywords ;
DROP TABLE db_example.recipe_instructions_list;
DROP TABLE db_example.recipe_ingredients_map;
DROP TABLE db_example.recipe_steps;
DROP TABLE db_example.recipe_printable_ingredients;
	

DROP TABLE db_example.RECIPES;



SELECT * FROM db_example.RECIPES ;
SELECT * FROM db_example.recipe_instructions_list;
SELECT * FROM db_example.recipe_printable_ingredients;
SELECT * FROM db_example.recipe_ingredients_map;


alter table db_example.RECIPES modify column description longtext;
commit;

SELECT * from db_example.recipe_recipe_ingredient_parts_list;

select * from db_example.recipe_recipe_ingredients_map;
select * from db_example.recipes where recipe_id = 2;

ROLLBACK;