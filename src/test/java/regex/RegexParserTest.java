package regex;

import static org.junit.Assert.*;

import org.junit.Test;

public class RegexParserTest {

	@Test
	public void testSimpleString() {
		String regex = "abc";
		NFA nfa = RegexParser.parseRegex(regex);
		System.out.println(nfa);
		// I expect this to pass
		//nfa.parse(regex);
	}

}
