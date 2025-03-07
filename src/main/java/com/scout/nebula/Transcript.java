/*
* This class contains the uploaded media data that will help in obtaining the transcript for the specified file.
*/

package com.scout.nebula;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Transcript {
    private  String audio_url;
    private  String id;
    private  String status;
    private  String text;
}
