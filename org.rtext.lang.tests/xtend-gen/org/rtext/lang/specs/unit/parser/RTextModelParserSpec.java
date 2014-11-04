/**
 * Copyright (c) 2012 E.S.R. Labs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License
 * which accompanies this distribution, and is available at
 * http://opensource.org/licenses/MIT
 */
package org.rtext.lang.specs.unit.parser;

import com.google.common.base.Objects;
import java.util.List;
import org.eclipse.jface.text.Position;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.jnario.runner.Contains;
import org.jnario.runner.ExampleGroupRunner;
import org.jnario.runner.Named;
import org.jnario.runner.Subject;
import org.junit.runner.RunWith;
import org.rtext.lang.model.Element;
import org.rtext.lang.model.RTextModelParser;
import org.rtext.lang.specs.unit.parser.RTextModelParserErrorHandlingSpec;
import org.rtext.lang.specs.unit.parser.RTextModelParserFullTextRegionSpec;
import org.rtext.lang.specs.unit.parser.RTextModelParserStructureSpec;
import org.rtext.lang.specs.util.SimpleDocument;

@Contains({ RTextModelParserStructureSpec.class, RTextModelParserFullTextRegionSpec.class, RTextModelParserErrorHandlingSpec.class })
@Named("RTextModelParser")
@RunWith(ExampleGroupRunner.class)
@SuppressWarnings("all")
public class RTextModelParserSpec {
  @Subject
  public RTextModelParser subject;
  
  SimpleDocument document;
  
  public String fullText(final Element element, final CharSequence s) {
    Position _position = element.getPosition();
    Position _position_1 = element.getPosition();
    return this.document.get(_position.offset, _position_1.length);
  }
  
  public Element elements(final CharSequence input, final String name) {
    List<Element> _parse = this.parse(input);
    return this.find(_parse, name);
  }
  
  public Element find(final List<Element> elements, final String name) {
    final Function1<Element, Boolean> _function = new Function1<Element, Boolean>() {
      public Boolean apply(final Element candidate) {
        String _name = candidate.getName();
        return Boolean.valueOf(Objects.equal(_name, name));
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
      _xblockexpression = this.subject.parse();
    }
    return _xblockexpression;
  }
}
