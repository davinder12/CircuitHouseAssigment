package com.example.moviescreen.data.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Entity(tableName = "MovieList")
data class LocalMovie(
    @PrimaryKey
    val id: Int,
    val adult: Boolean,
    val backdrop_path: String?,
    val original_language: String,
    val original_title: String,
    val overview: String,
    val popularity: Double,
    val poster_path: String,
    val release_date: String,
    val title: String,
    val video: Boolean?? ,
    val vote_average: Double,
    val vote_count: Int,
    var isCheck :Boolean = false,
    var pageSize : Int
) {
    object Mapper {
        fun from(page :Int, obj: com.example.moviescreen.data.models.Result): LocalMovie {
            return obj.run {
                LocalMovie(id = id,
                 adult = adult,
                 backdrop_path = backdrop_path ?: "" ,
                 original_language = original_language,
                 original_title = original_title,
                 overview = overview ,
                 popularity = popularity,
                 poster_path = poster_path ,
                 release_date= release_date,
                 title = title,
                 video = video,
                 vote_average = vote_average,
                 vote_count = vote_count, pageSize = page)
            }
        }
    }
}

@Dao
interface LocalMovieCache {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(genre: List<LocalMovie>)

    @Update
    fun updateRecord(movie:  LocalMovie)

    @Query("SELECT * FROM MovieList WHERE pageSize = :pageSize")
    fun getMovieList(pageSize : Int): List<LocalMovie>

    @Query("SELECT * FROM MovieList WHERE isCheck = :slectedGener")
    fun getFavMoiveList(slectedGener: Boolean): LiveData<List<LocalMovie>>

}
