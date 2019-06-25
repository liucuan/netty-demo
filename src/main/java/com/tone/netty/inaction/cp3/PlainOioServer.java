package com.tone.netty.inaction.cp3;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.concurrent.Executors;

/**
 * Created by zhaoxiang.liu on 2017/4/14.
 */
public class PlainOioServer {
    public void server(int port) throws IOException {
        final ServerSocket serverSocket = new ServerSocket(port);
        try {
            for (;;) {
                final Socket clientSocket = serverSocket.accept();
                System.out.println("Accepted connection from " + clientSocket);
                new Thread(new Runnable(){ // 3
                            @Override
                            public void run() {
                                OutputStream out;
                                try {
                                    out = clientSocket.getOutputStream();
                                    out.write("Hi!\r\n".getBytes(Charset.forName("UTF-8"))); // 4
                                    out.flush();
                                    clientSocket.close(); // 5
                                }catch (IOException e) {
                                    e.printStackTrace();
                                    try {
                                        clientSocket.close();
                                    }catch (IOException ex) {
                                        // ignore on close
                                    }
                                }
                            }
                        }).start();
            }
        }catch (Exception e) {
        }
    }
}
