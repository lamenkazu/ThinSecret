package com.daedrii.bodyapp.controller.fatsecret;

import java.io.IOException;

import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.signature.QueryStringSigningStrategy;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;

public class FatSecretApiService {
    private static final String BASE_URL = "https://platform.fatsecret.com/rest/server.api";
    private static final String CONSUMER_KEY = "2025ae264c714fd780759b99204d576f";
    private static final String CONSUMER_SECRET = "2b98c335db38495dba65b6396bc66069";

    public static String searchFoods(String query) throws IOException, OAuthException {
        OkHttpClient client = new OkHttpClient();

        // Construa a URL para a solicitação
        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL).newBuilder();
        urlBuilder.addQueryParameter("method", "foods.search");
        urlBuilder.addQueryParameter("search_expression", query);
        String url = urlBuilder.build().toString();

        // Crie o objeto OAuthConsumer com suas credenciais
        OAuthConsumer consumer = new CommonsHttpOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
        consumer.setSigningStrategy(new QueryStringSigningStrategy());

        // Assine a URL com o objeto OAuthConsumer
        String signedUrl = consumer.sign(url);

        // Crie a solicitação GET
        Request request = new Request.Builder()
                .url(signedUrl)
                .build();

        // Faça a chamada à API e obtenha a resposta
        Response response = client.newCall(request).execute();

        // Verifique se a resposta foi bem-sucedida
        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            throw new IOException("Erro na solicitação: " + response);
        }
    }
    public static String getFoodDetails(String foodId) throws IOException, OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException {
        OkHttpClient client = new OkHttpClient();

        // Construa a URL para a solicitação
        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL).newBuilder();
        urlBuilder.addQueryParameter("method", "food.get");
        urlBuilder.addQueryParameter("food_id", foodId);
        String url = urlBuilder.build().toString();

        // Crie o objeto OAuthConsumer com suas credenciais
        OAuthConsumer consumer = new CommonsHttpOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
        consumer.setSigningStrategy(new QueryStringSigningStrategy());

        // Assine a URL com o objeto OAuthConsumer
        String signedUrl = consumer.sign(url);

        // Crie a solicitação GET
        Request request = new Request.Builder()
                .url(signedUrl)
                .build();

        // Faça a chamada à API e obtenha a resposta
        Response response = client.newCall(request).execute();

        // Verifique se a resposta foi bem-sucedida
        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            throw new IOException("Erro na solicitação: " + response);
        }
    }
}
