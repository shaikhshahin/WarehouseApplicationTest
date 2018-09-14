package com.example.nex4jmq.warehouseapplication.warehouse;

import android.app.AppComponentFactory;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.nex4jmq.warehouseapplication.Constant;
import com.example.nex4jmq.warehouseapplication.R;
import com.example.nex4jmq.warehouseapplication.warehouse.adapter.WarehouseAdapter;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProductDetailsActivity extends AppCompatActivity {

    private TextView tvProductName;
    private TextView tvBarcode;
    private TextView tvPrice;
    private ImageView imImageProduct;
    private String barcode;
    private String productName;
    private String userID;
    private String price;
    private String url;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            productName = bundle.getString(Constant.DESC);
            barcode = bundle.getString(Constant.BARCODE);
            userID = bundle.getString(Constant.USER_ID);
            url = bundle.getString(Constant.IMAGE_URL);

            getPriceList();

        }

        initViews();

        loadImage(url);
    }

    void initViews() {

        tvProductName = (TextView) findViewById(R.id.tvProductName);
        tvBarcode = (TextView) findViewById(R.id.tvBarcode);
        tvPrice = (TextView) findViewById(R.id.tvPrice);
        imImageProduct = (ImageView) findViewById(R.id.imImageProduct);
        tvProductName.setText("PRODUCT NAME : " + productName);
        tvBarcode.setText("PRODUCT BARCODE : " + barcode);

    }

    void loadImage(String URL) {
        Picasso.get()
                .load(URL)
                .placeholder(R.drawable.warehouse)
                .error(R.mipmap.ic_launcher)
                .resize(280, 00)
                .centerInside()
                .tag(this)
                .into(imImageProduct);
    }


    void getPriceList() {

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Constant.GET_PRICE_API + "?" + Constant.BARCODE + "=" + barcode + "&" +
                        Constant.PRODUCT_MACHINE_IDNAME + "=" + Constant.PRODUCT_MACHINE_ID
                        + "&" + Constant.USER_ID + "=" + userID, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.e("RESPONSE", String.valueOf(response));
                        JSONObject jsonResult = new JSONObject();
                        try {

                            JSONObject jsonProductResult = new JSONObject();
                            jsonProductResult = response.getJSONObject(Constant.PRODUCT);
                            JSONObject jsonpriceObject = new JSONObject();
                            jsonpriceObject = jsonProductResult.getJSONObject(Constant.PRICE);
                            price = jsonpriceObject.getString(Constant.PRICE);
                            tvPrice.setText("PRICE : " + price);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Failure Callback

                    }
                }) {
            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();


                headers.put(Constant.OCM_KEY, Constant.SUBSCRIPTION_KEY);
                return headers;
            }
        };
// Adding the request to the queue along with a unique string tag
        queue.add(jsonObjReq);


    }

}
