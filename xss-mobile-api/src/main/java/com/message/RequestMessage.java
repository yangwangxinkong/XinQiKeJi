package com.message;

import com.wxap.config.WXUtils;
import org.apache.commons.io.IOUtils;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.InputStream;
import java.math.BigDecimal;

@XmlType(propOrder = { "toUserName", "fromUserName", "createTime", "msgType", "content", "picUrl", "mediaId", "format",
        "thumbMediaId", "locationX", "locationY", "scale", "label", "title", "description", "url", "msgId", "event",
        "eventKey", "ticket", "latitude", "longitude", "precision" })
@XmlRootElement(name = "xml")
public class RequestMessage {

	protected String toUserName;
	protected String fromUserName;
	protected long createTime;
	protected String msgType;

	private String event;
	// 扫描带参数二维码事件
	private String eventKey;
	private String ticket;

	// 上报地理位置事件
	private BigDecimal latitude;
	private BigDecimal longitude;
	private BigDecimal precision;

	private String content; // 文本消息

	private String picUrl; // 图片消息
	private String mediaId;// 图片消息、小视频消息、语音消息
	private String thumbMediaId; // 小视频消息
	private String format; // 语音消息

	// 地理位置消息
	private BigDecimal locationX;
	private BigDecimal locationY;
	private BigDecimal scale;
	private String label;

	// 链接消息
	private String title;
	private String description;
	private String url;

	private long msgId;

	@XmlElement(name = "ToUserName")
	public String getToUserName() {
		return toUserName;
	}

	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}

	@XmlElement(name = "FromUserName")
	public String getFromUserName() {
		return fromUserName;
	}

	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}

	@XmlElement(name = "CreateTime")
	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	@XmlElement(name = "MsgType")
	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	@XmlElement(name = "Event")
	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	@XmlElement(name = "Content")
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@XmlElement(name = "PicUrl")
	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	@XmlElement(name = "MediaId")
	public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}

	@XmlElement(name = "Format")
	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	@XmlElement(name = "ThumbMediaId")
	public String getThumbMediaId() {
		return thumbMediaId;
	}

	public void setThumbMediaId(String thumbMediaId) {
		this.thumbMediaId = thumbMediaId;
	}

	@XmlElement(name = "Location_X")
	public BigDecimal getLocationX() {
		return locationX;
	}

	public void setLocationX(BigDecimal locationX) {
		this.locationX = locationX;
	}

	@XmlElement(name = "Location_Y")
	public BigDecimal getLocationY() {
		return locationY;
	}

	public void setLocationY(BigDecimal locationY) {
		this.locationY = locationY;
	}

	@XmlElement(name = "Scale")
	public BigDecimal getScale() {
		return scale;
	}

	public void setScale(BigDecimal scale) {
		this.scale = scale;
	}

	@XmlElement(name = "Label")
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	@XmlElement(name = "Title")
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@XmlElement(name = "Description")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@XmlElement(name = "Url")
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@XmlElement(name = "MsgId")
	public long getMsgId() {
		return msgId;
	}

	public void setMsgId(long msgId) {
		this.msgId = msgId;
	}

	@XmlElement(name = "EventKey")
	public String getEventKey() {
		return eventKey;
	}

	public void setEventKey(String eventKey) {
		this.eventKey = eventKey;
	}

	@XmlElement(name = "Ticket")
	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	@XmlElement(name = "Latitude")
	public BigDecimal getLatitude() {
		return latitude;
	}

	public void setLatitude(BigDecimal latitude) {
		this.latitude = latitude;
	}

	@XmlElement(name = "Longitude")
	public BigDecimal getLongitude() {
		return longitude;
	}

	public void setLongitude(BigDecimal longitude) {
		this.longitude = longitude;
	}

	@XmlElement(name = "Precision")
	public BigDecimal getPrecision() {
		return precision;
	}

	public void setPrecision(BigDecimal precision) {
		this.precision = precision;
	}

	public static void main(String[] args) throws JAXBException {
		String xml = "<xml>"+
				 "<ToUserName><![CDATA[toUser]]></ToUserName>"+
				 "<FromUserName><![CDATA[fromUser]]></FromUserName> "+
				 "<CreateTime>1348831860</CreateTime>"+
				 "<MsgType><![CDATA[text]]></MsgType>"+
				 "<Content><![CDATA[this is a test]]></Content>"+
				 "<MsgId>1234567890123456</MsgId>"+
				 "</xml>";

		InputStream is = IOUtils.toInputStream(xml);
		
		RequestMessage rm = WXUtils.unmarshal(is);
		System.out.println(rm);
	}
}
