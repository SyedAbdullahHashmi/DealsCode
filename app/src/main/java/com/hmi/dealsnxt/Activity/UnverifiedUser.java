package com.hmi.dealsnxt.Activity;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hmi.dealsnxt.HelperClass.Constaints;
import com.hmi.dealsnxt.HelperClass.SessionManager;
import com.hmi.dealsnxt.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UnverifiedUser extends AppCompatActivity {

    int is_verified = 0;
    EditText name,email,phone;

    TextView tvTitle, tvaddress, tvusername;
    LinearLayout LLloc;
    ProgressBar progress_bar;
    TextView texttop;
    Spinner spinner;
    String [] sex={"Mr. ","Mrs."};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unverified_user);

        email=(EditText)findViewById(R.id.email);
        name=(EditText)findViewById(R.id.name);
        phone=(EditText)findViewById(R.id.phone);

        name.setText(SessionManager.getUserName(getApplicationContext()));
        email.setText(SessionManager.getUserEmail(getApplicationContext()));
        phone.setText(SessionManager.getMobileno(getApplicationContext()) );
        phone.setEnabled(false);
        phone.setClickable(false);

        tvTitle = (TextView) findViewById(R.id.tvtitile);
        texttop= (TextView) findViewById(R.id.texttop);
        spinner=(Spinner)findViewById(R.id.spinnergender);
        tvTitle.setVisibility(View.VISIBLE);

        tvTitle.setText("Fill your contact details");


        ArrayAdapter arrayAdapter=new ArrayAdapter(UnverifiedUser.this, R.layout.resource,sex);
        spinner.setAdapter(arrayAdapter);
        Button button = (Button) findViewById(R.id.button2);
        texttop.setText(SessionManager.getVerificationMessage(getApplicationContext()));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(UnverifiedUser.this, "Checking for Verification", Toast.LENGTH_SHORT).show();
                SessionManager.setVerificationMessage(getApplicationContext(),"Verification pending");
                texttop.setText(SessionManager.getVerificationMessage(getApplicationContext()));
                SessionManager.setUserEmail(getApplicationContext(), email.getText().toString());
                SessionManager.setUserName(getApplicationContext(), name.getText().toString());
                checkForVerification();

            }
        });

        if (SessionManager.getIs_verified(getApplicationContext()).equals("0")){
           // SessionManager.setVerificationMessage(getApplicationContext(),"Verification pending");
            texttop.setText(SessionManager.getVerificationMessage(getApplicationContext()));

        }else {

        }

    }

    public void checkForVerification() {

        String url = Constaints.IsUserVerified;

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jSONObject = new JSONObject(new String(response));
                    //   JSONObject jSONObject = new JSONObject(new String(""));
                    int  Status = jSONObject.optInt("status");
                    System.out.println("dataytyutyuuttyyu "+response);
                  if (Status == 1) {
                        JSONArray jsonArray = jSONObject.optJSONArray("info");
                        JSONObject jsonObject = jsonArray.optJSONObject(0);
                        SessionManager.setIs_verified(getApplicationContext(), String.valueOf(jsonObject.optInt("is_verified")));
                      String email = jsonObject.optString("email");
                      String uid = jsonObject.optString("id");
                      String name = jsonObject.optString("name");
                      String mobile = jsonObject.optString("phone");
                      SessionManager.setMobileno(getApplicationContext(), mobile);
                      SessionManager.setUserID(getApplicationContext(), uid);
                      SessionManager.setUserName(getApplicationContext(), name);
                      SessionManager.setUserImagePath(getApplicationContext(), "http://logiquebrainexaminer.com/deals_nxt/application/public/uploads/users" + "/" + jsonObject.optString("user_dp"));
                      SessionManager.setUserEmail(getApplicationContext(), jsonObject.optString("email").toString());
                      SessionManager.setUserDOB(getApplicationContext(), jsonObject.optString("dob").toString());
                      SessionManager.setUserGender(getApplicationContext(), jsonObject.optString("gender").toString());
                      if (!jsonObject.optString("gender").toString().equals("")) {
                          SessionManager.setIsotp(getApplicationContext(), true);
                      }

                        if(jsonObject.optInt("is_verified")==1){
                            startActivity(new Intent(UnverifiedUser.this,SplashSplashActivity.class));
                            finish();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), jSONObject.optString("msg"), Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Log.e("", e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("XAPIKEY", "XXXXX");
                params.put("id", SessionManager.getUserID(getApplicationContext()));
                params.put("phone", phone.getText().toString());
                params.put("name", name.getText().toString());
                params.put("email", email.getText().toString());
                System.out.println("data "+ params);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };

        int socketTimeout = 30000;
        Volley.newRequestQueue(getApplicationContext()).add(request);
        request.setRetryPolicy(new DefaultRetryPolicy(
                socketTimeout,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

      /*  } else {

            //   List<HotDealsModel> deallist = HotDealsModel.getAlldeals();
            //    arrayList = HotDealsModel.getAlldeals();
            arrayList.clear();
            adapter = new HotDealsAdaptor(arrayList, getActivity());
            mRecyclerView.setLayoutManager(linearLayoutManager);
            mRecyclerView.setAdapter(adapter);

        }*/

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
