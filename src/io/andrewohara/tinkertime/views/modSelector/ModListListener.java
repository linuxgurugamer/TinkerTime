package io.andrewohara.tinkertime.views.modSelector;

import io.andrewohara.common.views.Dialogs;
import io.andrewohara.common.views.selectorPanel.SelectorListListener;
import io.andrewohara.tinkertime.controllers.ModManager;
import io.andrewohara.tinkertime.controllers.ModExceptions.NoModSelectedException;
import io.andrewohara.tinkertime.models.mod.Mod;
import io.andrewohara.tinkertime.views.TinkerDialogs;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.google.inject.Inject;

public class ModListListener implements KeyListener, SelectorListListener<Mod> {

	private final ModManager mm;

	@Inject
	ModListListener(ModManager mm){
		this.mm = mm;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// Do Nothing
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// Do Nothing
	}

	@Override
	public void keyTyped(KeyEvent evt) {
		try{
			switch(evt.getKeyChar()){
			case KeyEvent.VK_DELETE:
				Mod selectedMod = mm.getSelectedMod();
				if (TinkerDialogs.confirmDeleteMod(evt.getComponent(), selectedMod.getName())){
					mm.deleteMod(selectedMod);
				}
				break;
			case KeyEvent.VK_ENTER:
				mm.toggleMod(mm.getSelectedMod());
				break;
			}
		} catch (NoModSelectedException ex){
			// Do nothing
		} catch(Exception ex){
			Dialogs.errorDialog(evt.getComponent(), ex);
		}
	}

	@Override
	public void elementClicked(Mod mod, int numTimes) {
		if (numTimes == 2){
			mm.toggleMod(mod);
		}
	}

	@Override
	public void elementSelected(Mod element) {
		mm.selectMod(element);
	}

}