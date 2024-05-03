
DROP TABLE IF EXISTS `recipe_printable_ingredients`;
DROP TABLE IF EXISTS `recipe_keywords`;
DROP TABLE IF EXISTS `recipe_instructions_list`;
DROP TABLE IF EXISTS `recipe_ingredients`;
DROP TABLE IF EXISTS `recipe_image_list`;

DROP TABLE IF EXISTS `recipes`;
CREATE TABLE `recipes` (
  `id` int NOT NULL,
  `author_id` int DEFAULT NULL,
  `author_name` varchar(255) DEFAULT NULL,
  `calories` float DEFAULT NULL,
  `carbohydrate_content` float DEFAULT NULL,
  `recipe_category` varchar(255) DEFAULT NULL,
  `cholesterol_content` float DEFAULT NULL,
  `cook_time` decimal(21,0) DEFAULT NULL,
  `date_published` varchar(255) DEFAULT NULL,
  `description` longtext,
  `fat_content` float DEFAULT NULL,
  `fiber_content` float DEFAULT NULL,
  `prep_time` decimal(21,0) DEFAULT NULL,
  `protein_content` float DEFAULT NULL,
  `servings` float DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `yield` varchar(255) DEFAULT NULL,
  `review_count` float DEFAULT NULL,
  `saturated_fat_content` float DEFAULT NULL,
  `sodium_content` float DEFAULT NULL,
  `sugar_content` float DEFAULT NULL,
  `total_time` decimal(21,0) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
CREATE TABLE `recipe_printable_ingredients` (
  `recipe_id` int NOT NULL,
  `printable_ingredients` varchar(255) DEFAULT NULL,
  KEY `recipe_printing` (`recipe_id`),
  CONSTRAINT `recipe_printing` FOREIGN KEY (`recipe_id`) REFERENCES `recipes` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



CREATE TABLE `recipe_keywords` (
  `recipe_id` int NOT NULL,
  `keywords` varchar(255) DEFAULT NULL,
  KEY `recipe_kw` (`recipe_id`),
  CONSTRAINT `recipe_kw` FOREIGN KEY (`recipe_id`) REFERENCES `recipes` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



CREATE TABLE `recipe_instructions_list` (
  `recipe_id` int NOT NULL,
  `instructions` varchar(255) DEFAULT NULL,
  `instructions_list_key` int NOT NULL,
  PRIMARY KEY (`recipe_id`,`instructions_list_key`),
  CONSTRAINT `recipe_instrlist` FOREIGN KEY (`recipe_id`) REFERENCES `recipes` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



CREATE TABLE `recipe_ingredients` (
  `recipe_id` int NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `type` tinyint DEFAULT NULL,
  `value` float DEFAULT NULL,
  KEY `recipe_ing` (`recipe_id`),
  CONSTRAINT `recipe_ing` FOREIGN KEY (`recipe_id`) REFERENCES `recipes` (`id`),
  CONSTRAINT `recipe_ingredients_chk_1` CHECK ((`type` between 0 and 18))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



CREATE TABLE `recipe_image_list` (
  `recipe_id` int NOT NULL,
  `image_list` varchar(255) DEFAULT NULL,
  KEY `recipe_imglist` (`recipe_id`),
  CONSTRAINT `recipe_imglist` FOREIGN KEY (`recipe_id`) REFERENCES `recipes` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


