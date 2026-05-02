package com.hastashilpa.app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.hastashilpa.app.R;
import com.hastashilpa.app.models.ProductListing;

import java.util.List;
import java.util.Locale;

public class ListingAdapter extends RecyclerView.Adapter<ListingAdapter.ViewHolder> {

    private List<ProductListing> listings;
    private final OnListingActionListener listener;

    public interface OnListingActionListener {
        void onAction(ProductListing listing, String action);
    }

    public ListingAdapter(List<ProductListing> listings, OnListingActionListener listener) {
        this.listings = listings;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_listing_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(listings.get(position));
    }

    @Override
    public int getItemCount() { return listings.size(); }

    public void updateData(List<ProductListing> newData) {
        this.listings = newData;
        notifyDataSetChanged();
    }

    public void updateStatus(long id, String status) {
        for (int i = 0; i < listings.size(); i++) {
            if (listings.get(i).getId() == id) {
                listings.get(i).setStatus(status);
                notifyItemChanged(i);
                return;
            }
        }
    }

    public void removeListing(long id) {
        for (int i = 0; i < listings.size(); i++) {
            if (listings.get(i).getId() == id) {
                listings.remove(i);
                notifyItemRemoved(i);
                return;
            }
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvCategory, tvDate, tvPrice, tvArtisan, tvStatus, tvQty;
        MaterialButton btnMarkSold;
        ImageButton btnDelete;

        ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvListingName);
            tvCategory = itemView.findViewById(R.id.tvListingCategory);
            tvDate = itemView.findViewById(R.id.tvListingDate);
            tvPrice = itemView.findViewById(R.id.tvListingPrice);
            tvArtisan = itemView.findViewById(R.id.tvListingArtisan);
            tvStatus = itemView.findViewById(R.id.tvListingStatus);
            tvQty = itemView.findViewById(R.id.tvListingQty);
            btnMarkSold = itemView.findViewById(R.id.btnMarkSold);
            btnDelete = itemView.findViewById(R.id.btnDeleteListing);
        }

        void bind(ProductListing listing) {
            tvName.setText(listing.getProductName());
            tvCategory.setText(listing.getCategory());
            tvDate.setText(listing.getDate());
            tvPrice.setText("₹" + String.format(Locale.getDefault(), "%.0f", listing.getAskingPrice()));
            tvArtisan.setText(listing.getArtisanName());
            tvQty.setText("Qty: " + listing.getQuantity());

            boolean available = listing.isAvailable();
            tvStatus.setText(available ? "● Available" : "✓ Sold");
            tvStatus.setTextColor(available ? 0xFF34A853 : 0xFF9E9E9E);
            btnMarkSold.setVisibility(available ? View.VISIBLE : View.GONE);

            btnMarkSold.setOnClickListener(v -> {
                if (listener != null) listener.onAction(listing, "SOLD");
            });

            btnDelete.setOnClickListener(v -> {
                if (listener != null) listener.onAction(listing, "DELETE");
            });
        }
    }
}
