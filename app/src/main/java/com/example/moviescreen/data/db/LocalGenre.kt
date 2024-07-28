package com.example.moviescreen.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.moviescreen.data.models.Genre

@Entity(tableName = "Genre")
data class LocalGenre(
    @PrimaryKey
    val id: Int,
    var name: String,
    var isCheck: Boolean
) {
    object Mapper {
        fun from(obj: Genre): LocalGenre {
            return obj.run {
                LocalGenre(id = id, name = name, isCheck = isCheck)
            }
        }
    }
}

@Dao
interface LocalGenreCache {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(genre: List<LocalGenre>)

    @Update
    fun updateGenre(genre: LocalGenre)

    @Query("SELECT * FROM Genre WHERE isCheck = :slectedGener")
    fun getFavGenerList(slectedGener: Boolean): LiveData<List<LocalGenre>>

    @Query("SELECT * FROM Genre")
    fun getGenerList(): LiveData<List<LocalGenre>>

    @Query("SELECT COUNT(*) FROM Genre")
    fun getGenreCount(): Int
}
