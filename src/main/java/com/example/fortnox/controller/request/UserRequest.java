package com.example.fortnox.controller.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UserRequest(
        @JsonProperty("id") Long id,
        @JsonProperty("name") String name,
        @JsonProperty("dateOfBirth") String dateOfBirth
) {}
