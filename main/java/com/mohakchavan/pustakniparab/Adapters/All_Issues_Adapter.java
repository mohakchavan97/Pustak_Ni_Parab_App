package com.mohakchavan.pustakniparab.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mohakchavan.pustakniparab.Models.Issues;
import com.mohakchavan.pustakniparab.R;

import java.util.List;

public class All_Issues_Adapter extends RecyclerView.Adapter<All_Issues_Adapter.All_Issues_ViewHolder> implements Filterable {

    private Context context;
    private List<Issues> filteredIssuesList;
    private List<Issues> originalIssuesList;
    private All_Issues_Adapter.All_Issues_Filter filter = new All_Issues_Adapter.All_Issues_Filter();

    public All_Issues_Adapter(Context context, List<Issues> originalIssuesList) {
        this.context = context;
        this.filteredIssuesList = originalIssuesList;
        this.originalIssuesList = originalIssuesList;
    }

    @NonNull
    @Override
    public All_Issues_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.card_view_issues, null);
        return new All_Issues_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull All_Issues_ViewHolder holder, int position) {
        Issues issues = filteredIssuesList.get(position);
        holder.is_tv_issId.setText(String.valueOf(issues.getIssueNo()));
        holder.is_tv_bkName.setText(issues.getBookName());
        holder.is_tv_nameId.setText(issues.getIssuerId());
        holder.is_tv_fullName.setText(issues.getIssuerName());
        holder.is_tv_issDate.setText(issues.getIssueDate());
        holder.is_cb.setChecked(false);
    }

    @Override
    public int getItemCount() {
        return filteredIssuesList.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    public class All_Issues_ViewHolder extends RecyclerView.ViewHolder {

        TextView is_tv_issId, is_tv_bkName, is_tv_nameId, is_tv_fullName, is_tv_issDate;
        CheckBox is_cb;

        public All_Issues_ViewHolder(@NonNull View itemView) {
            super(itemView);

            is_tv_issId = itemView.findViewById(R.id.is_tv_issId);
            is_tv_bkName = itemView.findViewById(R.id.is_tv_bkName);
            is_tv_nameId = itemView.findViewById(R.id.is_tv_nameId);
            is_tv_fullName = itemView.findViewById(R.id.is_tv_fullName);
            is_tv_issDate = itemView.findViewById(R.id.is_tv_issDate);
            is_cb = itemView.findViewById(R.id.is_cb);
        }
    }

    public class All_Issues_Filter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            return null;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

        }
    }
}
