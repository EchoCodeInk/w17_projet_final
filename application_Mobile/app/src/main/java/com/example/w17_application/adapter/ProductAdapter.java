package com.example.w17_application.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.w17_application.R;
import com.example.w17_application.entite.Product;

import java.io.IOException;
import java.util.List;

public class ProductAdapter extends ArrayAdapter<Product> {
    int mRessource;

    public ProductAdapter(@NonNull Context context, int resource, @NonNull List<Product> objects) {
        super(context, resource, objects);
        mRessource = resource;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(mRessource, parent, false);
        } else {
            view = convertView;
        }
        Product product = getItem(position);
        TextView tvNameProduct = view.findViewById(R.id.tv_product_name);
        ImageView imgProduct = view.findViewById(R.id.img_product);
        try {
            Bitmap bitmapImage = BitmapFactory.decodeStream(getContext().getAssets().open("images/" + product.getImageUrl()));
            imgProduct.setImageBitmap(bitmapImage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        tvNameProduct.setText(product.getName());
        return view;

    }
}