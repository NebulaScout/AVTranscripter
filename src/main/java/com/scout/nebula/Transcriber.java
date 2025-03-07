package com.scout.nebula;

import com.assemblyai.api.resources.transcripts.types.TranscriptStatus;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

//private static final String baseURL = "https://api.assemblyai.com";
//private static final String transcribeEndpoint = baseURL + "/v2/transcript";
//private final String serviceURL = "https://api.assemblyai.com/v2/transcript";

public class Transcriber {

    private static final String serviceURL = "https://api.assemblyai.com/v2/transcript";
    HttpClient httpClient = HttpClient.newHttpClient();

    public Transcriber() throws URISyntaxException, IOException, InterruptedException{
        ConfigUtil configUtil = new ConfigUtil();

        Transcript transcript = new Transcript();
        transcript.setAudio_url("https://cdn.assemblyai.com/upload/b3feb041-0ad2-48e9-ba92-6a568f99cb48");
        Gson gson = new Gson();
        String jsonRequest = gson.toJson(transcript);

//        System.out.println(jsonRequest);

//        HttpRequest postRequest = HttpRequest.newBuilder()
//                .uri(new URI("https://api.assemblyai.com/v2/transcript"))
//                .header("Authorization", configUtil.getApiKey())
//                .POST(HttpRequest.BodyPublishers.ofString(jsonRequest))
//                .build();
//
//        HttpClient httpClient = HttpClient.newHttpClient();
//        HttpResponse<String> postResponse = httpClient.send(postRequest, HttpResponse.BodyHandlers.ofString());
        // TODO: Finish up deluttering the code for better readability and maintainability
        HttpResponse<String> postResponse = sendPostRequest(configUtil, jsonRequest);
//        System.out.println(postResponse.body());
        transcript = gson.fromJson(postResponse.body(), Transcript.class);

        System.out.println(transcript.getId());

        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(new URI(serviceURL + "/" + transcript.getId()))
                .header("Authorization", configUtil.getApiKey())
                .build();

        while (true) {
            HttpResponse<String> getResponse = httpClient.send(getRequest, HttpResponse.BodyHandlers.ofString());
            transcript = gson.fromJson(getResponse.body(), Transcript.class);

            System.out.println(transcript.getStatus());

            if(transcript.getStatus().equalsIgnoreCase(String.valueOf(TranscriptStatus.COMPLETED)) || transcript.getStatus().equalsIgnoreCase(String.valueOf(TranscriptStatus.ERROR))){
                break;
            }

            Thread.sleep(1000);
        }
// TODO: Add a method for uploading a file to AssemblyAI servers for convertion as the URI needs to start with https
//  or maybe figure out if i can work with a file

        System.out.println("Transcription Completed");
        System.out.println(transcript.getText());

    }

    private HttpResponse<String> sendPostRequest(ConfigUtil configUtil, String json) throws URISyntaxException, IOException, InterruptedException {
//        return "This is a POST request";
        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(new URI(serviceURL))
                .header("Authorization", configUtil.getApiKey())
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();


        HttpResponse<String> response = httpClient.send(postRequest, HttpResponse.BodyHandlers.ofString());

        return response;
    }
}
