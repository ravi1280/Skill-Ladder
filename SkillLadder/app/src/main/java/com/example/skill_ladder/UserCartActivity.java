package com.example.skill_ladder;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class UserCartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_cart);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ImageView imageViewprofile = findViewById(R.id.CartBackIcon01);
        imageViewprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        RecyclerView recyclerView = findViewById(R.id.CartRV01);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(UserCartActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        List<Cart> catdetails = new ArrayList<>();
        catdetails.add(new Cart("Software Engineer"));
        catdetails.add(new Cart("Software Engineer"));
        catdetails.add(new Cart("Software Engineer"));
        catdetails.add(new Cart("Software Engineer"));
        catdetails.add(new Cart("Software Engineer"));
        catdetails.add(new Cart("Software Engineer"));
        catdetails.add(new Cart("Software Engineer"));
        catdetails.add(new Cart("Software Engineer"));
        catdetails.add(new Cart("Software Engineer"));
        catdetails.add(new Cart("Software Engineer"));
        catdetails.add(new Cart("Software Engineer"));
        catdetails.add(new Cart("Software Engineer"));
        catdetails.add(new Cart("Software Engineer"));

        CartAdapter jobListAdapter = new CartAdapter(catdetails);
        recyclerView.setAdapter(jobListAdapter);

    }
}

class  Cart{

    String Cartitle;

    public Cart(String carttitle ) {
        this.Cartitle = carttitle;
    }
}

class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private final List<Cart> cartdetails;

    public CartAdapter(List<Cart> cartdetails) {
        this.cartdetails = cartdetails;
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {

        TextView CartTitle;
        View ContainerView;
        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            CartTitle = itemView.findViewById(R.id.CartItemTV01);
            ContainerView = itemView;
        }
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.cart_item, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {

        Cart cartDetails = cartdetails.get(position);
        holder.CartTitle.setText(cartDetails.Cartitle);
        String company = cartDetails.Cartitle.toString();

        holder.ContainerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(holder.itemView.getContext(),company,Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartdetails.size();
    }
}