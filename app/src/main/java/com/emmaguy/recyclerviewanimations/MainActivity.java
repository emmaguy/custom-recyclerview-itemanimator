package com.emmaguy.recyclerviewanimations;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements OnItemClickListener {
    @Bind(R.id.recyclerview) RecyclerView recyclerView;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        recyclerView.setAdapter(new ColouredTileAdapter(this, buildColourList()));
        recyclerView.setItemAnimator(new ExpandItemAnimator());
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
    }

    @NonNull private List<Integer> buildColourList() {
        final List<Integer> colours = new ArrayList<>();
        final Random random = new Random();
        for (int i = 0; i < 30; i++) {
            final int colour = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
            colours.add(colour);
        }
        return colours;
    }

    @Override public void onItemClicked(@NonNull final View view) {
        final int itemAdapterPosition = recyclerView.getChildAdapterPosition(view);
        if (itemAdapterPosition == RecyclerView.NO_POSITION) {
            return;
        }

        recyclerView.getAdapter().notifyItemChanged(itemAdapterPosition);
    }
}
