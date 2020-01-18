package com.tamirm.reali;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Date;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class MessagesAppServer {

    private static final String REDIS_MESSAGES_KEY = "messages";
    private static final String REDIS_HOST = "localhost";
    private static final int REDIS_PORT = 6379;

    private static final int MAX_RETRIES = 3;
    private static final int WAIT_MINS = 1 * 60 * 1000;
    private int retries = 0;

    private Jedis jedis;

    private void checkJedis() {
        if(jedis == null) {
            this.jedis = new Jedis(REDIS_HOST, REDIS_PORT);
            jedis.select(2);
        }
    }

    public static void main(String[] args) throws Exception {
        MessagesAppServer server = new MessagesAppServer();
        server.initServer();
        server.printMessages();
    }
    private class EchoAtTimeHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange request) throws IOException {
            checkJedis();
            Message message = MessageUtil.formatMessage(request.getRequestURI().getQuery());
            jedis.zadd(REDIS_MESSAGES_KEY, message.getPrintTime(), message.getText());

            String response = "Message added: " + message.getText();
            request.sendResponseHeaders(200, response.length());
            OutputStream os = request.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    private void initServer() throws IOException {
        HttpServer server= HttpServer.create(new InetSocketAddress(8000), 0);

        // server's single api for creating a new message
        server.createContext("/echoAtTime", new EchoAtTimeHandler());

        // in order to shut the server down gracefully.
        server.createContext("/exit", new ExitHandler());
        server.setExecutor(java.util.concurrent.Executors.newCachedThreadPool());
        server.start();

        System.out.println("Server is listening...");
    }

    private class ExitHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange request) {
            System.exit(0);
        }
    }

    private void printMessages() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    checkJedis();
                    Set<String> messages = jedis.zrangeByScore(REDIS_MESSAGES_KEY, 0, new Date().getTime());
                    if (!messages.isEmpty()) {
                        jedis.zremrangeByScore(REDIS_MESSAGES_KEY, 0, new Date().getTime());
                        messages.forEach(message -> {
                            if (message.toLowerCase().trim().equals("exit")) {
                                System.out.println("Bye bye!");
                                return;
                            } else {
                                System.out.println(message);
                                Long zcount = jedis.zcount(REDIS_MESSAGES_KEY, 0, Long.MAX_VALUE);
                                if(zcount > 0) {
                                    System.out.println("Redis still contains " + zcount + " awaiting messages");
                                } else {
                                    System.out.println("There are no messages in Redis");
                                }
                            }
                        });
                    }

                } catch (JedisConnectionException e) {
                    retries++;
                    if (retries < MAX_RETRIES) {
                        System.err.println("MessagesClient cannot connect to redis trying again in 1 min...");
                    } else {
                        System.err.println("MessagesClient connect to redis stopping client");
                        return;
                    }
                    try {
                        Thread.sleep(WAIT_MINS);
                        printMessages();
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }, 1000L, 1000L);
    }

    @AllArgsConstructor
    @Getter
    public static class Message {
        private String text;
        private Long printTime;
    }
}