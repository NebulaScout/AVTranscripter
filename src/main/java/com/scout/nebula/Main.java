/*
* This class transcribes an uploaded audio file
*/

package com.scout.nebula;

import com.assemblyai.api.resources.transcripts.types.TranscriptStatus;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;

public class Main {
    public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException {
        ConfigUtil configUtil = new ConfigUtil();

        Transcript transcript = new Transcript();
        transcript.setAudio_url("https://cdn.assemblyai.com/upload/b3feb041-0ad2-48e9-ba92-6a568f99cb48");
        Gson gson = new Gson();
        String jsonRequest = gson.toJson(transcript);

//        System.out.println(jsonRequest);

        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(new URI("https://api.assemblyai.com/v2/transcript"))
                .header("Authorization", configUtil.getApiKey())
//                .header("Content-Type", "application/json")
                .POST(BodyPublishers.ofString(jsonRequest))
                .build();

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpResponse<String> postResponse = httpClient.send(postRequest, HttpResponse.BodyHandlers.ofString());

//        System.out.println(postResponse.body());
        transcript = gson.fromJson(postResponse.body(), Transcript.class);

        System.out.println(transcript.getId());

        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(new URI("https://api.assemblyai.com/v2/transcript" + "/" + transcript.getId()))
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
}
