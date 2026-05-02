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

public class DesignCardAdapter extends RecyclerView.Adapter<DesignCardAdapter.ViewHolder> {

    private List<Design> designs;
    private final OnDesignClickListener listener;

    public interface OnDesignClickListener {
        void onClick(Design design);
    }

    public DesignCardAdapter(List<Design> designs, OnDesignClickListener listener) {
        this.designs = designs;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_design_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(designs.get(position));
    }

    @Override
    public int getItemCount() { return designs.size(); }

    public void updateData(List<Design> newData) {
        this.designs = newData;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvCategory, tvDifficulty, tvHours, tvValue, tvTag,
                tvMaterials, tvBamboo, tvCane;

        ViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvCardTitle);
            tvCategory = itemView.findViewById(R.id.tvCardCategory);
            tvDifficulty = itemView.findViewById(R.id.tvCardDifficulty);
            tvHours = itemView.findViewById(R.id.tvCardHours);
            tvValue = itemView.findViewById(R.id.tvCardValue);
            tvTag = itemView.findViewById(R.id.tvCardTag);
            tvMaterials = itemView.findViewById(R.id.tvCardMaterials);
            tvBamboo = itemView.findViewById(R.id.tvCardBamboo);
            tvCane = itemView.findViewById(R.id.tvCardCane);
        }

        void bind(Design design) {
            tvTitle.setText(design.getTitle());
            tvCategory.setText(design.getCategory());
            tvDifficulty.setText(design.getDifficulty());
            tvHours.setText(design.getEstimatedHours() + " hrs");
            tvValue.setText("₹" + String.format(Locale.getDefault(), "%.0f", design.getMarketValue()));
            tvBamboo.setText(design.getBambooPoles());
            tvCane.setText(design.getCaneMeters());

            // Materials summary
            if (design.getMaterials() != null && !design.getMaterials().isEmpty()) {
                tvMaterials.setText(design.getMaterials().size() + " materials needed");
            }

            // Tag visibility
            String tag = design.getTag();
            if ("TRENDING".equals(tag)) {
                tvTag.setVisibility(View.VISIBLE);
                tvTag.setText("🔥 Trending");
                tvTag.setBackgroundResource(R.drawable.bg_tag_trending);
                tvTag.setTextColor(0xFFFFFFFF);
            } else if ("NEW".equals(tag)) {
                tvTag.setVisibility(View.VISIBLE);
                tvTag.setText("✨ New");
                tvTag.setBackgroundResource(R.drawable.bg_tag_new);
                tvTag.setTextColor(0xFFFFFFFF);
            } else {
                tvTag.setVisibility(View.GONE);
            }

            // Difficulty color
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
