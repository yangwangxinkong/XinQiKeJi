package com.xss.util;

import net.sf.json.JSONObject;
import org.apache.commons.collections.map.MultiValueMap;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.io.IOUtils;
import org.springframework.util.StringUtils;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.*;

public class ApiUtils {
    
	public static final String encoding = "utf-8";
	
	public static String getForObject(String url, MultiValueMap map, Object requestEntity) throws IOException {
        
        byte[] bytes = callForApiByGet(url, map, requestEntity);
        
        return bytes == null ? "" : new String(bytes, encoding);
    }
	
    public static String postForObject(String url, MultiValueMap map, Object requestEntity) throws IOException {
        
        byte[] bytes = callForApi(url, map, requestEntity);
        
        return bytes == null ? "" : new String(bytes, encoding);
    }

    public static String postForObject(String url, Map map, Object requestEntity) throws IOException {

        byte[] bytes = callForApi(url, map, requestEntity);

        return bytes == null ? "" : new String(bytes, encoding);
    }
    
    public static byte[] callForApi(String url, MultiValueMap map, Object requestEntity) {

        HttpClient httpClient = new HttpClient();
        PostMethod postMethod = new PostMethod(url);
        InputStream in = null;
        byte[] bytes = null;
        try
        {
            List<NameValuePair> values = new ArrayList<NameValuePair>();
            if (map != null) {
                for (Object key : map.keySet()) {
                    Iterator<?> it = map.getCollection(key).iterator();
                    while (it.hasNext()) {
                        Object value = it.next();
                        values.add(new NameValuePair(key.toString(), value == null ? "" : value.toString()));
                    }
                }
            }

            postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, encoding);
            postMethod.setRequestBody(values.toArray(new NameValuePair[values.size()]));
            if (requestEntity != null) {
                if (requestEntity instanceof RequestEntity) {
                    postMethod.setRequestEntity((RequestEntity) requestEntity);
                } else if (requestEntity instanceof Part[]) {
                    MultipartRequestEntity entity = new MultipartRequestEntity((Part[]) requestEntity, postMethod.getParams());
                    postMethod.setRequestEntity(entity);
                }
            }
            //设置连接超时时间15秒(单位:毫秒)
            httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(60000);
            //设置读取数据超时时间30秒(单位:毫秒)
            httpClient.getHttpConnectionManager().getParams().setSoTimeout(60000);
            int statusCode = httpClient.executeMethod(postMethod);
            System.out.println(" status code:" + statusCode);
            // HttpClient对于要求接受后继服务的请求，象POST和PUT等不能自动处理转发

            if (statusCode == HttpStatus.SC_OK)
            {
                InputStream is = postMethod.getResponseBodyAsStream();
                ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
                byte[] buff = new byte[100];
                int rc = 0;
                while ((rc = is.read(buff, 0, 100)) > 0) {
                    swapStream.write(buff, 0, rc);
                }
                bytes = swapStream.toByteArray();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(in);
            postMethod.releaseConnection();//释放连接
        }

        return bytes;
    }
    
    public static byte[] callForApiByGet(String url, MultiValueMap map, Object requestEntity) {
        
        HttpClient httpClient = new HttpClient();
        
        InputStream in = null;
        byte[] bytes = null;
        try
        {
        	String params = "";
            List<NameValuePair> values = new ArrayList<NameValuePair>();
            if (map != null) {
                for (Object key : map.keySet()) {
                    Iterator<?> it = map.getCollection(key).iterator();
                    while (it.hasNext()) {
                        Object value = it.next();
                        values.add(new NameValuePair(key.toString(), value == null ? "" : value.toString()));
                        params = (params.equals("")?params: params+ "&")+key.toString()+"="+URLEncoder.encode((value == null ? "" : value.toString()),"utf-8");
                    }
                }
            }
            url = url +"?"+params;
            System.out.println(url);
            GetMethod getMethod = new GetMethod(url);
            getMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, encoding);
            //璁剧疆杩炴帴瓒呮椂鏃堕棿15绉?鍗曚綅:姣)
            httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(150000);
            
            //璁剧疆璇诲彇鏁版嵁瓒呮椂鏃堕棿30绉?鍗曚綅:姣)
            httpClient.getHttpConnectionManager().getParams().setSoTimeout(3000000);
            int statusCode = httpClient.executeMethod(getMethod);
            System.out.println(" status code:" + statusCode);
            // HttpClient瀵逛簬瑕佹眰鎺ュ彈鍚庣户鏈嶅姟鐨勮姹傦紝璞OST鍜孭UT绛変笉鑳借嚜鍔ㄥ鐞嗚浆鍙?
            
            if (statusCode == HttpStatus.SC_OK)
            {
                bytes = getMethod.getResponseBody();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(in);
        }
        
        return bytes;
    }

    public static byte[] callForApi(String url, Map map, Object requestEntity) {

        HttpClient httpClient = new HttpClient();
        PostMethod postMethod = new PostMethod(url);
        InputStream in = null;
        byte[] bytes = null;
        try
        {
            List<NameValuePair> values = new ArrayList<NameValuePair>();
            if (map != null) {
                Set<String> set = map.keySet();
                Iterator<String> it = set.iterator();
                while(it.hasNext())
                {
                    String key = it.next();
                    values.add(new NameValuePair(key.toString(), map.get(key) == null ? "" : map.get(key).toString()));
                }
            }

            postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, encoding);
            postMethod.setRequestBody(values.toArray(new NameValuePair[values.size()]));
            if (requestEntity != null) {
                if (requestEntity instanceof RequestEntity) {
                    postMethod.setRequestEntity((RequestEntity) requestEntity);
                } else if (requestEntity instanceof Part[]) {
                    MultipartRequestEntity entity = new MultipartRequestEntity((Part[]) requestEntity, postMethod.getParams());
                    postMethod.setRequestEntity(entity);
                }
            }
            //设置连接超时时间15秒(单位:毫秒)
            httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(60000);
            //设置读取数据超时时间30秒(单位:毫秒)
            httpClient.getHttpConnectionManager().getParams().setSoTimeout(60000);
            int statusCode = httpClient.executeMethod(postMethod);
            System.out.println(" status code:" + statusCode);
            // HttpClient对于要求接受后继服务的请求，象POST和PUT等不能自动处理转发

            //if (statusCode == HttpStatus.SC_OK)
            //{
                InputStream is = postMethod.getResponseBodyAsStream();
                ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
                byte[] buff = new byte[100];
                int rc = 0;
                while ((rc = is.read(buff, 0, 100)) > 0) {
                    swapStream.write(buff, 0, rc);
                }
                bytes = swapStream.toByteArray();
           // }
        }
        catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(in);
            postMethod.releaseConnection();//释放连接
        }

        return bytes;
    }

    /**
     *　　sendUrl    （远程请求的URL）
     *　　param    （远程请求参数）
     *　　JSONObject    （远程请求返回的JSON）
     */
    public static JSONObject sendPostUrl(String url, MultiValueMap map){
        PrintWriter out = null;
        BufferedReader in = null;
        JSONObject jsonObject = null;
        String result = "";
        try {

            String strParams = "";
            List<NameValuePair> values = new ArrayList<NameValuePair>();
            if (map != null) {
                for (Object key : map.keySet()) {
                    Iterator<?> it = map.getCollection(key).iterator();
                    while (it.hasNext()) {
                        Object value = it.next();
                        values.add(new NameValuePair(key.toString(), value == null ? "" : value.toString()));
                        strParams += "&" + key.toString() + "=" + value == null ? "" : value.toString();
                    }
                }
            }

            if(StringUtils.hasText(strParams)) {
                strParams = strParams.substring(1);
            }

            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            //conn.setRequestProperty("accept", "*/*");
            //conn.setRequestProperty("connection", "Keep-Alive");
           // conn.setRequestProperty("Access-Token", "687b3fb519d5ed71bacda3db94c6dc24");
           // conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");
            //conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");

            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流（设置请求编码为UTF-8）
            out = new PrintWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));
            // 发送请求参数
            out.print(strParams);
            // flush输出流的缓冲
            out.flush();
            // 获取请求返回数据（设置返回数据编码为UTF-8）
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
            jsonObject = JSONObject.fromObject(result);
            System.out.println(jsonObject);
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
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

        return jsonObject;
    }

    /**
     *　　sendUrl    （远程请求的URL）
     *　　param    （远程请求参数）
     *　　JSONObject    （远程请求返回的JSON）
     */
    public static JSONObject sendPostUrl(String url, Map map){
        PrintWriter out = null;
        BufferedReader in = null;
        JSONObject jsonObject = null;
        String result = "";
        try {

            //参数
            String strParams = linkStr(map);

            // 创建URL对象
            URL realUrl = new URL(url);
            // 打开连接 获取连接对象
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            //conn.setRequestProperty("accept", "*/*");
            //conn.setRequestProperty("connection", "Keep-Alive");
            // conn.setRequestProperty("Access-Token", "687b3fb519d5ed71bacda3db94c6dc24");
            // 设置请求编码
            conn.addRequestProperty("encoding", "UTF-8");
            conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");
            //conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");

            // 发送POST请求必须设置如下两行
            // 设置允许输入
            conn.setDoOutput(true);
            // 设置允许输出
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流（设置请求编码为UTF-8）
            out = new PrintWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));
            // 发送请求参数
            out.print(strParams);
            // flush输出流的缓冲
            out.flush();
            // 获取请求返回数据（设置返回数据编码为UTF-8）
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
            jsonObject = JSONObject.fromObject(result);
            System.out.println(jsonObject);
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
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

        return jsonObject;
    }

    /**
     * 使用GET方法读取http中的数据
     *
     * @param urlAddress url地址
     * @return 请求的响应数据
     */
    public static String doGET(String urlAddress) {
        try {
            // 创建URL对象
            URL url = new URL(urlAddress);
            // 打开连接 获取连接对象
            URLConnection connection = url.openConnection();

            // 从连接对象中获取网络连接中的输入字节流对象
            InputStream inputStream = connection.getInputStream();
            // 将输入字节流包装成输入字符流对象,并进行字符编码
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            // 创建一个输入缓冲区对象，将字符流对象传入
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            // 定义一个字符串变量，用来接收输入缓冲区中的每一行字符串数据
            String line;
            // 创建一个可变字符串对象，用来装载缓冲区对象的数据，使用字符串追加的方式，将响应的所有数据都保存在该对象中
            StringBuilder stringBuilder = new StringBuilder();
            // 使用循环逐行读取输入缓冲区的数据，每次循环读入一行字符串数据赋值给line字符串变量，直到读取的行为空时标识内容读取结束循环
            while ((line = bufferedReader.readLine()) != null) {
                // 将从输入缓冲区读取到的数据追加到可变字符对象中
                stringBuilder.append(line);
            }
            // 依次关闭打开的输入流
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
            // 将可变字符串转换成String对象返回
            return stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 使用POST方法读取HTTP中的数据
     *
     * @param urlAddress url地址
     * @param map     参数
     * @return 请求的响应数据
     */
    public static String doPOST(String urlAddress, Map map) {
        try {
            String strParams = linkStr(map);

            if(StringUtils.hasText(strParams)) {
                strParams = strParams.substring(1);
            }

            // 创建URL对象
            URL url = new URL(urlAddress);
            // 打开连接 获取连接对象
            URLConnection connection = url.openConnection();
            // 设置请求编码
            connection.addRequestProperty("encoding", "UTF-8");
            connection.setRequestProperty("X-Requested-With", "XMLHttpRequest");
            // 设置允许输入
            connection.setDoInput(true);
            // 设置允许输出
            connection.setDoOutput(true);

            // 从连接对象中获取输出字节流对象
            OutputStream outputStream = connection.getOutputStream();
            // 将输出的字节流对象包装成字符流写出对象
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
            // 创建一个输出缓冲区对象,将要输出的字符流写出对象传入
            BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
            // 向输出缓冲区中写入请求参数
            bufferedWriter.write(strParams);
            // 刷新输出缓冲区
            bufferedWriter.flush();

            // 从连接对象中获取输入字节流对象
            InputStream inputStream = connection.getInputStream();
            // 将输入字节流对象包装成输入字符流对象，并将字符编码为UTF-8格式
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            // 创建一个输入缓冲区对象，将要输入的字符流对象传入
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            // 创建一个字符串对象，用来接收每次从输入缓冲区中读入的字符串
            String line;
            // 创建一个可变字符串对象，用来装载缓冲区对象的最终数据，使用字符串追加的方式，将响应的所有数据都保存在该对象中
            StringBuilder stringBuilder = new StringBuilder();
            // 使用循环逐行读取缓冲区的数据，每次循环读入一行字符串数据赋值给line字符串变量，直到读取的行为空时标识内容读取结束循环
            while ((line = bufferedReader.readLine()) != null) {
                // 将缓冲区读取到的数据追加到可变字符对象中
                stringBuilder.append(line);
            }
            // 依次关闭打开的输入流
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
            // 依次关闭打开的输出流
            bufferedWriter.close();
            outputStreamWriter.close();
            outputStream.close();
            // 将可变字符串转换成String对象返回
            return stringBuilder.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将map转换为key=val&的形式
     * 如 name=w&age=123
     * @param paras
     * @return
     */
    private static String linkStr(Map<String,Object> paras){
        String link="";
        Iterator<Map.Entry<String,Object>> it=paras.entrySet().iterator();
        while (it.hasNext()){
            Map.Entry<String, Object> entry = it.next();
            String key=entry.getKey();
            Object object=entry.getValue();
            link+=key+"="+object+"&";
        }
        return  link.substring(0,link.length()-1);
    }


    public static void main(String[] args){
        String url = "http://jdudu.sptxmall.com/weixin/callback.jhtml";
        try{
            MultiValueMap map = new MultiValueMap();
            map.put("signature","asfdaf");
            map.put("timestamp","asfdaf");
            map.put("nonce","asfdaf");
            String aaa = ApiUtils.postForObject(url, map, null);
            System.out.println("aaa:" + aaa);
        } catch (Exception e){
            e.printStackTrace();
        }

    }


}
