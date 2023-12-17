package com.example.aplikasikontak;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView lv;
    private ImageView add;
    private ImageView edit;
    private ImageView hapus;
    private ImageView cari;

    private kontakAdapter kAdapter;

    private SQLiteDatabase dbku;
    private SQLiteOpenHelper dbopen;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv = (ListView) findViewById(R.id.listView);

        add = (ImageView) findViewById(R.id.tambah);
        add.setOnClickListener(operasi);

        hapus = (ImageView)findViewById(R.id.del);
        hapus.setOnClickListener(operasi);

        edit = (ImageView)findViewById(R.id.edit);
        edit.setOnClickListener(operasi);

        cari = (ImageView)findViewById(R.id.cari);
        cari.setOnClickListener(operasi);


        ArrayList<kontak> listKontak = new ArrayList<kontak>();
        kAdapter = new kontakAdapter(this, 0, listKontak);
        lv.setAdapter(kAdapter);

        dbopen = new SQLiteOpenHelper(this, "kontak.db", null, 1) {
            @Override
            public void onCreate(SQLiteDatabase db) {
            }

            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            }
        };

        dbku = dbopen.getWritableDatabase();
        dbku.execSQL("create table if not exists kontak(nama TEXT, nohp TEXT, mk TEXT, tugas INT, quiz INT, ets INT, eas INT);");
        ambildata();
    }

    View.OnClickListener operasi = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (v.getId() == R.id.tambah) {
                tambah_data();
            }
            else if (v.getId() == R.id.del) {
                hapus_data();
            }
            else if (v.getId() == R.id.edit) {
                cari_data();
            }
            else if (v.getId() == R.id.cari) {
                cari_saja();
            }


        }
    };
//
    private void add_item(String nm, String hp, String mk, Integer tugas, Integer quiz, Integer ets, Integer eas) {
        ContentValues datanya = new ContentValues();
        datanya.put("nama", nm);
        datanya.put("nohp", hp);
        datanya.put("mk", mk);
        datanya.put("tugas", tugas);
        datanya.put("quiz", quiz);
        datanya.put("ets", ets);
        datanya.put("eas", eas);
        dbku.insert("kontak", null, datanya);
        kontak newKontak = new kontak(nm, hp, mk, tugas, quiz, ets, eas);
//        kontak newKontak = new kontak(nm, hp, mk, tugas, quiz);
        kAdapter.add(newKontak);

    }
//
    private void insertKontak(String nm, String hp, String mk, Integer tugas, Integer quiz, Integer ets, Integer eas) {
        kontak newKontak = new kontak(nm, hp, mk, tugas, quiz, ets, eas);

        kAdapter.add(newKontak);
    }
//
    private void ambildata() {
        Cursor cur = dbku.rawQuery("select * from kontak", null);
        Toast.makeText(this, "Terdapat sejumlah " + cur.getCount(),
                Toast.LENGTH_LONG).show();
        int i = 0;
        kAdapter.clear();
        if (cur.getCount() > 0) cur.moveToFirst();
        while (i < cur.getCount()) {
            insertKontak(cur.getString(cur.getColumnIndex("nama")),
                    cur.getString(cur.getColumnIndex("nohp")),
            cur.getString(cur.getColumnIndex("mk")),
            cur.getInt(cur.getColumnIndex("tugas")),
            cur.getInt(cur.getColumnIndex("quiz")),
            cur.getInt(cur.getColumnIndex("ets")),
            cur.getInt(cur.getColumnIndex("eas")));

            cur.moveToNext();
            i++;
        }
    }

    private void ambil_saja(String nohp) {
        Cursor cur = dbku.rawQuery("select * from kontak where nohp = '"+nohp+"'", null);
        Toast.makeText(this, "Terdapat sejumlah " + cur.getCount(),
                Toast.LENGTH_LONG).show();
        int i = 0;
        kAdapter.clear();
        if (cur.getCount() > 0) cur.moveToFirst();
        while (i < cur.getCount()) {
            insertKontak(cur.getString(cur.getColumnIndex("nama")),
                    cur.getString(cur.getColumnIndex("nohp")),
                    cur.getString(cur.getColumnIndex("mk")),
                    cur.getInt(cur.getColumnIndex("tugas")),
                    cur.getInt(cur.getColumnIndex("quiz")),
                    cur.getInt(cur.getColumnIndex("ets")),
                    cur.getInt(cur.getColumnIndex("eas")));

            cur.moveToNext();
            i++;
        }
    }

//
//
    private void tambah_data() {
        AlertDialog.Builder buat = new AlertDialog.Builder(this);
        buat.setTitle("Masukkan Data Nilai");

        View vAdd = LayoutInflater.from(this).inflate(R.layout.add_kontak, null);

        final EditText nm = (EditText) vAdd.findViewById(R.id.nm);
        final EditText hp = (EditText) vAdd.findViewById(R.id.hp);

        final EditText mk = (EditText) vAdd.findViewById(R.id.mk);
        final EditText tugas = (EditText) vAdd.findViewById(R.id.tugas);
        final EditText quiz = (EditText) vAdd.findViewById(R.id.quiz);
//
        final EditText ets = (EditText) vAdd.findViewById(R.id.ets);
        final EditText eas = (EditText) vAdd.findViewById(R.id.eas);

        buat.setView(vAdd);
        // Set up the buttons
        buat.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                add_item(nm.getText().toString(), hp.getText().toString(),
                        mk.getText().toString(),
                        Integer.parseInt(tugas.getText().toString()),
                        Integer.parseInt(quiz.getText().toString()),
                        Integer.parseInt(ets.getText().toString()),
                        Integer.parseInt(eas.getText().toString()));


                Toast.makeText(getBaseContext(), "Data Tersimpan", Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });
        buat.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        buat.show();


    }

    private void edit_item(String nm, String hp, String mk, Integer tugas, Integer quiz, Integer ets, Integer eas){
        ContentValues datanya = new ContentValues();
        datanya.put("nama", nm);
        datanya.put("nohp", hp);
        datanya.put("mk", mk);
        datanya.put("tugas", tugas);
        datanya.put("quiz", quiz);
        datanya.put("ets", ets);
        datanya.put("eas", eas);
        dbku.update("kontak", datanya, "nohp='"+hp+"'",null);
    }

    private void tampil_edit(String ehp){
        LayoutInflater li = LayoutInflater.from(this);
        View editnya = li.inflate(R.layout.edit_kontak,null);

        AlertDialog.Builder dialognya = new AlertDialog.Builder(this);
        dialognya.setView(editnya);
        dialognya.setTitle("Update Kontak");

        final EditText nm = (EditText) editnya.findViewById(R.id.nm);
        final EditText hp = (EditText) editnya.findViewById(R.id.hp);
        final EditText mk = (EditText) editnya.findViewById(R.id.mk);
        final EditText tugas = (EditText) editnya.findViewById(R.id.tugas);
        final EditText quiz = (EditText) editnya.findViewById(R.id.quiz);
        final EditText ets = (EditText) editnya.findViewById(R.id.ets);
        final EditText eas = (EditText) editnya.findViewById(R.id.eas);

        Cursor c = dbku.rawQuery("SELECT * FROM kontak WHERE nohp = '"+ehp+"'", null);
        c.moveToNext();
        nm.setText(c.getString(c.getColumnIndex("nama")));
        hp.setText(c.getString(c.getColumnIndex("nohp")));
        mk.setText(c.getString(c.getColumnIndex("mk")));
        tugas.setText(c.getString(c.getColumnIndex("tugas")));
        quiz.setText(c.getString(c.getColumnIndex("quiz")));
        ets.setText(c.getString(c.getColumnIndex("ets")));
        eas.setText(c.getString(c.getColumnIndex("eas")));

        dialognya.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                edit_item(nm.getText().toString(), hp.getText().toString(),
                        mk.getText().toString(),
                        Integer.parseInt(tugas.getText().toString()),
                        Integer.parseInt(quiz.getText().toString()),
                        Integer.parseInt(ets.getText().toString()),
                        Integer.parseInt(eas.getText().toString()));
                Toast.makeText(getBaseContext(), "Data Berhasil diupdate", Toast.LENGTH_LONG).show();
                ambildata();
                dialog.dismiss();
            }
        });
        dialognya.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        dialognya.show();
    }

    private void cari_data(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cari Data");

        View view = getLayoutInflater().inflate(R.layout.cari_kontak, null);
        builder.setView(view);


        final EditText nohp = (EditText) view.findViewById(R.id.etHP);

        builder.setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tampil_edit(nohp.getText().toString());
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        builder.show();
    }

    private void cari_saja(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cari Data");

        View view = getLayoutInflater().inflate(R.layout.cari_kontak, null);
        builder.setView(view);


        final EditText nohp = (EditText) view.findViewById(R.id.etHP);

        builder.setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ambil_saja(nohp.getText().toString());
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        builder.show();
    }

    private void hapus_data(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Hapus Data");

        // Set up the layout for the dialog
        View view = getLayoutInflater().inflate(R.layout.hapus_user, null);
        builder.setView(view);

        final EditText nrpInput = view.findViewById(R.id.deletenohp);

        builder.setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    // Get the data from the dialog
                    String nrpValue = nrpInput.getText().toString();

                    deleteData(nrpValue);

                    // Update the ListView

                    Toast.makeText(MainActivity.this, "Data Berhasil Dihapus", Toast.LENGTH_LONG).show();
                    ambildata();
                } catch (Exception e) {
                    // Log any exceptions for debugging
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

        builder.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
    }

    private void deleteData(String noHP){
        dbku.delete("kontak","noHP = ?", new String[]{noHP});
    }


//
    public class kontakAdapter extends ArrayAdapter<kontak> {
        // View lookup cache
//        private class ViewHolder {
//            TextView nama;
//            TextView nohp;
//        }

        public kontakAdapter(Context context, int resource, List<kontak> objects) {
            super(context, resource, objects);
        }

    public View getView(int position, View ConvertView, ViewGroup parent){
        kontak dtkontak = getItem(position);
        if (ConvertView==null){
            ConvertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.item_user,parent,false);
        }

        TextView tNama = (TextView) ConvertView.findViewById(R.id.tNama);
        TextView tNohp = (TextView) ConvertView.findViewById(R.id.tnoHp);
        TextView tmk = (TextView) ConvertView.findViewById(R.id.tmk);
        TextView ttugas = (TextView) ConvertView.findViewById(R.id.ttugas);
        TextView tquiz = (TextView) ConvertView.findViewById(R.id.tquiz);
        TextView tets = (TextView) ConvertView.findViewById(R.id.tets);
        TextView teas = (TextView) ConvertView.findViewById(R.id.teas);

        tNama.setText(dtkontak.getNama());
        tNohp.setText(dtkontak.getNoHp());
        tmk.setText(dtkontak.getMk());
        ttugas.setText(dtkontak.getTugas());
        tquiz.setText(dtkontak.getQuiz());
        tets.setText(dtkontak.getEts());
        teas.setText(dtkontak.getEas());

        return ConvertView;
    }


    }
}
