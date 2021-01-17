package com.citroncode.runtimestickerimport.adapter;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.citroncode.runtimestickerimport.MainActivity;
import com.citroncode.runtimestickerimport.Sticker;
import com.citroncode.runtimestickerimport.StickerPack;
import com.citroncode.runtimestickerimport.R;
import com.citroncode.runtimestickerimport.activity.StickerDetailsActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class StickerAdapter extends RecyclerView.Adapter<StickerAdapter.ViewHolder> {

    Context context;
    ArrayList<StickerPack> StickerPack;

    public StickerAdapter(Context context, ArrayList<StickerPack> StickerPack) {
        this.context = context;
        this.StickerPack = StickerPack;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_sticker, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        List<Sticker> models = StickerPack.get(i).getStickers();
        viewHolder.name.setText(StickerPack.get(i).name);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Toast.makeText(context, MainActivity.path + "/1/" + models.get(0).imageFileName, Toast.LENGTH_SHORT).show();
        Bitmap bitmap1,bitmap2,bitmap3,bitmap4;
        try{
            bitmap1 = BitmapFactory.decodeFile(MainActivity.path + "/1/" + models.get(0).imageFileName, options);
            viewHolder.imone.setImageBitmap(bitmap1);
        }catch (Exception e){

        }
        try{
            bitmap2 = BitmapFactory.decodeFile(MainActivity.path + "/1/" + models.get(1).imageFileName, options);
            viewHolder.imtwo.setImageBitmap(bitmap2);
        }catch (Exception e){

        }
        try{
            bitmap3 = BitmapFactory.decodeFile(MainActivity.path + "/1/" + models.get(2).imageFileName, options);
            viewHolder.imthree.setImageBitmap(bitmap3);
        }catch (Exception e){

        }
        try{
            bitmap4 = BitmapFactory.decodeFile(MainActivity.path + "/1/" + models.get(3).imageFileName, options);
            viewHolder.imfour.setImageBitmap(bitmap4);
        }catch (Exception e){
            viewHolder.imfour.setVisibility(View.INVISIBLE);
        }


        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, StickerDetailsActivity.class)
                                .putExtra(MainActivity.EXTRA_STICKERPACK, StickerPack.get(viewHolder.getAdapterPosition())),
                        ActivityOptionsCompat.makeScaleUpAnimation(v, (int) v.getX(), (int) v.getY(), v.getWidth(),
                                v.getHeight()).toBundle());
            }
        });

        File file = new File(MainActivity.path + "/" + StickerPack.get(i).identifier + "/" + models.get(0).imageFileName);
        if (!file.exists()) {
            viewHolder.rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        } else {
            viewHolder.rl.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return StickerPack.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        ImageView imone, imtwo, imthree, imfour, download;
        CardView cardView;
        ProgressBar bar;
        RelativeLayout rl;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.rv_sticker_name);
            imone = itemView.findViewById(R.id.sticker_one);
            imtwo = itemView.findViewById(R.id.sticker_two);
            imthree = itemView.findViewById(R.id.sticker_three);
            imfour = itemView.findViewById(R.id.sticker_four);
            download = itemView.findViewById(R.id.download);
            cardView = itemView.findViewById(R.id.card_view);
            bar = itemView.findViewById(R.id.progressBar);
            rl = itemView.findViewById(R.id.download_layout);
        }
    }
}
