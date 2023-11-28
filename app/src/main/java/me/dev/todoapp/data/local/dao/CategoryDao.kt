package me.dev.todoapp.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import me.dev.todoapp.data.local.entity.CategoryEntity

@Dao
interface CategoryDao {
    @Insert(entity = CategoryEntity::class)
    suspend fun insert(categoryEntity: CategoryEntity)

    @Update(entity = CategoryEntity::class)
    suspend fun update(categoryEntity: CategoryEntity)

    @Delete(entity = CategoryEntity::class)
    suspend fun delete(categoryEntity: CategoryEntity)

    @Query("SELECT * FROM category WHERE id = :id")
    fun getCategoryEntity(id: Int): LiveData<CategoryEntity>

    @Query("SELECT * FROM category")
    fun getAllCategoryEntity(): LiveData<List<CategoryEntity>>
}