package org.rtext.lang.backend2;

public class LoadModelCommand extends Command<LoadedModel> {
	public LoadModelCommand() {
		super("load_model");
	}
	@Override
	protected void setReturnType(LoadedModel type) {
		super.setReturnType(type);
	}
}
