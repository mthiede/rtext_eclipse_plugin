package rules;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

public class LabelRule implements IPredicateRule {
	IToken fToken;
	
	public LabelRule(IToken token) {
		fToken = token;
	}

	public IToken evaluate(ICharacterScanner scanner, boolean resume) {
		int c = scanner.read();
		int count = 1;
		while (Character.isLetterOrDigit(c)) {
			c = scanner.read();
			count++;
		}
		if (c == ':' && count > 1) {
			return fToken;
		}
		while (count > 0) {
			scanner.unread();
			count--;
		}
		return Token.UNDEFINED;
	}

	public IToken getSuccessToken() {
		return fToken;
	}

	public IToken evaluate(ICharacterScanner scanner) {
		return evaluate(scanner, false);
	}

}
