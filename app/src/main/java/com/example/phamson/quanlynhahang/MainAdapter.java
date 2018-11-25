package com.example.phamson.quanlynhahang;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


import static com.example.phamson.quanlynhahang.MainActivity.ID_PHONG;
import static com.example.phamson.quanlynhahang.MainActivity.mData;
import static com.example.phamson.quanlynhahang.TinhTienActivity.banAn;

class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private ArrayList<DanhSachThucAn> mDataset;
    private Activity context;

    public MainAdapter(ArrayList<DanhSachThucAn> mDataset, Activity context) {
        this.mDataset = mDataset;
        this.context = context;
    }

    @NonNull
    @Override
    public MainAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row , viewGroup , false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MainAdapter.ViewHolder viewHolder, final int i) {
        viewHolder.mTitle.setText(mDataset.get(i).getName());
        viewHolder.imageView.setImageResource(Integer.parseInt(mDataset.get(i).getHinhAnh()));
        viewHolder.ln.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DanhSachThucAn thucAn = new DanhSachThucAn(mDataset.get(i).getHinhAnh() , mDataset.get(i).getName() , mDataset.get(i).getNumber(), mDataset.get(i).getPrice() );
                mData.child("ID").child(ID_PHONG).child(banAn).child("DanhSachThucAn").push().setValue(thucAn);
                mData.child("ID").child(ID_PHONG).child(banAn).child("NumberFood").setValue("1");
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView mTitle;
        public LinearLayout ln;
        public ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.custom_imgThemMon);
            mTitle = itemView.findViewById(R.id.title);
            ln = itemView.findViewById(R.id.rvliner);
        }
    }
}
