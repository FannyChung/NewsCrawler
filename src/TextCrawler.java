import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.regex.Pattern;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import cn.edu.hfut.dmic.webcollector.crawler.DeepCrawler;
import cn.edu.hfut.dmic.webcollector.model.Links;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.util.RegexRule;

public class TextCrawler extends DeepCrawler {
	int FileNum = 0;
	private RegexRule regexRule = new RegexRule();
	BufferedWriter bufferWritter;
	/**
	 * 避免有新闻文本的网页重复
	 */
	private HashSet<NewsUrl> linkSet = new HashSet<NewsUrl>();
	/**
	 * 避免访问过的记录重复
	 */
	private HashSet<String> visited = new HashSet<String>();

	public TextCrawler(String crawlPath) {
		super(crawlPath);
		regexRule.addRule("http://news.cntv.cn/.*");
	}

	public Links visitAndGetNextLinks(Page page) {
		String newsReg = "div[class=cnt_bd]";
		String titleReg = "h1";
		String textReg = "p";

		String vedioTextReg = "div[id=content_body]>p";
		String vedTitle = "div[id=top_title]>h1";

		org.jsoup.nodes.Document doc = page.getDoc();
		String url = page.getUrl();
		visited.add(url);
		String text = "";
		String titile;
		Elements ele1, textsEle;
		Links nextLinks = new Links();
		Elements as = doc.select("a[href]");
		for (Element element : as) {
			String href = element.attr("abs:href");
			href = href.split("#")[0];
			if (Pattern.matches("http://news.cntv.cn/.*", href)
					&& visited.add(href)) {
				nextLinks.add(href);
				System.out.println(href);
			}
		}

		// 处理新闻内容
		if (Pattern.matches("http://news.cntv.cn/[0-9]+/[0-9]+/[0-9]+/.*shtml",
				url) && linkSet.add(new NewsUrl(url))) {// 能够获取新闻文本的网页
			if (url.split("/")[6].startsWith("VIDE")) {// 视频
				titile = doc.select(vedTitle).text();
				System.out.println(titile);
				textsEle = doc.select(vedioTextReg);
			} else {// 文本
				ele1 = doc.select(newsReg);
				titile = ele1.select(titleReg).text();
				textsEle = ele1.select(textReg);
			}

			if (textsEle.isEmpty()) {
				System.err.println("没找到文本\t" + url);
				String failedUrl = "";
				failedUrl += url;
				failedUrl += "\r\n";
				try {
					bufferWritter.write(failedUrl);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				for (Element element : textsEle) {
					if (element.getElementsByClass("o-tit").isEmpty()) {
						// 跳过“原标题”字串
						text += element.text();
						text += "\r\n";
					}
				}
				System.out.println("title " + titile + '\n' + "text: " + text);
				String filename = "file" + FileNum + ".txt";
				FileNum++;
				printFile(filename, titile, text);
			}
		}

		// try {
		// Thread.sleep(1000);
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
		return nextLinks;
	}

	public void printFile(String filename, String title, String text) {
		File file = new File(filename);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			FileWriter fileWritter = new FileWriter(file.getName(), true);
			BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
			bufferWritter.write(title);
			bufferWritter.newLine();
			bufferWritter.newLine();
			bufferWritter.write(text);
			bufferWritter.newLine();
			bufferWritter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Done");

	}

	public void openFile() throws IOException {
		File file = new File("failed.txt");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			FileWriter fileWritter = new FileWriter(file.getName());
			bufferWritter = new BufferedWriter(fileWritter);
		}
	}

	public void closeFile() {
		try {
			bufferWritter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {
		TextCrawler crawler = new TextCrawler("./news");
		String seed = "http://news.cntv.cn/2015/04/07/VIDE1428412914199990.shtml";
		crawler.addSeed(seed);
		crawler.linkSet.add(new NewsUrl(seed));
		crawler.setResumable(false);
		crawler.setThreads(1);
		
		crawler.openFile();
		crawler.start(30);
		crawler.closeFile();
	}
}
