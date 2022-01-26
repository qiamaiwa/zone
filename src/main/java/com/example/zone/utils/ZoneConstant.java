package com.example.zone.utils;

public  interface ZoneConstant   {

    int ACTIVATION_REPEAT=1;
    int ACTIVATION_SUCCESS=0;
     int ACTIVATION_FAILURE=2;

     int DEFAULT_EXPIRED_SECONDS=3600*12;
    int REMEMBER_EXPIRED_SECONDS=3600*24;

    int ENTITY_TYPE_POST=1;
    int ENTITY_TYPE_COMMENT=2;
    int ENTITY_TYPE_USER=3;
    String TOPIC_COMMENT="comment";
    String TOPIC_LIKE="like";
    String TOPIC_FOLLOW="follow";
    int SYSTEM_USER_ID=1;


}
