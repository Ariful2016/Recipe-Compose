package bd.safety.recipecompose.ui.screens.mainScreen

import bd.safety.recipecompose.data.database.entities.FavoritesEntity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import bd.safety.recipecompose.models.FoodRecipe
import bd.safety.recipecompose.models.Result
import bd.safety.recipecompose.util.Constants.Companion.API_KEY
import bd.safety.recipecompose.util.NetworkResult
import bd.safety.recipecompose.viewmodels.MainViewModel
import coil.compose.AsyncImage
import androidx.compose.foundation.lazy.items


@Composable
fun MainScreen(navController: NavController, viewModel: MainViewModel = hiltViewModel()) {
    val recipesResponse by viewModel.recipesResponse.observeAsState()
    val searchedRecipesResponse by viewModel.searchedRecipesResponse.observeAsState()
    val favoriteRecipes by viewModel.readFavoriteRecipes.observeAsState(emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
            Button(onClick = {
                viewModel.getRecipes(
                    mapOf(
                        "apiKey" to API_KEY,
                        "number" to "10",
                        "type" to "main course"
                    )
                )
            }) {
                Text("Get Recipes")
            }
            Button(onClick = {
                viewModel.searchRecipes(
                    mapOf(
                        "apiKey" to API_KEY,
                        "query" to "chicken",
                        "number" to "50"
                    )
                )
            }) {
                Text("Search Chicken Recipes")
            }
        }
        Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
            Button(onClick = { navController.navigate("favorites") }) {
                Text("Favorites")
            }
            Button(onClick = { navController.navigate("food_joke") }) {
                Text("Food Joke")
            }
        }
        when (val response = recipesResponse) {
            is NetworkResult.Loading<FoodRecipe> -> CircularProgressIndicator()
            is NetworkResult.Error<FoodRecipe> -> Text("Error: ${response.message}", color = MaterialTheme.colorScheme.error)
            is NetworkResult.Success<FoodRecipe> -> RecipeList(response.data?.results.orEmpty(), favoriteRecipes, viewModel)
            null -> Unit
        }
        when (val response = searchedRecipesResponse) {
            is NetworkResult.Loading<FoodRecipe> -> CircularProgressIndicator()
            is NetworkResult.Error<FoodRecipe> -> Text("Error: ${response.message}", color = MaterialTheme.colorScheme.error)
            is NetworkResult.Success<FoodRecipe> -> RecipeList(response.data?.results.orEmpty(), favoriteRecipes, viewModel)
            null -> Unit
        }
    }
}

@Composable
fun RecipeList(recipes: List<Result>, favoriteRecipes: List<FavoritesEntity>, viewModel: MainViewModel) {
    LazyColumn {
        items(recipes) { recipe ->
            val isFavorite = favoriteRecipes.any { it.result.recipeId == recipe.recipeId }
            RecipeItem(recipe, isFavorite) {
                if (isFavorite) {
                    viewModel.deleteFavoriteRecipe(FavoritesEntity(result = recipe))
                } else {
                    viewModel.insertFavoriteRecipe(FavoritesEntity(result = recipe))
                }
            }
        }
    }
}

@Composable
fun RecipeItem(recipe: Result, isFavorite: Boolean, onFavoriteClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = recipe.image,
                contentDescription = recipe.title,
                modifier = Modifier.size(80.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(recipe.title, style = MaterialTheme.typography.titleMedium)
                Text("Ready in ${recipe.readyInMinutes} mins", style = MaterialTheme.typography.bodySmall)
                Text(if (recipe.vegan) "Vegan" else "Non-Vegan", style = MaterialTheme.typography.bodySmall)
            }
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = onFavoriteClick) {
                Icon(
                    imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = "Favorite",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}