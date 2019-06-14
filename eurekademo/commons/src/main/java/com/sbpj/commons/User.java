package com.sbpj.commons;

import lombok.Data;

@Data
public class User {

    private String userName;

    private String host;

    private int port;

    private String serviceId;
}
