package com.WhoKnows.Medix.model.DTOs;

import lombok.Data;

@Data
public class Response {
    private String message;
    // Constructor that accepts a message string
    public Response(String message) {
        this.message = message;
    }
}
