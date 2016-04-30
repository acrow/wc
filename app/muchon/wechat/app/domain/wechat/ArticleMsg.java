package muchon.wechat.app.domain.wechat;

import java.util.ArrayList;

/***
 * 图文消息
 * @author zhengcc
 *
 */
public class ArticleMsg {
	private ArrayList<Article> articles;

	public ArticleMsg() {
		articles = new ArrayList<Article>();
	}
	
	public ArrayList<Article> getArticles() {
		return articles;
	}

	public void setArticles(ArrayList<Article> articles) {
		this.articles = articles;
	}
	
}
