package com.example.roomdbapp.database

import androidx.room.Entity
import java.io.Serializable
import androidx.room.ColumnInfo
import androidx.room.Ignore

import androidx.room.PrimaryKey
import java.util.*


@Entity(tableName = Constants.TABLE_NAME_NOTE)
class Note: Serializable {

    // Columns:
    @PrimaryKey(autoGenerate = true)
    private var note_id: Long = 0

    @ColumnInfo(name = "note_content")
    private var content: String? = null
    private var title: String? = null
    private var date: Date? = null

    // Constructors:
    constructor(content: String?, title: String?) {
        this.content = content
        this.title = title
        date = Date(System.currentTimeMillis())
    }

    @Ignore
    fun Note() {
    }

    fun getDate(): Date? {
        return date
    }

    fun setDate(date: Date?) {
        this.date = date
    }

    fun getNote_id(): Long {
        return note_id
    }

    fun setNote_id(note_id: Long) {
        this.note_id = note_id
    }

    fun getContent(): String? {
        return content
    }

    fun setContent(content: String?) {
        this.content = content
    }

    fun getTitle(): String? {
        return title
    }

    fun setTitle(title: String?) {
        this.title = title
    }


    override fun toString(): String {
        return "Note{" +
                "note_id=" + note_id +
                ", content='" + content + '\'' +
                ", title='" + title + '\'' +
                ", date=" + date +
                '}'
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Note

        if (note_id != other.note_id) return false
        if (title != other.title) return false

        return true
    }

    override fun hashCode(): Int {
        var result = note_id.hashCode()
        result = 31 * result + (title?.hashCode() ?: 0)
        return result
    }
}