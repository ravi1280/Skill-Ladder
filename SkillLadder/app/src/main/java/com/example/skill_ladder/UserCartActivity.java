package com.example.skill_ladder;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
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

import com.example.skill_ladder.model.AppConfig;
import com.example.skill_ladder.model.Cart;
import com.example.skill_ladder.model.customAlert;
import com.example.skill_ladder.model.showCustomToast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UserCartActivity extends AppCompatActivity {
String UserIdShared;
    List<Cart> catdetails;
    CartAdapter cartListAdapter;
    RecyclerView recyclerView;
    Integer price;
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
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        UserIdShared = sharedPreferences.getString("UserID", "");
        loadCartDetails(UserIdShared);



        ImageView imageViewprofile = findViewById(R.id.CartBackIcon01);
        imageViewprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Button checkOut = findViewById(R.id.CartCheckOutBtn);
        checkOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                price=0;

                CheckOutBottomsheet(price);
            }
        });

        recyclerView = findViewById(R.id.CartRV01);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(UserCartActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        catdetails = new ArrayList<>();
        cartListAdapter = new CartAdapter(catdetails,UserIdShared);
        recyclerView.setAdapter(cartListAdapter);

    }

    private void loadCartDetails(String id){
        String userId = id;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    OkHttpClient okHttpClient = new OkHttpClient();
                    String url = AppConfig.BASE_URL + "/LoadCart?id=" + userId;

                    Request request = new Request.Builder()
                            .url(url)
                            .get()
                            .build();

                    Response response = okHttpClient.newCall(request).execute();
                    String responseText = response.body().string();

                    Gson gson= new Gson();
                    JsonObject jsonObject = gson.fromJson(responseText, JsonObject.class);


                    if (jsonObject.has("cartItem")) {
                        Type listType01 = new TypeToken<List<Cart>>() {}.getType();
                        List<Cart> cartItems01 = gson.fromJson(jsonObject.get("cartItem"), listType01);

                        if (cartItems01 != null) {
                            runOnUiThread(() -> {
                                catdetails.clear();  // Clear existing data
                                catdetails.addAll(cartItems01);  // Add new data
                                cartListAdapter.notifyDataSetChanged();  // Refresh RecyclerView
                            });
                        }
                    } else {
                        runOnUiThread(() -> showCustomToast.showToast(UserCartActivity.this, "Cart is Empty", R.drawable.cancel));
                    }


                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }
        }).start();

    }
    private void CheckOutBottomsheet(Integer price){
        Integer Price = price;

        View bottomSheetView = getLayoutInflater().inflate(R.layout.cart_checkout_bottom, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(UserCartActivity.this);
        bottomSheetDialog.setContentView(bottomSheetView);

        Button actionOne = bottomSheetView.findViewById(R.id.CartBottomSheetPayBtn);
        actionOne.setOnClickListener(view -> {
                bottomSheetDialog.dismiss();
        });
        bottomSheetDialog.show();
    }
}


class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private final List<Cart> cartdetails;
    private final String userId;

    public CartAdapter(List<Cart> cartdetails,String userid) {
        this.cartdetails = cartdetails;
        this.userId = userid;
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {

        TextView CartTitle, CartPrice;
        ImageView CartDeleteIcon;
        RadioButton radio;
        View ContainerView;
        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            CartTitle = itemView.findViewById(R.id.CartItemTV01);
            CartPrice = itemView.findViewById(R.id.CartItemTV02);
            CartDeleteIcon = itemView.findViewById(R.id.CartDeleteIcon);
            radio = itemView.findViewById(R.id.CartradioButton01);
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
        holder.CartTitle.setText(cartDetails.getLessonName());
        holder.CartPrice.setText("$"+cartDetails.getLessonPrice());
        String company = cartDetails.getLessonName().toString();

        holder.CartDeleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               String lessonId = cartDetails.getLessonId();
               String UserId = userId;

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            OkHttpClient client = new OkHttpClient();
                            Gson gson = new Gson();
                            JsonObject cartJson = new JsonObject();
                            cartJson.addProperty("userId",UserId);
                            cartJson.addProperty("lessonId",lessonId);

                            RequestBody jsonRequestBody = RequestBody.create(gson.toJson(cartJson), MediaType.get("application/json"));
                            // Make request
                            Request request = new Request.Builder()
                                    .url(AppConfig.BASE_URL+"/DeleteCart")
                                    .post(jsonRequestBody)
                                    .build();
                            Response response = client.newCall(request).execute();
                            String responseText = response.body().string();

                            if (responseText.equals("Cart item deleted successfully !")){
                                ((UserCartActivity) holder.itemView.getContext()).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        cartdetails.remove(holder.getAdapterPosition());
                                        // Notify adapter
                                        notifyItemRemoved(holder.getAdapterPosition());
                                        notifyItemRangeChanged(holder.getAdapterPosition(), cartdetails.size());
                                        showCustomToast.showToast(holder.itemView.getContext(),"Item Deleted", R.drawable.checked);

                                    }
                                });
                            }else{
                                ((UserCartActivity) holder.itemView.getContext()).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        showCustomToast.showToast(holder.itemView.getContext(), responseText, R.drawable.cancel);
                                    }
                                });
                            }

                        }catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }).start();

            }
        });

    }

    @Override
    public int getItemCount() {
        return cartdetails.size();
    }
}