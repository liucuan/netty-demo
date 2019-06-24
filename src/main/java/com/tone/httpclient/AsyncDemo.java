package com.tone.httpclient;

import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.nio.IOControl;
import org.apache.http.nio.client.methods.AsyncCharConsumer;
import org.apache.http.nio.client.methods.HttpAsyncMethods;
import org.apache.http.nio.protocol.HttpAsyncRequestProducer;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;
import java.nio.CharBuffer;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class AsyncDemo {
    public static void main(String[] args) {
        String url = "http://www.apache.org/";
        CloseableHttpAsyncClient httpAsyncClient = HttpAsyncClients.createDefault();
        try {
            httpAsyncClient.start();
            final HttpGet request1 = new HttpGet(url);
            Future<HttpResponse> future = httpAsyncClient.execute(request1, null);
            // wait until a response is received
            HttpResponse response1 = future.get();
            System.out.println(request1.getRequestLine() + "->" + response1.getStatusLine());

            final CountDownLatch latch1 = new CountDownLatch(1);
            final HttpGet request2 = new HttpGet(url);
            httpAsyncClient.execute(request2, new FutureCallback<HttpResponse>() {
                @Override
                public void completed(HttpResponse httpResponse) {
                    latch1.countDown();
                    System.out.println(request2.getRequestLine() + "->" + httpResponse.getStatusLine());
                }

                @Override
                public void failed(Exception e) {
                    latch1.countDown();
                    System.out.println(request2.getRequestLine() + "->" + e);
                }

                @Override
                public void cancelled() {
                    latch1.countDown();
                    System.out.println(request2.getRequestLine() + " cancelled");
                }
            });
            latch1.await();

            final CountDownLatch latch2 = new CountDownLatch(1);
            final HttpGet request3 = new HttpGet(url);
            HttpAsyncRequestProducer producer = HttpAsyncMethods.create(request3);
            AsyncCharConsumer<HttpResponse> consumer = new AsyncCharConsumer<HttpResponse>() {
                HttpResponse response;

                @Override
                protected void onCharReceived(CharBuffer buf, IOControl ioctrl) throws IOException {
                }

                @Override
                protected void onResponseReceived(HttpResponse httpResponse) throws HttpException, IOException {
                    this.response = httpResponse;
                }

                @Override
                protected HttpResponse buildResult(HttpContext httpContext) throws Exception {
                    return this.response;
                }
            };
            httpAsyncClient.execute(producer, consumer, new FutureCallback<HttpResponse>() {
                @Override
                public void completed(HttpResponse httpResponse) {
                    latch2.countDown();
                    System.out.println(request3.getRequestLine() + "->" + httpResponse.getStatusLine());
                }

                @Override
                public void failed(Exception e) {
                    latch2.countDown();
                    System.out.println(request3.getRequestLine() + "->" + e);

                }

                @Override
                public void cancelled() {
                    latch2.countDown();
                    System.out.println(request3.getRequestLine() + " cancelled");

                }
            });
            latch2.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }finally {
            try {
                httpAsyncClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
