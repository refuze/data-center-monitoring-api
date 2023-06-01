package com.example.datacentermonitoringapi.util;

import com.example.datacentermonitoringapi.configuration.exception.HttpRuntimeException;
import org.springframework.http.HttpStatus;

public class Utils {

    public static long extractIdFromUsername(String username) {
        String[] split = username.split("_");

        if (split.length != 2) {
            throw new HttpRuntimeException("Incorrect username format", HttpStatus.BAD_REQUEST);
        }

        try {
            return Long.parseLong(split[1]);
        } catch (NumberFormatException e) {
            throw new HttpRuntimeException("Incorrect username format", HttpStatus.BAD_REQUEST);
        }
    }

}
