package com.example.nex4jmq.warehouseapplication;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.nex4jmq.warehouseapplication.network.IResult;
import com.example.nex4jmq.warehouseapplication.network.VolleyService;
import com.example.nex4jmq.warehouseapplication.warehouse.WarehouseActivity;
import com.example.nex4jmq.warehouseapplication.warehouse.database.PreferenceManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SplashScreenActivity extends AppCompatActivity {

    private static final String TAG = "SPLASH ACTIVITY";
    private TextView mTextView;
    private String userID;
    private IResult mCallback;
    private VolleyService mVolleyService;
    private static int SPLASH_TIME_OUT = 8000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = (TextView) findViewById(R.id.tvTextView);


        mVolleyService = new VolleyService(mCallback, this);

        //mVolleyService.getDataVolley(Constant.GET_USER_ID_TYPE, Constant.NEW_USER_URL);
        //initCallBack();

       // splashScreen();


        ///

        getUserID();



    }
        //

void getUserID(){
    RequestQueue queue = Volley.newRequestQueue(this);
    JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
            Constant.NEW_USER_URL, null,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    Log.e("RESPONSE ", String.valueOf(response));
                    try {
                        userID = response.getString(Constant.USER_ID);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if(userID!=null) {
                        PreferenceManager prefManager = new PreferenceManager();
                        prefManager.writeSharedPreference(SplashScreenActivity.this, Constant.USER_ID, userID);
                        Intent warehouseIntent = new Intent(SplashScreenActivity.this, WarehouseActivity.class);
                        warehouseIntent.putExtra("USER_ID",userID);
                        startActivity(warehouseIntent);


                    }
                    else{

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
            //headers.put("Content-Type", "application/json");
            headers.put(Constant.OCM_KEY, Constant.SUBSCRIPTION_KEY);
            return headers;
        }
    };
// Adding the request to the queue along with a unique string tag
    queue.add(jsonObjReq);



}


    void initCallBack() {
        mCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType, JSONObject response) {


                try {
                    userID = response.getString(Constant.USER_ID);
                    PreferenceManager prefManager = new PreferenceManager();
                    prefManager.writeSharedPreference(SplashScreenActivity.this, Constant.USER_ID, userID);

                    mTextView.setText(userID);

                    if(userID!=null){
                        Intent warehouseIntent = new Intent(SplashScreenActivity.this, WarehouseActivity.class);
                        startActivity(warehouseIntent);

                    }
                    else {
                        Intent splashIntent = new Intent(SplashScreenActivity.this, SplashScreenActivity.class);
                        startActivity(splashIntent);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.d(TAG, "Volley requester " + requestType);
                Log.d(TAG, "Volley JSON post" + response);

            }

            @Override
            public void notifyError(String requestType, VolleyError error) {

                Log.d(TAG, "Volley requester " + requestType);
                Log.d(TAG, "Volley JSON post" + "That didn't work!");

                Toast.makeText(SplashScreenActivity.this, String.valueOf( error),Toast.LENGTH_LONG).show();

            }
        };
    }


    void splashScreen() {

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity

                initCallBack();
                Intent warehouseIntent = new Intent(SplashScreenActivity.this, WarehouseActivity.class);
                startActivity(warehouseIntent);


                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

}

