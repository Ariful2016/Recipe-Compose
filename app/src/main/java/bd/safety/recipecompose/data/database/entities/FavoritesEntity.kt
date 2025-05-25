package bd.safety.recipecompose.data.database.entities
import androidx.room.Entity
import androidx.room.PrimaryKey
import bd.safety.recipecompose.models.Result
import bd.safety.recipecompose.util.Constants.Companion.FAVORITE_RECIPES_TABLE

@Entity(tableName = FAVORITE_RECIPES_TABLE)
class FavoritesEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var result: Result
)