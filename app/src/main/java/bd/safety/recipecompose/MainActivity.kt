package bd.safety.recipecompose
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import bd.safety.recipecompose.ui.screens.favoriteScreen.FavoritesScreen
import bd.safety.recipecompose.ui.screens.jokeScreen.FoodJokeScreen
import bd.safety.recipecompose.ui.screens.mainScreen.MainScreen
import bd.safety.recipecompose.ui.theme.FoodyTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FoodyTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "main") {
                    composable("main") { MainScreen(navController) }
                    composable("favorites") { FavoritesScreen(navController) }
                    composable("food_joke") { FoodJokeScreen(navController) }
                }
            }
        }
    }
}