package bd.safety.recipecompose.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import bd.safety.recipecompose.models.FoodRecipe
import bd.safety.recipecompose.util.Constants.Companion.RECIPES_TABLE

@Entity(tableName = RECIPES_TABLE)
class RecipesEntity(
    var foodRecipe: FoodRecipe
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0
}