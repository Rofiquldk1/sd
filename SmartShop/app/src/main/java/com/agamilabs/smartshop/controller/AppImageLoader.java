package com.agamilabs.smartshop.controller;

import android.net.Uri;
import android.widget.ImageView;

import com.agamilabs.smartshop.R;
import com.agamilabs.smartshop.Interfaces.IImageLoadedCallback;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by sazzad on 6/27/2018.
 */

public class AppImageLoader {

    // Loading an image from string url into an imageView
    public static void loadImageInView(String url, ImageView imageView) {

        if (url.trim().length() == 0) {
            Picasso.get().load(R.drawable.no_image).into(imageView);
            return;
        }

        Picasso.get().load(url).error(R.drawable.no_image).into(imageView, new Callback() {
            @Override
            public void onSuccess() {
                AppController.getAppController().getInAppNotifier().log("Picasso", "Success");
            }

            @Override
            public void onError(Exception e) {
                AppController.getAppController().getInAppNotifier().log("Picasso", "Failed " + e.toString());
            }
        });


    }

    // Loading an image from string url into an imageView
    public static void loadImageInView(String url, ImageView imageView, final Callback callback) {

        if (url.trim().length() == 0) {
            Picasso.get().load(R.drawable.no_image).into(imageView);
            return;
        }

        Picasso.get().load(url).error(R.drawable.no_image).into(imageView, new Callback() {
            @Override
            public void onSuccess() {
                AppController.getAppController().getInAppNotifier().log("Picasso", "Success");
                if(callback!=null){
                    try {
                        callback.onSuccess();
                    }catch (Exception e){

                    }
                }
            }

            @Override
            public void onError(Exception e) {
                AppController.getAppController().getInAppNotifier().log("Picasso", "Failed " + e.toString());
                if(callback!=null){
                    try {
                        callback.onError(e);
                    }catch (Exception e1){

                    }
                }
            }
        });


    }

    // Load an image from the uri into an imageView
    public static boolean loadImageInView(Uri uri, ImageView imageView) {
        if (uri.toString().trim().length() == 0) {
            Picasso.get().load(R.drawable.no_image).into(imageView);
            return true;
        }

        Picasso.get().load(uri).error(R.drawable.no_image).into(imageView);

        return true;
    }

    public static void loadImageInView(String url, CircleImageView circleImageView) {

        if (url.trim().length() == 0) {
            Picasso.get().load(R.drawable.no_image).into(circleImageView);
            return;
        }

        Picasso.get().load(url).error(R.drawable.no_image).into(circleImageView, new Callback() {
            @Override
            public void onSuccess() {
                AppController.getAppController().getInAppNotifier().log("Picasso", "Success");
            }

            @Override
            public void onError(Exception e) {
                AppController.getAppController().getInAppNotifier().log("Picasso", "Failed " + e.toString());

            }
        });

    }

    public static void loadImageInView(String url, CircleImageView circleImageView, int width, int height) {

        if (url.trim().length() == 0) {
            Picasso.get().load(R.drawable.no_image).resize(width, height).into(circleImageView);
            return;
        }

        Picasso.get().load(url).error(R.drawable.no_image).resize(width, height).into(circleImageView, new Callback() {
            @Override
            public void onSuccess() {
                AppController.getAppController().getInAppNotifier().log("Picasso", "Success");
            }

            @Override
            public void onError(Exception e) {
                AppController.getAppController().getInAppNotifier().log("Picasso", "Failed " + e.toString());

            }
        });

    }

    // Load an image from url into an imageView by resizing it with specified width and height
    public static void loadImageInView(String url, ImageView imageView, int width, int height) {

        if (url.trim().length() == 0) {
            Picasso.get().load(R.drawable.no_image).resize(width, height).into(imageView);
            return;
        }

        Picasso.get().load(url).error(R.drawable.no_image).resize(width, height).into(imageView);
    }

    // Load an image from resource and in case of failure, load another resource(default) into an imageView
    public static void loadImageInView(int targetResourceId, int defaultResourceId, ImageView imageView) {

        Picasso.get().load(targetResourceId).error(defaultResourceId).into(imageView);
    }

    // Load an image from url and in case of failure, load a resource(default) into an imageView
    public static void loadImageInView(String url, int defaultResourceId, ImageView imageView) {

        if (url.trim().length() == 0) {
            Picasso.get().load(R.drawable.no_image).into(imageView);
            return;
        }

        Picasso.get().load(url).error(defaultResourceId).into(imageView);
    }

    // new

    // Loading an image from string url into an imageView
    public static void loadImageInView(String url, final ImageView imageView, final IImageLoadedCallback callback) {

        if (url.trim().length() == 0) {
            Picasso.get().load(R.drawable.no_image).into(imageView, new Callback() {
                @Override
                public void onSuccess() {
                    AppController.getAppController().getInAppNotifier().log("Picasso", "DEfault Image Set");
                    callback.onImageLoaded(imageView.getDrawable());
                }

                @Override
                public void onError(Exception e) {
                    AppController.getAppController().getInAppNotifier().log("Picasso", "Failed " + e.toString());
                }
            });
            return;
        }


        Picasso.get().load(url).error(R.drawable.no_image).into(imageView, new Callback() {
            @Override
            public void onSuccess() {
                AppController.getAppController().getInAppNotifier().log("Picasso", "Success");
                callback.onImageLoaded(imageView.getDrawable());
            }

            @Override
            public void onError(Exception e) {
                AppController.getAppController().getInAppNotifier().log("Picasso", "Failed " + e.toString());
            }
        });


    }

}
