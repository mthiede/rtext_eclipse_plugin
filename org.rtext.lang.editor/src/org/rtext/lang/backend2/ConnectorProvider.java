package org.rtext.lang.backend2;

public interface ConnectorProvider {
	Connector get(String modelFilePath);
	void dispose();
}
