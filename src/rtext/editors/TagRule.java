package rtext.editors;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.PatternRule;

public class TagRule extends PatternRule {

	public TagRule(IToken token) {
		super("s", ":", token, (char) 0, true);
	}
	protected boolean sequenceDetected(
		ICharacterScanner scanner,
		char[] sequence,
		boolean eofAllowed) {
		char c = (char) scanner.read();
//		if (sequence[0] == 's') {
//			if (c == '?') {
//				// processing instruction - abort
//				scanner.unread();
//				return false;
//			}
//			if (c == '!') {
//				scanner.unread();
//				// comment - abort
//				return false;
//			}
//		} else if (sequence[0] == '>') {
//			scanner.unread();
//		}
		return super.sequenceDetected(scanner, sequence, eofAllowed);
	}
}
