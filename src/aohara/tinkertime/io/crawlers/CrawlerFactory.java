package aohara.tinkertime.io.crawlers;

import java.net.MalformedURLException;
import java.net.URL;

import org.jsoup.nodes.Document;

import aohara.tinkertime.io.crawlers.pageLoaders.PageLoader;
import aohara.tinkertime.models.Mod;

import com.google.gson.JsonElement;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class CrawlerFactory {
	
	public static final String
		HOST_CURSE = "curse.com",
		HOST_GITHUB = "github.com",
		HOST_MODULE_MANAGER = "ksp.sarbian.com",
		HOST_KERBAL_STUFF = "kerbalstuff.com";
	
	public static final String[] ACCEPTED_MOD_HOSTS	 = new String[]{
		HOST_KERBAL_STUFF, HOST_CURSE, HOST_GITHUB
	};
	
	private final PageLoader<Document> docLoader;
	private final PageLoader<JsonElement> jsonLoader;
	
	@Inject
	CrawlerFactory(PageLoader<Document> docLoader, PageLoader<JsonElement> jsonLoader){
		this.docLoader = docLoader;
		this.jsonLoader = jsonLoader;
	}
	
	public static URL getModuleManagerUrl(){
		try {
			return new URL("https", HOST_MODULE_MANAGER, "/jenkins/job/ModuleManager");
		} catch (MalformedURLException e) {
			throw new RuntimeException(e); // Programming error if this occurs
		}
	}

	public Crawler<?> getCrawler(URL url, Integer existingModId) throws UnsupportedHostException{
		String host = url.getHost();
		if (host.contains(HOST_CURSE)){
			return new CurseCrawler(url, docLoader, existingModId);
		} else if (host.contains(HOST_GITHUB)){
			return new GithubCrawler(url, jsonLoader, existingModId);
		} else if (host.contains(HOST_KERBAL_STUFF)){
			return new KerbalStuffCrawler(url, jsonLoader, existingModId);
		} else if (host.equals(HOST_MODULE_MANAGER)){
			return new JenkinsCrawler(url, jsonLoader, Mod.MODULE_MANAGER_ID);
		}
		throw new UnsupportedHostException(host);
	}
	
	@SuppressWarnings("serial")
	public static class UnsupportedHostException extends Exception {
		
		private UnsupportedHostException(String host){
			super(String.format("Unsupported host: %s", host)); 
		}
	}
}