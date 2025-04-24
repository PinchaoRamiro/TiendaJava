package com.tiendajava.repository;

import java.net.http.HttpClient;

import com.google.gson.Gson;

public interface IRepository {

  Gson gson = new Gson();

  String URL_BASE = "http://localhost:5000/api/";
  HttpClient client = HttpClient.newHttpClient();

}
