package com.eagle.auth.dto.response;


import java.util.List;

public record LoginResponse(String id,List<String> roles) {}
