package io.andrewohara.tinkertime.io.crawlers.pageLoaders;

import java.io.IOException;
import java.net.URL;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
	
/**
 * PageLoader for loading and caching HTML documents from the web.
 * 
 * @author Andrew O'Hara
 */
public class WebpageLoader extends PageLoader<Document>{
	
	private Connection connection;

	@Override
	protected Document loadPage(URL url) throws IOException {
		if (connection == null){
			connection = Jsoup.connect(url.toString());
		} else {
			connection.url(url);
		}

		return connection.get();
	}
}
