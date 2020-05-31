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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ItemViewHolder> {

    private Context context;
    ArrayList<Movie> movieList;
    private static OnItemClickListener mListener;
    public static final float POSTER_ASPECT_RATIO = 1.5f;


    private class VIEW_TYPES {
        public static final int Header = 1;
        public static final int Normal = 2;
        public static final int Footer = 3;
    }
    boolean isFooter;
    boolean isHeader;

    public interface OnItemClickListener {
        void onItemClick(int position, ArrayList<Movie> movieList);
    }

    public static void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public MovieAdapter(Context context, ArrayList<Movie> movieList) {
        this.context = context;
        this.movieList = movieList;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        switch (viewType) {
            case VIEW_TYPES.Footer:
                isFooter = true;
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.footer, parent, false);
                break;
            default:
                isFooter = false;
                isHeader = false;
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item, parent, false);
                break;

        }
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapter.ItemViewHolder holder, int position) {

        if (!isHeader && !isFooter) {
            Picasso.get().load(movieList.get(position).getPosterPath()).placeholder(R.drawable.image_placeholder).into(holder.Thumbnail);
        }
        //holder.title.setText(movieList.get(position).getOriginalTitle());
        //holder.rating.setText(movieList.get(position).getVoteAverage());
        //holder.synopsis.setText(movieList.get(position).getOverview());

    }

    @Override
    public int getItemViewType(int position) {
        if (movieList.get(position).isFooter()) {
            return VIEW_TYPES.Footer;
        } else if (movieList.get(position).isHeader()) {
            return VIEW_TYPES.Header;
        } else {
            return VIEW_TYPES.Normal;
        }
    }


    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView title, synopsis, rating;
        ImageView Thumbnail;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            Thumbnail = itemView.findViewById(R.id.thumbnail);
            //title = itemView.findViewById(R.id.movie_title);
            //synopsis = itemView.findViewById(R.id.plot_synopsis);
            //rating = itemView.findViewById(R.id.user_rating);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onItemClick(position, movieList);
                        }
                    }
                }
            });
        }
    }
}
