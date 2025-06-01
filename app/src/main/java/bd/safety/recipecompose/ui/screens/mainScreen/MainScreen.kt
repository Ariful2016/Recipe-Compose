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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight


@Composable
fun MainScreen(navController: NavController, viewModel: MainViewModel = hiltViewModel()) {
    val recipesResponse by viewModel.recipesResponse.observeAsState()
    val searchedRecipesResponse by viewModel.searchedRecipesResponse.observeAsState()
    val favoriteRecipes by viewModel.readFavoriteRecipes.observeAsState(emptyList())

    // Trigger getRecipes when the Composable is first composed
    LaunchedEffect(Unit) {
        viewModel.getRecipes(
            mapOf(
                "apiKey" to API_KEY, // Ensure API_KEY is defined
                "number" to "50",
                "type" to "main course"
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
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
            /*RecipeItem(recipe, isFavorite) {
                if (isFavorite) {
                    viewModel.deleteFavoriteRecipe(FavoritesEntity(result = recipe))
                } else {
                    viewModel.insertFavoriteRecipe(FavoritesEntity(result = recipe))
                }
            }*/
            RecipeCard(
                recipe = recipe,
                isFavorite = isFavorite,
                onFavoriteClick = { // Pass the onFavoriteClick lambda
                    if (isFavorite) {
                        viewModel.deleteFavoriteRecipe(FavoritesEntity(result = recipe))
                    } else {
                        viewModel.insertFavoriteRecipe(FavoritesEntity(result = recipe))
                    }
                }
            )
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

@Composable
fun RecipeCard(
    recipe: Result,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(130.dp) // Fixed height to match the screenshot's proportions
            .padding(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1A1A1A) // Dark background to match the screenshot
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = recipe.image,
                contentDescription = recipe.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .weight(2f) // Take proportional width
                    .fillMaxHeight() // Take maximum height of the card
                    .clip(RoundedCornerShape(8.dp))
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                modifier = Modifier
                    .weight(3f) // Give more space to the text column
                    .fillMaxHeight()
                    .padding(8.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = recipe.title,
                    maxLines = 2,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.W600,
                        color = Color.White // White text to match the screenshot
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Favorite,
                        contentDescription = "Likes",
                        tint = Color.Red, // Red heart to match the screenshot
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = recipe.aggregateLikes.toString(),
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color.White
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.Outlined.Build,
                        contentDescription = "Preparation Time",
                        tint = Color.White, // White clock to match the screenshot
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${recipe.readyInMinutes}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color.White
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    if (recipe.vegan) {
                        Icon(
                            imageVector = Icons.Outlined.Face,
                            contentDescription = "Vegan",
                            tint = Color.White, // White leaf to match the screenshot
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Vegan",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color.White
                            )
                        )
                    }
                }
            }
            IconButton(onClick = onFavoriteClick) {
                Icon(
                    imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = "Favorite",
                    tint = Color.Red // Red heart for the favorite button
                )
            }
        }
    }
}


