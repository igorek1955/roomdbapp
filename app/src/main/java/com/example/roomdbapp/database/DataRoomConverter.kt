package com.example.roomdbapp.database

import androidx.room.TypeConverter
import java.util.*

class DataRoomConverter {
    @TypeConverter
    fun toDate(value: Long?): Date? {
        return if (value == null) null else Date(value)
    }

    @TypeConverter
    fun toLong(value: Date?): Long? {
        return value?.time
    }
}