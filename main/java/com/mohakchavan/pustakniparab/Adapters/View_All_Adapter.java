package com.mohakchavan.pustakniparab.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.mohakchavan.pustakniparab.Models.Names;
import com.mohakchavan.pustakniparab.NameModule.AddPerson;
import com.mohakchavan.pustakniparab.R;

import java.util.ArrayList;
import java.util.List;

public class View_All_Adapter extends RecyclerView.Adapter<View_All_Adapter.View_All_ViewHolder> implements Filterable {

    private Context context;
    private List<Names> filteredNamesList;
    private List<Names> originalNamesList;
    private View_All_Filter filter = new View_All_Filter();

    public View_All_Adapter(Context context, List<Names> namesList) {
        this.context = context;
        this.filteredNamesList = namesList;
        this.originalNamesList = namesList;
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
        final Names names = filteredNamesList.get(i);
        view_all_viewHolder.va_ser.setText(String.valueOf(names.getSer_no()));
        view_all_viewHolder.va_name.setText(names.returnFullName());
        view_all_viewHolder.va_blk.setText(names.getBlkOrFltNo());
        view_all_viewHolder.va_strt.setText(names.getStreetName());
        view_all_viewHolder.va_area.setText(names.getLocalityOrArea());
        view_all_viewHolder.va_call.setText(String.valueOf(names.getContact()));
        view_all_viewHolder.cv_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddPerson.class);
                intent.putExtra("nameId", String.valueOf(names.getSer_no()));
                intent.putExtra("view", true);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return filteredNamesList.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    public class View_All_ViewHolder extends RecyclerView.ViewHolder {

        TextView va_ser, va_name, va_blk, va_strt, va_area, va_call;
        CardView cv_cv;

        public View_All_ViewHolder(@NonNull View itemView) {
            super(itemView);

            va_ser = itemView.findViewById(R.id.cv_tv_ser);
            va_name = itemView.findViewById(R.id.cv_tv_name);
            va_blk = itemView.findViewById(R.id.cv_tv_blk);
            va_strt = itemView.findViewById(R.id.cv_tv_strt);
            va_area = itemView.findViewById(R.id.cv_tv_area);
            va_call = itemView.findViewById(R.id.cv_tv_call);
            cv_cv = itemView.findViewById(R.id.cv_cv);
        }
    }

    private class View_All_Filter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            List<Names> filteredOutput = new ArrayList<>(originalNamesList.size());

            if (!constraint.toString().isEmpty()) {
                String filterString = constraint.toString().trim().toUpperCase();
                for (Names name : originalNamesList) {
                    if (String.valueOf(name.getSer_no()).contentEquals(filterString) ||
                            name.getFirstName().contains(filterString) || name.getLastName().contains(filterString) ||
                            name.returnFullName().contains(filterString)) {
                        filteredOutput.add(name);
                    }
                }
            } else {
                filteredOutput = originalNamesList;
            }
            results.values = filteredOutput;
            results.count = filteredOutput.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredNamesList = (List<Names>) results.values;
            notifyDataSetChanged();
        }
    }
}
