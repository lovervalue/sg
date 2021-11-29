package com.sofn.bigdata.utils;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;

/**
 * @author:袁彬锋
 * @date:2020/4/27 9:33
 * @company:曙光集团绿康元公司
 * @description:获取html页面,下载并保存图片工具类
 */
public class Crawlers {

    //这是get请求不带参数
    public static String getContent(String url, CloseableHttpClient httpClient, RequestConfig requestConfig) {
        //获取请求对象
        HttpGet httpGet = new HttpGet(url);
        httpGet.setConfig(requestConfig);

        //发送请求
        CloseableHttpResponse httpResponse = null;
        try {
            httpResponse = httpClient.execute(httpGet);
            if (httpResponse == null || httpResponse.getStatusLine().getStatusCode() != 200) {
                throw new RuntimeException("请求失败");
            }
            HttpEntity httpEntity = httpResponse.getEntity();
            if (httpEntity != null) {
                String content = EntityUtils.toString(httpEntity);
                return content;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //关闭资源
            try {
                httpResponse.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    //这是get请求自定义请求对象，可提前自封装参数
    public static String getContent(HttpGet httpGet, RequestConfig requestConfig, CloseableHttpClient httpClient) {
        //设置请求对象
        httpGet.setConfig(requestConfig);

        //发送请求
        CloseableHttpResponse httpResponse = null;
        try {
            httpResponse = httpClient.execute(httpGet);
            if (httpResponse == null || httpResponse.getStatusLine().getStatusCode() != 200) {
                throw new RuntimeException("请求失败");
            }
            HttpEntity httpEntity = httpResponse.getEntity();
            if (httpEntity != null) {
                String content = EntityUtils.toString(httpEntity);
                return content;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //关闭资源
            try {
                httpResponse.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    //通过图片url获取图片名称并将图片下载上传到服务器
    public static String getImgNameAndUpload(String url, CloseableHttpClient httpClient, RequestConfig requestConfig){
        //封装请求参数
        HttpGet httpGet = new HttpGet(url);
        httpGet.setConfig(requestConfig);

        //发送请求，获取相应
        CloseableHttpResponse closeableHttpResponse = null;
        CloseableHttpResponse response = null;
        try {
            closeableHttpResponse = httpClient.execute(httpGet);
            if (closeableHttpResponse == null || closeableHttpResponse.getStatusLine().getStatusCode() != 200){
                throw new RuntimeException("请求失败");
            }
            HttpEntity httpEntity = closeableHttpResponse.getEntity();
            if (httpEntity != null){
                //先将图片上传到文件服务器上
                InputStream inputStream = new URL(url).openStream();

                //获取名称
                String ext = url.substring(url.lastIndexOf(".") - 1);
                String pictureName = UUID.randomUUID().toString() + ext;

                HttpPost httpPost = new HttpPost("http://localhost:8484/file/uploadFile");
                MultipartEntityBuilder builder = MultipartEntityBuilder.create();
                builder.addBinaryBody("file", inputStream, ContentType.MULTIPART_FORM_DATA, pictureName);
                HttpEntity entity = builder.build();
                httpPost.setEntity(entity);
                httpPost.setConfig(requestConfig);
                
                //将文件上传
                response = httpClient.execute(httpPost);
                if (response != null && response.getStatusLine().getStatusCode() == 200){
                    //返回图片名称
                    return pictureName;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                closeableHttpResponse.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    //post
    public static String postEntity(String url, CloseableHttpClient httpClient, RequestConfig requestConfig){
        //封装httpPost请求
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader("Method","SelectRealData");
        httpPost.setConfig(requestConfig);
        //发送请求
        CloseableHttpResponse httpResponse = null;
        try {
            httpResponse = httpClient.execute(httpPost);
            if (httpResponse == null || httpResponse.getStatusLine().getStatusCode() != 200) {
                throw new RuntimeException("请求失败");
            }
            HttpEntity httpEntity = httpResponse.getEntity();
            if (httpEntity != null) {
                String content = EntityUtils.toString(httpEntity);
                return content;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //关闭资源
            try {
                httpResponse.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);

            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();

            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");

            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);

            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());

            // 发送请求参数
            out.print(param);

            // flush输出流的缓冲
            out.flush();

            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }

        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }
}
