package com.uday.controller;

import com.uday.model.Contacts.ContactsOutput;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Uday on 13-02-2018.
 */

public interface APIService {


    @GET("contacts.json")
    Call<List<ContactsOutput>> readContacts();


}


