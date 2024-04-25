
import pandas as pd
from sklearn.metrics.pairwise import cosine_similarity
import numpy as np

#Pas 1: ranking retete(poate fi nr stele, nr accesari, etc)
data = pd.DataFrame({
    'user': ['User1', 'User2', 'User3', 'User4', 'User5'],
    'Recipe_A': [5, 3, 4, 2, 1],
    'Recipe_B': [4, 2, 5, 3, 1],
    'Recipe_C': [2, 3, 4, 1, 5],
    'Recipe_D': [5, 1, 2, 4, 3]
})

datacpy=data.copy()

#pas 2 drop column user
user_data = data.set_index('user').T
print("Datele utilizatorilor:")
print(user_data)

# Calculate the cosine similarity between users
user_similarity = cosine_similarity(user_data.T) #transpus
user_similarity_df = pd.DataFrame(user_similarity, index=user_data.columns, columns=user_data.columns)


print("\nMatrice similaritate user:")
print(user_similarity_df)

# User 1 cu User 4 : 6/4 = 1.5
# User 1 cu User 2 : 9/4 = 2.25
# User 1 cu User 3 : 7/4 = 1.75
# User 1 cu User 5 : 12/4 = 3

# User 2 cu User 1 : 9/4 = 2.25
# User 2 cu User 3 : 6/4 = 1.5
# User 2 cu User 4 : 7/4 = 1.75
# User 2 cu User 5 : 7/4= = 1.75


# Find the most similar user to User 1
target_user = 'User1'
similar_users = user_similarity_df[target_user].sort_values(ascending=False)
similar_users = similar_users[similar_users.index != target_user]

print("\nUtilizatori similari cu User1:")
print(similar_users)


# Find the most similar users for each user
similar_users = user_similarity_df.apply(lambda x: x.sort_values(ascending=False).index[1:3], axis=1)
print("\nUtilizatori similari:")
print(similar_users)

# pas 2 copy data
data = datacpy.copy()
recipes = data.drop(columns=['user'])

# pas 3: calcul cosine similarity
recipe_similarity = cosine_similarity(recipes.T)  # transpus
recipe_similarity_df = pd.DataFrame(recipe_similarity, index=recipes.columns, columns=recipes.columns)

# matrice similaritate item-item
print("\nMatrice similaritate reteta:")
print(recipe_similarity_df)

# pas 4: retete similare retetei A
target_recipe = 'Recipe_A'
similar_recipes = recipe_similarity_df[target_recipe].sort_values(ascending=False)

# top n recomandari
n=2
recommended_recipes = similar_recipes[1:1+n]

print("\nRetete similare:")
print(recommended_recipes)


# RECOMANDARI RETETE

target_user = 'User2'

# Replace the target user's ratings of 3 with NaN
target_user_tried_recipes = user_data[target_user].replace(3, np.nan)
target_user_tried_recipes = target_user_tried_recipes.dropna()
print("\nRetetele incercate de User2:")
print(target_user_tried_recipes)


# Mergem la top 2 useri similari, conform lor sa selectam una dintre cele 2 neincercate de User2 si sa o recomandam
similar_users = user_similarity_df[target_user].sort_values(ascending=False)
similar_users = similar_users[similar_users.index != target_user][:2]
print("\nUtilizatori similari cu User2:")
print(similar_users)



# Find the untried recipes for target user that are rated high by similar users

# Find the untried recipes for target user
untried_recipes = user_data[target_user][user_data[target_user] == 3]
untried_recipes = untried_recipes.index

print("\nRetetele neincercate de User2:")
print(untried_recipes)

# Sort untried_recipes by the average rating of similar users

# For each untried recipe, calculate the average review given by the most similar users
average_reviews = {}
for recipe in untried_recipes:
    reviews = []
    for user in similar_users.index:
        review = user_data.loc[recipe, user]
        if not np.isnan(review):
            reviews.append(review)
    print("\nReviews:")
    print(reviews)
    print("\n")
    print(f"Reteta: {recipe}")
    if reviews:
        average_reviews[recipe] = sum(reviews) / len(reviews)

print("\nAverage reviews of untried recipes by similar users:")


# COMPUTE RECOMMENDED RECIPES FOR USER2
recommended_recipes = pd.Series(average_reviews).sort_values(ascending=False)
print("\nRetete recomandate pentru User2:")
print(recommended_recipes)
