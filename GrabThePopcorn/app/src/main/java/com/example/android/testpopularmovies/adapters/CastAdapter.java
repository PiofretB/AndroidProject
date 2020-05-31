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
import com.example.android.testpopularmovies.models.Cast;
import com.example.android.testpopularmovies.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CastAdapter extends RecyclerView.Adapter<CastAdapter.CastViewHolder> {

    Context context;
    ArrayList<Cast> castList;
    private static CastAdapter.OnItemClickListener mListener;

    public CastAdapter(Context context, ArrayList<Cast> mCast) {
        this.context = context;
        this.castList = mCast;
    }


    public interface OnItemClickListener {
        void onItemClick(int position, ArrayList<Cast> castList);
    }

    public static void setOnItemClickListener(CastAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public CastAdapter.CastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cast_item, parent, false);
        return new CastViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CastAdapter.CastViewHolder holder, int position) {

        Picasso.get().load(castList.get(position).getImg_link()).placeholder(R.drawable.default_cast_foreground).into(holder.picture);
        holder.name.setText(castList.get(position).getName());
        holder.role.setText(castList.get(position).getRole());
    }

    @Override
    public int getItemCount() {
        return castList.size();
    }

    public class CastViewHolder extends RecyclerView.ViewHolder {

        TextView name, role;
        ImageView picture;

        public CastViewHolder(@NonNull View itemView) {
            super(itemView);
            picture = itemView.findViewById(R.id.img_cast);
            name = itemView.findViewById(R.id.name_cast);
            role = itemView.findViewById(R.id.role_cast);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onItemClick(position, castList);
                        }
                    }
                }
            });

        }
    }
}
