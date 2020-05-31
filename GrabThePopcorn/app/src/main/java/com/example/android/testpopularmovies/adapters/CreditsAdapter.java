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
import com.example.android.testpopularmovies.models.Movie;
import com.example.android.testpopularmovies.models.TVShow;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CreditsAdapter extends RecyclerView.Adapter<CreditsAdapter.CreditsViewHolder> {

    Context context;
    ArrayList<Object> creditsMovieList;
    //ArrayList<TVShow> creditsShowList;
    private static CreditsAdapter.OnItemClickListener mListener;

    public CreditsAdapter(Context context, ArrayList<Object> creditsMovieList) {
        this.context = context;
        this.creditsMovieList = creditsMovieList;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, ArrayList<Object> creditsList);
    }

    public static void setOnItemClickListener(CreditsAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public CreditsAdapter.CreditsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false);
        return new CreditsAdapter.CreditsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CreditsAdapter.CreditsViewHolder holder, int position) {
        //Log.d("ITEM", String.valueOf(creditsMovieList.get(position)));
        try {
            Movie movie = (Movie) creditsMovieList.get(position);
            Picasso.get().load(movie.getPosterPath()).placeholder(R.drawable.image_placeholder).into(holder.picture);
            holder.title.setText(movie.getTitle());
        } catch (Exception e) {
            e.printStackTrace();
            TVShow show = (TVShow) creditsMovieList.get(position);
            Picasso.get().load(show.getPosterPath()).placeholder(R.drawable.image_placeholder).into(holder.picture);
            holder.title.setText(show.getName());
        }


    }

    @Override
    public int getItemCount() {
        return creditsMovieList.size();
    }

    public class CreditsViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        ImageView picture;

        public CreditsViewHolder(@NonNull View itemView) {
            super(itemView);
            picture = itemView.findViewById(R.id.img_movie);
            title = itemView.findViewById(R.id.title_movie);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onItemClick(position, creditsMovieList);
                        }
                    }
                }
            });

        }
    }
}