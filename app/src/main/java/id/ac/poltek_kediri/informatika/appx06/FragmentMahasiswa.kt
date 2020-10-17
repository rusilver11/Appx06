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
import android.widget.*
import kotlinx.android.synthetic.main.frag_data_mhs.*
import kotlinx.android.synthetic.main.frag_data_mhs.view.*


class FragmentMahasiswa : Fragment(), View.OnClickListener, AdapterView.OnItemSelectedListener {
    override fun onNothingSelected(parent: AdapterView<*>?) {
        spinner.setSelection(0, true)
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
       val c = spAdapter.getItem(position) as Cursor
        namaProdi = c.getString(c.getColumnIndex("_id"))
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnDeleteMhs ->{
                dialog.setTitle("Konfirmasi").setIcon(android.R.drawable.ic_dialog_info)
                    .setMessage("Yakin akan menghapus data ini?")
                    .setPositiveButton("Ya", btnDeleteDialog)
                    .setNegativeButton("Tidak", null)
                dialog.show()
            }
            R.id.btnUpdateMhs ->{
                dialog.setTitle("Konfirmasi").setIcon(android.R.drawable.ic_dialog_info)
                    .setMessage("Yakin akan mengupdate data ini?")
                    .setPositiveButton("Ya", btnUpdateDialog)
                    .setNegativeButton("Tidak", null)
                dialog.show()
            }
            R.id.btnInsertMhs ->{
                dialog.setTitle("Konfirmasi").setIcon(android.R.drawable.ic_dialog_info)
                    .setMessage("Apakah data yang akan dimasukkan sudah benar?")
                    .setPositiveButton("Ya", btnInsertDialog)
                    .setNegativeButton("Tidak", null)
                dialog.show()
            }
            R.id.btnCari ->{
                showDataMhs(edNamaMhs.text.toString())
            }
        }
    }

    lateinit var thisParent : MainActivity
    lateinit var lsAdapter : ListAdapter
    lateinit var spAdapter: SimpleCursorAdapter
    lateinit var dialog : AlertDialog.Builder
    lateinit var v : View
    var pos = 0
    var idMhs = "String"
    var namaMhs : String = ""
    var namaProdi : String = ""
    lateinit var db : SQLiteDatabase

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        thisParent = activity as MainActivity
        v = inflater.inflate(R.layout.frag_data_mhs,container,false)
        db = thisParent.getDbObject()
        dialog = AlertDialog.Builder(thisParent)

        v.btnDeleteMhs.setOnClickListener(this)
        v.btnInsertMhs.setOnClickListener(this)
        v.btnUpdateMhs.setOnClickListener(this)
        v.spinner.onItemSelectedListener = this
        v.btnCari.setOnClickListener(this)
        v.lsMhs.setOnItemClickListener(itemClick)

        return v
    }

    override fun onStart() {
        super.onStart()
        showDataMhs("")
        showDataProdi()
    }

    fun showDataMhs(namaMhs : String){
        var sql = ""
        if(!namaMhs.trim().equals("")){
            sql = "select m.nim as _id, m.nama, p.id_prodi, p.nama_prodi from mhs m, prodi p " +
                    "where m.id_prodi = p.id_prodi and m.nama like '%$namaMhs%'"
        }else{
            sql = "select m.nim as _id, m.nama, p.id_prodi, p.nama_prodi from mhs m, prodi p " +
                    "where m.id_prodi = p.id_prodi order by m.nama asc"
        }

        val c: Cursor = db.rawQuery(sql, null)
        lsAdapter = SimpleCursorAdapter(thisParent,R.layout.item_data_mhs,c,
            arrayOf("_id", "nama", "nama_prodi", "id_prodi"), intArrayOf(R.id.txNimMhs, R.id.txNamaMhs, R.id.txNamaPS, pos),
            CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER)
        v.lsMhs.adapter = lsAdapter
    }

    fun showDataProdi(){
        val c: Cursor = db.rawQuery("select nama_prodi as _id from prodi order by nama_prodi asc", null)
        spAdapter = SimpleCursorAdapter(thisParent, android.R.layout.simple_spinner_item,c,
            arrayOf("_id"), intArrayOf(android.R.id.text1), CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER)
        spAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        v.spinner.adapter = spAdapter
        v.spinner.setSelection(0)
    }

    val itemClick = AdapterView.OnItemClickListener{parent, view, position,id ->
        val c: Cursor = parent.adapter.getItem(position)as Cursor
        idMhs = c.getString(c.getColumnIndex("_id"))
        v.edNimMhs.setText(c.getString(c.getColumnIndex("_id")))
        v.edNamaMhs.setText(c.getString(c.getColumnIndex("nama")))

    }

    fun insertDataMhs(nim : String, namaMhs: String, id_prodi : Int){
        var sql = "insert into mhs(nim, nama, id_prodi) values (?,?,?)"
        db.execSQL(sql, arrayOf(nim, namaMhs, id_prodi))
        showDataMhs("")
    }

    fun updateDataMhs(nim: String, prodi: String, nama: String, idMhs: String){
        var cv: ContentValues = ContentValues()
        cv.put("nim", nim)
        cv.put("nama", nama)
        cv.put("id_prodi", prodi)
        db.update("mhs", cv, "nim = $idMhs", null)
        showDataMhs("")
    }

    fun deleteDataMhs(idMhs: String){
        db.delete("mhs", "nim = $idMhs", null)
        showDataMhs("")
    }

    val btnInsertDialog = DialogInterface.OnClickListener { dialog, which ->
        var sql = "select id_prodi from prodi where nama_prodi='$namaProdi'"
        val c : Cursor = db.rawQuery(sql, null)
        if(c.count>0){
            c.moveToFirst()
            insertDataMhs(v.edNimMhs.text.toString(), v.edNamaMhs.text.toString(),
                c.getInt(c.getColumnIndex("id_prodi")))

            v.edNimMhs.setText("")
            v.edNamaMhs.setText("")
        }
    }

    val btnUpdateDialog = DialogInterface.OnClickListener { dialog, which ->
        var sql = "select id_prodi from prodi where nama_prodi = '$namaProdi'"
        val c: Cursor = db.rawQuery(sql, null)
        if (c.count > 0) {
            c.moveToFirst()
            var nim = v.edNimMhs.text.toString()
            var nama = v.edNamaMhs.text.toString()
            var prodi = c.getInt(c.getColumnIndex("id_prodi")).toString()
            updateDataMhs(nim, prodi, nama, idMhs)

            v.edNimMhs.setText("")
            v.edNamaMhs.setText("")
        }
    }

    val btnDeleteDialog = DialogInterface.OnClickListener { dialog, which ->
        deleteDataMhs(idMhs)
        v.edNimMhs.setText("")
        v.edNamaMhs.setText("")
    }
}