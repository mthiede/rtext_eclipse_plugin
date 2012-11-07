package org.rtext.lang.specs.unit.backend;

import org.jnario.runner.CreateWith;
import org.jnario.runner.ExampleGroupRunner;
import org.jnario.runner.Named;
import org.jnario.runner.Order;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.rtext.lang.backend2.RTextFile;
import org.rtext.lang.backend2.RTextFiles;
import org.rtext.lang.specs.util.MockInjector;

@SuppressWarnings("all")
@RunWith(ExampleGroupRunner.class)
@Named("FileSystemBasedConfigProvider")
@CreateWith(value = MockInjector.class)
public class FileSystemBasedConfigProviderSpec {
  @Mock
  RTextFiles rtextFiles;
  
  @Mock
  RTextFile rtextFile;
  
  @Test
  @Ignore
  @Named("finds matching  [PENDING]")
  @Order(0)
  public void _findsMatching() throws Exception {
    throw new UnsupportedOperationException("_findsMatchingis not implemented");
  }
}
