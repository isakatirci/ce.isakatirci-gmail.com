package com.huawei.mockserver;

import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.mockserver.client.server.MockServerClient;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.HttpForward;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.matchers.Times.exactly;
import static org.mockserver.model.HttpForward.forward;
import static org.mockserver.model.HttpRequest.request;

public class MockServerApplication {
    private static ClientAndServer mockServer;

    public static void main(String[] args) {

       /* System.setProperty("mockserver.forwardHttpProxy",args[0]);
        System.setProperty("mockserver.forwardHttpsProxy",args[0]);*/
        System.setProperty("mockserver.forwardProxyAuthenticationUsername",args[0]);
        System.setProperty("mockserver.proxyAuthenticationUsername",args[0]);
        System.setProperty("mockserver.forwardProxyAuthenticationPassword",args[1]);
        System.setProperty("mockserver.proxyAuthenticationPassword",args[1]);


        mockServer = startClientAndServer(9095);

        System.out.println("mockServerClient.isRunning() => " + mockServer.isRunning());

        mockServer.when(request())
                .forward(
                        forward()
                                .withHost(args[2])
                                .withPort(Integer.parseInt(args[3]))
                                .withScheme(HttpForward.Scheme.HTTP)
                );

        String url = "http://127.0.0.1:9095";
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet get = new HttpGet(url);
        //get.setHeader("Content-type", "application/json");
        org.apache.http.HttpResponse response = null;

        try {
            response = client.execute(get);
            HttpEntity entity = response.getEntity();

            // Read the contents of an entity and return it as a String.
            String content = EntityUtils.toString(entity);
            System.out.println(content);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

}
