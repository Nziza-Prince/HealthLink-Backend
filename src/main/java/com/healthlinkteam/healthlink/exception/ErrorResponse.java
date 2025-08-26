package com.healthlinkteam.healthlink.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponse {
    private int status;
    private String error;    // e.g. "Bad Request"
    private String message;  // e.g. "Doctor not found"
    private String path;     // request URI
    private long timestamp;  // epoch millis
}
