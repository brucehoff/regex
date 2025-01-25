package regex;

import static regex.State.START_STATE;
import static regex.State.TERMINAL_STATE;

import java.util.Arrays;
import java.util.List;

public class RegexParser {
	
	private static final Character STAR = '*';
	private static final Character PLUS = '+';
	private static final Character QUESTION_MARK = '?';
	private static final Character WILDCARD = '.';
	private static final Character BACKSLASH = '\\';
	private static final WildcardCharacterMatch WILDCARD_MATCH = new WildcardCharacterMatch();
	private static final List<Character> ALL_CONTROLS = Arrays.asList(new Character[] {STAR, PLUS, QUESTION_MARK, WILDCARD});
	
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
			Character peek = i<re.length()-1 ? re.charAt(i+1) : null;
			State nextState = null;
			if (c.equals(BACKSLASH)) { // \
				if (peek==null) throw new RuntimeException("Regex cannot end with '\\'.");
				if (!ALL_CONTROLS.contains(peek)) throw new RuntimeException("`\\` must be followed by one of "+ALL_CONTROLS);
				i++;
				nextState = i<re.length()-1 ? new State(""+i) : TERMINAL_STATE;
				CharacterMatch match = new LiteralCharacterMatch(peek);
				nfa.addTransition(state, new MatchTransitionPair(match, new Transition(nextState)));
			} else if (peek!=null && peek.equals(STAR)) { // *
				i++;
				nextState = i<re.length()-1 ? new State(""+i) : TERMINAL_STATE;
				CharacterMatch match = c.equals(WILDCARD) ? WILDCARD_MATCH : new LiteralCharacterMatch(c);
				nfa.addTransition(state, Arrays.asList(new MatchTransitionPair[] {
						new MatchTransitionPair(match, new Transition(state)),
						new MatchTransitionPair(match, new Transition(nextState)),
						// epsilon transition.  The rule matches anything
						new MatchTransitionPair(WILDCARD_MATCH, new Transition(nextState, true))
				}));
			} else if (peek!=null && peek.equals(QUESTION_MARK)) { // ?
				i++;
				nextState = i<re.length()-1 ? new State(""+i) : TERMINAL_STATE;
				CharacterMatch match = c.equals(WILDCARD) ? WILDCARD_MATCH : new LiteralCharacterMatch(c);
				nfa.addTransition(state, Arrays.asList(new MatchTransitionPair[] {
						new MatchTransitionPair(match, new Transition(nextState)),
						// epsilon transition.  The rule matches anything
						new MatchTransitionPair(WILDCARD_MATCH, new Transition(nextState, true))
				}));
			} else if (peek!=null && peek.equals(PLUS)) { // +
				throw new RuntimeException("Not yet impelemented");
			} else { // no modifier character
				nextState = i<re.length()-1 ? new State(""+i) : TERMINAL_STATE;
				CharacterMatch match = c.equals(WILDCARD) ? WILDCARD_MATCH : new LiteralCharacterMatch(c);
				nfa.addTransition(state, new MatchTransitionPair(match, new Transition(nextState)));
			} 
			if (nextState==null) throw new IllegalStateException();
			state=nextState;
		}
		return nfa;
	}

}
