/*
* This class transcribes an uploaded audio file.
* Entry point of the program
*/

package com.scout.nebula;

public  class AVTrascripter {

    private static Exception exceptionCaught;

    public static void main(String[] args)  {
        System.out.println("Transcribing audio file...");

        try{
            new Transcriber();
        } catch(Exception ex) {
            exceptionCaught = (Exception) ex;
            System.out.println("An error occurred: " + exceptionCaught.getMessage());
        }

    }
}

