package regex;

import static regex.State.START_STATE;
import static regex.State.TERMINAL_STATE;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

public class NFATest {

	@Test
	public void testNFAwithSimpleString() throws Exception {
		// regex = "abc"
		NFA nfa = new NFA();
		State s1 = new State("s1");
		nfa.addTransition(START_STATE, new MatchTransitionPair('a', new Transition(s1)));
		State s2 = new State("s2");
		nfa.addTransition(s1, new MatchTransitionPair('b', new Transition(s2)));
		nfa.addTransition(s2, new MatchTransitionPair('c', new Transition(TERMINAL_STATE)));
		
		// matching case
		nfa.parse("abc");
		
		// not matching case
		Assert.assertThrows(RuntimeException.class, () -> {nfa.parse("abd");});
		
		// not matching case
		Assert.assertThrows(RuntimeException.class, () -> {nfa.parse("dbc");});
	}
	
	@Test
	public void testNFAWithBacktracking() throws Exception {
		// regex = "a*ab"
		NFA nfa = new NFA();
		State s1 = new State("s1");
		nfa.addTransition(START_STATE, Arrays.asList(new MatchTransitionPair[] {
				new MatchTransitionPair('a', new Transition(START_STATE)), new MatchTransitionPair('a', new Transition(s1))}));
		nfa.addTransition(s1, new MatchTransitionPair('b', new Transition(TERMINAL_STATE)));
		
		// matching cases
		nfa.parse("ab");
		nfa.parse("aab");
		
		// not matching cases
		Assert.assertThrows(RuntimeException.class, () -> {nfa.parse("b");});
	}

}
