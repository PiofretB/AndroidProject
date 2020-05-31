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
import com.example.android.testpopularmovies.models.TVShow;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TVShowAdapter extends RecyclerView.Adapter<TVShowAdapter.ShowViewHolder> {

    private Context context;
    ArrayList<TVShow> showList;
    private static OnShowClickListener mListener;
    public static final float POSTER_ASPECT_RATIO = 1.5f;


    public interface OnShowClickListener {
        void onShowClick(int position, ArrayList<TVShow> showList);
    }

    public static void setOnShowClickListener(OnShowClickListener listener) {
        mListener = listener;
    }

    public TVShowAdapter(Context context, ArrayList<TVShow> showList) {
        this.context = context;
        this.showList = showList;
    }

    @NonNull
    @Override
    public TVShowAdapter.ShowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item, parent, false);
        TVShowAdapter.ShowViewHolder showViewHolder = new TVShowAdapter.ShowViewHolder(view);

        return showViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TVShowAdapter.ShowViewHolder holder, int position) {
        Picasso.get().load(showList.get(position).getPosterPath()).placeholder(R.drawable.image_placeholder).into(holder.Thumbnail);
    }

    @Override
    public int getItemCount() {
        return showList.size();
    }

    public class ShowViewHolder extends RecyclerView.ViewHolder {

        TextView title, synopsis, rating;
        ImageView Thumbnail;

        public ShowViewHolder(@NonNull View itemView) {
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
                            mListener.onShowClick(position, showList);
                        }
                    }
                }
            });
        }
    }
}
