package com.dirror.music.room

import androidx.room.TypeConverter
import com.dirror.music.music.standard.data.StandardSongData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class StandardArtistDataConverter {

    @TypeConverter
    fun objectToString(list: ArrayList<StandardSongData.StandardArtistData>): String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun stringToObject(json: String): ArrayList<StandardSongData.StandardArtistData> {
        val listType: Type = object : TypeToken<ArrayList<StandardSongData.StandardArtistData>>() {}.type
        return Gson().fromJson(json, listType)
    }

}

class StandardSongDataConverter {

    @TypeConverter
    fun objectToString(song: StandardSongData): String {
        return Gson().toJson(song)
    }

    @TypeConverter
    fun stringToObject(json: String): StandardSongData {
        val songType: Type = object : TypeToken<StandardSongData>() {}.type
        return Gson().fromJson(json, songType)
    }

}