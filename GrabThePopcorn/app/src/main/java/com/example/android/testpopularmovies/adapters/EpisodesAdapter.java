package com.example.android.testpopularmovies.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.testpopularmovies.R;
import com.example.android.testpopularmovies.models.EpisodeTVShow;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class EpisodesAdapter extends RecyclerView.Adapter<EpisodesAdapter.EpisodesViewHolder> {

    Context context;
    ArrayList<EpisodeTVShow> episodes;

    public EpisodesAdapter(Context context, ArrayList<EpisodeTVShow> episodes) {
        this.context = context;
        this.episodes = episodes;
    }

    @NonNull
    @Override
    public EpisodesAdapter.EpisodesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.episode_item, parent, false);
        return new EpisodesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EpisodesAdapter.EpisodesViewHolder holder, int position) {

        Picasso.get().load(episodes.get(position).getStillPath()).placeholder(R.drawable.image_placeholder).into(holder.still);
        holder.episode_title.setText(episodes.get(position).getName());
        holder.episode_overview.setText(episodes.get(position).getOverview());
        holder.episode_number.setText("Episode "+String.valueOf(episodes.get(position).getEpisodeNumber()));
    }

    @Override
    public int getItemCount() {
        return episodes.size();
    }

    public class EpisodesViewHolder extends RecyclerView.ViewHolder {

        ImageView still;
        TextView episode_number, episode_title, episode_overview;

        public EpisodesViewHolder(@NonNull View itemView) {
            super(itemView);

            still = itemView.findViewById(R.id.episode_still);
            episode_number = itemView.findViewById(R.id.episode_number);
            episode_overview = itemView.findViewById(R.id.season_title);
            episode_title = itemView.findViewById(R.id.episode_title);
        }
    }
}
