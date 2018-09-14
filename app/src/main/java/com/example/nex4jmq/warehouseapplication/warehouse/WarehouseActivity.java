package com.example.nex4jmq.warehouseapplication.warehouse;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.nex4jmq.warehouseapplication.Constant;
import com.example.nex4jmq.warehouseapplication.R;
import com.example.nex4jmq.warehouseapplication.network.IResult;
import com.example.nex4jmq.warehouseapplication.network.VolleyService;
import com.example.nex4jmq.warehouseapplication.warehouse.adapter.WarehouseAdapter;
import com.example.nex4jmq.warehouseapplication.warehouse.database.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WarehouseActivity extends Activity {

    // List view
    private ListView lvProduct;

    private static final String TAG = "WAREHOUSE ACTIVITY";


    // Listview Adapter
    private WarehouseAdapter adapter;

    // Search EditText
    private EditText inputSearch;

    // ArrayList for Listview
    private ArrayList<HashMap<String, String>> productList;

    private IResult mCallback;
    private VolleyService mVolleyService;
    private String userID;
    private PreferenceManager prefManager = new PreferenceManager();
    private ArrayList<String> descriptionList;
    private ArrayList<String> urlList;
    private ArrayList<String> barcodeList;

    private String URL;
    private String barcode;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warehouse);


        // Listview Data
        String products[] = {"Dell Inspiron", "HTC One X", "HTC Wildfire S", "HTC Sense", "HTC Sensation XE",
                "iPhone 4S", "Samsung Galaxy Note 800",
                "Samsung Galaxy S3", "MacBook Air", "Mac Mini", "MacBook Pro", "SHAHIN"};

        lvProduct = (ListView) findViewById(R.id.lvProduct);
        inputSearch = (EditText) findViewById(R.id.inputSearch);

        // Adding items to listview
        //adapter = new ArrayAdapter<String>(this, R.layout.product_list_item, R.id.product_name, products);

        mVolleyService = new VolleyService(mCallback, this);

        userID = getIntent().getStringExtra("USER_ID");


        //mVolleyService.getProdList(Constant.GET_USER_ID_TYPE, Constant.GET_PRODUCT_LIST_URL);
        // initCallBack();

        getProductList();


        /**
         * Enabling Search Filter
         * */
        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                WarehouseActivity.this.adapter.getFilter().filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });
    }


    void initCallBack() {
        mCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType, JSONObject response) {


                // try {



                /*} catch (JSONException e) {
                    e.printStackTrace();
                }
*/
                Log.d(TAG, "Volley requester " + requestType);
                Log.d(TAG, "Volley JSON post" + response);

            }

            @Override
            public void notifyError(String requestType, VolleyError error) {

                Log.d(TAG, "Volley requester " + requestType);
                Log.d(TAG, "Volley JSON post" + "That didn't work!");

            }
        };
    }

    void getProductList() {

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Constant.GET_PRODUCT_LIST_URL + "?" + Constant.PRODUCT_STARTNAME + "=" + Constant.PRODUCT_START + "&" + Constant.PRODUCT_LIMITNAME + "=" + Constant.PRODUCT_LIMIT + "&"
                        + Constant.PRODUCT_BRANCHNAME + "=" + Constant.PRODUCT_BRANCH + "&" + Constant.PRODUCT_SEARCHNAME + "=" + Constant.PRODUCT_SEARCH + "&" + Constant.PRODUCT_MACHINE_IDNAME + "=" + Constant.PRODUCT_MACHINE_ID
                        + "&" + Constant.USER_ID + "=" + userID, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.e("RESPONSE", String.valueOf(response));
                        JSONObject jsonResult = new JSONObject();
                        try {

                            JSONArray jsonArrayResult = new JSONArray();
                            jsonArrayResult = response.getJSONArray(Constant.RESULTNAME);
                            descriptionList = new ArrayList<>();
                            urlList = new ArrayList<>();
                            barcodeList = new ArrayList<>();
                            String description = null;
                            JSONObject desc = new JSONObject();
                            JSONObject prodObject = new JSONObject();
                            for (int i = 0; i < jsonArrayResult.length(); i++) {

                                desc = jsonArrayResult.getJSONObject(i);
                                description = desc.getString(Constant.DESC);
                                descriptionList.add(String.valueOf(description));

                                for (int j = 0; j < 1; j++) {
                                    prodObject = desc.getJSONArray(Constant.PRODUCTS_NAME).getJSONObject(j);
                                    URL = prodObject.getString(Constant.IMAGE_URL);
                                    barcode = prodObject.getString(Constant.BARCODE);
                                    //"https://twg.azure-api.net/twlProductImage/productImage/9400032667140/format/png/size/small";
                                    urlList.add(URL);
                                    barcodeList.add(barcode);
                                }

                            }

                            adapter = new WarehouseAdapter(WarehouseActivity.this, descriptionList, urlList, barcodeList);
                            lvProduct.setAdapter(adapter);

                            lvProduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    barcodeList.get(i);
                                    Intent detailsIntent = new Intent(WarehouseActivity.this, ProductDetailsActivity.class);
                                    Bundle detailBundle = new Bundle();
                                    detailBundle.putString(Constant.BARCODE, barcodeList.get(i));
                                    detailBundle.putString(Constant.DESC, descriptionList.get(i));
                                    detailBundle.putString(Constant.USER_ID, userID);
                                    detailBundle.putString(Constant.IMAGE_URL, urlList.get(i));
                                   /* detailsIntent.putExtra(Constant.BARCODE,barcodeList.get(i));
                                    detailsIntent.putExtra(Constant.DESC,descriptionList.get(i));*/
                                    detailsIntent.putExtras(detailBundle);
                                    startActivity(detailsIntent);
                                }
                            });

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

                //userID = prefManager.readSharedPreference(WarehouseActivity.this, Constant.USER_ID,"");

                //headers.put("Content-Type", "application/json");
                headers.put(Constant.OCM_KEY, Constant.SUBSCRIPTION_KEY);
                return headers;
            }
        };
// Adding the request to the queue along with a unique string tag
        queue.add(jsonObjReq);


    }

}
