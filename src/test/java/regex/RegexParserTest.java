package regex;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;

public class RegexParserTest {
	private void shouldPass(String regex, String input) {
		try {
			NFA nfa = RegexParser.parseRegex(regex);
			nfa.parse(input);	
		} catch (InvalidRegularExpression ire) {
			fail("Unexpected failure in regular expression.");
		} catch (NoRegularExpressionMatchException nreme) {
			fail("Unexpected failure to parse.");			
		}
	}
	
	public void noMatch(String regex, String input) {
		try {
			NFA nfa = RegexParser.parseRegex(regex);
			Assert.assertThrows(NoRegularExpressionMatchException.class, () -> {nfa.parse(input);});
		} catch (InvalidRegularExpression e) {
			fail("Unexpected failure in regular expression.");
		}
	}
	
	public void badRegex(String regex) {
		Assert.assertThrows(InvalidRegularExpression.class, () -> {RegexParser.parseRegex(regex);});
	}
	
	@Test
	public void testEmptyString() {
		shouldPass("", "");
	}

	@Test
	public void testSimpleString() {
		shouldPass("abc", "abc");
		noMatch("abc", "dbc");
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
		noMatch("a+", "");
		shouldPass("a+", "a");
		shouldPass("a+", "aa");
		
		noMatch("a+aab", "aab");
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
		badRegex("a*?b");
		badRegex("a+?b");
		shouldPass(".?", "X");
		shouldPass(".+", "X");
		shouldPass(".*", "X");
		
		noMatch("a", "abc"); // TODO match as a substring
		noMatch("b", "abc"); // TODO match as a substring
		noMatch("c", "abc"); // TODO match as a substring
		
		shouldPass("abc", "abc");
		noMatch("abc", "abd");
		badRegex("a**b");
	}	
	
	@Test
	public void testTourDeForce() {
		shouldPass("a*b*c*", "");
		noMatch("a*b*c*c", "");
		shouldPass("\\\\\\?\\+", "\\?+");
		// shouldPass("\\+*", "++++++"); TODO
	}
	

}
