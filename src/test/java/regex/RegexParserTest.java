package regex;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;

public class RegexParserTest {

	@Test
	public void testSimpleString() {
		String regex = "abc";
		NFA nfa = RegexParser.parseRegex(regex);
		
		// passes
		nfa.parse(regex);
		
		// fails
		Assert.assertThrows(RuntimeException.class, () -> {nfa.parse("dbc");});
	}

}
