package org.rtext.lang.backend2;

import org.rtext.lang.backend.ConnectorConfig;

public interface ConnectorConfigProvider {
	ConnectorConfig get(String modelFilePath);
}
