package com.ychong.kankan.utils.animator;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import java.util.ArrayList;
import java.util.List;

public class FlyAnimator extends SimpleItemAnimator {
    private List<RecyclerView.ViewHolder> removeHolders = new ArrayList<>();
    private List<RecyclerView.ViewHolder> removeAnimators = new ArrayList<>();

    private List<RecyclerView.ViewHolder> moveHolders = new ArrayList<>();
    private List<RecyclerView.ViewHolder> moveAnimators = new ArrayList<>();
    @Override
    public boolean animateRemove(RecyclerView.ViewHolder holder) {
        removeHolders.add(holder);
        return true;
    }

    @Override
    public boolean animateAdd(RecyclerView.ViewHolder holder) {
        return false;
    }

    @Override
    public boolean animateMove(RecyclerView.ViewHolder holder, int fromX, int fromY, int toX, int toY) {
        holder.itemView.setTranslationY(fromY-toX);
        moveHolders.add(holder);
        return true;
    }

    @Override
    public boolean animateChange(RecyclerView.ViewHolder oldHolder, RecyclerView.ViewHolder newHolder, int fromLeft, int fromTop, int toLeft, int toTop) {
        return false;
    }

    @Override
    public void runPendingAnimations() {
        if (!removeHolders.isEmpty()){
            for (RecyclerView.ViewHolder holder:removeHolders){
                remove(holder);
            }
            removeHolders.clear();
        }
        if (!moveHolders.isEmpty()){
            for (RecyclerView.ViewHolder holder:moveHolders){
                move(holder);
            }
            moveHolders.clear();
        }

    }

    @Override
    public void endAnimation(@NonNull RecyclerView.ViewHolder item) {

    }

    @Override
    public void endAnimations() {

    }

    @Override
    public boolean isRunning() {
        return !(removeHolders.isEmpty()&&removeAnimators.isEmpty()
        &&moveHolders.isEmpty()&&moveAnimators.isEmpty());
    }
    private void remove(RecyclerView.ViewHolder holder){
        removeAnimators.add(holder);
        TranslateAnimation animation = new TranslateAnimation(0,1000,0,0);
        animation.setDuration(5000);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                dispatchRemoveStarting(holder);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                removeAnimators.remove(holder);
                dispatchRemoveFinished(holder);
                if (!isRunning()){
                    dispatchAnimationsFinished();
                }

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        holder.itemView.startAnimation(animation);
    }

    private void move(RecyclerView.ViewHolder holder){
        moveAnimators.add(holder);
        ObjectAnimator animator = ObjectAnimator.ofFloat(holder.itemView,"translationY",holder.itemView.getTranslationY(),0);
        animator.setDuration(500);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                dispatchMoveFinished(holder);
                moveAnimators.remove(holder);
                if (!isRunning()){
                    dispatchAnimationFinished(holder);
                }
            }

            @Override
            public void onAnimationStart(Animator animation) {
                dispatchMoveStarting(holder);
            }
        });
        animator.start();
    }
}
