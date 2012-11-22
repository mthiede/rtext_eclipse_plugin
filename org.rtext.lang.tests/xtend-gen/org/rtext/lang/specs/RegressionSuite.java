package org.rtext.lang.specs;

import org.jnario.runner.Contains;
import org.jnario.runner.ExampleGroupRunner;
import org.jnario.runner.Named;
import org.junit.runner.RunWith;
import org.rtext.lang.specs.unit.backend.ConnectorProviderSpec;
import org.rtext.lang.specs.unit.backend.ConnectorSpec;
import org.rtext.lang.specs.unit.backend.ConvertingCommandsToJSONSpec;
import org.rtext.lang.specs.unit.backend.DefaultConnectorProviderSpec;
import org.rtext.lang.specs.unit.backend.FileSystemBasedConfigProviderSpec;
import org.rtext.lang.specs.unit.backend.LoadModelCallbackSpec;
import org.rtext.lang.specs.unit.backend.RTextFileParserSpec;
import org.rtext.lang.specs.unit.backend.RTextFilesSpec;
import org.rtext.lang.specs.unit.backend.ResponseParserSpec;
import org.rtext.lang.specs.unit.backend.TcpClientSpec;
import org.rtext.lang.specs.unit.parser.RTextModelParserSpec;
import org.rtext.lang.specs.unit.parser.SyntaxScannerSpec;
import org.rtext.lang.specs.unit.util.FilesSpec;
import org.rtext.lang.specs.unit.util.StringsSpec;

@Named("Regression")
@Contains({ ConnectorSpec.class, ConnectorProviderSpec.class, ConvertingCommandsToJSONSpec.class, DefaultConnectorProviderSpec.class, FilesSpec.class, FileSystemBasedConfigProviderSpec.class, LoadModelCallbackSpec.class, ResponseParserSpec.class, RTextFileParserSpec.class, RTextFilesSpec.class, RTextModelParserSpec.class, StringsSpec.class, SyntaxScannerSpec.class, TcpClientSpec.class })
@SuppressWarnings("all")
@RunWith(ExampleGroupRunner.class)
public class RegressionSuite {
}
