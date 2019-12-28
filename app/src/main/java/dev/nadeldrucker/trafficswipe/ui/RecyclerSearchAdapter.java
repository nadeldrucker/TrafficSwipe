package dev.nadeldrucker.trafficswipe.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import dev.nadeldrucker.trafficswipe.R;
import dev.nadeldrucker.trafficswipe.data.db.entities.Abbreviation;

import java.util.ArrayList;
import java.util.List;

public class RecyclerSearchAdapter extends RecyclerView.Adapter<RecyclerSearchAdapter.ViewHolder> {

    private List<Abbreviation> abbreviationList = new ArrayList<>();

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final Button tvAbbreviation;
        final TextView tvName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAbbreviation = itemView.findViewById(R.id.tvAbbreviation);
            tvName = itemView.findViewById(R.id.tvName);
        }
    }

    public void setAbbreviationList(List<Abbreviation> abbreviationList) {
        this.abbreviationList = abbreviationList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_search, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Abbreviation abbreviation = abbreviationList.get(position);

        holder.tvAbbreviation.setText(abbreviation.getAbbreviation());
        holder.tvName.setText(abbreviation.getName());
    }

    @Override
    public int getItemCount() {
        return abbreviationList.size();
    }
}
