package com.hastashilpa.app.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.hastashilpa.app.BlueprintDetailActivity;
import com.hastashilpa.app.R;
import com.hastashilpa.app.adapters.DesignCardAdapter;
import com.hastashilpa.app.adapters.FeaturedPagerAdapter;
import com.hastashilpa.app.models.Design;
import com.hastashilpa.app.utils.DataRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * FIXED: Replaced NestedScrollView + ViewPager2 with a ConcatAdapter approach.
 * Root cause of ANR: ViewPager2 (built on RecyclerView) inside NestedScrollView
 * caused a layout measurement deadlock on the main thread.
 *
 * Fix: Single top-level RecyclerView with ConcatAdapter combining:
 *   1. HeaderAdapter      — green gradient header with search bar
 *   2. ChipsAdapter       — category filter chip row
 *   3. FeaturedAdapter    — featured cards in horizontal RecyclerView
 *   4. SectionLabelAdapter— "All Designs" label row
 *   5. DesignCardAdapter  — individual design cards (existing adapter reused)
 */
public class TrendsFragment extends Fragment {

    private RecyclerView rvMain;
    private DesignCardAdapter designCardAdapter;
    private List<Design> allDesigns;

    // Held so search can drive chip filter and vice versa
    private ChipGroup chipGroup;
    private EditText etSearch;
    private String activeCategory = null; // null = All

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_trends, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvMain = view.findViewById(R.id.rvMain);
        allDesigns = DataRepository.getAllDesigns();

        setupConcatAdapter();
    }

    private void setupConcatAdapter() {
        // 1 — Header adapter (inflates item_trends_header, wires search bar)
        HeaderAdapter headerAdapter = new HeaderAdapter();

        // 2 — Chips adapter (inflates item_trends_chips, wires chip group)
        ChipsAdapter chipsAdapter = new ChipsAdapter();

        // 3 — Featured horizontal cards (replaces ViewPager2)
        FeaturedSectionAdapter featuredSectionAdapter = new FeaturedSectionAdapter(
                DataRepository.getFeaturedDesigns()
        );

        // 4 — "All Designs" section label
        SectionLabelAdapter sectionLabelAdapter = new SectionLabelAdapter();

        // 5 — Design cards list
        designCardAdapter = new DesignCardAdapter(new ArrayList<>(allDesigns), design -> {
            openBlueprintDetail(design);
        });

        ConcatAdapter concatAdapter = new ConcatAdapter(
                headerAdapter,
                chipsAdapter,
                featuredSectionAdapter,
                sectionLabelAdapter,
                designCardAdapter
        );

        rvMain.setLayoutManager(new LinearLayoutManager(getContext()));
        rvMain.setAdapter(concatAdapter);
    }

    private void applyFilters() {
        String query = etSearch != null ? etSearch.getText().toString().trim() : "";
        List<Design> filtered = DataRepository.getDesignsByCategory(activeCategory);
        if (!query.isEmpty()) {
            filtered = searchInList(filtered, query);
        }
        designCardAdapter.updateData(filtered);
    }

    private List<Design> searchInList(List<Design> source, String query) {
        if (query.isEmpty()) return source;
        List<Design> result = new ArrayList<>();
        String lower = query.toLowerCase();
        for (Design d : source) {
            if (d.getTitle().toLowerCase().contains(lower) ||
                    d.getCategory().toLowerCase().contains(lower) ||
                    d.getDescription().toLowerCase().contains(lower)) {
                result.add(d);
            }
        }
        return result;
    }

    private void openBlueprintDetail(Design design) {
        Intent intent = new Intent(getActivity(), BlueprintDetailActivity.class);
        intent.putExtra("design", design);
        startActivity(intent);
        if (requireActivity() != null) {
            requireActivity().overridePendingTransition(
                    android.R.anim.slide_in_left, android.R.anim.fade_out);
        }
    }

    // ─────────────────────────── Inner Adapters ────────────────────────────

    /** Header: green gradient + title + search bar */
    class HeaderAdapter extends RecyclerView.Adapter<HeaderAdapter.VH> {

        @NonNull @Override
        public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_trends_header, parent, false);
            return new VH(v);
        }

        @Override public void onBindViewHolder(@NonNull VH holder, int position) {}
        @Override public int getItemCount() { return 1; }

        class VH extends RecyclerView.ViewHolder {
            VH(@NonNull View itemView) {
                super(itemView);
                etSearch = itemView.findViewById(R.id.etSearch);
                etSearch.addTextChangedListener(new TextWatcher() {
                    @Override public void beforeTextChanged(CharSequence s, int i, int c, int a) {}
                    @Override public void onTextChanged(CharSequence s, int i, int b, int c) {
                        applyFilters();
                    }
                    @Override public void afterTextChanged(Editable s) {}
                });
            }
        }
    }

    /** Chips row: category filter chips */
    class ChipsAdapter extends RecyclerView.Adapter<ChipsAdapter.VH> {

        @NonNull @Override
        public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_trends_chips, parent, false);
            return new VH(v);
        }

        @Override public void onBindViewHolder(@NonNull VH holder, int position) {}
        @Override public int getItemCount() { return 1; }

        class VH extends RecyclerView.ViewHolder {
            VH(@NonNull View itemView) {
                super(itemView);
                chipGroup = itemView.findViewById(R.id.chipGroup);

                String[] categories = {"All", "Office", "Home Décor", "Kitchen", "Garden"};
                for (String cat : categories) {
                    Chip chip = new Chip(itemView.getContext());
                    chip.setText(cat);
                    chip.setCheckable(true);
                    chip.setChecked(cat.equals("All"));
                    chip.setChipBackgroundColor(
                            android.content.res.ColorStateList.valueOf(
                                    itemView.getContext().getColor(R.color.chip_bg)));
                    chip.setTextColor(
                            android.content.res.ColorStateList.valueOf(
                                    itemView.getContext().getColor(R.color.chip_text)));
                    chip.setTypeface(null, android.graphics.Typeface.BOLD);
                    chipGroup.addView(chip);

                    chip.setOnCheckedChangeListener((btn, isChecked) -> {
                        if (isChecked) {
                            activeCategory = cat.equals("All") ? null : cat;
                            applyFilters();
                        }
                    });
                }
                chipGroup.setSingleSelection(true);
            }
        }
    }

    /** Featured section: label + horizontal RecyclerView (replaces ViewPager2) */
    class FeaturedSectionAdapter extends RecyclerView.Adapter<FeaturedSectionAdapter.VH> {

        private final List<Design> featured;

        FeaturedSectionAdapter(List<Design> featured) {
            this.featured = featured;
        }

        @NonNull @Override
        public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_trends_featured_section, parent, false);
            return new VH(v);
        }

        @Override
        public void onBindViewHolder(@NonNull VH holder, int position) {
            // adapter set once in VH constructor - nothing to rebind
        }

        @Override public int getItemCount() { return 1; }

        class VH extends RecyclerView.ViewHolder {
            RecyclerView rvFeatured;
            VH(@NonNull View itemView) {
                super(itemView);
                rvFeatured = itemView.findViewById(R.id.rvFeatured);
                rvFeatured.setLayoutManager(
                        new LinearLayoutManager(itemView.getContext(),
                                LinearLayoutManager.HORIZONTAL, false));
                // Set adapter once here, not in onBindViewHolder
                FeaturedPagerAdapter featuredAdapter = new FeaturedPagerAdapter(featured,
                        design -> openBlueprintDetail(design));
                rvFeatured.setAdapter(featuredAdapter);
                // Pre-cache item views
                rvFeatured.setItemViewCacheSize(5);
            }
        }
    }

    /** "All Designs" section label row */
    class SectionLabelAdapter extends RecyclerView.Adapter<SectionLabelAdapter.VH> {

        @NonNull @Override
        public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_trends_section_label, parent, false);
            return new VH(v);
        }

        @Override public void onBindViewHolder(@NonNull VH holder, int position) {}
        @Override public int getItemCount() { return 1; }

        class VH extends RecyclerView.ViewHolder {
            VH(@NonNull View itemView) { super(itemView); }
        }
    }
}
