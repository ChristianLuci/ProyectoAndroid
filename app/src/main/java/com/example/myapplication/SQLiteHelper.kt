package com.example.myapplication


import android.content.Context
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteDatabase.CursorFactory
import android.os.Parcel
import android.os.Parcelable


class SQLiteHelper(context: Context, name: String, factory: CursorFactory?,
                            version: Int) : SQLiteOpenHelper(context, name, factory, version),
    Parcelable {
    constructor(parcel: Parcel) : this(
        TODO("context"),
        TODO("name"),
        TODO("factory"),
        TODO("version")
    )

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("create table usuarios(usuario text, password text, primary key(usuario,password))")
        db.execSQL("create table nombreListas(usuario text,nombreLista text, datosProducto text, primary key(usuario,nombreLista,datosProducto))")
    }


    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SQLiteHelper> {
        override fun createFromParcel(parcel: Parcel): SQLiteHelper {
            return SQLiteHelper(parcel)
        }

        override fun newArray(size: Int): Array<SQLiteHelper?> {
            return arrayOfNulls(size)
        }
    }
}