package id.ac.poltek_kediri.informatika.appx06

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBOpenHelper(context: Context) : SQLiteOpenHelper(context,DB_Name,null,DB_Var){
    override fun onCreate(db: SQLiteDatabase?) {
        val tMhs = "create table mhs(nim text primary key, nama text not null, id_prodi int not null)"
        val tProdi = "create table prodi(id_prodi integer primary key autoincrement, nama_prodi text not null)"
        val insProdi = "insert into prodi(nama_prodi) values ('TI'),('AK'),('ME')"
        db?.execSQL(tMhs)
        db?.execSQL(tProdi)
        db?.execSQL(insProdi)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    companion object {
        val DB_Name = "mahasiswa"
        val DB_Var = 1
    }
}