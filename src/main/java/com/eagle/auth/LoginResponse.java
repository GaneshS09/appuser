package com.eagle.auth;


import java.util.List;

record LoginResponse(String id,List<String> roles) {}
