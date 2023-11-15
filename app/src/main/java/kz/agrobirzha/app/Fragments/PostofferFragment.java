package kz.agrobirzha.app.Fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import kz.agrobirzha.app.Activities.MainActivity;
import kz.agrobirzha.app.Classes.AppConfig;
import kz.agrobirzha.app.Classes.AppController;
import kz.agrobirzha.app.Helpers.SQLiteHandler;
import kz.agrobirzha.app.R;

import static com.android.volley.VolleyLog.TAG;

public class PostofferFragment extends Fragment {
    private SQLiteHandler db;
    private Button btnSubmit, btnPhoto;
    private EditText etTitle, etDesc, etPrice;
    private Spinner categoryspinner;
    private ProgressDialog pDialog;
    Bitmap FixBitmap;
    byte[] byteArray;
    String ConvertImage;
    private String category_id;
    ByteArrayOutputStream byteArrayOutputStream;
    private int GALLERY = 1, CAMERA = 2;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_postoffer, container, false);
        String[] arraySpinner = new String[] {
                "Сельхозпродукты", "Транспортные компании", "Ремонт сельхозтехники"
        };
        categoryspinner = (Spinner) rootView.findViewById(R.id.snCategory);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoryspinner.setAdapter(adapter);
        etTitle = (EditText) rootView.findViewById(R.id.etTitle);
        etDesc = (EditText) rootView.findViewById(R.id.etDesc);
        etPrice = (EditText) rootView.findViewById(R.id.etPrice);
        btnSubmit = (Button) rootView.findViewById(R.id.btnSubmit);
        btnPhoto = (Button) rootView.findViewById(R.id.btnPic);
        pDialog = new ProgressDialog(getContext());
        pDialog.setCancelable(false);
        categoryspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                category_id = String.valueOf(categoryspinner.getSelectedItemPosition() + 1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        // Register Button Click event
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String title = etTitle.getText().toString().trim();
                String shortdesc = etDesc.getText().toString().trim();
                String price = etPrice.getText().toString().trim();

                if (!title.isEmpty() && !shortdesc.isEmpty() && !price.isEmpty()) {
                        registerUser(title, shortdesc, category_id, price, ConvertImage);

                } else {
                    Toast.makeText(getActivity(),
                            "Пожалуйста, заполните все поля!", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });
        byteArrayOutputStream = new ByteArrayOutputStream();
        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPictureDialog();
            }
        });
        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{android.Manifest.permission.CAMERA},
                        5);
            }
        }
        return rootView;
    }
    private void showPictureDialog(){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(getContext());
        pictureDialog.setTitle("Выберите действие");
        String[] pictureDialogItems = {
                "Галерея",
                "Камера" };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }
    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    FixBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), contentURI);
                    Toast.makeText(getActivity(), "Картинка сохранена!", Toast.LENGTH_SHORT).show();

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            FixBitmap = (Bitmap) data.getExtras().get("data");
            Toast.makeText(getContext(), "Картинка сохранена!", Toast.LENGTH_SHORT).show();
        }
    }

    private void registerUser(final String title, final String shortdesc,
                              final String category_id, final String price, final String image) {

        db = new SQLiteHandler(getContext());
        final String uid = db.getUserDetails().get("uid");
        FixBitmap.compress(Bitmap.CompressFormat.JPEG, 40, byteArrayOutputStream);

        byteArray = byteArrayOutputStream.toByteArray();

        ConvertImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
        // Tag used to cancel the request
        String tag_string_req = "offer_register";

        pDialog.setMessage("Добавление ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_ADDOFFER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Adding Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {

                        Toast.makeText(getContext(), "Предложение успешно добавлено!", Toast.LENGTH_LONG).show();

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

                Map<String, String> params = new HashMap<String, String>();
                params.put("title", title);
                params.put("shortdesc", shortdesc);
                params.put("category_id", category_id);
                params.put("price", price);
                params.put("image", ConvertImage);
                params.put("uid", uid);
                return params;
            }

        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

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
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 5) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {


            }
            else {

                Toast.makeText(getContext(), "Невозможно подключиться к камере.", Toast.LENGTH_LONG).show();

            }
        }
    }
}
