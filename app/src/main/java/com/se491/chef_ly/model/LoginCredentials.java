package com.se491.chef_ly.model;

import android.os.Parcelable;
import android.os.Parcel;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


/**
 * Created by tom on 5/1/17.
 */

public class LoginCredentials implements Parcelable, Serializable {

    @SerializedName("access_token")
    private String accessToken;

    @SerializedName("expires_in")
    private int expiresIn;

    @SerializedName("scope")
    private String scope;

    @SerializedName("id_token")
    private String idToken;

    @SerializedName("token_type")
    private String tokenType;

    public LoginCredentials(String accessToken, int expiresIn, String scope, String idToken, String tokenType){
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
        this.scope = scope;
        this.idToken = idToken;
        this.tokenType = tokenType;
    }

    protected LoginCredentials(Parcel in){
        accessToken = in.readString();
        expiresIn = in.readInt();
        scope = in.readString();
        idToken = in.readString();
        tokenType = in.readString();
    }

    @Override
    public int describeContents (){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(accessToken);
        dest.writeInt(expiresIn);
        dest.writeString(scope);
        dest.writeString(idToken);
        dest.writeString(tokenType);
    }

    public static final Creator<LoginCredentials> CREATOR = new Creator<LoginCredentials>() {
        @Override
        public LoginCredentials createFromParcel(Parcel in) {
            return new LoginCredentials(in);
        }

        @Override
        public LoginCredentials[] newArray(int size) {
            return new LoginCredentials[size];
        }
    };

}
