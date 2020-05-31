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
import com.example.android.testpopularmovies.models.SeasonTVShow;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SeasonsAdapter extends RecyclerView.Adapter<SeasonsAdapter.SeasonsViewHolder> {

    Context context;
    ArrayList<SeasonTVShow> seasons;
    private static SeasonsAdapter.OnItemClickListener mListener;

    public SeasonsAdapter(Context context, ArrayList<SeasonTVShow> seasons) {
        this.context = context;
        this.seasons = seasons;
    }

    public interface OnItemClickListener {
        void onSeasonClick(int position, ArrayList<SeasonTVShow> seasons);
    }

    public static void setOnItemClickListener(SeasonsAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public SeasonsAdapter.SeasonsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.season_item, parent, false);
        return new SeasonsAdapter.SeasonsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SeasonsAdapter.SeasonsViewHolder holder, int position) {

        Picasso.get().load(seasons.get(position).getPosterPath()).placeholder(R.drawable.image_placeholder).into(holder.season_poster);
        holder.season_number.setText("Season "+seasons.get(position).getSeasonNumber());
        holder.episode_count.setText(seasons.get(position).getEpisodeCount()+" episodes");
    }

    @Override
    public int getItemCount() {
        return seasons.size();
    }

    public class SeasonsViewHolder extends RecyclerView.ViewHolder {

        ImageView season_poster;
        TextView season_number, episode_count;

        public SeasonsViewHolder(@NonNull View itemView) {
            super(itemView);
            season_number = itemView.findViewById(R.id.season_number);
            episode_count = itemView.findViewById(R.id.episode_number);
            season_poster = itemView.findViewById(R.id.poster);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onSeasonClick(position, seasons);
                        }
                    }
                }
            });
        }
    }
}
