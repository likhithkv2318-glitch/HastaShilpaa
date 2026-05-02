package com.hastashilpa.app.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hastashilpa.app.R;
import com.hastashilpa.app.models.Design;

import java.util.List;
import java.util.Locale;

public class FeaturedPagerAdapter extends RecyclerView.Adapter<FeaturedPagerAdapter.ViewHolder> {

    private final List<Design> designs;
    private final OnDesignClickListener listener;

    public interface OnDesignClickListener {
        void onClick(Design design);
    }

    // Gradient backgrounds for card variety
    private static final int[] BG_COLORS = {
            0xFF2D5016, 0xFF1A3009, 0xFFC8860A, 0xFF4A7C28, 0xFF9A6508
    };

    public FeaturedPagerAdapter(List<Design> designs, OnDesignClickListener listener) {
        this.designs = designs;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_featured_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Design design = designs.get(position);
        holder.bind(design, position);
    }

    @Override
    public int getItemCount() { return designs.size(); }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvCategory, tvMarketValue, tvDifficulty, tvHours, tvTag, tvDesc;
        View cardBg;
        ImageView ivDesign;

        ViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvFeaturedTitle);
            tvCategory = itemView.findViewById(R.id.tvFeaturedCategory);
            tvMarketValue = itemView.findViewById(R.id.tvFeaturedValue);
            tvDifficulty = itemView.findViewById(R.id.tvFeaturedDifficulty);
            tvHours = itemView.findViewById(R.id.tvFeaturedHours);
            tvTag = itemView.findViewById(R.id.tvFeaturedTag);
            tvDesc = itemView.findViewById(R.id.tvFeaturedDesc);
            cardBg = itemView.findViewById(R.id.featuredCardRoot);
            ivDesign = itemView.findViewById(R.id.ivFeaturedDesign);
        }

        void bind(Design design, int position) {
            tvTitle.setText(design.getTitle());
            tvCategory.setText(design.getCategory());
            tvMarketValue.setText("₹" + String.format(Locale.getDefault(), "%.0f", design.getMarketValue()));
            tvDifficulty.setText(design.getDifficulty());
            tvHours.setText(design.getEstimatedHours() + " hrs");
            tvDesc.setText(design.getDescription());
            tvTag.setText("🔥 " + design.getTag());

            // Blueprint icon for visual variety
            ivDesign.setImageResource(R.drawable.ic_blueprint);
            ivDesign.setColorFilter(Color.argb(80, 255, 255, 255));

            itemView.setOnClickListener(v -> {
                if (listener != null) listener.onClick(design);
            });
        }
    }
}
