package com.tamirm.reali;

import redis.clients.jedis.Jedis;

// thread safe redis singleton
public class RedisSingleton {
    private static final String REDIS_HOST = "localhost";
    private static final int REDIS_PORT = 6379;
    private static final int REDIS_INDEX = 2;

    private static Jedis instance;

    private RedisSingleton(){}

    public static synchronized Jedis getInstance(){
        if(instance == null){
            instance = new Jedis(REDIS_HOST, REDIS_PORT);
            instance.select(REDIS_INDEX);
        }
        return instance;
    }
}
