package com.mohakchavan.pustakniparab.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mohakchavan.pustakniparab.Models.Issues;
import com.mohakchavan.pustakniparab.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class All_Issues_Adapter extends RecyclerView.Adapter<All_Issues_Adapter.All_Issues_ViewHolder> implements Filterable {

    private Context context;
    private List<Issues> filteredIssuesList;
    private List<Issues> originalIssuesList;
    private ChangeListener listener;
    private All_Issues_Adapter.All_Issues_Filter filter = new All_Issues_Adapter.All_Issues_Filter();
    private boolean isForJustDisplay;

    public All_Issues_Adapter(Context context, List<Issues> originalIssuesList) {
        this.context = context;
        this.filteredIssuesList = originalIssuesList;
        this.originalIssuesList = originalIssuesList;
        this.isForJustDisplay = false;
    }

    @NonNull
    @Override
    public All_Issues_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.card_view_issues, null, false);
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        return new All_Issues_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull All_Issues_ViewHolder holder, final int position) {
        Issues issues = filteredIssuesList.get(position);
        holder.is_tv_issId.setText(String.valueOf(issues.getIssueNo()));
        holder.is_tv_bkName.setText(issues.getBookName());
        holder.is_tv_nameId.setText(issues.getIssuerId());
        holder.is_tv_fullName.setText(issues.getIssuerName());
        holder.is_tv_issDate.setText(issues.getIssueDate());
        holder.is_cb.setOnCheckedChangeListener(null);
        if (isForJustDisplay) {
            holder.is_cb.setChecked(issues.getIsReturned()
                    .equalsIgnoreCase(context.getString(R.string.hasReturned)));
            holder.is_cb.setClickable(false);
        } else {
            issues.setChecked(false);
            holder.is_cb.setChecked(false);
            holder.is_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    filteredIssuesList.get(position).setChecked(isChecked);
                    if (listener != null)
                        listener.onChange();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return filteredIssuesList.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    public List<Issues> getFilteredIssuesList() {
        return filteredIssuesList;
    }

    public void setListener(ChangeListener listener) {
        this.listener = listener;
    }

    public void setForJustDisplay(boolean isForJustDisplay) {
        this.isForJustDisplay = isForJustDisplay;
    }

    public interface ChangeListener {
        public void onChange();
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
            FilterResults results = new FilterResults();
            List<Issues> filteredOutput = new ArrayList<>(originalIssuesList.size());

            if (!constraint.toString().trim().isEmpty()) {
                try {
                    JSONObject object = new JSONObject(constraint.toString().trim());

                    String filterString = object.getString("value");
//                String filterString = constraint.toString().trim().toUpperCase();

                    switch (object.getString("key")) {
                        case "nameId":
                            for (Issues issue : originalIssuesList) {
                                if (issue.getIssuerId().contentEquals(filterString)) {
                                    filteredOutput.add(issue);
                                }
                            }
                            break;

                        case "bookId":
                            for (Issues issue : originalIssuesList) {
                                if (String.valueOf(issue.getIssueNo()).contentEquals(filterString)) {
                                    filteredOutput.add(issue);
                                }
                            }
                            break;

                        case "bookName":
                            for (Issues issue : originalIssuesList) {
                                if (issue.getBookName().contains(filterString)) {
                                    filteredOutput.add(issue);
                                }
                            }
                            break;

                        case "issrName":
                            for (Issues issue : originalIssuesList) {
                                if (issue.getIssuerName().contains(filterString)) {
                                    filteredOutput.add(issue);
                                }
                            }
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                filteredOutput = originalIssuesList;
            }
            results.values = filteredOutput;
            results.count = filteredOutput.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredIssuesList = (List<Issues>) results.values;
            notifyDataSetChanged();
        }
    }
}
