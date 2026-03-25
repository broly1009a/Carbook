/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

/**
 *
 * @author
 */
/*

 */

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import constant.Iconstant;
import java.io.IOException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import constant.FbIconstant;
import constant.Iconstant;
import java.io.IOException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;

import java.io.IOException;

import java.io.IOException;

import java.io.IOException;

/**
 *
 * @author hson5
 */
public class FacebookLogin {
    public static String getToken(String code) throws ClientProtocolException, IOException {
        String response = Request.Post(FbIconstant.FACEBOOK_LINK_GET_TOKEN)
                .bodyForm(
                        Form.form()
       .add("client_id", FbIconstant.FACEBOOK_CLIENT_ID)
                        .add("client_secret", FbIconstant.FACEBOOK_CLIENT_SECRET)
                        .add("redirect_uri", FbIconstant.FACEBOOK_REDIRECT_URI)
                        .add("code", code)
                        .build()
                )
                .execute().returnContent().asString();
        JsonObject jobj = new Gson().fromJson(response, JsonObject.class);
        String accessToken = jobj.get("access_token").toString().replaceAll("\"", "");
        return accessToken;
    }
    
     public static FacebookAccount getUserInfo(final String accessToken) throws ClientProtocolException, IOException {
        String link = FbIconstant.FACEBOOK_LINK_GET_USER_INFO + accessToken;
        String response = Request.Get(link).execute().returnContent().asString();
        FacebookAccount fbAccount= new Gson().fromJson(response, FacebookAccount .class);
        return fbAccount;
    }
}
