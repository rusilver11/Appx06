package id.ac.poltek_kediri.informatika.appx06

import android.app.AlertDialog
import android.content.ContentValues
import android.content.DialogInterface
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.CursorAdapter
import android.widget.ListAdapter
import android.widget.SimpleCursorAdapter
import kotlinx.android.synthetic.main.frag_data_prodi.view.*

class FragmentProdi : Fragment(),View.OnClickListener {
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnInsert ->{
                builder.setTitle("Konfirmasi").setMessage("Apakah data yang akan dimasukkan sudah benar?")
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setPositiveButton("Ya",btnInsertDialog)
                    .setNegativeButton("Tidak",null)
                builder.show()
            }
            R.id.btnDelete ->{
                builder.setTitle("Konfirmasi").setMessage("Yakin akan menghapus data ini?")
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setPositiveButton("Ya",btnDeleteDialog)
                    .setNegativeButton("Tidak",null)
                builder.show()
            }
            R.id.btnUpdate ->{
                builder.setTitle("Konfirmasi").setMessage("Yakin akan mengupdate data ini?")
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setPositiveButton("Ya",btnUpdateDialog)
                    .setNegativeButton("Tidak",null)
                builder.show()
            }
        }
    }

    lateinit var thisParent : MainActivity
    lateinit var db : SQLiteDatabase
    lateinit var adapter: ListAdapter
    lateinit var v : View
    lateinit var builder: AlertDialog.Builder
    var idProdi : String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        thisParent = activity as MainActivity
        db = thisParent.getDbObject()

        v = inflater.inflate(R.layout.frag_data_prodi,container,false)
        v.btnUpdate.setOnClickListener(this)
        v.btnInsert.setOnClickListener(this)
        v.btnDelete.setOnClickListener(this)
        builder = AlertDialog.Builder(thisParent)
        v.lsProdi.setOnItemClickListener(itemClick)

        return v
    }

    fun showDataProdi(){
        val cursor : Cursor = db.query("prodi", arrayOf("nama_prodi", "id_prodi as _id"),
            null, null, null, null, "nama_prodi asc")
        adapter = SimpleCursorAdapter(thisParent,R.layout.item_data_prodi,cursor,
            arrayOf("_id","nama_prodi"), intArrayOf(R.id.txldProdi, R.id.txNamaProdi),
            CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER)

        v.lsProdi.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        showDataProdi()
    }

    val itemClick = AdapterView.OnItemClickListener { parent, view, position, id ->
        val c: Cursor = parent.adapter.getItem(position) as Cursor
        idProdi = c.getString(c.getColumnIndex("_id"))
        v.edNamaProdi.setText(c.getString(c.getColumnIndex("nama_prodi")))
    }

    fun insertDataProdi(namaProdi: String){
        var cv : ContentValues = ContentValues()
        cv.put("nama_prodi", namaProdi)
        db.insert("prodi", null,cv)
        showDataProdi()
    }

    fun updateDataProdi(namaProdi: String, idProdi: String){
        var cv : ContentValues = ContentValues()
        cv.put("nama_prodi", namaProdi)
        db.update("prodi",cv,"id_prodi = $idProdi", null)
        showDataProdi()
    }

    fun deleteDataProdi(idProdi: String){
        db.delete("prodi", "id_prodi = $idProdi", null)
        showDataProdi()
    }

    val btnInsertDialog = DialogInterface.OnClickListener { dialog, which ->
        insertDataProdi(v.edNamaProdi.text.toString())
        v.edNamaProdi.setText("")
    }

    val btnUpdateDialog = DialogInterface.OnClickListener { dialog, which ->
        updateDataProdi(v.edNamaProdi.text.toString(),idProdi)
        v.edNamaProdi.setText("")
    }

    val btnDeleteDialog = DialogInterface.OnClickListener { dialog, which ->
        deleteDataProdi(idProdi)
        v.edNamaProdi.setText("")
    }
}