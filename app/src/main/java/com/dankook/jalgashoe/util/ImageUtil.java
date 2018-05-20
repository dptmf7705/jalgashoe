package com.dankook.jalgashoe.util;

import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.bumptech.glide.request.target.Target;

import jp.wasabeef.glide.transformations.CropCircleTransformation;


public class ImageUtil {

    public static void loadImage(ImageView imageView, String url){

        if(url.contains("13.125.173.118:8080")){
            if(!url.startsWith("http://")){
                url = "http://".concat(url);
            }
        }

        Glide.with(imageView.getContext())
                .load(url)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        GlideDrawableImageViewTarget glideTarget = (GlideDrawableImageViewTarget) target;
                        ImageView view = glideTarget.getView();
                        int width = view.getMeasuredWidth();
                        int targetHeight = width * resource.getIntrinsicHeight() / resource.getIntrinsicWidth();
                        if(view.getLayoutParams().height != targetHeight){
                            view.getLayoutParams().height = targetHeight;
                            view.requestLayout();
                        }
                        return false;
                    }
                })
                .into(imageView);
    }

    @BindingAdapter(value = {"imageId"})
    public static void loadImage(ImageView imageView, int id){
        Glide.with(imageView.getContext())
                .load(id)
                .fitCenter()
                .into(imageView);
    }

    public static void loadImage(ImageView imageView, Uri url){
        Glide.with(imageView.getContext())
                .load(url)
                .listener(new RequestListener<Uri, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, Uri model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, Uri model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        GlideDrawableImageViewTarget glideTarget = (GlideDrawableImageViewTarget) target;
                        ImageView view = glideTarget.getView();
                        int width = view.getMeasuredWidth();
                        int targetHeight = width * resource.getIntrinsicHeight() / resource.getIntrinsicWidth();
                        if(view.getLayoutParams().height != targetHeight){
                            view.getLayoutParams().height = targetHeight;
                            view.requestLayout();
                        }

                        return false;
                    }
                })
                .into(imageView);
    }

    public static void loadCircleImage(ImageView imageView, String url, Drawable errorDrawable) {

        if(url.contains("13.125.173.118:8080")){
            if(!url.startsWith("http://")){
                url = "http://".concat(url);
            }
        }

        Glide.with(imageView.getContext())
                .load(url)
                .error(errorDrawable)
                .bitmapTransform(new CropCircleTransformation(Glide.get(imageView.getContext()).getBitmapPool()))
                .into(imageView);
    }

    public static void loadCircleImage(ImageView imageView, int id, Drawable errorDrawable) {
        Glide.with(imageView.getContext())
                .load(id)
                .error(errorDrawable)
                .bitmapTransform(new CropCircleTransformation(Glide.get(imageView.getContext()).getBitmapPool()))
                .into(imageView);
    }

    public static void loadCircleImage(ImageView imageView, Uri url, Drawable errorDrawable) {
        Glide.with(imageView.getContext())
                .load(url)
                .error(errorDrawable)
                .bitmapTransform(new CropCircleTransformation(Glide.get(imageView.getContext()).getBitmapPool()))
                .into(imageView);
    }
}
