package com.example.phamson.quanlynhahang;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class AdapterThucAn extends ArrayAdapter {
    Activity context;
    int resource;
    List<DanhSachThucAn> objects;
    public AdapterThucAn(Activity context, int resource, List<DanhSachThucAn> objects) {
        super(context, resource, objects);
        this.objects = objects;
        this.context = context;
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = this.context.getLayoutInflater();
        View row = inflater.inflate(this.resource,null);

        TextView txtThucAn = row.findViewById(R.id.txtMonAn);
        TextView txtSoLuong = row.findViewById(R.id.txtSoLuong);
        TextView txtGia = row.findViewById(R.id.txtGia);
        ImageView imgThucAn = row.findViewById(R.id.imgMonAn);

        DanhSachThucAn danhSachThucAn = this.objects.get(position);
        txtThucAn.setText(danhSachThucAn.getName());
        txtSoLuong.setText(danhSachThucAn.getNumber() +"");
        txtGia.setText(danhSachThucAn.getPrice()+"");
        imgThucAn.setImageResource(Integer.parseInt(danhSachThucAn.getHinhAnh()));

        return  row;
    }
}
