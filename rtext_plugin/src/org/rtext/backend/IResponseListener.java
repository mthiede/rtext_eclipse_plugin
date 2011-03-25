package org.rtext.backend;

import java.util.StringTokenizer;

public interface IResponseListener {

	void responseReceived(StringTokenizer st);
	void requestTimedOut();
	
}
