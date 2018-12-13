package com.example.phamson.quanlynhahang;

import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import static com.example.phamson.quanlynhahang.MainActivity.listCheckBanAn;

public class AdapterBanAn extends ArrayAdapter<DanhSachBanAn> {

    ;
    DatabaseReference mData;

    Activity context;
    int resource;
    List<DanhSachBanAn> objects;
    public AdapterBanAn(Activity context, int resource, List<DanhSachBanAn> objects) {
        super(context, resource, objects);
        this.objects = objects;
        this.context = context;
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = this.context.getLayoutInflater();
        View row = inflater.inflate(this.resource,null);
        mData = FirebaseDatabase.getInstance().getReference();
        TextView txtBanAn = row.findViewById(R.id.custom_txt_banan);
        final ImageView imgBanAn = row.findViewById(R.id.custom_imgBanAn);

        DanhSachBanAn danhSachBanAn = this.objects.get(position);

        txtBanAn.setText(danhSachBanAn.getTenBan());


        mData.child("ID").child("1122").child(danhSachBanAn.getTenBan()).child("NumberFood").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue().toString().equals("0")){
                    imgBanAn.setBackgroundColor(Color.GREEN);
                }else{
                    imgBanAn.setBackgroundColor(Color.RED);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return  row;
    }
}
