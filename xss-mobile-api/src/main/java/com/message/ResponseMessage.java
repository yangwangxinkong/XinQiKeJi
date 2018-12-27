package com.message;

import com.wxap.config.WXUtils;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.List;

@XmlType(propOrder = { "toUserName", "fromUserName", "createTime", "msgType", "content","articleCount", "image", "voice", "video", "music", "item" })
@XmlRootElement(name = "xml")
public class ResponseMessage {

	protected String toUserName;
	protected String fromUserName;
	protected long createTime;
	protected String msgType;

	private String content; // 文本消息

	private Media image;
	private Media voice;
	private Media video;
	
	private Music music;
	private int articleCount;
	
	private List<Article> item;

	@XmlElement(name = "ToUserName")
	@XmlJavaTypeAdapter(CDataAdapter.class)
	public String getToUserName() {
		return toUserName;
	}

	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}

	@XmlElement(name = "FromUserName")
	@XmlJavaTypeAdapter(CDataAdapter.class)
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
	@XmlJavaTypeAdapter(CDataAdapter.class)
	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	@XmlElement(name = "Content")
	@XmlJavaTypeAdapter(CDataAdapter.class)
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@XmlElement(name = "Image")
	public Media getImage() {
		return image;
	}

	public void setImage(Media image) {
		this.image = image;
	}

	@XmlElement(name = "Voice")
	public Media getVoice() {
		return voice;
	}

	public void setVoice(Media voice) {
		this.voice = voice;
	}

	@XmlElement(name = "Video")
	public Media getVideo() {
		return video;
	}

	public void setVideo(Media video) {
		this.video = video;
	}

	@XmlElement(name = "Music")
	public Music getMusic() {
    	return music;
    }

	public void setMusic(Music music) {
    	this.music = music;
    }
	
	@XmlElement(name = "ArticleCount")
	public int getArticleCount() {
		return articleCount;
	}

	public void setArticleCount(int articleCount) {
		this.articleCount = articleCount;
	}

	@XmlElementWrapper(name = "Articles")
	public List<Article> getItem() {
    	return item;
    }

	public void setItem(List<Article> item) {
    	this.item = item;
    }

	@XmlType(propOrder = { "mediaId", "title", "description" })
	public static class Media {

		private String mediaId;// 图片消息
		private String title;
		private String description;

		@XmlElement(name = "MediaId")
		@XmlJavaTypeAdapter(CDataAdapter.class)
		public String getMediaId() {
			return mediaId;
		}

		public void setMediaId(String mediaId) {
			this.mediaId = mediaId;
		}

		@XmlElement(name = "Title")
		@XmlJavaTypeAdapter(CDataAdapter.class)
		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		@XmlElement(name = "Description")
		@XmlJavaTypeAdapter(CDataAdapter.class)
		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

	}

	@XmlType(propOrder = { "title", "description", "musicUrl", "hqMusicUrl", "thumbMediaId" })
	public static class Music {
		private String title;
		private String description;
		private String musicUrl;
		private String hqMusicUrl;
		private String thumbMediaId;

		@XmlElement(name = "Title")
		@XmlJavaTypeAdapter(CDataAdapter.class)
		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		@XmlElement(name = "Description")
		@XmlJavaTypeAdapter(CDataAdapter.class)
		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		@XmlElement(name = "MusicUrl")
		@XmlJavaTypeAdapter(CDataAdapter.class)
		public String getMusicUrl() {
			return musicUrl;
		}

		public void setMusicUrl(String musicUrl) {
			this.musicUrl = musicUrl;
		}

		@XmlElement(name = "HQMusicUrl")
		@XmlJavaTypeAdapter(CDataAdapter.class)
		public String getHqMusicUrl() {
			return hqMusicUrl;
		}

		public void setHqMusicUrl(String hqMusicUrl) {
			this.hqMusicUrl = hqMusicUrl;
		}

		@XmlElement(name = "ThumbMediaId")
		@XmlJavaTypeAdapter(CDataAdapter.class)
		public String getThumbMediaId() {
			return thumbMediaId;
		}

		public void setThumbMediaId(String thumbMediaId) {
			this.thumbMediaId = thumbMediaId;
		}

	}
	
	public static class Article {
		
		private String title;
		private String description;
		private String picUrl;
		private String url;

		@XmlElement(name = "Title")
		@XmlJavaTypeAdapter(CDataAdapter.class)
		public String getTitle() {
        	return title;
        }

		public void setTitle(String title) {
        	this.title = title;
        }

		@XmlElement(name = "Description")
		@XmlJavaTypeAdapter(CDataAdapter.class)
		public String getDescription() {
        	return description;
        }

		public void setDescription(String description) {
        	this.description = description;
        }

		@XmlElement(name = "PicUrl")
		@XmlJavaTypeAdapter(CDataAdapter.class)
		public String getPicUrl() {
        	return picUrl;
        }

		public void setPicUrl(String picUrl) {
        	this.picUrl = picUrl;
        }

		@XmlElement(name = "Url")
		@XmlJavaTypeAdapter(CDataAdapter.class)
		public String getUrl() {
        	return url;
        }

		public void setUrl(String url) {
        	this.url = url;
        }
		
	}

	public static void main(String[] args) throws JAXBException {
		ResponseMessage tm = new ResponseMessage();
		tm.setFromUserName("213123");
		tm.setToUserName("123456");
		tm.setCreateTime(System.currentTimeMillis());
//		tm.setMsgType(MsgType.image.name());
		tm.setContent(null);

//		Media image = new Media();
//		image.setMediaId("ttttt");
//		tm.setImage(image);
		
		tm.setMsgType(MsgType.news.name());
		List<Article> articles = new ArrayList<Article>();
		Article a = new Article();
		a.setDescription("10");
		a.setPicUrl("11");
		a.setTitle("12");
		a.setUrl("13");
		articles.add(a);
		
		Article a2 = new Article();
		a2.setDescription("20");
		a2.setPicUrl("21");
		a2.setTitle("22");
		a2.setUrl("23");
		articles.add(a2);
		
		tm.setItem(articles);
		String res = WXUtils.marshaller(tm);

		System.out.println(res);
	}
}
