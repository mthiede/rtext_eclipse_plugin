package org.rtext.backend;

import java.util.List;

public interface IResponseListener {

	void responseReceived(List<String> responseLines);
	void responseUpdate(List<String> responseLines);
	void requestTimedOut();
	
}
