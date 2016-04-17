package com.emmaguy.recyclerviewanimations;

import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ColouredTileAdapter extends RecyclerView.Adapter<ColouredTileAdapter.ViewHolder> {
    private final OnItemClickListener listener;
    private final List<Integer> colours;

    public ColouredTileAdapter(@NonNull final OnItemClickListener listener, @NonNull final List<Integer> colours) {
        this.listener = listener;
        this.colours = colours;
    }

    @Override public ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                listener.onItemClicked(v);
            }
        });
        return viewHolder;
    }

    @Override public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.bind(colours.get(position), position);
    }

    @Override public int getItemCount() {
        return colours.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.title) TextView titleTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        private void bind(@ColorInt final Integer colour, final int position) {
            itemView.setBackgroundColor(colour);
            titleTextView.setText(String.valueOf(position));
        }
    }
}
