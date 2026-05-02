package com.hastashilpa.app.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.hastashilpa.app.MarketplaceListActivity;
import com.hastashilpa.app.PriceSuggesterActivity;
import com.hastashilpa.app.R;
import com.hastashilpa.app.adapters.ListingAdapter;
import com.hastashilpa.app.models.ProductListing;
import com.hastashilpa.app.utils.PrefsHelper;

import java.util.List;

public class MarketplaceFragment extends Fragment {

    private RecyclerView rvListings;
    private ListingAdapter adapter;
    private PrefsHelper prefsHelper;
    private TextView tvEmptyState, tvListingCount;
    private ExtendedFloatingActionButton fabList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_marketplace, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        prefsHelper = new PrefsHelper(requireContext());
        rvListings = view.findViewById(R.id.rvListings);
        tvEmptyState = view.findViewById(R.id.tvEmptyState);
        tvListingCount = view.findViewById(R.id.tvListingCount);
        fabList = view.findViewById(R.id.fabListProduct);

        view.findViewById(R.id.btnPriceSuggester).setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), PriceSuggesterActivity.class));
        });

        fabList.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), MarketplaceListActivity.class));
        });

        loadListings();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadListings();
    }

    private void loadListings() {
        List<ProductListing> listings = prefsHelper.getAllListings();
        tvListingCount.setText(listings.size() + " listings");

        if (rvListings.getAdapter() == null) {
            adapter = new ListingAdapter(listings, (listing, action) -> {
                if ("SOLD".equals(action)) {
                    prefsHelper.updateListingStatus(listing.getId(), "SOLD");
                    adapter.updateStatus(listing.getId(), "SOLD");
                } else if ("DELETE".equals(action)) {
                    prefsHelper.deleteListing(listing.getId());
                    adapter.removeListing(listing.getId());
                    tvListingCount.setText(adapter.getItemCount() + " listings");
                    checkEmptyState();
                }
            });
            rvListings.setLayoutManager(new LinearLayoutManager(getContext()));
            rvListings.setAdapter(adapter);
        } else {
            adapter.updateData(listings);
            tvListingCount.setText(listings.size() + " listings");
        }
        checkEmptyState();
    }

    private void checkEmptyState() {
        if (adapter == null || adapter.getItemCount() == 0) {
            tvEmptyState.setVisibility(View.VISIBLE);
            rvListings.setVisibility(View.GONE);
        } else {
            tvEmptyState.setVisibility(View.GONE);
            rvListings.setVisibility(View.VISIBLE);
        }
    }
}
