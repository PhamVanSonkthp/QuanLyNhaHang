package com.example.phamson.quanlynhahang;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
public class MainActivity extends AppCompatActivity {

    GridView grBanAn;
    ArrayList<DanhSachBanAn> dsBanan;
    AdapterBanAn adapterBanAn;

    public static DatabaseReference mData;
    SharedPreferences pre;
    SharedPreferences.Editor edit;
    boolean check = false;
    public static String ID_PHONG ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mData = FirebaseDatabase.getInstance().getReference();
        pre = getSharedPreferences("myData", MODE_PRIVATE);
        edit=pre.edit();
        if(pre.getString("myData" , "").equals("")){
            Check1();
        }else {
            ID_PHONG = pre.getString("myData","");
            AnhXa();
            Events();
        }
    }



    private void Check1() {

        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_dialog_accset);
        final EditText edtID = dialog.findViewById(R.id.edtDialog_idPhong);
        Button btnID = dialog.findViewById(R.id.btnDialog_IDPhong);
        btnID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!edtID.getText().toString().equals("")){
                    mData.child("ID").child(edtID.getText().toString()).child("Bàn 1").addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            if(!check){
                                check = true;
                                    // co du lieu
                                    mData.child("DanhSachID").child(edtID.getText().toString()).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            edit.putString("myData" , edtID.getText().toString());
                                            edit.commit();
                                            dialog.dismiss();
                                            ID_PHONG = pre.getString("myData","");
                                            AnhXa();
                                            Events();
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                            }
                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        }

                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });
        dialog.show();
    }

    private void Events() {
        grBanAn.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this , TinhTienActivity.class);
                intent.putExtra("BanAn" , dsBanan.get(position).getTenBan());
                startActivity(intent);
            }
        });

    }

    private void AnhXa() {
        grBanAn = findViewById(R.id.grBanAn);
        //grBanAn.setPadding(0 , 10 ,0 ,10);
        dsBanan = new ArrayList<>();

        for (int i = 1 ; i <= 50 ; i++){
            dsBanan.add(new DanhSachBanAn("Bàn "+i));
        }

        adapterBanAn = new AdapterBanAn(MainActivity.this  ,R.layout.custom_ban_an , dsBanan);
        grBanAn.setAdapter(adapterBanAn);




    }

}
