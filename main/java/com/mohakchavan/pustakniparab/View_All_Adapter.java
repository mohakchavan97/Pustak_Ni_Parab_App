package com.mohakchavan.pustakniparab;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mohakchavan.pustakniparab.Models.Names;

import java.util.List;

public class View_All_Adapter extends RecyclerView.Adapter<View_All_Adapter.View_All_ViewHolder> {

    private Context context;
    private List<Names> namesList;

    public View_All_Adapter(Context context, List<Names> namesList) {
        this.context = context;
        this.namesList = namesList;
    }

    @NonNull
    @Override
    public View_All_ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.card_view, null);
        return new View_All_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull View_All_ViewHolder view_all_viewHolder, int i) {
        Names names = namesList.get(i);
        view_all_viewHolder.va_ser.setText(String.valueOf(names.getSer_no()));
        view_all_viewHolder.va_name.setText(new StringBuilder().append(names.getFirstName()).append(" ").append(names.getLastName()).toString());
        view_all_viewHolder.va_blk.setText(names.getBlockOrHouseNum());
        view_all_viewHolder.va_strt.setText(names.getStreetName());
        view_all_viewHolder.va_area.setText(names.getArea());
        view_all_viewHolder.va_call.setText(String.valueOf(names.getCall()));
    }

    @Override
    public int getItemCount() {
        return namesList.size();
    }

    public class View_All_ViewHolder extends RecyclerView.ViewHolder {

        TextView va_ser, va_name, va_blk, va_strt, va_area, va_call;

        public View_All_ViewHolder(@NonNull View itemView) {
            super(itemView);

            va_ser = itemView.findViewById(R.id.cv_tv_ser);
            va_name = itemView.findViewById(R.id.cv_tv_name);
            va_blk = itemView.findViewById(R.id.cv_tv_blk);
            va_strt = itemView.findViewById(R.id.cv_tv_strt);
            va_area = itemView.findViewById(R.id.cv_tv_area);
            va_call = itemView.findViewById(R.id.cv_tv_call);
        }
    }

}
