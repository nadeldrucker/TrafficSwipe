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
import java.util.function.Consumer;

public class RecyclerSearchAdapter extends RecyclerView.Adapter<RecyclerSearchAdapter.ViewHolder> {

    private List<Abbreviation> abbreviationList = new ArrayList<>();
    private Consumer<Abbreviation> listener;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final Button btnAbbreviation;
        final TextView tvName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            btnAbbreviation = itemView.findViewById(R.id.tvAbbreviation);
            tvName = itemView.findViewById(R.id.tvName);
        }
    }

    public void updateAbbreviationList(List<Abbreviation> abbreviationList) {
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

        holder.btnAbbreviation.setText(abbreviation.getAbbreviation());
        holder.tvName.setText(abbreviation.getName());

        holder.btnAbbreviation.setOnClickListener(v -> {
            if (listener != null) {
                listener.accept(abbreviationList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return abbreviationList.size();
    }

    public void setItemButtonClickedListener(Consumer<Abbreviation> listener) {
        this.listener = listener;
    }
}
