package regex;

import static regex.State.START_STATE;
import static regex.State.TERMINAL_STATE;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

public class NFATest {

	@Test
	public void testNFAwithSimpleString() {
		// regex = "abc"
		NFA nfa = new NFA();
		State s1 = new State("s1");
		nfa.addTransition(START_STATE, 'a', s1);
		State s2 = new State("s2");
		nfa.addTransition(s1, 'b', s2);
		nfa.addTransition(s2, 'c', TERMINAL_STATE);
		
		// matching case
		nfa.parse("abc");
		
		// not matching case
		Assert.assertThrows(RuntimeException.class, () -> {nfa.parse("abd");});
		
		// not matching case
		Assert.assertThrows(RuntimeException.class, () -> {nfa.parse("dbc");});
	}
	
	@Test
	public void testNFAWithBacktracking() {
		// regex = "a*ab"
		NFA nfa = new NFA();
		State s1 = new State("s1");
		nfa.addTransition(START_STATE, 'a', Arrays.asList(new State[] {START_STATE, s1}));
		nfa.addTransition(s1, 'b', TERMINAL_STATE);
		
		// matching cases
		nfa.parse("ab");
		nfa.parse("aab");
		
		// not matching cases
		Assert.assertThrows(RuntimeException.class, () -> {nfa.parse("b");});
	}

}
