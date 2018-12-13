package com.example.phamson.quanlynhahang;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    GridView grBanAn;
    ArrayList<DanhSachBanAn> dsBanan;
    AdapterBanAn adapterBanAn;

    public static DatabaseReference mData;
    public static ArrayList<Boolean> listCheckBanAn;
    SharedPreferences pre;
    SharedPreferences.Editor edit;
    boolean check = false;
    public static String ID_PHONG ;

    ArrayList<DanhSachThucAn> dsThucAn;

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

        for (int i = 1 ; i <=50 ; i++){
            final int finalI = i;
            mData.child("ID").child("1122").child("Bàn "+i).child("NumberFood").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue().toString().equals("0")){
                        listCheckBanAn.set(finalI -1,true);
                    }else{
                        listCheckBanAn.set(finalI -1,false);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void AnhXa() {
        grBanAn = findViewById(R.id.grBanAn);
        //grBanAn.setPadding(0 , 10 ,0 ,10);
        dsBanan = new ArrayList<>();
        listCheckBanAn = new ArrayList<>();

        for (int i = 1 ; i <= 50 ; i++){
            dsBanan.add(new DanhSachBanAn("Bàn "+i));
            listCheckBanAn.add(false);
        }

        adapterBanAn = new AdapterBanAn(MainActivity.this  ,R.layout.custom_ban_an , dsBanan);
        grBanAn.setAdapter(adapterBanAn);

        registerForContextMenu(grBanAn);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        if (item.getTitle() == "Chuyển Bàn"){
            final Dialog dialog = new Dialog(MainActivity.this);
            dialog.setContentView(R.layout.dialog_chuyen_ban);

            final EditText edtChuyenBan = dialog.findViewById(R.id.dialog_edtBanChuyen);
            TextView txtChuyenBan = dialog.findViewById(R.id.dialog_txtBanChuyen);
            Button btnChuyenBan = dialog.findViewById(R.id.dialog_btnChuyenBan);
            txtChuyenBan.setText(dsBanan.get(info.position).tenBan);

            dialog.show();
            btnChuyenBan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (Integer.parseInt(edtChuyenBan.getText().toString()) == 0 || Integer.parseInt(edtChuyenBan.getText().toString() ) > dsBanan.size() || edtChuyenBan.getText().toString().equals(dsBanan.get(info.position).tenBan.replace("Bàn ","")) ){
                        Toast.makeText(getApplicationContext() , "Bàn Không Hợp Lệ" , Toast.LENGTH_SHORT).show();
                    }else {
                        ChuyenBan(info.position+1 , Integer.parseInt(edtChuyenBan.getText().toString()));
                    }

                }
            });

        }
        return super.onContextItemSelected(item);
    }

    private void ChuyenBan(final int banChuyen , final int banBiChuyen) {
        if (!listCheckBanAn.get(banBiChuyen-1)){
            Toast.makeText(getApplicationContext() , "Bàn " + banBiChuyen +" Hiện Đang Có Ngưới" , Toast.LENGTH_SHORT).show();
        }else{
            final TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Intent intent = new Intent(MainActivity.this , MainActivity.class);

                            startActivity(intent);
                            finish();
                            System.exit(0);
                        }
                    });
                }
            };
            Timer timer = new Timer();
            timer.schedule(timerTask , 10000);
            ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setTitle("Thông Báo");
            progressDialog.setMessage("Cần 10 Giây Đang Chuyển Bàn");
            progressDialog.setCancelable(false);
            progressDialog.show();
            // lay thong tin ban chuyen toi
            mData.child("ID").child(ID_PHONG).child("Bàn " + banChuyen).child("DanhSachThucAn").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    ThucAn thucAn = dataSnapshot.getValue(ThucAn.class);
                    mData.child("ID").child(ID_PHONG).child("Bàn "+banBiChuyen).child("DanhSachThucAn").push().setValue(thucAn);

                        mData.child("ID").child(ID_PHONG).child("Bàn " + banChuyen).child("DachSachThucAn").setValue("null", new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                mData.child("ID").child(ID_PHONG).child("Bàn " + banChuyen).child("NumberFood").setValue("0", new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                        mData.child("ID").child(ID_PHONG).child("Bàn " + banChuyen).child("DanhSachThucAn").setValue("null");
                                        mData.child("ID").child(ID_PHONG).child("Bàn " + banBiChuyen).child("NumberFood").setValue("1");

                                    }
                                });
                            }
                        });

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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add("Chuyển Bàn");
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public void onBackPressed() {

    }
}
