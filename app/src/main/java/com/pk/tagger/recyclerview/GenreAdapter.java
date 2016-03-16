package com.pk.tagger.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pk.tagger.R;

import java.util.List;

/**
 * Created by PK on 27/02/2016.
 */
public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.ViewHolder> {

    private List<Genre> mGenres;

    public GenreAdapter(List<Genre> genres) {

        mGenres = genres;

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {


        public TextView nameTextView;
        public ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);

            nameTextView = (TextView) itemView.findViewById(R.id.genre_name);
            imageView = (ImageView) itemView.findViewById(R.id.genre_image);
        }
    }

    @Override
    public GenreAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View genreView = inflater.inflate(R.layout.item_genre, parent, false);

        ViewHolder viewHolder = new ViewHolder(genreView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(GenreAdapter.ViewHolder viewHolder, int position) {

        Genre genre = mGenres.get(position);

        TextView textView = viewHolder.nameTextView;

        textView.setText(genre.getName());

       ImageView imageView = viewHolder.imageView;

        imageView.setImageResource(R.drawable.note2);

    }

    @Override
    public int getItemCount() {
        return mGenres.size();
    }
}