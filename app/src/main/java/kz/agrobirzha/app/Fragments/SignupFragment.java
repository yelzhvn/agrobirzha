package kz.agrobirzha.app.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import kz.agrobirzha.app.Activities.MainActivity;
import kz.agrobirzha.app.Classes.AppConfig;
import kz.agrobirzha.app.Classes.AppController;
import kz.agrobirzha.app.Helpers.SQLiteHandler;
import kz.agrobirzha.app.Helpers.SessionManager;
import kz.agrobirzha.app.R;

import static com.android.volley.VolleyLog.TAG;


public class SignupFragment extends Fragment {
    private Button btnRegister;
    private EditText etName, etEmail, etPassword;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_signup, container, false);

        etName = (EditText) rootView.findViewById(R.id.etName);
        etEmail = (EditText) rootView.findViewById(R.id.etEmail);
        etPassword = (EditText) rootView.findViewById(R.id.etPassword);
        btnRegister = (Button) rootView.findViewById(R.id.btnRegister);
        pDialog = new ProgressDialog(getContext());
        pDialog.setCancelable(false);

        session = new SessionManager(getContext());


        db = new SQLiteHandler(getContext());


        if (session.isLoggedIn()) {
            Intent intent = new Intent(getActivity(),
                    MainActivity.class);
            startActivity(intent);
            getActivity().finish();
        }


        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String name = etName.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                    registerUser(name, email, password);
                } else {
                    Toast.makeText(getActivity(),
                            "Введите данные!", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

        return rootView;
    }

    private void registerUser(final String name, final String email,
                              final String password) {

        String tag_string_req = "req_register";

        pDialog.setMessage("Регистрация ...");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_REGISTER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {

                        String uid = jObj.getString("uid");

                        JSONObject user = jObj.getJSONObject("user");
                        String name = user.getString("name");
                        String email = user.getString("email");
                        String created_at = user
                                .getString("created_at");

                        db.addUser(name, email, uid, created_at);

                        Toast.makeText(getContext(), "Вы прошли регистрацию! Можете входить в аккаунт.", Toast.LENGTH_LONG).show();


                        Intent intent = new Intent(
                                getContext(),
                                MainActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    } else {



                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", name);
                params.put("email", email);
                params.put("password", password);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
