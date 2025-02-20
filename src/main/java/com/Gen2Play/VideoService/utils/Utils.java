package com.Gen2Play.VideoService.utils;

import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
public class Utils {
    public static UUID getCurentUserID(){
        UUID userID;
        //testtingcode
        userID = new UUID(0, 0);
        //get user id through jwt tokenn
        //TODO: Get Userid by token given
        return userID;
    }
}
