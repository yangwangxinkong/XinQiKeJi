import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.tomcat.util.digester.DocumentProperties;
import org.junit.Test;
import org.springframework.boot.ApplicationPid;

import java.nio.charset.Charset;

public class TestCustomerEvaluation {
    @Test
    public void test() {
        try {
            String uri = "localhost:8082/api/customerEvaluation/save";
            HttpPost post = new HttpPost();
            post.setHeader("Content-type","application/json;charset=utf-8");
            post.setHeader("Connection","Close");
            post.setHeader("appid", ApplicationPid.class.getName());
            //定义请求实体内容
            String json = "[{'customerName','客户姓名'}," +
                    "companyName" +"公司名称测试"+
                    "evaluation" +"这后台写的真棒！"+
                    "customerImg" +"大图片"+
                    "]";



            StringEntity entity = new StringEntity(json, Charset.forName("utf-8"));
            entity.setContentType("application/json");
            //设置请求体
            post.setEntity(entity);
            //创建httpclient对象，并发起post请求
            HttpResponse response = HttpClients.createDefault().execute(post);
            // 检验返回码
            int statusCode = response.getStatusLine().getStatusCode();
            System.out.println("检查返回码===="+statusCode);
            String str = response.getEntity().toString();
            System.out.println("打印响应结果=============" + str);
        }catch (Exception e){
            System.out.println("请求失败了");
        }
    }
}
