package io.andrewohara.tinkertime.views;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class FileChoosers {

	public static Path lastJsonLocation = null, lastZipLocation = null, lastGameDataLocation = null;

	public static Path chooseJsonFile(boolean save) throws FileNotFoundException {
		return chooseJsonFile(Paths.get("mods.json"), save);
	}

	public static Path chooseJsonFile(Path defaultPath, boolean save) throws FileNotFoundException{
		JFileChooser chooser = new JFileChooser(lastJsonLocation != null ? lastJsonLocation.toFile() : null);
		chooser.setDialogTitle("Choose Mod Json File");
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);  // Only accept files
		chooser.setFileFilter(new FileNameExtensionFilter("Json File", "json"));  // Only accept JSON files
		chooser.setSelectedFile(defaultPath.toFile());

		return lastJsonLocation = save ? showSaveDialog(chooser) : showOpenDialog(chooser);
	}

	public static Path chooseModZip() throws FileNotFoundException {
		JFileChooser chooser = new JFileChooser(lastZipLocation != null ? lastZipLocation.toFile() : null);
		chooser.setDialogTitle("Please select the mod zip to add.");
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);  // Only accept files
		chooser.setFileFilter(new FileNameExtensionFilter("Zip Archive", "zip"));

		return lastZipLocation = showOpenDialog(chooser);
	}

	public static Path chooseGameDataFolder() throws FileNotFoundException{
		JFileChooser chooser = new JFileChooser(lastZipLocation != null ? lastGameDataLocation.toFile() : null);
		chooser.setDialogTitle("Please select the GameData folder");
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);  // Only accept folders

		return lastGameDataLocation = showOpenDialog(chooser);
	}

	public static Path chooseImportExportFile(boolean exportMods) throws FileNotFoundException{
		JFileChooser chooser = new JFileChooser(lastJsonLocation != null ? lastJsonLocation.toFile() : null);
		chooser.setDialogTitle(String.format("Choose Mod %s file", exportMods ? "Export" : "Import"));

		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);  // Only accept files
		if (exportMods){
			chooser.setFileFilter(new FileNameExtensionFilter("Text File (.txt)", "txt"));
		} else {
			chooser.setFileFilter(new FileNameExtensionFilter("txt or legacy json file", "txt", "json"));
		}


		return lastJsonLocation = exportMods ? showSaveDialog(chooser) : showOpenDialog(chooser);
	}

	//-- Helpers --------------------------------------------------------------

	private static Path showSaveDialog(JFileChooser chooser) throws FileNotFoundException {
		if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION){
			Path path = chooser.getSelectedFile().toPath();
			if (path != null){
				return path;
			}
		}
		throw new FileNotFoundException("A File was not selected in the chooser");
	}

	private static Path showOpenDialog(JFileChooser chooser) throws FileNotFoundException {
		if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
			Path path = chooser.getSelectedFile().toPath();
			if (path != null){
				return path;
			}
		}
		throw new FileNotFoundException("A File was not selected in the chooser");
	}

}
