package com.example.navigationmap.ui.clubselector;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.navigationmap.R;
import com.example.navigationmap.room.Club;

import java.util.List;

public class ClubAdapter extends RecyclerView.Adapter<ClubAdapter.ClubAdapterViewHolder> {

    private List<Club> clubs;

    @NonNull
    @Override
    public ClubAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_club_entry, parent, false);
        return new ClubAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClubAdapterViewHolder holder, int position) {
        holder.updateView(clubs.get(position));
    }

    @Override
    public int getItemCount() {
        return clubs.size();
    }

    public void setClubs(List<Club> clubs) {
        this.clubs = clubs;
    }
    private void deselectAllItems() {
        for(Club club : clubs) {
            club.setSelected(false);
        }
        notifyDataSetChanged();
    }

    public class ClubAdapterViewHolder extends RecyclerView.ViewHolder {

        private TextView tvClubName;
        private TextView tvAvgDistance;
        private TextView tvLongestDistance;
        private RadioButton radioButton;
        private Club club;

        public ClubAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            tvClubName = itemView.findViewById(R.id.tvClubName);
            tvAvgDistance = itemView.findViewById(R.id.tvAvg);
            tvLongestDistance = itemView.findViewById(R.id.tvLongest);
            radioButton = itemView.findViewById(R.id.rbSelected);
            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deselectAllItems();
                    club.setSelected(true);
                    radioButton.setChecked(true);
                    notifyDataSetChanged();
                }
            });
        }

        public void updateView(Club club) {
            this.club = club;
            tvClubName.setText(club.getClub());
            tvAvgDistance.setText(Integer.toString(club.getAvgDistance()));
            tvLongestDistance.setText(Integer.toString(club.getLongestDistance()));
            radioButton.setChecked(club.getSelected());
        }
    }
}
