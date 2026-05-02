package com.hastashilpa.app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hastashilpa.app.R;
import com.hastashilpa.app.models.Design;

import java.util.List;
import java.util.Locale;

public class BlueprintGridAdapter extends RecyclerView.Adapter<BlueprintGridAdapter.ViewHolder> {

    private final List<Design> designs;
    private final OnBlueprintClickListener listener;

    public interface OnBlueprintClickListener {
        void onClick(Design design);
    }

    public BlueprintGridAdapter(List<Design> designs, OnBlueprintClickListener listener) {
        this.designs = designs;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_blueprint_grid, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(designs.get(position));
    }

    @Override
    public int getItemCount() { return designs.size(); }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvCategory, tvDifficulty, tvDimCount, tvValue;

        ViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvBpTitle);
            tvCategory = itemView.findViewById(R.id.tvBpCategory);
            tvDifficulty = itemView.findViewById(R.id.tvBpDifficulty);
            tvDimCount = itemView.findViewById(R.id.tvBpDimCount);
            tvValue = itemView.findViewById(R.id.tvBpValue);
        }

        void bind(Design design) {
            tvTitle.setText(design.getTitle());
            tvCategory.setText(design.getCategory());
            tvDifficulty.setText(design.getDifficulty());
            int dimCount = design.getDimensions() != null ? design.getDimensions().size() : 0;
            tvDimCount.setText(dimCount + " dimensions");
            tvValue.setText("₹" + String.format(Locale.getDefault(), "%.0f", design.getMarketValue()));

            int diffColor;
            switch (design.getDifficulty()) {
                case "Easy": diffColor = 0xFF34A853; break;
                case "Hard": diffColor = 0xFFEA4335; break;
                default: diffColor = 0xFFC8860A; break;
            }
            tvDifficulty.setTextColor(diffColor);

            itemView.setOnClickListener(v -> {
                if (listener != null) listener.onClick(design);
            });
        }
    }
}
