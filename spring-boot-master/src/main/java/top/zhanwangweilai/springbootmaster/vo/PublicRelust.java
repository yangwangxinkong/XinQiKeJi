package top.zhanwangweilai.springbootmaster.vo;

import java.io.Serializable;

public class PublicRelust implements Serializable {
    private static final long serialVersionUID = 6205026608497559648L;
    /**状态码*/
    private Integer state=200;
    /**状态码对应的消息*/
    private String message="ok";
    /**服务端返回给客户端的数据*/
    private Object data;

    public PublicRelust(Integer state, String message) {
        this.state = state;
        this.message = message;
    }

    public PublicRelust(Object data) {
        this.data = data;
    }

    public PublicRelust(String message) {
        this.message = message;
    }

    public PublicRelust(Throwable e){
        this.state=0;
        this.message=e.getMessage();
    }

    public Integer getState() {
        return state;
    }
    public void setState(Integer state) {
        this.state = state;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public Object getData() {
        return data;
    }
    public void setData(Object data) {
        this.data = data;
    }
}
