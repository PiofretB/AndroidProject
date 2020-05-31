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

public class DisplayAdapter extends RecyclerView.Adapter<DisplayAdapter.DisplayViewHolder> {

    Context context;
    ArrayList<Object> list;
    private static DisplayAdapter.OnItemClickListener mListener;

    public DisplayAdapter(Context context, ArrayList<Object> list) {
        this.context = context;
        this.list = list;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, ArrayList<Object> list);
    }

    public static void setOnItemClickListener(DisplayAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public DisplayAdapter.DisplayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item, parent, false);
        return new DisplayAdapter.DisplayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DisplayAdapter.DisplayViewHolder holder, int position) {

        try {
            Movie movie = (Movie) list.get(position);
            Picasso.get().load(movie.getPosterPath()).placeholder(R.drawable.image_placeholder).into(holder.picture);
            holder.title.setText(movie.getTitle());
        } catch (Exception e) {
            e.printStackTrace();
            TVShow show = (TVShow) list.get(position);
            Picasso.get().load(show.getPosterPath()).placeholder(R.drawable.image_placeholder).into(holder.picture);
            holder.title.setText(show.getName());
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class DisplayViewHolder extends RecyclerView.ViewHolder {

        ImageView picture;
        TextView title;

        public DisplayViewHolder(@NonNull View itemView) {
            super(itemView);
            picture = itemView.findViewById(R.id.thumbnail);
            title = itemView.findViewById(R.id.title);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onItemClick(position, list);
                        }
                    }
                }
            });

        }
    }
}
