package bd.safety.recipecompose.util

sealed class Screen(val route: String, val title: String) {
    object Main : Screen("main", "Home")
    object Favorites : Screen("favorites", "Favorites")
    object FoodJoke : Screen("food_joke", "Food Joke")
    object About : Screen("about", "About")
}