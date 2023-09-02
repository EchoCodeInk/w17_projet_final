package com.example.w17_application;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.w17_application.entite.CartProduct;
import com.example.w17_application.entite.Product;
import com.example.w17_application.manager.CartProductManager;
import com.example.w17_application.manager.ProductManager;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {
    ScrollView scrollView;
    Context context;
    LinearLayout linearLayout;
    ImageButton btnDelete;
    Button btnCheckOut;
    TextView  tvProductName, tvProductPrice, tvProductQuantity, tvTotalAmount;
    private double totalAmount = 0.0;

    ArrayList<CartProduct> itemsCartProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        context = this;
        itemsCartProduct = CartProductManager.getAll(context);
        scrollView = findViewById(R.id.scroller);

        linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);


        for (CartProduct cartProduct : itemsCartProduct) {
            LinearLayout cartLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.single_row_design, null);
            tvProductName = cartLayout.findViewById(R.id.cart_product_name);
            tvProductPrice = cartLayout.findViewById(R.id.cart_product_prix);
            tvProductQuantity = cartLayout.findViewById(R.id.cart_product_quantity);

            Product product = ProductManager.getById(this, cartProduct.getProductId());

            btnDelete = cartLayout.findViewById(R.id.delete_btn);
            tvProductName.setText(String.valueOf(getFirstNWords(product.getName(), 5)));
            tvProductPrice.setText(String.valueOf(cartProduct.getProductPrice() + "$"));
            tvProductQuantity.setText(String.valueOf(cartProduct.getProductQuantity()));

            double productAmount = cartProduct.getProductPrice() * cartProduct.getProductQuantity();
            totalAmount += productAmount;

            btnDelete.setOnClickListener(v -> {
                CartProductManager.delete(this, cartProduct.getProductId());
                linearLayout.removeView(cartLayout);

                CartProductManager.delete(this, cartProduct.getProductId());
                linearLayout.removeView(cartLayout);
                // Mettez à jour le montant total après la suppression du produit.
                double productAmountDelete = cartProduct.getProductPrice() * cartProduct.getProductQuantity();
                totalAmount -= productAmountDelete;
                tvTotalAmount.setText("Total Amount : " + String.format("%.2f$", totalAmount));
            });

            linearLayout.addView(cartLayout);
        }
        scrollView.addView(linearLayout);

        tvTotalAmount = findViewById(R.id.tv_total_amount);
        tvTotalAmount.setText("Total Amount : " + String.format("%.2f$", totalAmount));
        btnCheckOut = findViewById(R.id.btn_checkout);
        btnCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, PaymentActivity.class);
                intent.putExtra("totalAmount", String.valueOf(totalAmount).toString());
                intent.putExtra("page", "pageCart");

//                intent.putExtra("quantity", quantityEditText.getText().toString());
                finish();
                startActivity(intent);
            }
        });
    }

    public String getFirstNWords(String input, int n) {
        String[] words = input.split("\\s+");
        StringBuilder newString = new StringBuilder();

        for (int i = 0; i < Math.min(n, words.length); i++) {
            newString.append(words[i]).append(" ");
        }

        return newString.toString().trim();
    }

}
