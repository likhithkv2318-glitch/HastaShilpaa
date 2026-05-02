package com.hastashilpa.app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hastashilpa.app.R;
import com.hastashilpa.app.models.Batch;

import java.util.List;
import java.util.Locale;

public class BatchAdapter extends RecyclerView.Adapter<BatchAdapter.ViewHolder> {

    private final List<Batch> batches;
    private final OnDeleteListener deleteListener;

    public interface OnDeleteListener {
        void onDelete(long batchId);
    }

    public BatchAdapter(List<Batch> batches, OnDeleteListener deleteListener) {
        this.batches = batches;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_batch_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(batches.get(position));
    }

    @Override
    public int getItemCount() { return batches.size(); }

    public void addBatch(Batch batch) {
        batches.add(0, batch);
        notifyItemInserted(0);
    }

    public void removeBatch(long batchId) {
        for (int i = 0; i < batches.size(); i++) {
            if (batches.get(i).getId() == batchId) {
                batches.remove(i);
                notifyItemRemoved(i);
                return;
            }
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvBatchName, tvProductType, tvDate, tvPoles, tvCane, tvHours,
                tvUnits, tvEfficiency, tvCost;
        ImageButton btnDelete;

        ViewHolder(View itemView) {
            super(itemView);
            tvBatchName = itemView.findViewById(R.id.tvBatchName);
            tvProductType = itemView.findViewById(R.id.tvBatchProduct);
            tvDate = itemView.findViewById(R.id.tvBatchDate);
            tvPoles = itemView.findViewById(R.id.tvBatchPoles);
            tvCane = itemView.findViewById(R.id.tvBatchCane);
            tvHours = itemView.findViewById(R.id.tvBatchHours);
            tvUnits = itemView.findViewById(R.id.tvBatchUnits);
            tvEfficiency = itemView.findViewById(R.id.tvBatchEfficiency);
            tvCost = itemView.findViewById(R.id.tvBatchCost);
            btnDelete = itemView.findViewById(R.id.btnDeleteBatch);
        }

        void bind(Batch batch) {
            tvBatchName.setText(batch.getBatchName());
            tvProductType.setText(batch.getProductType() != null ? batch.getProductType() : "—");
            tvDate.setText(batch.getDate());
            tvPoles.setText(batch.getBambooPoles() + " poles");
            tvCane.setText(String.format(Locale.getDefault(), "%.1f m", batch.getCaneMeters()));
            tvHours.setText(String.format(Locale.getDefault(), "%.1f hrs", batch.getHoursWorked()));
            tvUnits.setText(batch.getUnitsProduced() + " units");

            double eff = batch.getEfficiencyRatio();
            tvEfficiency.setText(String.format(Locale.getDefault(), "%.1f poles/unit", eff));

            double cost = batch.getTotalMaterialCost();
            tvCost.setText(cost > 0 ? "₹" + String.format(Locale.getDefault(), "%.0f", cost) : "—");

            btnDelete.setOnClickListener(v -> {
                if (deleteListener != null) deleteListener.onDelete(batch.getId());
            });
        }
    }
}
