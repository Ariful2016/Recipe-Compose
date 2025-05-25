package bd.safety.recipecompose.ui.screens.jokeScreen
import bd.safety.recipecompose.viewmodels.MainViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import bd.safety.recipecompose.util.Constants.Companion.API_KEY
import bd.safety.recipecompose.util.NetworkResult

@Composable
fun FoodJokeScreen(navController: NavController, viewModel: MainViewModel = hiltViewModel()) {
    val foodJokeResponse by viewModel.foodJokeResponse.observeAsState()
    val cachedJokes by viewModel.readFoodJoke.observeAsState(emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { navController.navigate("main") }) {
            Text("Back to Main")
        }
        Button(onClick = { viewModel.getFoodJoke(API_KEY) }) {
            Text("Get New Food Joke")
        }
        when (val response = foodJokeResponse) {
            is NetworkResult.Loading<*> -> CircularProgressIndicator()
            is NetworkResult.Error -> Text("Error: ${response.message}", color = MaterialTheme.colorScheme.error)
            is NetworkResult.Success -> Text(response.data?.text ?: "", style = MaterialTheme.typography.bodyLarge)
            null -> Unit
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text("Cached Jokes:", style = MaterialTheme.typography.titleMedium)
        cachedJokes.forEach { jokeEntity ->
            Text(jokeEntity.foodJoke.text, style = MaterialTheme.typography.bodyMedium)
        }
    }
}