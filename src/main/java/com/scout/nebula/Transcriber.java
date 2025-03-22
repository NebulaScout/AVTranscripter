package com.scout.nebula;

import com.assemblyai.api.AssemblyAI;
import com.assemblyai.api.resources.transcripts.types.Transcript;

import java.io.File;
import java.io.IOException;

public class Transcriber {
    private final ConfigUtil configUtil = new ConfigUtil();
    private Transcript transcript;

    public Transcriber() throws IOException {
        AssemblyAI aai = AssemblyAI.builder()
                .apiKey(configUtil.getApiKey())
                .build();

//        Transcript transcript = aai.transcripts().get("transcript-id");
        // Transcribe file at remote URL
//        Transcript transcript = aai.transcripts().transcribe("https://assembly.ai/espn.m4a");

        // Upload a file via local path and transcribe
        transcript = aai.transcripts().transcribe(
                new File("/home/kabi/Desktop/Thirsty.mp4"));

        // TODO: Make user select a file (can be from drive, local, or remote URL)
//        TODO: Make user select a language
//        TODO: Check the file format and ensure it adheres to the required formats

        System.out.println("Received response!" + transcript);


    }
}
