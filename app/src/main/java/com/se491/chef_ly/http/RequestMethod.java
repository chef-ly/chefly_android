package com.se491.chef_ly.http;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class RequestMethod implements Parcelable {

    private String endPoint;
    private String method = "GET";
    private Map<String, String> params = new HashMap<>();
    private Map<String, String> headers = new HashMap<>();

    public String getEndpoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setParam(String key, String value) {
        params.put(key, value);
    }

    public void setHeader(String key, String value) {
        headers.put(key, value);
    }

    public String getEncodedParams() {
        StringBuilder sb = new StringBuilder();

        for (String key : params.keySet()) {
            String value = null;
            try {
                value = URLEncoder.encode(params.get(key), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }catch (NullPointerException e){
                Log.d("REQUESTMETHOD", e.getMessage());
            }

            if (sb.length() > 0) {
                sb.append("&");
            }
            sb.append(key).append("=").append(value);
        }
        return sb.toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.endPoint);
        dest.writeString(this.method);
        dest.writeInt(this.params.size());
        for (Map.Entry<String, String> entry : this.params.entrySet()) {
            dest.writeString(entry.getKey());
            dest.writeString(entry.getValue());
        }
        dest.writeInt(headers.size());
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            dest.writeString(entry.getKey());
            dest.writeString(entry.getValue());
        }
    }

    public RequestMethod() {
    }

    private RequestMethod(Parcel in) {
        this.endPoint = in.readString();
        this.method = in.readString();
        int paramsSize = in.readInt();
        this.params = new HashMap<>(paramsSize);
        for (int i = 0; i < paramsSize; i++) {
            String key = in.readString();
            String value = in.readString();
            this.params.put(key, value);
        }
        int headersSize = in.readInt();
        headers = new HashMap<>(headersSize);
        for (int i = 0; i < headersSize; i++) {
            String key = in.readString();
            String value = in.readString();
            headers.put(key, value);
        }
    }

    public static final Parcelable.Creator<RequestMethod> CREATOR = new Parcelable.Creator<RequestMethod>() {
        @Override
        public RequestMethod createFromParcel(Parcel source) {
            return new RequestMethod(source);
        }

        @Override
        public RequestMethod[] newArray(int size) {
            return new RequestMethod[size];
        }
    };
}