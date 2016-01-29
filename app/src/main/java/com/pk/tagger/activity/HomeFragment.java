package com.pk.tagger.activity;

/**
 * Created by PK on 16/01/2016.
 */
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.pk.tagger.AppController;
import com.pk.tagger.R;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.pk.tagger.services.DatabaseSyncService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;


public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }

    @Bind(R.id.TVusername)
    TextView _textviewusername;
    @Bind(R.id.btnArrayRequest)
    Button _btnMakeArrayRequest;
    @Bind(R.id.btnStartService)
    Button _btnStartService;
    @Bind(R.id.TVusername2)
    TextView _textviewusername2;

    private static String TAG = MainActivity.class.getSimpleName();

    // Progress dialog
    private ProgressDialog pDialog;

    // temporary string to show the parsed response
    private String jsonResponse;

    private static final String QUERY_URL = "http://52.31.31.106:9000/apiunsecure";

    Handler handler = new Handler();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, rootView);

        String username = "Username";
        _textviewusername.setText(username);

        _btnMakeArrayRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeJsonArrayRequest();
            }
        });

        _btnStartService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickedStartService();
            }
        });

        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void makeJsonArrayRequest() {

        showpDialog();

        JsonArrayRequest req = new JsonArrayRequest(QUERY_URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());

                        try {
                            // Parsing json array response
                            // loop through each json object
                            jsonResponse = "";
                            for (int i = 0; i < response.length(); i++) {

                                JSONObject person = (JSONObject) response
                                        .get(i);

                                String message = person.getString("message");
                                //String email = person.getString("email");
                                //JSONObject phone = person
                                //      .getJSONObject("phone");

                                jsonResponse += "Message: " + message + "\n\n";
                                //jsonResponse += "Email: " + email + "\n\n";
                            }

                            Log.d(TAG, jsonResponse);
                            _textviewusername.setText(jsonResponse);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(),
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }

                        hidepDialog();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                hidepDialog();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req);
    }

    private void onClickedStartService() {
        Intent i = new Intent(getContext(), DatabaseSyncService.class);
        i.putExtra(DatabaseSyncService.RESULT_RECEIVER_NAME, syncResultReceiver);
        getContext().startService(i);
        onServiceStarted();
    }



    final ResultReceiver syncResultReceiver = new ResultReceiver(handler) {
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            if (resultCode == DatabaseSyncService.DATA) {
                String result = resultData.getString("result");
                Toast.makeText(getContext(),
                        result,
                        Toast.LENGTH_LONG).show();
            } else if (resultCode == DatabaseSyncService.FINISHED) {
                String result = resultData.getString("result");
                Toast.makeText(getContext(),
                        result,
                        Toast.LENGTH_LONG).show();
                onServiceFinished();
            } else if (resultCode == DatabaseSyncService.JSONSENT) {
                String result = resultData.getString("result");
                Toast.makeText(getContext(),
                        result,
                        Toast.LENGTH_LONG).show();
                onJSONReceived(result);
            }
        }
        }

        ;

        private void onServiceStarted() {
            _btnStartService.setEnabled(false);
        }

        private void onServiceFinished() {
            _btnStartService.setEnabled(true);
        }

    private void onJSONReceived(String result) {
        _textviewusername2.setText(result);
    }

        private void showpDialog() {
            if (!pDialog.isShowing())
                pDialog.show();
        }

        private void hidepDialog() {
            if (pDialog.isShowing())
                pDialog.dismiss();
        }
    }


