package com.example.nex4jmq.warehouseapplication.network;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.nex4jmq.warehouseapplication.Constant;
import com.example.nex4jmq.warehouseapplication.warehouse.WarehouseActivity;
import com.example.nex4jmq.warehouseapplication.warehouse.database.PreferenceManager;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class VolleyService {


    IResult mResultCallback = null;
    Context mContext;
    PreferenceManager prefManager = new PreferenceManager();
    String userID = null;


    public VolleyService(IResult resultCallback, Context context) {
        mResultCallback = resultCallback;
        mContext = context;
    }


    public void postDataVolley(final String requestType, String url, JSONObject sendObj) {
        try {
            RequestQueue queue = Volley.newRequestQueue(mContext);

            JsonObjectRequest jsonObj = new JsonObjectRequest(url, sendObj, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    if (mResultCallback != null)
                        mResultCallback.notifySuccess(requestType, response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (mResultCallback != null)
                        mResultCallback.notifyError(requestType, error);
                }
            });

            queue.add(jsonObj);

        } catch (Exception e) {

        }
    }

    public void getDataVolley(final String requestType, String url) {
        try {
            RequestQueue queue = Volley.newRequestQueue(mContext);
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                    Constant.NEW_USER_URL, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            if (mResultCallback != null)
                                mResultCallback.notifySuccess(requestType, response);


                        }

                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //Failure Callback
                            if (mResultCallback != null)
                                mResultCallback.notifyError(requestType, error);

                        }
                    }) {
                @Override
                public Map getHeaders() throws AuthFailureError {
                    HashMap headers = new HashMap();
                    //headers.put("Content-Type", "application/json");
                    headers.put(Constant.OCM_KEY, Constant.SUBSCRIPTION_KEY);
                    return headers;
                }
            };
// Adding the request to the queue along with a unique string tag
            queue.add(jsonObjReq);


        } catch (Exception e) {

        }
    }


    public void getProdList(final String requestType, String url) {
        try {
            RequestQueue queue = Volley.newRequestQueue(mContext);
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                    Constant.GET_PRODUCT_LIST_URL, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            if (mResultCallback != null)
                                mResultCallback.notifySuccess(requestType, response);


                        }

                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //Failure Callback
                            if (mResultCallback != null)
                                mResultCallback.notifyError(requestType, error);

                        }
                    }) {
                @Override
                public Map getHeaders() throws AuthFailureError {
                    HashMap headers = new HashMap();

                    userID = String.valueOf(prefManager.readSharedPreference(mContext, Constant.USER_ID,""));

                    //headers.put("Content-Type", "application/json");
                    headers.put(Constant.OCM_KEY, Constant.SUBSCRIPTION_KEY);
                    headers.put(Constant.PRODUCT_STARTNAME,Constant.PRODUCT_START);
                    headers.put(Constant.PRODUCT_LIMITNAME,Constant.PRODUCT_LIMIT);
                    headers.put(Constant.PRODUCT_BRANCHNAME,Constant.PRODUCT_BRANCH);
                    headers.put(Constant.PRODUCT_SEARCHNAME,Constant.PRODUCT_SEARCH);
                    headers.put(Constant.PRODUCT_MACHINE_IDNAME,userID);
                    headers.put(Constant.USER_ID,userID);
                    return headers;
                }
            };
// Adding the request to the queue along with a unique string tag
            queue.add(jsonObjReq);


        } catch (Exception e) {

        }
    }
}





