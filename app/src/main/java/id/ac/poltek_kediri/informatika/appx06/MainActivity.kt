package id.ac.poltek_kediri.informatika.appx06

import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.FragmentTransaction
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    lateinit var db : SQLiteDatabase
    lateinit var fragProdi: FragmentProdi
    lateinit var fragMhs: FragmentMahasiswa
    lateinit var ft: FragmentTransaction

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigationView.setOnNavigationItemSelectedListener(this)
        fragProdi = FragmentProdi()
        fragMhs = FragmentMahasiswa()
        db = DBOpenHelper(this).writableDatabase
    }

    fun getDbObject() : SQLiteDatabase{
        return db
    }

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        when (p0.itemId) {
            R.id.itemProdi -> {
                ft = supportFragmentManager.beginTransaction()
                ft.replace(R.id.frameLayout, fragProdi).commit()
                frameLayout.setBackgroundColor(Color.argb(245, 255, 255, 225))
                frameLayout.visibility = View.VISIBLE
            }
            R.id.itemMhs -> {
                ft = supportFragmentManager.beginTransaction()
                ft.replace(R.id.frameLayout, fragMhs).commit()
                frameLayout.setBackgroundColor(Color.argb(245, 255, 225, 255))
                frameLayout.visibility = View.VISIBLE
            }
            R.id.itemAbout -> frameLayout.visibility = View.GONE
        }
        return true
    }
}
