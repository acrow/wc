package muchon.wechat.app.domain.wechat;

public class Article {

	// 图文消息缩略图的media_id，可以在基础支持-上传多媒体文件接口中获得
	private String thumb_media_id;
	// 图文消息的作者
	private String author;
	// 图文消息的标题
	private String title;
	// 在图文消息页面点击“阅读原文”后的页面
	private String content_source_url;
	// 图文消息页面的内容，支持HTML标签。具备微信支付权限的公众号，可以使用a标签，其他公众号不能使用
	private String content;
	// 图文消息的描述
	private String digest;
	// 是否显示封面，1为显示，0为不显示
	private int show_cover_pic;
}
