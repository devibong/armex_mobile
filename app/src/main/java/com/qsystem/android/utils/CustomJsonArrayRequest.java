package com.qsystem.android.utils;

import android.support.annotation.Nullable;


import com.android.volley.NetworkResponse;

import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.ParseError;
import com.android.volley.request.JsonRequest;
import com.android.volley.toolbox.HttpHeaderParser;


import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/** A request for retrieving a {@link JSONArray} response body at a given URL. */
public class CustomJsonArrayRequest extends JsonRequest<JSONArray> {

    /**
     * Creates a new request.
     *
     * @param url URL to fetch the JSON from
     * @param listener Listener to receive the JSON response
     * @param errorListener Error listener, or null to ignore errors.
     */
    Map<String,String> params;
    public CustomJsonArrayRequest(
            String url, Listener<JSONArray> listener, @Nullable ErrorListener errorListener) {
        super(Method.GET, url, null, listener, errorListener);
    }


    public CustomJsonArrayRequest(int method,String url,Map<String, String> params,Listener<JSONArray> listener,@Nullable ErrorListener errorListener) {
        super(method,url,null,listener,errorListener);
        this.params = params;

        //super(method,url,listener,errorListener);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {

        HashMap<String, String> headers = new HashMap<String, String>();
        //headers.put("Content-Type", "application/json; charset=utf-8");
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        //headers.put("User-agent", "My useragent");

        return headers;
        //return super.getHeaders();
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return this.params;
    }


    @Override
    protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString =
                    new String(
                            response.data,
                            HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));
            return Response.success(
                    new JSONArray(jsonString), HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }
}
