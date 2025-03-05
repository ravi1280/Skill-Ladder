package com.example.skill_ladder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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

import lk.payhere.androidsdk.PHConfigs;
import lk.payhere.androidsdk.PHConstants;
import lk.payhere.androidsdk.PHMainActivity;
import lk.payhere.androidsdk.PHResponse;
import lk.payhere.androidsdk.model.InitRequest;
import lk.payhere.androidsdk.model.StatusResponse;
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
    Boolean haveItem;


    private static final int PAYHERE_REQUEST = 11001;
    private static final String TAG = "UserCartActivity";
//    private TextView textView;

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

//        textView = findViewById(R.id.payheretv);

        ImageView imageViewpback = findViewById(R.id.CartBackIcon01);
        imageViewpback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Button checkOut = findViewById(R.id.CartCheckOutBtn);
        checkOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(haveItem){
                    CheckOutBottomsheet();
                }else {
                    showCustomToast.showToast(UserCartActivity.this,"No Items To CheckOut !",R.drawable.cancel);
                }
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
                            int totalPrice = 0;
                            for (Cart item : cartItems01) {
                                totalPrice += item.getLessonPrice();
                            }
                            final int finalTotalPrice = totalPrice;
                            runOnUiThread(() -> {
                                catdetails.clear();
                                catdetails.addAll(cartItems01);
                                cartListAdapter.notifyDataSetChanged();

                                TextView cartCheckOutPrice = findViewById(R.id.CartCheckOutPriceTV);
                                cartCheckOutPrice.setText("$" + finalTotalPrice);
                            });
                            haveItem=true;
                        }
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showCustomToast.showToast(UserCartActivity.this, "Cart is Empty", R.drawable.cancel);
                                haveItem=false;


                            }
                        });
                    }

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }
        }).start();

    }
    private void CheckOutBottomsheet(){
        TextView CheckOutPrice = findViewById(R.id.CartCheckOutPriceTV);
        Integer CPrice = Integer.parseInt(CheckOutPrice.getText().toString().replaceAll("[^\\d]", ""));

        View bottomSheetView = getLayoutInflater().inflate(R.layout.cart_checkout_bottom, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(UserCartActivity.this);
        bottomSheetDialog.setContentView(bottomSheetView);

        Button actionOne = bottomSheetView.findViewById(R.id.CartBottomSheetPayBtn);
        actionOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InitRequest req = new InitRequest();
                req.setMerchantId("Merchant_ID");
                req.setCurrency("LKR");
                req.setAmount(CPrice);
                req.setOrderId("230000123");
                req.setItemsDescription("Cart Item Check Out");
                req.setCustom1("This is the custom message 1");
                req.setCustom2("This is the custom message 2");
                req.getCustomer().setFirstName("Saman");
                req.getCustomer().setLastName("Perera");
                req.getCustomer().setEmail("samanp@gmail.com");
                req.getCustomer().setPhone("+94771234567");
                req.getCustomer().getAddress().setAddress("No.1, Galle Road");
                req.getCustomer().getAddress().setCity("Colombo");
                req.getCustomer().getAddress().setCountry("Sri Lanka");

                Intent intent = new Intent(UserCartActivity.this, PHMainActivity.class);
                intent.putExtra(PHConstants.INTENT_EXTRA_DATA, req);
                PHConfigs.setBaseUrl(PHConfigs.SANDBOX_URL);
                startActivityForResult(intent, PAYHERE_REQUEST);

                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PAYHERE_REQUEST && data != null && data.hasExtra(PHConstants.INTENT_EXTRA_RESULT)) {
            PHResponse<StatusResponse> response = (PHResponse<StatusResponse>) data.getSerializableExtra(PHConstants.INTENT_EXTRA_RESULT);

            if (resultCode == Activity.RESULT_OK) {
                if (response != null && response.isSuccess()) {
                    Log.d(TAG, "Payment Successful: " + response.getData().toString());

                    ArrayList<String> lessonIds01 = new ArrayList<>();
                    for (Cart item : catdetails) {
                        lessonIds01.add(item.getLessonId());
                    }
                    Intent intent = new Intent(UserCartActivity.this, PaymentSuccessActivity.class);
                    intent.putExtra("UserId", UserIdShared);
                    intent.putStringArrayListExtra("lessonIds", lessonIds01);
                    startActivity(intent);
                    finish();

                } else {
                    Log.d(TAG, "Payment Failed: " + (response != null ? response.toString() : "No response"));
//                    textView.setText("Payment failed. Please try again.");
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.d(TAG, "Payment Canceled: " + (response != null ? response.toString() : "User canceled the request"));
//                textView.setText("User canceled the payment.");
            }
        }
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
        View ContainerView;
        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            CartTitle = itemView.findViewById(R.id.CartItemTV01);
            CartPrice = itemView.findViewById(R.id.CartItemTV02);
            CartDeleteIcon = itemView.findViewById(R.id.CartDeleteIcon);
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
                                        notifyItemRemoved(holder.getAdapterPosition());
                                        notifyItemRangeChanged(holder.getAdapterPosition(), cartdetails.size());
                                        int newTotalPrice = 0;
                                        for (Cart item : cartdetails) {
                                            newTotalPrice += item.getLessonPrice();
                                        }

                                        TextView cartCheckOutPrice = ((Activity) holder.itemView.getContext()).findViewById(R.id.CartCheckOutPriceTV);
                                        cartCheckOutPrice.setText("$" + newTotalPrice);

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