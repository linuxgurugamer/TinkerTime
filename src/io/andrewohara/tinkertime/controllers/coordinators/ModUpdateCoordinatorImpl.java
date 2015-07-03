package io.andrewohara.tinkertime.controllers.coordinators;

import io.andrewohara.common.workflows.tasks.TaskCallback;
import io.andrewohara.common.workflows.tasks.WorkflowTask.TaskEvent;
import io.andrewohara.tinkertime.db.ModLoader;
import io.andrewohara.tinkertime.models.Installation;
import io.andrewohara.tinkertime.models.ModFile;
import io.andrewohara.tinkertime.models.mod.Mod;
import io.andrewohara.tinkertime.views.modSelector.ModListCellRenderer;
import io.andrewohara.tinkertime.views.modSelector.ModSelectorPanelController;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.j256.ormlite.dao.Dao;

@Singleton
public class ModUpdateCoordinatorImpl extends TaskCallback implements ModUpdateCoordinator {

	private final Dao<ModFile, Integer> modFilesDao;
	private final ModLoader modLoader;

	private ModSelectorPanelController modSelector;
	private ModListCellRenderer modListCellRenderer;


	@Inject
	ModUpdateCoordinatorImpl(Dao<ModFile, Integer> modFilesDao, ModLoader modLoader){
		this.modFilesDao = modFilesDao;
		this.modLoader = modLoader;
	}

	@Override
	public void setListeners(ModSelectorPanelController modSelector, ModListCellRenderer modListCellRender){
		this.modSelector = modSelector;
		this.modListCellRenderer = modListCellRender;
	}

	@Override
	public void reload(Installation newInstallation){
		modLoader.reload(newInstallation);
		modSelector.reload(newInstallation);
	}

	@Override
	public void updateMod(Mod mod) {
		modLoader.updateMod(mod);
		modSelector.updateMod(mod);
	}

	@Override
	public void deleteMod(Mod mod) {
		modLoader.deleteMod(mod);
		modSelector.deleteMod(mod);
	}

	@Override
	protected void processTaskEvent(TaskEvent event) {
		modListCellRenderer.handleTaskEvent(event);
	}

	@Override
	public void updateModFiles(Mod mod, Collection<ModFile> modFiles, String readmeText){
		try {
			// Update Mod Files
			modFilesDao.delete(mod.getModFiles());
			for (ModFile newFile : modFiles){
				modFilesDao.create(newFile);
			}
			mod.setModFiles(modFiles);
			mod.setReadmeText(readmeText);

			updateMod(mod);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void updateModImage(Mod mod, BufferedImage image){
		try {
			mod.setImage(image);
			updateMod(mod);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
