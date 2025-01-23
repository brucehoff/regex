package regex;

import static regex.State.START_STATE;
import static regex.State.TERMINAL_STATE;

public class RegexParser {
	
	private static final Character star = '*';
	
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
				// TODO
			} else {
				nfa.addTransition(state, c, nextState);
				state=nextState;
			} 
		}
		return nfa;
	}

}
