package com.emmaguy.recyclerviewanimations;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import java.util.HashMap;

public class ExpandItemAnimator extends DefaultItemAnimator {
    private static final float MAX_SCALE = 2f;

    private final HashMap<RecyclerView.ViewHolder, AnimatorInfo> animatorMap = new HashMap<>();

    @Override public boolean canReuseUpdatedViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder) {
        return true;
    }

    @Override public boolean animateChange(@NonNull final RecyclerView.ViewHolder oldHolder, @NonNull final RecyclerView.ViewHolder newHolder, @NonNull final ItemHolderInfo preInfo, @NonNull final ItemHolderInfo postInfo) {
        final ColouredTileAdapter.ViewHolder holder = (ColouredTileAdapter.ViewHolder) newHolder;

        final DecelerateInterpolator decelerateInterpolator = new DecelerateInterpolator(2f);
        final AccelerateInterpolator accelerateInterpolator = new AccelerateInterpolator(2f);

        final ObjectAnimator zoomInXAnimator = ObjectAnimator.ofFloat(holder.itemView, View.SCALE_X, MAX_SCALE);
        final ObjectAnimator zoomInYAnimator = ObjectAnimator.ofFloat(holder.itemView, View.SCALE_Y, MAX_SCALE);

        final ObjectAnimator zoomOutXAnimator = ObjectAnimator.ofFloat(holder.itemView, View.SCALE_X, 1f);
        final ObjectAnimator zoomOutYAnimator = ObjectAnimator.ofFloat(holder.itemView, View.SCALE_Y, 1f);

        zoomInXAnimator.setInterpolator(decelerateInterpolator);
        zoomInYAnimator.setInterpolator(decelerateInterpolator);

        zoomOutXAnimator.setInterpolator(accelerateInterpolator);
        zoomOutYAnimator.setInterpolator(accelerateInterpolator);

        final AnimatorSet overallAnim = new AnimatorSet();
        overallAnim.play(zoomInXAnimator).with(zoomInYAnimator);
        overallAnim.play(zoomOutXAnimator).with(zoomOutYAnimator).after(zoomInXAnimator);
        overallAnim.addListener(new AnimatorListenerAdapter() {
            @Override public void onAnimationEnd(@NonNull final Animator animation) {
                dispatchAnimationFinished(newHolder);
                animatorMap.remove(newHolder);
            }
        });

        final AnimatorInfo animatorInfo = animatorMap.get(newHolder);
        if (animatorInfo != null) {
            final boolean firstHalf = animatorInfo.zoomInXAnimator.isRunning();
            if (firstHalf) {
                zoomInXAnimator.setCurrentPlayTime(animatorInfo.zoomInXAnimator.getCurrentPlayTime());
                zoomInYAnimator.setCurrentPlayTime(animatorInfo.zoomInYAnimator.getCurrentPlayTime());
            } else {
                zoomInXAnimator.setCurrentPlayTime(animatorInfo.zoomInXAnimator.getDuration());
                zoomInYAnimator.setCurrentPlayTime(animatorInfo.zoomInYAnimator.getDuration());
                zoomOutXAnimator.setCurrentPlayTime(animatorInfo.zoomOutXAnimator.getCurrentPlayTime());
                zoomOutYAnimator.setCurrentPlayTime(animatorInfo.zoomOutYAnimator.getCurrentPlayTime());
            }
            animatorInfo.overallAnim.cancel();
            animatorMap.remove(newHolder);
        }

        animatorMap.put(newHolder, new AnimatorInfo(overallAnim, zoomInXAnimator, zoomInYAnimator, zoomOutXAnimator, zoomOutYAnimator));
        overallAnim.start();
        holder.itemView.bringToFront();

        return super.animateChange(oldHolder, newHolder, preInfo, postInfo);
    }

    private static class AnimatorInfo {
        final Animator overallAnim;
        final ObjectAnimator zoomInXAnimator;
        final ObjectAnimator zoomInYAnimator;
        final ObjectAnimator zoomOutXAnimator;
        final ObjectAnimator zoomOutYAnimator;

        private AnimatorInfo(@NonNull final Animator overallAnim, @NonNull final ObjectAnimator zoomInXAnimator, @NonNull final ObjectAnimator zoomInYAnimator, @NonNull final ObjectAnimator zoomOutXAnimator, @NonNull final ObjectAnimator zoomOutYAnimator) {
            this.overallAnim = overallAnim;
            this.zoomInXAnimator = zoomInXAnimator;
            this.zoomInYAnimator = zoomInYAnimator;
            this.zoomOutXAnimator = zoomOutXAnimator;
            this.zoomOutYAnimator = zoomOutYAnimator;
        }
    }
}
