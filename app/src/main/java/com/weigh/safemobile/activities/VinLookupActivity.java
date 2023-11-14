package com.weigh.safemobile.activities;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.weigh.safemobile.R;
import com.weigh.safemobile.adapters.VinNumbersAdapter;
import com.weigh.safemobile.helpers.AppHelper;
import com.weigh.safemobile.helpers.ConstantManager;
import com.weigh.safemobile.helpers.UrlBuilder;
import com.weigh.safemobile.models.VinModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VinLookupActivity extends BaseRequestActivity {

    @BindView(R.id.input_vin_number)
    EditText input_vin_number;

    @BindView(R.id.btn_clear_text)
    ImageButton btnClear;

    @BindView(R.id.list_vin_numbers)
    RecyclerView listVinNumberView;

    @BindView(R.id.ll_comment_scan)
    LinearLayout ll_comment;

    @BindView(R.id.txt_scan_camera)
    TextView txt_scan_camera;

    private List<VinModel> vinModelList;
    private VinNumbersAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vin_lookup);

        ButterKnife.bind(this);

        initializerView();
        initializeListener();
    }

    public void initializerView() {
        ll_comment.setVisibility(View.VISIBLE);
        btnClear.setVisibility(View.INVISIBLE);

        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listVinNumberView.setLayoutManager(mLinearLayoutManager);
        listVinNumberView.setItemAnimator(new DefaultItemAnimator());
        listVinNumberView.getItemAnimator().setChangeDuration(0);

        //fix slow recyclerview start
        listVinNumberView.setHasFixedSize(true);
        listVinNumberView.setItemViewCacheSize(10);
        listVinNumberView.setDrawingCacheEnabled(true);
        listVinNumberView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        listVinNumberView.addItemDecoration(new DividerItemDecoration(listVinNumberView.getContext(), DividerItemDecoration.VERTICAL));

        vinModelList = new ArrayList<>();
        mAdapter = new VinNumbersAdapter(vinModelList, this);
        listVinNumberView.setAdapter(mAdapter);
    }

    public void initializeListener() {
        input_vin_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int strLength = input_vin_number.getText().length();
                if (strLength > 0)
                    btnClear.setVisibility(View.VISIBLE);
                else
                    btnClear.setVisibility(View.INVISIBLE);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                int strLength = input_vin_number.getText().length();
                if (strLength > 0)
                    btnClear.setVisibility(View.VISIBLE);
                else
                    btnClear.setVisibility(View.INVISIBLE);
            }
        });

        input_vin_number.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    String searchText = input_vin_number.getText().toString();
                    fetchVINDatafromAPI(searchText);
                    return true;
                }
                return false;
            }
        });

        input_vin_number.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String searchText = input_vin_number.getText().toString();
                    fetchVINDatafromAPI(searchText);
                    return true;
                }
                return false;
            }
        });

        txt_scan_camera.setClickable(true);
        txt_scan_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recognizeTextOCR();
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input_vin_number.setText("");
                mAdapter.removeAll();
                ll_comment.setVisibility(View.VISIBLE);
            }
        });


    }

    private void recognizeTextOCR() {

    }

    private void fetchVINDatafromAPI(String searchText)
    {
        if (searchText.isEmpty())
            return;

        mAdapter.removeAll();

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = new UrlBuilder(UrlBuilder.API_DECODE_VIN).build();

        StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.isEmpty())
                {
                    JSONObject json = null;
                    try {
                        json = new JSONObject(response);

                        JSONObject basic_data = json.getJSONObject("query_responses").getJSONObject("Request-Sample").getJSONObject("us_market_data").getJSONObject("common_us_data").getJSONObject("basic_data");
                        String vehicleName = basic_data.optString("year") + " " + basic_data.optString("make") + " " + basic_data.optString("model");

                        VinModel headerItem = new VinModel();
                        headerItem.setViewType(VinNumbersAdapter.VIEW_TYPE_HEADER);
                        headerItem.setHeaderString(vehicleName);
                        mAdapter.addItem(headerItem);

                        JSONArray styleArray = json.getJSONObject("query_responses").getJSONObject("Request-Sample").getJSONObject("us_market_data").getJSONArray("us_styles");
                        if (styleArray != null)
                        {
                            for (int i=0; i<styleArray.length(); i++)
                            {
                                JSONObject jsonStyle = styleArray.getJSONObject(i);
                                VinModel item = new VinModel();
                                item.setViewType(VinNumbersAdapter.VIEW_TYPE_ITEM);
                                item.setName(jsonStyle.optString("name"));
                                item.setYear(basic_data.optString("year"));
                                item.setMake(basic_data.optString("make"));
                                item.setMake(basic_data.optString("model"));
                                item.setVin(searchText);

                                JSONArray specArray = jsonStyle.getJSONArray("standard_specifications");
                                if (specArray != null)
                                {
                                    for (int j=0; j<specArray.length(); j++)
                                    {
                                        JSONObject jsonSpec = specArray.getJSONObject(j);
                                        String specCategory = jsonSpec.optString("specification_category");
                                        if (!specCategory.isEmpty() && specCategory.equals("Weights and Capacities")) {

                                            JSONArray valueArray = jsonSpec.getJSONArray("specification_values");
                                            if (valueArray != null)
                                            {
                                                for (int k=0; k<valueArray.length(); k++)
                                                {
                                                    JSONObject jsonValue = valueArray.getJSONObject(k);
                                                    String specificName = jsonValue.optString("specification_name").replace(" ", "_");
                                                    String specificValue = jsonValue.optString("specification_value");
                                                    item.getSpecData().put(specificName, specificValue);
                                                }
                                            }

                                        }
                                    }
                                }

                                vinModelList.add(item);
                            }

                            mAdapter.setListData(vinModelList);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AppHelper.CustomToast(VinLookupActivity.this, "Error happened.");
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                try {
                    String decodeString = ConstantManager.encode_me.replace("searchText", searchText);

                    JSONObject obj = new JSONObject(decodeString);
                    Map<String,String> params = new HashMap<String, String>();
                    params.put("access_key_id", ConstantManager.accessKeyId);
                    params.put("secret_access_key", ConstantManager.secretAccessKey);
                    params.put("decoder_query", obj.toString());
                    return params;
                } catch (Throwable t) {
                    return null;
                }
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        queue.add(sr);
    }
}