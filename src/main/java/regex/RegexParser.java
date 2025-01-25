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
	private static final EndOfLineMatch END_OF_LINE_MATCH = new EndOfLineMatch();
	private static final List<Character> ALL_CONTROLS = Arrays.asList(new Character[] {STAR, PLUS, QUESTION_MARK, WILDCARD, BACKSLASH});
	
	/**
	 * Parse a regular expression String and return an NFA
	 * @param re
	 * @return
	 */
	public static NFA parseRegex(String re) {
		NFA nfa = new NFA();
		State state = START_STATE;
		if (re.length()==0) { // special case: no rules
			nfa.addTransition(START_STATE, new MatchTransitionPair(END_OF_LINE_MATCH, new Transition(TERMINAL_STATE, true)));
			return nfa;
		}
		int nextStateId=0;
		for (int i=0; i<re.length(); i++) {
			Character c = re.charAt(i); // the current character
			Character peek = i<re.length()-1 ? re.charAt(i+1) : null;
			State nextState = null;
			if (c.equals(BACKSLASH)) { // \
				if (peek==null) throw new RuntimeException("Regex cannot end with '\\'.");
				if (!ALL_CONTROLS.contains(peek)) throw new RuntimeException("`\\` must be followed by one of "+ALL_CONTROLS);
				i++;
				nextState = i<re.length()-1 ? new State(""+(nextStateId++)) : TERMINAL_STATE;
				CharacterMatch match = new LiteralCharacterMatch(peek);
				nfa.addTransition(state, new MatchTransitionPair(match, new Transition(nextState)));
			} else if (peek!=null && peek.equals(STAR)) { // *
				i++;
				nextState = i<re.length()-1 ? new State(""+(nextStateId++)) : TERMINAL_STATE;
				CharacterMatch match = c.equals(WILDCARD) ? WILDCARD_MATCH : new LiteralCharacterMatch(c);
				nfa.addTransition(state, Arrays.asList(new MatchTransitionPair[] {
						new MatchTransitionPair(match, new Transition(state)),
						new MatchTransitionPair(match, new Transition(nextState)),
						// epsilon transition.  The rule matches anything
						new MatchTransitionPair(WILDCARD_MATCH, new Transition(nextState, true)),
						// match end of line. (Alternative is to add a special char' on the end of the input string
						new MatchTransitionPair(END_OF_LINE_MATCH, new Transition(nextState, true))
				}));
			} else if (peek!=null && peek.equals(QUESTION_MARK)) { // ?
				i++;
				nextState = i<re.length()-1 ? new State(""+(nextStateId++)) : TERMINAL_STATE;
				CharacterMatch match = c.equals(WILDCARD) ? WILDCARD_MATCH : new LiteralCharacterMatch(c);
				nfa.addTransition(state, Arrays.asList(new MatchTransitionPair[] {
						new MatchTransitionPair(match, new Transition(nextState)),
						// epsilon transition.  The rule matches anything
						new MatchTransitionPair(WILDCARD_MATCH, new Transition(nextState, true)),
						// match end of line. (Alternative is to add a special char' on the end of the input string
						new MatchTransitionPair(END_OF_LINE_MATCH, new Transition(nextState, true))
				}));
			} else if (peek!=null && peek.equals(PLUS)) { // +
				i++;
				nextState = i<re.length()-1 ? new State(""+(nextStateId++)) : TERMINAL_STATE;
				CharacterMatch match = c.equals(WILDCARD) ? WILDCARD_MATCH : new LiteralCharacterMatch(c);
				nfa.addTransition(state, Arrays.asList(new MatchTransitionPair[] {
						new MatchTransitionPair(match, new Transition(state)),
						new MatchTransitionPair(match, new Transition(nextState))
				}));
			} else { // no modifier character
				nextState = i<re.length()-1 ? new State(""+(nextStateId++)) : TERMINAL_STATE;
				CharacterMatch match = c.equals(WILDCARD) ? WILDCARD_MATCH : new LiteralCharacterMatch(c);
				nfa.addTransition(state, new MatchTransitionPair(match, new Transition(nextState)));
			} 
			if (nextState==null) throw new IllegalStateException();
			state=nextState;
		}
		return nfa;
	}

}
