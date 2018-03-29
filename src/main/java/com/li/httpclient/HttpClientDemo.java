package com.li.httpclient;

import org.apache.http.*;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicListHeaderIterator;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;


public class HttpClientDemo {

    public static void main(String[] args) throws Exception {

        CloseableHttpClient httpClient = HttpClients.createDefault();

        URI uri = new URIBuilder()
                .setScheme("http")
                .setHost("www.163.com")
                .build();

        HttpGet httpGet = new HttpGet(uri);

        CloseableHttpResponse response = httpClient.execute(httpGet);

        String result = EntityUtils.toString(response.getEntity(), "utf-8");
        System.out.println(result);

        response.close();
    }

    @Test
    public void response() {

        BasicHttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "ok2");

        System.out.println(response.getProtocolVersion());
        System.out.println(response.getStatusLine().getStatusCode());
        System.out.println(response.getStatusLine().getReasonPhrase());
        System.out.println(response.getStatusLine().toString());
    }

    @Test
    public void cookie() {

        BasicHttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "ok");
        response.addHeader("Set-Cookie", "c1=a;path=/;domain=localhost");
        response.addHeader("Set-Cookie", "c2=b;path=/;domain=\"localhost\"");

        Header h1 = response.getFirstHeader("Set-Cookie");
//        System.out.println(h1);

        Header h2 = response.getLastHeader("Set-Cookie");
//        System.out.println(h2);

//        Header[] hs = response.getHeaders("Set-Cookie");
//        System.out.println(hs.length);

        /*HeaderIterator it = response.headerIterator("Set-Cookie");
        while (it.hasNext()){

            System.out.println(it.next());
        }*/

        BasicHeaderElementIterator it = new BasicHeaderElementIterator(response.headerIterator("Set-Cookie"));

        while (it.hasNext()) {

            HeaderElement elem = it.nextElement();
            System.out.println(elem.getName() + "=" + elem.getValue());
            NameValuePair[] params = elem.getParameters();

            for (int i = 0; i < params.length; i++) {

                System.out.println(" " + params[i]);
            }
        }

    }

    @Test
    public void httpEntity() throws IOException {

        StringEntity myEntity = new StringEntity("important message",
                ContentType.create("text/plain", "UTF-8"));

        System.out.println(myEntity.getContentType());
        System.out.println(myEntity.getContentLength());
        System.out.println(EntityUtils.toString(myEntity));
        System.out.println(EntityUtils.toByteArray(myEntity).length);

    }

    @Test
    public void closeableHttpClientDemo() {

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("http://www.baidu.com");
        CloseableHttpResponse response = null;
        try {

            response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();

            String s = EntityUtils.toString(entity);

//            InputStream instream = entity.getContent();
//
//            int byteone = instream.read();
//            int bytetwo = instream.read();


            long len = entity.getContentLength();
            System.out.println(len);
            if (len != -1 && len < 2048) {
                System.out.println(EntityUtils.toString(entity));
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void fileEntity(){

        File file = new File("a.txt");
        FileEntity entity = new FileEntity(file,ContentType.create("text/plain","UTF-8"));

        HttpPost httpPost = new HttpPost("http://localhost/action.do");

        httpPost.setEntity(entity);
    }

    @Test
    public void httpform(){

        ArrayList<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("param1","value1"));
        formparams.add(new BasicNameValuePair("param2","value2"));

        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);
        HttpPost httpPost = new HttpPost("http://localhost/action.do");

        httpPost.setEntity(entity);
    }
}
