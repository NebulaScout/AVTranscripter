/*
* This class transcribes an uploaded audio file.
* Entry point of the program
*/

package com.scout.nebula;

import java.io.IOException;
import java.net.URISyntaxException;

public  class AVTrascripter {
    public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException {
        System.out.println("Transcribing audio file...");

        new Transcriber();

    }
}

