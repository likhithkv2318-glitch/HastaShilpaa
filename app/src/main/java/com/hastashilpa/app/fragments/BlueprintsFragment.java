package com.hastashilpa.app.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hastashilpa.app.BlueprintDetailActivity;
import com.hastashilpa.app.R;
import com.hastashilpa.app.adapters.BlueprintGridAdapter;
import com.hastashilpa.app.models.Design;
import com.hastashilpa.app.utils.DataRepository;

import java.util.List;

public class BlueprintsFragment extends Fragment {

    private RecyclerView rvBlueprints;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_blueprints, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvBlueprints = view.findViewById(R.id.rvBlueprints);

        List<Design> designs = DataRepository.getAllDesigns();

        BlueprintGridAdapter adapter = new BlueprintGridAdapter(designs, design -> {
            Intent intent = new Intent(getActivity(), BlueprintDetailActivity.class);
            intent.putExtra("design", design);
            intent.putExtra("show_blueprint", true);
            startActivity(intent);
            requireActivity().overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.fade_out);
        });

        rvBlueprints.setLayoutManager(new GridLayoutManager(getContext(), 2));
        rvBlueprints.setAdapter(adapter);
    }
}
