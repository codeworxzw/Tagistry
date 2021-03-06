package com.pk.tagger.recyclerview;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pk.tagger.R;

import java.util.List;

/**
 * Created by PK on 27/02/2016.
 */
public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Genre mGenre, int position, List<Genre> mGenres);
    }
    // instantiate genres and listener
    private List<Genre> mGenres;
    private final OnItemClickListener listener;

    private SparseBooleanArray mSelectedPositions = new SparseBooleanArray();
    private Boolean mIsSelectable = false;

    public GenreAdapter(List<Genre> genres, OnItemClickListener listener) {
        this.listener = listener;
        mGenres = genres;
    }
    // Create empty adapter and holder here
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView nameTextView;
        //public ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);

            nameTextView = (TextView) itemView.findViewById(R.id.genre_name);
            //imageView = (ImageView) itemView.findViewById(R.id.genre_image);
        }

        public void bind(final Genre genre, final OnItemClickListener listener, final int position, final List<Genre> genresL) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(genre, position, genresL);
                }
            });
        }
    }
    // return the holder after inflating the item_genre xml
    @Override
    public GenreAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View genreView = inflater.inflate(R.layout.item_genre, parent, false);

        ViewHolder viewHolder = new ViewHolder(genreView);
        return viewHolder;
    }
    // populate data into the item using viewholder
    @Override
    public void onBindViewHolder(GenreAdapter.ViewHolder viewHolder, int position) {

        Genre genre = mGenres.get(position);
        TextView textView = viewHolder.nameTextView;
        textView.setText(genre.getName());
        //ImageView imageView = viewHolder.imageView;
        if (genre.getgSelected()) {
            textView.setBackgroundColor(Color.rgb(122, 125, 125));
            //imageView.setImageResource(R.drawable.note2);
        } else {
            textView.setBackgroundColor(Color.rgb(0, 0, 0));
            //imageView.setImageResource(R.drawable.note2invert);
        }
        viewHolder.bind(mGenres.get(position), listener, position, mGenres);

    }

    @Override
    public int getItemCount() {
        return mGenres.size();
    }

    public void setGenres(List<Genre> genresL) {
        mGenres = genresL;
    }
}
