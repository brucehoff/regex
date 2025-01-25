package regex;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;

public class RegexParserTest {
	private void shouldPass(String regex, String input) {
		NFA nfa = RegexParser.parseRegex(regex);
		nfa.parse(input);	
	}
	
	public void shouldFail(String regex, String input) {
		NFA nfa = RegexParser.parseRegex(regex);
		Assert.assertThrows(RuntimeException.class, () -> {nfa.parse(input);});
	}
	
	@Test
	public void testEmptyString() {
		shouldPass("", "");
	}

	@Test
	public void testSimpleString() {
		shouldPass("abc", "abc");
		shouldFail("abc", "dbc");
	}
	
	@Test
	public void testWildCardMatch() {
		shouldPass(".*", "");
		shouldPass(".*", "a");
		shouldPass(".*", "aa");
		
		shouldPass(".*aab", "aab");
		shouldPass(".*aab", "Xaab");
		shouldPass(".*aab", "XYaab");
	}
	
	@Test
	public void testAsterisk() {
		shouldPass("a*", "");

		shouldPass("a*", "a");
		shouldPass("a*", "aa");
		
		shouldPass("a*aab", "aab");
		shouldPass("a*aab", "aaab");
		shouldPass("a*aab", "aaaab");
	}
	
	@Test
	public void testQuestionMark() {
		shouldPass("a?", "");
		shouldPass("a?", "a");
		shouldPass("aab?cc", "aacc");
		shouldPass("aab?cc", "aabcc");
	}

	@Test
	public void testPlus() {
		shouldFail("a+", "");
		shouldPass("a+", "a");
		shouldPass("a+", "aa");
		
		shouldFail("a+aab", "aab");
		shouldPass("a+aab", "aaab");
		shouldPass("a+aab", "aaaab");
	}

	// cases from problem statement
	@Test
	public void testBasicCases() {
		shouldPass("a\\.b", "a.b");
		shouldPass("a\\*b", "a*b");
		shouldPass("a\\?b", "a?b");
		shouldPass("a\\+b", "a+b");
		shouldPass("a\\\\b", "a\\b");
		shouldFail("a*?b", "aab"); // TODO check that failure is from illegal regex
		shouldFail("a+?b", "aab"); // TODO check that failure is from illegal regex
		shouldPass(".?", "X");
		shouldPass(".+", "X");
		shouldPass(".*", "X");
		
		shouldFail("a", "abc"); // TODO match as a substring
		shouldFail("b", "abc"); // TODO match as a substring
		shouldFail("c", "abc"); // TODO match as a substring
		
		shouldPass("abc", "abc");
		shouldFail("abc", "abd");
		shouldFail("a**b", "abc"); // TODO check that failure is from illegal regex
	}	
	
	@Test
	public void testTourDeForce() {
		shouldPass("a*b*c*", "");
		shouldFail("a*b*c*c", "");
		shouldPass("\\\\\\?\\+", "\\?+");
		// shouldPass("\\+*", "++++++"); TODO
	}
	

}
