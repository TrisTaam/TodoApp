package me.dev.todoapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import me.dev.todoapp.data.base.model.Category

@Entity(tableName = "category")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String
) {
    companion object {
        fun from(category: Category): CategoryEntity {
            return CategoryEntity(
                id = category.id,
                name = category.name
            )
        }
    }

    fun toCategory(): Category {
        return Category(
            id = this.id,
            name = this.name
        )
    }
}
