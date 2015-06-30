package aohara.tinkertime.launcher;

import java.sql.SQLException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.jsoup.nodes.Document;

import aohara.common.OS;
import aohara.tinkertime.controllers.DaoModLoader;
import aohara.tinkertime.controllers.ModLoader;
import aohara.tinkertime.io.crawlers.pageLoaders.JsonLoader;
import aohara.tinkertime.io.crawlers.pageLoaders.PageLoader;
import aohara.tinkertime.io.crawlers.pageLoaders.WebpageLoader;
import aohara.tinkertime.io.kspLauncher.GameExecStrategy;
import aohara.tinkertime.io.kspLauncher.LinuxExecStrategy;
import aohara.tinkertime.io.kspLauncher.OsxExecStrategy;
import aohara.tinkertime.io.kspLauncher.WindowsExecStrategy;
import aohara.tinkertime.models.ConfigData;
import aohara.tinkertime.models.ConfigFactory;
import aohara.tinkertime.models.DaoConfigFactory;
import aohara.tinkertime.models.Installation;
import aohara.tinkertime.models.Mod;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.TypeLiteral;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;

public class MainModule extends AbstractModule {

	private ConnectionSource dbConnection;

	@Override
	protected void configure() {
		bind(new TypeLiteral<PageLoader<Document>>(){}).to(WebpageLoader.class);
		bind(new TypeLiteral<PageLoader<JsonElement>>(){}).to(JsonLoader.class);
		bind(GameExecStrategy.class).to(getExecStrategyType());
		bind(ConfigFactory.class).to(DaoConfigFactory.class);
		bind(ModLoader.class).to(DaoModLoader.class);
		getModsDao();
	}

	private Class<? extends GameExecStrategy> getExecStrategyType(){
		switch(OS.getOs()){
		case Windows: return WindowsExecStrategy.class;
		case Linux: return LinuxExecStrategy.class;
		case Osx: return OsxExecStrategy.class;
		default: throw new IllegalStateException();
		}
	}

	@Provides
	Gson provideGson(){
		return new GsonBuilder().setPrettyPrinting().create();
	}

	@Provides
	Executor provideExecutor(){
		return Executors.newSingleThreadExecutor();
	}

	@Provides
	ThreadPoolExecutor provideThreadedExecutor(){
		return (ThreadPoolExecutor) Executors.newFixedThreadPool(4);
	}

	@Provides
	ConnectionSource getConnectionSource(){
		try {
			if (dbConnection == null){
				dbConnection = new JdbcConnectionSource(TinkerTimeLauncher.getDbUrl());
			}
			return dbConnection;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Provides
	Dao<Mod, Integer> getModsDao(){
		try {
			ConnectionSource connection = getConnectionSource();
			//TableUtils.createTableIfNotExists(dbConnection, Mod.class);  //TODO Remove when migrations added
			return DaoManager.createDao(connection, Mod.class);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Provides
	Dao<Installation, Integer> getInstallationsDao(){
		try {
			ConnectionSource connection = getConnectionSource();
			//TableUtils.createTableIfNotExists(dbConnection, Installation.class);  //TODO Remove when migrations added
			return DaoManager.createDao(connection, Installation.class);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Provides
	Dao<ConfigData, Integer> getConfigDao(){
		try {
			ConnectionSource connection = getConnectionSource();
			//TableUtils.createTableIfNotExists(dbConnection, ConfigData.class);  //TODO Remove when migrations added
			return DaoManager.createDao(connection, ConfigData.class);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}