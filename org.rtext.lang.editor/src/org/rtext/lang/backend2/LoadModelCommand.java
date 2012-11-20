package org.rtext.lang.backend2;

public class LoadModelCommand extends Command<LoadedModel> {
	public LoadModelCommand() {
		super("load_model", LoadedModel.class);
	}
}
