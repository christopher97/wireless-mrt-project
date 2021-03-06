package com.example.dhyatmika.fp_layout.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.dhyatmika.fp_layout.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public final class Helper {

    private Helper() {
        /*
         *    private constructor to prevent instantiation of this class
         */
    }

    public static final void toast(Context ctx, String message, int length) {
        Toast toast = null;

        if (length == 0)
            toast = Toast.makeText(ctx, message, Toast.LENGTH_SHORT);
        else
            toast = Toast.makeText(ctx, message, Toast.LENGTH_LONG);

        toast.show();
    }

    public static final SharedPreferences getPreference(Context ctx) {
        return ctx.getSharedPreferences("LOGIN_CREDENTIALS", ctx.MODE_PRIVATE);
    }

    public static final String getToken(Context ctx) {
        SharedPreferences preference = getPreference(ctx);
        return preference.getString("token", "");
    }

    public static final void MakeJsonObjectRequest(Context context,
                                                   int method, String url, JSONObject body,
                                                   final VolleyResponseCallback callback) {

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (method, url, body, new Response.Listener<JSONObject>() {

                    // if response is HTTP 200 OK
                    @Override
                    public void onResponse(JSONObject response) {
                        callback.onResponse(response);
                    }

                }, new Response.ErrorListener() {

                    // on error
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onError(error);
                    }

                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = super.getHeaders();

                HashMap<String, String> res = callback.setHeaders();
                if (res != null)
                    headers = res;

                return headers;
            }
        };

        VolleySingleton.getInstance(context).addToRequestQueue(jsObjRequest);
    }

    public static final Bitmap customMarker(Resources resources) {
        int mWidth = 120;   // marker width
        int mHeight = 120;  // marker height
        BitmapDrawable bd = (BitmapDrawable) resources.getDrawable(R.drawable.train, null);
        Bitmap bmp = bd.getBitmap();
        Bitmap marker = Bitmap.createScaledBitmap(bmp, mWidth, mHeight, false);

        return marker;
    }
}
