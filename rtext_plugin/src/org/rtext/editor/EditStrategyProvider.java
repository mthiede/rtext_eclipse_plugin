package org.rtext.editor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.DefaultIndentLineAutoEditStrategy;
import org.eclipse.jface.text.IAutoEditStrategy;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.swt.custom.VerifyKeyListener;

public class EditStrategyProvider {
	
	public List<IAutoEditStrategy> getStrategies(final ISourceViewer sourceViewer,final String contentType) {
		final List<IAutoEditStrategy> strategies = new ArrayList<IAutoEditStrategy>();
		configure(new IEditStrategyAcceptor() {
			public void accept(IAutoEditStrategy strategy) {
				if (strategy instanceof ISourceViewerAware) {
					((ISourceViewerAware) strategy).setSourceViewer(sourceViewer);
				}
				if (strategy instanceof VerifyKeyListener) {
					sourceViewer.getTextWidget().addVerifyKeyListener((VerifyKeyListener) strategy);
				}
				strategies.add(strategy);
			}
		});
		return strategies;
	}

	private void configure(IEditStrategyAcceptor acceptor) {
		acceptor.accept(new DefaultIndentLineAutoEditStrategy());
	}

	public interface IEditStrategyAcceptor {
		public void accept(IAutoEditStrategy strategy);
	}
	
}
