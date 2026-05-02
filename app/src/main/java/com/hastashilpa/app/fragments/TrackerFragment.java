package com.hastashilpa.app.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.hastashilpa.app.R;
import com.hastashilpa.app.adapters.BatchAdapter;
import com.hastashilpa.app.models.Batch;
import com.hastashilpa.app.utils.PrefsHelper;

import java.util.List;

public class TrackerFragment extends Fragment {

    private RecyclerView rvBatches;
    private BatchAdapter adapter;
    private PrefsHelper prefsHelper;
    private TextView tvTotalPoles, tvTotalCane, tvTotalHours, tvTotalUnits, tvEmptyState;
    private ExtendedFloatingActionButton fabAddBatch;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tracker, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        prefsHelper = new PrefsHelper(requireContext());

        rvBatches = view.findViewById(R.id.rvBatches);
        tvTotalPoles = view.findViewById(R.id.tvTotalPoles);
        tvTotalCane = view.findViewById(R.id.tvTotalCane);
        tvTotalHours = view.findViewById(R.id.tvTotalHours);
        tvTotalUnits = view.findViewById(R.id.tvTotalUnits);
        tvEmptyState = view.findViewById(R.id.tvEmptyState);
        fabAddBatch = view.findViewById(R.id.fabAddBatch);

        setupRecyclerView();
        updateSummary();

        fabAddBatch.setOnClickListener(v -> showAddBatchDialog());
    }

    private void setupRecyclerView() {
        List<Batch> batches = prefsHelper.getAllBatches();
        adapter = new BatchAdapter(batches, batchId -> {
            prefsHelper.deleteBatch(batchId);
            adapter.removeBatch(batchId);
            updateSummary();
            checkEmptyState();
        });
        rvBatches.setLayoutManager(new LinearLayoutManager(getContext()));
        rvBatches.setAdapter(adapter);
        checkEmptyState();
    }

    private void checkEmptyState() {
        if (adapter.getItemCount() == 0) {
            tvEmptyState.setVisibility(View.VISIBLE);
            rvBatches.setVisibility(View.GONE);
        } else {
            tvEmptyState.setVisibility(View.GONE);
            rvBatches.setVisibility(View.VISIBLE);
        }
    }

    private void updateSummary() {
        List<Batch> batches = prefsHelper.getAllBatches();
        int totalPoles = 0;
        double totalCane = 0;
        double totalHours = 0;
        int totalUnits = 0;
        for (Batch b : batches) {
            totalPoles += b.getBambooPoles();
            totalCane += b.getCaneMeters();
            totalHours += b.getHoursWorked();
            totalUnits += b.getUnitsProduced();
        }
        tvTotalPoles.setText(String.valueOf(totalPoles));
        tvTotalCane.setText(String.format("%.1f m", totalCane));
        tvTotalHours.setText(String.format("%.1f", totalHours));
        tvTotalUnits.setText(String.valueOf(totalUnits));
    }

    private void showAddBatchDialog() {
        Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_batch);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        EditText etBatchName = dialog.findViewById(R.id.etBatchName);
        EditText etProductType = dialog.findViewById(R.id.etProductType);
        EditText etBambooPoles = dialog.findViewById(R.id.etBambooPoles);
        EditText etCaneMeters = dialog.findViewById(R.id.etCaneMeters);
        EditText etHoursWorked = dialog.findViewById(R.id.etHoursWorked);
        EditText etUnitsProduced = dialog.findViewById(R.id.etUnitsProduced);
        EditText etCostPerUnit = dialog.findViewById(R.id.etCostPerUnit);
        EditText etNotes = dialog.findViewById(R.id.etNotes);
        Button btnSave = dialog.findViewById(R.id.btnSaveBatch);
        Button btnCancel = dialog.findViewById(R.id.btnCancelBatch);

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        btnSave.setOnClickListener(v -> {
            String batchName = etBatchName.getText().toString().trim();
            String productType = etProductType.getText().toString().trim();
            String polesStr = etBambooPoles.getText().toString().trim();
            String caneStr = etCaneMeters.getText().toString().trim();
            String hoursStr = etHoursWorked.getText().toString().trim();
            String unitsStr = etUnitsProduced.getText().toString().trim();
            String costStr = etCostPerUnit.getText().toString().trim();
            String notes = etNotes.getText().toString().trim();

            if (batchName.isEmpty() || polesStr.isEmpty() || unitsStr.isEmpty()) {
                Toast.makeText(getContext(), "Please fill required fields", Toast.LENGTH_SHORT).show();
                return;
            }

            int poles = Integer.parseInt(polesStr);
            double cane = caneStr.isEmpty() ? 0 : Double.parseDouble(caneStr);
            double hours = hoursStr.isEmpty() ? 0 : Double.parseDouble(hoursStr);
            int units = Integer.parseInt(unitsStr);
            double cost = costStr.isEmpty() ? 0 : Double.parseDouble(costStr);

            Batch batch = new Batch(batchName, productType, poles, cane, hours, units, cost, notes);
            prefsHelper.saveBatch(batch);
            adapter.addBatch(batch);
            updateSummary();
            checkEmptyState();
            dialog.dismiss();
            Toast.makeText(getContext(), "Batch saved!", Toast.LENGTH_SHORT).show();
        });

        dialog.show();
    }
}
