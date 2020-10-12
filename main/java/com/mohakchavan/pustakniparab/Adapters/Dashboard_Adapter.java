package com.mohakchavan.pustakniparab.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mohakchavan.pustakniparab.Models.DashBoard.DashBoard;
import com.mohakchavan.pustakniparab.R;

import java.util.List;

public class Dashboard_Adapter extends RecyclerView.Adapter<Dashboard_Adapter.Dashboard_ViewHolder> {

    private Context context;
    private List<DashBoard> dashboardList;

    public Dashboard_Adapter(Context context, List<DashBoard> dashboardList) {
        this.context = context;
        this.dashboardList = dashboardList;
    }

    @NonNull
    @Override
    public Dashboard_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dashboard_view, parent, false);
        return new Dashboard_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Dashboard_ViewHolder holder, int position) {
        final DashBoard record = dashboardList.get(position);
        if (record.isSimple()) {
            holder.dv_ll_simple.setVisibility(View.VISIBLE);
            holder.dv_ll_tv_simpleTop.setText(String.valueOf(record.getData()[0].getTopData()));
            holder.dv_ll_tv_simpleBottom.setText(record.getData()[0].getBottomData());
        } else {
            holder.dv_ll_two_sided.setVisibility(View.VISIBLE);
            holder.dv_ll_tv_leftTop.setText(String.valueOf(record.getData()[0].getTopData()));
            holder.dv_ll_tv_leftBottom.setText(String.valueOf(record.getData()[0].getBottomData()));
            holder.dv_ll_tv_rightTop.setText(String.valueOf(record.getData()[1].getTopData()));
            holder.dv_ll_tv_rightBottom.setText(String.valueOf(record.getData()[1].getBottomData()));
        }
    }

    @Override
    public int getItemCount() {
        return dashboardList.size();
    }

    public class Dashboard_ViewHolder extends RecyclerView.ViewHolder {

        TextView dv_ll_tv_leftTop, dv_ll_tv_leftBottom, dv_ll_tv_rightTop, dv_ll_tv_rightBottom, dv_ll_tv_simpleTop, dv_ll_tv_simpleBottom;
        LinearLayout dv_ll_two_sided, dv_ll_simple;

        public Dashboard_ViewHolder(@NonNull View itemView) {
            super(itemView);

            dv_ll_tv_leftTop = itemView.findViewById(R.id.dv_ll_tv_leftTop);
            dv_ll_tv_leftBottom = itemView.findViewById(R.id.dv_ll_tv_leftBottom);
            dv_ll_tv_rightTop = itemView.findViewById(R.id.dv_ll_tv_rightTop);
            dv_ll_tv_rightBottom = itemView.findViewById(R.id.dv_ll_tv_rightBottom);
            dv_ll_tv_simpleTop = itemView.findViewById(R.id.dv_ll_tv_simpleTop);
            dv_ll_tv_simpleBottom = itemView.findViewById(R.id.dv_ll_tv_simpleBottom);
            dv_ll_two_sided = itemView.findViewById(R.id.dv_ll_two_sided);
            dv_ll_simple = itemView.findViewById(R.id.dv_ll_simple);
        }
    }
}
