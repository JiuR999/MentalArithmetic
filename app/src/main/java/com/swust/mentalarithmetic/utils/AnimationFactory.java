package com.swust.mentalarithmetic.utils;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.swust.mentalarithmetic.R;

public class AnimationFactory {
    private Context context;

    public AnimationFactory(Context context) {
        this.context = context;
    }
    public Animation slideOut(){
        return AnimationUtils.loadAnimation(context, R.anim.slide_out);
    }

    public Animation slideIn(){
        return AnimationUtils.loadAnimation(context, R.anim.slide_in);
    }
}
