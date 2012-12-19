package org.rtext.lang.specs.unit.parser;

import com.google.common.base.Objects;
import java.util.List;
import org.eclipse.jface.text.Position;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.jnario.lib.Should;
import org.jnario.runner.Contains;
import org.jnario.runner.ExampleGroupRunner;
import org.jnario.runner.Named;
import org.jnario.runner.Subject;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.rtext.lang.model.Element;
import org.rtext.lang.model.RTextModelParser;
import org.rtext.lang.specs.unit.parser.RTextModelParserErrorHandlingSpec;
import org.rtext.lang.specs.unit.parser.RTextModelParserFullTextRegionSpec;
import org.rtext.lang.specs.unit.parser.RTextModelParserStructureSpec;
import org.rtext.lang.specs.util.SimpleDocument;

@Contains({ RTextModelParserStructureSpec.class, RTextModelParserFullTextRegionSpec.class, RTextModelParserErrorHandlingSpec.class })
@SuppressWarnings("all")
@Named("RTextModelParser")
@RunWith(ExampleGroupRunner.class)
public class RTextModelParserSpec {
  @Subject
  public RTextModelParser subject;
  
  SimpleDocument document;
  
  public boolean operator_doubleArrow(final Object actual, final Object expected) {
    boolean _and = false;
    if (!(actual instanceof String)) {
      _and = false;
    } else {
      _and = ((actual instanceof String) && (expected instanceof String));
    }
    if (_and) {
      Assert.assertEquals(expected, actual);
      return true;
    } else {
      return Should.operator_doubleArrow(actual, expected);
    }
  }
  
  public String fullText(final Element element, final CharSequence s) {
    Position _position = element.getPosition();
    Position _position_1 = element.getPosition();
    String _get = this.document.get(_position.offset, _position_1.length);
    return _get;
  }
  
  public Element elements(final CharSequence input, final String name) {
    List<Element> _parse = this.parse(input);
    Element _find = this.find(_parse, name);
    return _find;
  }
  
  public Element find(final List<Element> elements, final String name) {
    final Function1<Element,Boolean> _function = new Function1<Element,Boolean>() {
        public Boolean apply(final Element candidate) {
          String _name = candidate.getName();
          boolean _equals = Objects.equal(_name, name);
          return Boolean.valueOf(_equals);
        }
      };
    Element result = IterableExtensions.<Element>findFirst(elements, _function);
    boolean _notEquals = (!Objects.equal(result, null));
    if (_notEquals) {
      return result;
    }
    for (final Element candidate : elements) {
      {
        String _name = candidate.getName();
        boolean _equals = Objects.equal(_name, name);
        if (_equals) {
          return candidate;
        }
        List<Element> _children = candidate.getChildren();
        Element _find = this.find(_children, name);
        result = _find;
        boolean _notEquals_1 = (!Objects.equal(result, null));
        if (_notEquals_1) {
          return result;
        }
      }
    }
    return null;
  }
  
  public List<Element> parse(final CharSequence s) {
    List<Element> _xblockexpression = null;
    {
      String _string = s.toString();
      SimpleDocument _simpleDocument = new SimpleDocument(_string);
      this.document = _simpleDocument;
      int _length = s.length();
      this.subject.setRange(this.document, 0, _length);
      List<Element> _parse = this.subject.parse();
      _xblockexpression = (_parse);
    }
    return _xblockexpression;
  }
}
