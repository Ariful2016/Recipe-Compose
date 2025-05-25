package bd.safety.recipecompose.ui.screens.favoriteScreen


import bd.safety.recipecompose.viewmodels.MainViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import bd.safety.recipecompose.ui.screens.mainScreen.RecipeItem

@Composable
fun FavoritesScreen(navController: NavController, viewModel: MainViewModel = hiltViewModel()) {
    val favoriteRecipes by viewModel.readFavoriteRecipes.observeAsState(emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
            Button(onClick = { navController.navigate("main") }) {
                Text("Back to Main")
            }
            Button(onClick = { viewModel.deleteAllFavoriteRecipes() }) {
                Text("Clear Favorites")
            }
        }
        LazyColumn {
            items(favoriteRecipes) { favorite ->
                RecipeItem(favorite.result, true) {
                    viewModel.deleteFavoriteRecipe(favorite)
                }
            }
        }
    }
}