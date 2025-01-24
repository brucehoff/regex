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
		shouldPass("a*", "aa");
		shouldPass("a*", "");
		shouldPass("a*", "a");
		
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
	
	@Test
	public void testBasicCases() {
		fail("TODO");
	}
	
	@Test
	public void testTourDeForce() {
		fail("TODO");
		
	}
	

}
