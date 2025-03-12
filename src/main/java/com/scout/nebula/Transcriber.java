package com.scout.nebula;

import com.assemblyai.api.resources.transcripts.types.TranscriptStatus;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Transcriber {

    private static final String serviceURL = "https://api.assemblyai.com/v2/transcript";
    private HttpClient httpClient = HttpClient.newHttpClient();
    private Transcript transcript = new Transcript();
    private Gson gson = new Gson();
    private ConfigUtil configUtil = new ConfigUtil();

    public Transcriber() throws URISyntaxException, IOException, InterruptedException{

        transcript.setAudio_url("https://cdn.assemblyai.com/upload/9c99915f-5583-48e1-b017-3681cb74b041");
        String jsonRequest = gson.toJson(transcript);

        // TODO: Finish up decluttering the code for better readability and maintainability
        HttpResponse<String> postResponse = sendPostRequest( jsonRequest);
        transcript = gson.fromJson(postResponse.body(), Transcript.class);

        System.out.println(transcript.getId());

        sendGetRequest(transcript.getId());

// TODO: Add a method for uploading a file to AssemblyAI servers for convertion as the URI needs to start with https
//  or maybe figure out if i can work with a file

        System.out.println("Transcription Completed");
        System.out.println(transcript.getText());

    }

    private HttpResponse<String> sendPostRequest(String json) throws URISyntaxException, IOException, InterruptedException {
        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(new URI(serviceURL))
                .header("Authorization", configUtil.getApiKey())
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        return httpClient.send(postRequest, HttpResponse.BodyHandlers.ofString());
    }

    private void sendGetRequest(String id) throws URISyntaxException, IOException, InterruptedException {
        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(new URI(serviceURL + "/" + id))
                .header("Authorization", configUtil.getApiKey())
                .build();

        while (true) {
            HttpResponse<String> getResponse = httpClient.send(getRequest, HttpResponse.BodyHandlers.ofString());
            transcript = gson.fromJson(getResponse.body(), Transcript.class);

            System.out.println(transcript.getStatus());

            if(transcript.getStatus().equalsIgnoreCase(String.valueOf(TranscriptStatus.COMPLETED)) || transcript.getStatus().equalsIgnoreCase(String.valueOf(TranscriptStatus.ERROR))){
                break;
            }

            Thread.sleep(1500);
        }

        httpClient.send(getRequest, HttpResponse.BodyHandlers.ofString());
    }
}
