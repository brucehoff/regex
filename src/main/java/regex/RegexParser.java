package regex;

import static regex.State.START_STATE;
import static regex.State.TERMINAL_STATE;

import java.util.Arrays;

public class RegexParser {
	
	private static final Character star = '*';
	private static final Character wildcard = '.';
	private static final WildcardCharacterMatch WILDCARD_MATCH = new WildcardCharacterMatch();
	
	/**
	 * Parse a regular expression String and return an NFA
	 * @param re
	 * @return
	 */
	public static NFA parseRegex(String re) {
		NFA nfa = new NFA();
		State state = START_STATE;
		for (int i=0; i<re.length(); i++) {
			Character c = re.charAt(i); // the current character
			Character peek = i<re.length()-1 ? re.charAt(+1) : null;
			State nextState = i<re.length()-1 ? new State(""+i) : TERMINAL_STATE;
			if (peek!=null && peek.equals(star)) {
				throw new RuntimeException("Not yet implemented.");
			} else {
				if (c.equals(wildcard)) {
					nfa.addTransition(state, new MatchTransitionPair(WILDCARD_MATCH, new Transition(nextState)));
				} else {
					nfa.addTransition(state, new MatchTransitionPair(new LiteralCharacterMatch(c), new Transition(nextState)));
				}
			} 
			state=nextState;
		}
		return nfa;
	}

}
