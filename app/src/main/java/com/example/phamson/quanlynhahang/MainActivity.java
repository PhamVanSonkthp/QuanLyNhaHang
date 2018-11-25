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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    GridView grBanAn;
    ArrayList<DanhSachBanAn> dsBanan;
    AdapterBanAn adapterBanAn;
    public static DatabaseReference mData;
    TextView txtTienChu , txtTienSo , txtChonBan;
    ListView lvThucAn;
    ArrayList<DanhSachThucAn> dsThucAn;
    AdapterThucAn adapterThucAn;
    SharedPreferences pre;
    SharedPreferences.Editor edit;
    boolean check = false;
    public static String ID_PHONG ;
    int varChec=-1;

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

                TinhTien(dsBanan.get(position).getTenBan());
                if (varChec != position){

                    varChec = position;
                    dsThucAn.clear();
                    txtTienChu.setText("Không");
                    txtTienSo.setText("0");
                        mData.child("ID").child(ID_PHONG).child(txtChonBan.getText().toString()).child("DanhSachThucAn").addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                                ThucAn thucAn = dataSnapshot.getValue(ThucAn.class);
                                if (thucAn.banMay.equals(txtChonBan.getText().toString())){
                                    int i;
                                    long tong = 0;
                                    for ( i = 0 ; i < dsThucAn.size() ; i++){

                                        if (thucAn.name.equals(dsThucAn.get(i).getName())){

                                            dsThucAn.add(i+1,new DanhSachThucAn(thucAn.hinhAnh , thucAn.name , (Integer.parseInt(thucAn.number) + Integer.parseInt(dsThucAn.get(i).number )) +"", thucAn.price , thucAn.banMay));
                                            dsThucAn.remove(i);
                                            adapterThucAn.notifyDataSetChanged();
                                            //tong += Long.parseLong(dsThucAn.get(i).price) * Integer.parseInt(dsThucAn.get(i).number);
                                            for(i = 0 ; i <dsThucAn.size() ; i++){
                                                tong += Long.parseLong(dsThucAn.get(i).price) * Integer.parseInt(dsThucAn.get(i).number);
                                            }
                                            txtTienChu.setText(NumberToWords(tong));
                                            txtTienSo.setText(tong+"");
                                            return;
                                            //break;
                                        }
                                        //tong += Long.parseLong(dsThucAn.get(i).price) * Integer.parseInt(dsThucAn.get(i).number);
                                    }
                                    dsThucAn.add(new DanhSachThucAn(thucAn.hinhAnh , thucAn.name , thucAn.number , thucAn.price , thucAn.banMay));
                                    //tong += Long.parseLong(dsThucAn.get(i).price) * Integer.parseInt(dsThucAn.get(i).number);
                                    adapterThucAn.notifyDataSetChanged();
                                    for(i = 0 ; i <dsThucAn.size() ; i++){
                                        tong += Long.parseLong(dsThucAn.get(i).price) * Integer.parseInt(dsThucAn.get(i).number);
                                    }
                                    txtTienChu.setText(NumberToWords(tong));
                                    txtTienSo.setText(tong+"");
                                }


                            }

                            @Override
                            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                            }

                            @Override
                            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                                txtTienSo.setText("0");
                                txtTienChu.setText("Không");
                                txtChonBan.setText("Không Bàn Nào Được Chọn");
                                dsThucAn.clear();
                                adapterThucAn.notifyDataSetChanged();
                                varChec =-1;
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

        grBanAn.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                TinhTien(dsBanan.get(position).getTenBan());
                if(txtChonBan.length() > 10){
                    txtChonBan.setText(dsBanan.get(position).getTenBan());
                }
                Intent intent = new Intent(MainActivity.this , TinhTienActivity.class);
                intent.putExtra("BanAn" , txtChonBan.getText().toString());
                startActivity(intent);
                return false;
            }
        });

    }

    private void TinhTien(String s){
        txtChonBan.setText(s);
    }

    private void AnhXa() {

        lvThucAn = findViewById(R.id.lvThucAn);
        txtChonBan = findViewById(R.id.txtBanDuocChon);
        txtTienChu = findViewById(R.id.txtTienChu);
        txtTienSo = findViewById(R.id.txtTienSo);
        grBanAn = findViewById(R.id.grBanAn);

        dsBanan = new ArrayList<>();

        for (int i = 1 ; i <= 50 ; i++){
            dsBanan.add(new DanhSachBanAn("Bàn "+i));
        }

        dsThucAn = new ArrayList<>();
        //dsThucAn.add(new DanhSachThucAn(R.drawable.food , "Meo Luoc" , 1));

        adapterBanAn = new AdapterBanAn(MainActivity.this  ,R.layout.custom_ban_an , dsBanan);
        grBanAn.setAdapter(adapterBanAn);
        adapterThucAn = new AdapterThucAn(MainActivity.this , R.layout.custom_thuc_an , dsThucAn);
        lvThucAn.setAdapter(adapterThucAn);




    }

    String NumberToWords(long number)
    {
        if (number == 0)
            return "Không";

        if (number < 0)
            return "minus " + NumberToWords(Math.abs(number));

        String words = "";

        if ((number / 1000000) > 0)
        {
            words += NumberToWords(number / 1000000) + " Triệu ";
            number %= 1000000;
        }

        if ((number / 1000) > 0)
        {
            words += NumberToWords(number / 1000) + " Nghìn";
            number %= 1000;
        }

        if ((number / 100) > 0)
        {
            words += NumberToWords(number / 100) + " Trăm ";
            number %= 100;
        }

        if (number > 0)
        {
            if (words != "")
                words += "";

            List<String> unitsMap = Arrays.asList( " Không", " Một", " Hai", " Ba", " Bốn", " Năm", " Sáu", " Bảy", " Tám", " Chín", " Mười", " Mười Một", " Mười Hai", " Mười Ba", " Mười Bốn", " Mười Năm", " Mười Sáu", " Mười Bảy", " Mười Tám", " Mười Chín" );
            List<String> tensMap = Arrays.asList( "Không", "Mười", "Hai Mươi", "Ba Mươi", "Bốn Mươi", "Năm Mươi", "Sáu Mươi", "Bảy Mươi", "Tám Mươi", "Chín Mươi" );

            if (number < 20)
                words += unitsMap.get(Integer.parseInt(number+""));
            else
            {
                words += tensMap.get(Integer.parseInt(number+"") / 10);
                if ((number % 10) > 0)
                    words += "-" + unitsMap.get(Integer.parseInt(number+"") % 10);
            }
        }

        return words;
    }

}
