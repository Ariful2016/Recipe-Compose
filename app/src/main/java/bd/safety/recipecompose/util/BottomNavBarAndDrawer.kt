package bd.safety.recipecompose.util

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

class BottomNavBarAndDrawer{
    companion object{
        @Composable
        fun BottomNavigationBar(navController: NavController) {
            val items = listOf(
                Screen.Main to "Home",
                Screen.Favorites to "Favorites",
                Screen.FoodJoke to "Joke"
            )
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            NavigationBar {
                items.forEach { (screen, label) ->
                    NavigationBarItem(
                        icon = {
                            when (screen) {
                                Screen.Main -> Icon(Icons.Default.Home, contentDescription = label)
                                Screen.Favorites -> Icon(Icons.Default.Favorite, contentDescription = label)
                                Screen.FoodJoke -> Icon(Icons.Default.Info, contentDescription = label)
                                else -> Icon(Icons.Default.Info, contentDescription = label)
                            }
                        },
                        label = { Text(label) },
                        selected = currentRoute == screen.route,
                        onClick = {
                            if (currentRoute != screen.route) {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                    )
                }
            }
        }

        @Composable
        fun DrawerContent(navController: NavController, currentRoute: String?, onItemClick: () -> Unit) {
            val items = listOf(Screen.Main, Screen.Favorites, Screen.FoodJoke, Screen.About)

            ModalDrawerSheet {
                Spacer(Modifier.height(12.dp))
                items.forEach { screen ->
                    NavigationDrawerItem(
                        icon = {
                            when (screen) {
                                Screen.Main -> Icon(Icons.Default.Home, contentDescription = null)
                                Screen.Favorites -> Icon(Icons.Default.Favorite, contentDescription = null)
                                Screen.FoodJoke -> Icon(Icons.Default.Info, contentDescription = null)
                                Screen.About -> Icon(Icons.Default.Info, contentDescription = null)
                            }
                        },
                        label = { Text(screen.title) },
                        selected = currentRoute == screen.route,
                        onClick = {
                            onItemClick()
                            if (currentRoute != screen.route) {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }
            }
        }
    }


}