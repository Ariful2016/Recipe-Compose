package bd.safety.recipecompose.data.database
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import bd.safety.recipecompose.data.database.entities.FavoritesEntity
import bd.safety.recipecompose.data.database.entities.FoodJokeEntity
import bd.safety.recipecompose.data.database.entities.RecipesEntity

@Database(
    entities = [RecipesEntity::class, FavoritesEntity::class, FoodJokeEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(RecipesTypeConverter::class)
abstract class RecipesDatabase: RoomDatabase() {

    abstract fun recipesDao(): RecipesDao

}