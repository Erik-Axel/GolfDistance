package com.example.navigationmap.ui.clubselector;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.navigationmap.R;
import com.example.navigationmap.room.Club;
import com.example.navigationmap.room.RoomDatabase;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ClubSelectorFragment extends Fragment {

    private ClubSelecorViewModel dashboardViewModel;
    private RoomDatabase clubDataBase;
    private ClubAdapter clubAdapter;
    private FloatingActionButton fabAddClub;

    private RecyclerView rvClubs;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        clubDataBase = RoomDatabase.getInstance(getContext());
        dashboardViewModel = ViewModelProviders.of(this).get(ClubSelecorViewModel.class);
        clubAdapter = new ClubAdapter();
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        rvClubs = root.findViewById(R.id.rvClubs);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvClubs.setLayoutManager(layoutManager);
        rvClubs.setAdapter(clubAdapter);

        // test git comment

        initFab(root);
        //getStoredClubs();
        setupFakeClubs();
        return root;
    }

    private void setupFakeClubs(){
        List<Club> fakeClubs = new ArrayList<>();
        Club club1 = new Club("5");
        club1.setAvgDistance(175);
        club1.setLongestDistance(190);
        fakeClubs.add(club1);

        Club club2 = new Club("7");
        club2.setAvgDistance(160);
        club2.setLongestDistance(170);
        fakeClubs.add(club2);

        Club club3 = new Club("P");
        club3.setAvgDistance(100);
        club3.setLongestDistance(120);
        fakeClubs.add(club3);
        clubAdapter.setClubs(fakeClubs);
        clubAdapter.notifyDataSetChanged();
    }

    private void initFab(View view) {
        fabAddClub = view.findViewById(R.id.fabAddClub);
        fabAddClub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = ";";
                Navigation.findNavController(v).navigate(R.id.addClubDialog);
            }
        });
    }

    private void getStoredClubs() {
        List<Club> storedClubs =  clubDataBase.clubDao().getClubs();
        if(!storedClubs.isEmpty()) {
            clubAdapter.setClubs(storedClubs);
            clubAdapter.notifyDataSetChanged();
        }
    }
}
