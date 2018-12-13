package com.example.phamson.quanlynhahang;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static com.example.phamson.quanlynhahang.MainActivity.ID_PHONG;
import static com.example.phamson.quanlynhahang.MainActivity.mData;

public class TinhTienActivity extends AppCompatActivity{

    ListView lvThucAn;
    ArrayList<DanhSachThucAn> dsThucAn;
    AdapterThucAn adapterThucAn;
    public static String banAn;
    TextView txtBanTinhTien , txtBanTinhTienChu , txtBanTinhTienSo;
    RecyclerView lvThemMon;
    RecyclerView.LayoutManager lvThemMonManager;
    RecyclerView.Adapter lvThemMonAdapter;
    ArrayList<DanhSachThucAn> mDataset;
    Button btnDonBan , btnDonTatCa;
    int varRun;
    ProgressDialog progressDialog;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tinh_tien);
        AnhXa();
        Events();
    }

    private void Events() {

        lvThucAn.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Collections.sort(dsThucAn, new Comparator<DanhSachThucAn>() {
                    @Override
                    public int compare(DanhSachThucAn danhSachThucAn, DanhSachThucAn t1) {
                        return danhSachThucAn.getName().compareTo(t1.getName());
                    }
                });
                DanhSachThucAn thucAn = new DanhSachThucAn(dsThucAn.get(i).getHinhAnh() , dsThucAn.get(i).getName() , -1+"", dsThucAn.get(i).getPrice()  , dsThucAn.get(i).banMay);
                    if (Integer.parseInt(dsThucAn.get(i).number) != 0){
                        mData.child("ID").child(ID_PHONG).child(banAn).child("DanhSachThucAn").push().setValue(thucAn);
                    }


            }
        });

        btnDonTatCa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.setMessage("Bạn Có Muốn Dọn Tất Cả Bàn Không ?");
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        progressDialog.setTitle("Đang Tiến Hành");
                        progressDialog.setMessage("Dọn Tất Cả Các Bàn");
                        progressDialog.setCancelable(false);
                        DonTatCa(1);
                    }
                });
                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.show();
            }
        });

        btnDonBan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.setMessage("Bạn Có Muốn Dọn Bàn Không ?");
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mData.child("ID").child(ID_PHONG).child(banAn).child("DachSachThucAn").setValue("null", new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                mData.child("ID").child(ID_PHONG).child(banAn).child("NumberFood").setValue("0", new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                        Toast.makeText(getApplicationContext() , "Đã Dọn Bàn !" , Toast.LENGTH_SHORT).show();
                                        mData.child("ID").child(ID_PHONG).child(banAn).child("DanhSachThucAn").setValue("null");
                                        dsThucAn.clear();
                                        adapterThucAn.notifyDataSetChanged();
                                        txtBanTinhTienChu.setText("Không");
                                        txtBanTinhTienSo.setText("0");
                                    }
                                });
                            }
                        });
                    }
                });
                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.show();


            }
        });

        //dsThucAn.clear();
        mData.child("ID").child(ID_PHONG).child(banAn).child("DanhSachThucAn").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                ThucAn thucAn = dataSnapshot.getValue(ThucAn.class);
                int i;
                long tong = 0;



                for ( i = 0 ; i < dsThucAn.size() ; i++){

                    if (thucAn.name.equals(dsThucAn.get(i).getName())){

                        dsThucAn.add(i+1,new DanhSachThucAn(thucAn.hinhAnh , thucAn.name , (Integer.parseInt(thucAn.number) + Integer.parseInt(dsThucAn.get(i).number )) +"", thucAn.price , thucAn.banMay));
                        dsThucAn.remove(i);
                        Collections.sort(dsThucAn, new Comparator<DanhSachThucAn>() {
                            @Override
                            public int compare(DanhSachThucAn danhSachThucAn, DanhSachThucAn t1) {
                                return danhSachThucAn.getName().compareTo(t1.getName());
                            }
                        });

                        int k = 0;
                        while (k < dsThucAn.size()){
                            if (Integer.parseInt(dsThucAn.get(k).getNumber() )== 0){
                                dsThucAn.remove(k);
                            }
                            k++;
                        }
                        if (dsThucAn.isEmpty()){
                            mData.child("ID").child(ID_PHONG).child(banAn).child("DachSachThucAn").setValue("null", new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                    mData.child("ID").child(ID_PHONG).child(banAn).child("NumberFood").setValue("0", new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                            mData.child("ID").child(ID_PHONG).child(banAn).child("DanhSachThucAn").setValue("null");
                                            dsThucAn.clear();
                                            adapterThucAn.notifyDataSetChanged();
                                            txtBanTinhTienChu.setText("Không");
                                            txtBanTinhTienSo.setText("0");
                                        }
                                    });
                                }
                            });
                        }
                        adapterThucAn.notifyDataSetChanged();
                        //tong += Long.parseLong(dsThucAn.get(i).price) * Integer.parseInt(dsThucAn.get(i).number);
                        for(i = 0 ; i <dsThucAn.size() ; i++){
                            tong += Long.parseLong(dsThucAn.get(i).price) * Integer.parseInt(dsThucAn.get(i).number);
                        }
                        txtBanTinhTienChu.setText(NumberToWords(tong) +" Đồng");
                        txtBanTinhTienSo.setText(tong+" Đồng");
                        return;
                        //break;
                    }
                    //tong += Long.parseLong(dsThucAn.get(i).price) * Integer.parseInt(dsThucAn.get(i).number);
                }
                dsThucAn.add(new DanhSachThucAn(thucAn.hinhAnh , thucAn.name , thucAn.number , thucAn.price , thucAn.banMay));
                Collections.sort(dsThucAn, new Comparator<DanhSachThucAn>() {
                    @Override
                    public int compare(DanhSachThucAn danhSachThucAn, DanhSachThucAn t1) {
                        return danhSachThucAn.getName().compareTo(t1.getName());
                    }
                });
                int k = 0;
                while (k < dsThucAn.size()){
                    if (Integer.parseInt(dsThucAn.get(k).getNumber() )== 0){
                        dsThucAn.remove(k);
                    }
                    k++;
                }
                adapterThucAn.notifyDataSetChanged();
                if (dsThucAn.isEmpty()){
                    mData.child("ID").child(ID_PHONG).child(banAn).child("DachSachThucAn").setValue("null", new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                            mData.child("ID").child(ID_PHONG).child(banAn).child("NumberFood").setValue("0", new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                    Toast.makeText(getApplicationContext() , "Đã Dọn Bàn !" , Toast.LENGTH_SHORT).show();
                                    mData.child("ID").child(ID_PHONG).child(banAn).child("DanhSachThucAn").setValue("null");
                                    dsThucAn.clear();
                                    adapterThucAn.notifyDataSetChanged();
                                    txtBanTinhTienChu.setText("Không");
                                    txtBanTinhTienSo.setText("0");
                                }
                            });
                        }
                    });
                }
                for(i = 0 ; i <dsThucAn.size() ; i++){
                    tong += Long.parseLong(dsThucAn.get(i).price) * Integer.parseInt(dsThucAn.get(i).number);
                }
                txtBanTinhTienChu.setText(NumberToWords(tong) + " Đồng");
                txtBanTinhTienSo.setText(tong+" Đồng");
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                dsThucAn.clear();
                adapterThucAn.notifyDataSetChanged();
                txtBanTinhTienSo.setText("0");
                txtBanTinhTienChu.setText("Không");
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void DonTatCa(int i) {
        varRun = 0;
        final int k = i +1;
        progressDialog.setMessage("Số Bàn Đã Dọn = " + i);
        progressDialog.dismiss();
        progressDialog.show();
        if (i == 51) {
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext() , "Đã Dọn Tất Cả Bàn !" , Toast.LENGTH_SHORT).show();
            return;
        }
            mData.child("ID").child(ID_PHONG).child("Bàn "+i).child("DanhSachThucAn").setValue("null", new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                    if(++varRun == 2){
                        DonTatCa(k);
                    }
                }
            });
            mData.child("ID").child(ID_PHONG).child("Bàn "+i).child("NumberFood").setValue("0", new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                    if(++varRun == 2){
                        DonTatCa(k);
                    }
                }
            });

    }

    private void AnhXa() {
        Bundle bundle = getIntent().getExtras();
        if(bundle!= null){
            banAn = bundle.getString("BanAn");
        }
        txtBanTinhTien = findViewById(R.id.txtBanTinhTien);
        txtBanTinhTien.setText(banAn);
        mDataset = new ArrayList<>();

        mDataset.add(new DanhSachThucAn(R.drawable.pig+"" , "Thịt Heo" , "1" , "250000" , txtBanTinhTien.getText().toString()));
        mDataset.add(new DanhSachThucAn(R.drawable.chicken+"" , "Thịt Gà Hầm" , "1" , "280000", txtBanTinhTien.getText().toString()));
        mDataset.add(new DanhSachThucAn(R.drawable.dog+"" , "Thịt Chó Thui" , "1" , "550000", txtBanTinhTien.getText().toString()));
        mDataset.add(new DanhSachThucAn(R.drawable.flower+"" , "Hoa Tươi" , "1" , "33000", txtBanTinhTien.getText().toString()));

        Collections.sort(mDataset, new Comparator<DanhSachThucAn>() {
            @Override
            public int compare(DanhSachThucAn danhSachThucAn, DanhSachThucAn t1) {
                return danhSachThucAn.getName().compareTo(t1.getName());
            }
        });
        lvThemMon = findViewById(R.id.lvThemMon);
        lvThemMon.setHasFixedSize(true);
        lvThemMonManager = new LinearLayoutManager(this , LinearLayoutManager.HORIZONTAL , false);
        lvThemMon.setLayoutManager(lvThemMonManager);

        lvThemMonAdapter = new MainAdapter(mDataset , this);

        lvThemMon.setAdapter(lvThemMonAdapter);
        txtBanTinhTienChu = findViewById(R.id.txtBanTinhTienChu);
        txtBanTinhTienSo = findViewById(R.id.txtBanTinhTienSo);

        btnDonBan = findViewById(R.id.btnDonBan);
        btnDonTatCa = findViewById(R.id.btnDonTatCa);


        lvThucAn = findViewById(R.id.lvBanTinhTien);
        dsThucAn = new ArrayList<DanhSachThucAn>();
        adapterThucAn = new AdapterThucAn(TinhTienActivity.this , R.layout.custom_thuc_an , dsThucAn);
        lvThucAn.setAdapter(adapterThucAn);
        progressDialog = new ProgressDialog(TinhTienActivity.this);

        builder = new AlertDialog.Builder(TinhTienActivity.this , android.R.style.Theme_DeviceDefault_Light_Dialog);
        builder.setTitle("Cảnh Báo");

        builder.setIcon(android.R.drawable.stat_sys_warning);

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
                    words += " " + unitsMap.get(Integer.parseInt(number+"") % 10);
            }
        }

        return words;
    }
}
