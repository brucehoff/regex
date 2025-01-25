package regex;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Assert;
import org.junit.Test;

public class RegexParserTest {
	private void shouldPass(String regex, String input, int start, int end) {
		try {
			NFA nfa = RegexParser.parseRegex(regex);
			StartAndEnd result = nfa.parse(input);	
			assertEquals(new StartAndEnd(start, end), result);
		} catch (InvalidRegularExpression ire) {
			fail("Unexpected failure in regular expression: "+ire.getMessage());
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
	
	public void badRegex(String regex, int failurePosition) {
		try {
			RegexParser.parseRegex(regex);
			fail("Expected failure.");
		} catch (InvalidRegularExpression e) {
			assertEquals(failurePosition, e.getPosition());
		}
	}
	
	@Test
	public void testEmptyString() {
		shouldPass("", "", 0, 0);
	}
	
	@Test
	public void testSimpleSubstring() {
		// 'a' is found as a substring of 'aX' so this should pass
		shouldPass("a", "aX", 0, 1);
		shouldPass("a", "Xa", 1, 2);
	}

	@Test
	public void testSimpleString() {
		shouldPass("abc", "abc", 0, 3);
		noMatch("abc", "dbc");
	}
	
	@Test
	public void testWildCardMatch() {
		shouldPass(".*", "", 0, 0);
		shouldPass(".*", "a", 0, 1);
		shouldPass(".*", "aa", 0, 2);
		
		shouldPass(".*aab", "aab", 0, 3);
		shouldPass(".*aab", "Xaab", 0, 4);
		shouldPass(".*aab", "XYaab", 0, 5);
	}
	
	@Test
	public void testAsterisk() {
		shouldPass("a*", "", 0, 0);

		shouldPass("a*", "a", 0, 1);
		shouldPass("a*", "aa", 0, 2);
		
		shouldPass("a*aab", "aab", 0, 3);
		shouldPass("a*aab", "aaab", 0, 4);
		shouldPass("a*aab", "aaaab", 0, 5);
	}
	
	@Test
	public void testQuestionMark() {
		shouldPass("a?", "", 0, 0);
		shouldPass("a?", "a", 0, 1);
		shouldPass("aab?cc", "aacc", 0, 4);
		shouldPass("aab?cc", "aabcc", 0, 5);
	}

	@Test
	public void testPlus() {
		noMatch("a+", "");
		shouldPass("a+", "a", 0, 1);
		shouldPass("a+", "aa", 0, 2);
		
		noMatch("a+aab", "aab");
		shouldPass("a+aab", "aaab", 0, 4);
		shouldPass("a+aab", "aaaab", 0, 5);
	}
	
	// cases from problem statement
	@Test
	public void testBasicCases() {
		shouldPass("a\\.b", "a.b", 0, 3);
		shouldPass("a\\*b", "a*b", 0, 3);
		shouldPass("a\\?b", "a?b", 0, 3);
		shouldPass("a\\+b", "a+b", 0, 3);
		shouldPass("a\\\\b", "a\\b", 0, 3);
		badRegex("a*?b", 2);
		badRegex("a+?b", 2);
		shouldPass(".?", "X", 0, 1);
		shouldPass(".+", "X", 0, 1);
		shouldPass(".*", "X", 0, 1);
		
		shouldPass("a", "abc", 0, 1);
		shouldPass("b", "abc", 1, 2);
		shouldPass("c", "abc", 2, 3);
		
		shouldPass("abc", "abc", 0, 3);
		noMatch("abc", "abd");
		badRegex("a**b", 2);
	}	
	
	@Test
	public void testTourDeForce() {
		shouldPass("a*b*c*", "", 0, 0);
		noMatch("a*b*c*c", "");
		shouldPass("\\\\\\?\\+", "\\?+", 0, 3);
		shouldPass("\\+*", "++++++", 0, 6);
	}
	

}
