package bd.safety.recipecompose.data.database.entities
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import bd.safety.recipecompose.models.FoodJoke
import bd.safety.recipecompose.util.Constants.Companion.FOOD_JOKE_TABLE

@Entity(tableName = FOOD_JOKE_TABLE)
class FoodJokeEntity(
    @Embedded
    var foodJoke: FoodJoke
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0
}