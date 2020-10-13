package com.mohakchavan.pustakniparab.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.mohakchavan.pustakniparab.Models.DashBoard.DashBoard;
import com.mohakchavan.pustakniparab.R;

import java.util.List;

public class Dashboard_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<DashBoard> dashboardList;

    public Dashboard_Adapter(Context context, List<DashBoard> dashboardList) {
        this.context = context;
        this.dashboardList = dashboardList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view;
        switch (viewType) {
            case 1:
                view = inflater.inflate(R.layout.dashboard_view_simple, parent, false);
                return new Dashboard_Simple_ViewHolder(view);
            case 2:
                view = inflater.inflate(R.layout.dashboard_view_two_sided, parent, false);
                return new Dashboard_TwoSided_ViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final DashBoard record = dashboardList.get(position);
        switch (holder.getItemViewType()) {
            case 1:
                ((Dashboard_Simple_ViewHolder) holder).dv_ll_tv_simpleTop.setText(String.valueOf(record.getData()[0].getTopData()));
                ((Dashboard_Simple_ViewHolder) holder).dv_ll_tv_simpleBottom.setText(record.getData()[0].getBottomData());
                LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[]{
                        new DataPoint(0, 1),
                        new DataPoint(1, 3),
                        new DataPoint(2, 5),
                        new DataPoint(3, 3)
                });
                series.setColor(Color.WHITE);
                ((Dashboard_Simple_ViewHolder) holder).dv_ll_gv_simpleTop.addSeries(series);
                ((Dashboard_Simple_ViewHolder) holder).dv_ll_gv_simpleTop.getViewport().setMinX(0);
                ((Dashboard_Simple_ViewHolder) holder).dv_ll_gv_simpleTop.getViewport().setMaxX(5);
                ((Dashboard_Simple_ViewHolder) holder).dv_ll_gv_simpleTop.getViewport().setMinY(0);
                ((Dashboard_Simple_ViewHolder) holder).dv_ll_gv_simpleTop.getViewport().setMaxY(7);
                ((Dashboard_Simple_ViewHolder) holder).dv_ll_gv_simpleTop.getViewport().setXAxisBoundsManual(true);
                ((Dashboard_Simple_ViewHolder) holder).dv_ll_gv_simpleTop.getViewport().setYAxisBoundsManual(true);
//                ((Dashboard_Simple_ViewHolder) holder).dv_ll_gv_simpleTop.
                break;
            case 2:
                ((Dashboard_TwoSided_ViewHolder) holder).dv_ll_tv_leftTop.setText(String.valueOf(record.getData()[0].getTopData()));
                ((Dashboard_TwoSided_ViewHolder) holder).dv_ll_tv_leftBottom.setText(String.valueOf(record.getData()[0].getBottomData()));
                ((Dashboard_TwoSided_ViewHolder) holder).dv_ll_tv_rightTop.setText(String.valueOf(record.getData()[1].getTopData()));
                ((Dashboard_TwoSided_ViewHolder) holder).dv_ll_tv_rightBottom.setText(String.valueOf(record.getData()[1].getBottomData()));
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (dashboardList.get(position).isSimple())
            return 1;
        else
            return 2;
    }

    @Override
    public int getItemCount() {
        return dashboardList.size();
    }

    public class Dashboard_Simple_ViewHolder extends RecyclerView.ViewHolder {

        TextView dv_ll_tv_simpleTop, dv_ll_tv_simpleBottom;
        GraphView dv_ll_gv_simpleTop;

        public Dashboard_Simple_ViewHolder(@NonNull View itemView) {
            super(itemView);

            dv_ll_tv_simpleTop = itemView.findViewById(R.id.dv_ll_tv_simpleTop);
            dv_ll_tv_simpleBottom = itemView.findViewById(R.id.dv_ll_tv_simpleBottom);
            dv_ll_gv_simpleTop = itemView.findViewById(R.id.dv_ll_gv_simpleTop);
        }
    }

    public class Dashboard_TwoSided_ViewHolder extends RecyclerView.ViewHolder {

        TextView dv_ll_tv_leftTop, dv_ll_tv_leftBottom, dv_ll_tv_rightTop, dv_ll_tv_rightBottom;

        public Dashboard_TwoSided_ViewHolder(@NonNull View itemView) {
            super(itemView);

            dv_ll_tv_leftTop = itemView.findViewById(R.id.dv_ll_tv_leftTop);
            dv_ll_tv_leftBottom = itemView.findViewById(R.id.dv_ll_tv_leftBottom);
            dv_ll_tv_rightTop = itemView.findViewById(R.id.dv_ll_tv_rightTop);
            dv_ll_tv_rightBottom = itemView.findViewById(R.id.dv_ll_tv_rightBottom);
        }
    }
}
