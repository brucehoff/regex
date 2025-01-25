package regex;

import static regex.NFA.START_STATE;
import static regex.NFA.*;

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
	private static final List<Character> ILLEGAL_START = Arrays.asList(new Character[] {STAR, PLUS, QUESTION_MARK});
	
	
	private static final List<MatchTransitionPair> createNonGreedyWildcardTransitionList(State currentState, State nextState) {
		return Arrays.asList(new MatchTransitionPair[] {
				// epsilon transition.  The rule matches anything
				new MatchTransitionPair(WILDCARD_MATCH, new Transition(nextState, true)),
				new MatchTransitionPair(WILDCARD_MATCH, new Transition(currentState)),
				// match end of line. (Alternative is to add a special char' on the end of the input string
				new MatchTransitionPair(END_OF_LINE_MATCH, new Transition(nextState, true))
		});
	}
	
	/**
	 * Parse a regular expression String and return an NFA
	 * @param re
	 * @return
	 */
	public static NFA parseRegex(String re) throws InvalidRegularExpression {
		NFA nfa = new NFA();
		// we start by adding 'non-greedy' wildcards (like '.*') to the beginning and end of the patterns
		nfa.addTransition(START_STATE, createNonGreedyWildcardTransitionList(START_STATE, START_OF_PATTERN));
		nfa.addTransition(AFTER_PATTERN, createNonGreedyWildcardTransitionList(AFTER_PATTERN, TERMINAL_STATE));
		if (re.length()==0) { // special case: no rules
			nfa.addTransition(START_STATE, new MatchTransitionPair(END_OF_LINE_MATCH, new Transition(AFTER_PATTERN, true)));
			return nfa;
		}
		State state = START_OF_PATTERN;
		int nextStateId=0; // a convenience generator for unique state ids
		for (int i=0; i<re.length(); i++) {
			Character c = re.charAt(i); // the current character
			Character peek = i<re.length()-1 ? re.charAt(i+1) : null;
			State nextState = null;
			if (ILLEGAL_START.contains(c)) {
				throw new InvalidRegularExpression("Illegal char at "+i);				
			} else if (c.equals(BACKSLASH)) { // \
				if (peek==null) throw new InvalidRegularExpression("Regex cannot end with '\\'.");
				if (!ALL_CONTROLS.contains(peek)) throw new InvalidRegularExpression("`\\` must be followed by one of "+ALL_CONTROLS);
				i++;
				nextState = i<re.length()-1 ? new State(""+(nextStateId++)) : AFTER_PATTERN;
				CharacterMatch match = new LiteralCharacterMatch(peek);
				nfa.addTransition(state, new MatchTransitionPair(match, new Transition(nextState)));
			} else if (peek!=null && peek.equals(STAR)) { // *
				i++;
				nextState = i<re.length()-1 ? new State(""+(nextStateId++)) : AFTER_PATTERN;
				CharacterMatch match = c.equals(WILDCARD) ? WILDCARD_MATCH : new LiteralCharacterMatch(c);
				nfa.addTransition(state, Arrays.asList(new MatchTransitionPair[] {
						new MatchTransitionPair(match, new Transition(state)),
						// epsilon transition.  The rule matches anything
						new MatchTransitionPair(WILDCARD_MATCH, new Transition(nextState, true)),
						// match end of line. (Alternative is to add a special char' on the end of the input string
						new MatchTransitionPair(END_OF_LINE_MATCH, new Transition(nextState, true))
				}));
			} else if (peek!=null && peek.equals(QUESTION_MARK)) { // ?
				i++;
				nextState = i<re.length()-1 ? new State(""+(nextStateId++)) : AFTER_PATTERN;
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
				nextState = i<re.length()-1 ? new State(""+(nextStateId++)) : AFTER_PATTERN;
				CharacterMatch match = c.equals(WILDCARD) ? WILDCARD_MATCH : new LiteralCharacterMatch(c);
				nfa.addTransition(state, Arrays.asList(new MatchTransitionPair[] {
						new MatchTransitionPair(match, new Transition(state)),
						new MatchTransitionPair(match, new Transition(nextState))
				}));
			} else { // no modifier character
				nextState = i<re.length()-1 ? new State(""+(nextStateId++)) : AFTER_PATTERN;
				CharacterMatch match = c.equals(WILDCARD) ? WILDCARD_MATCH : new LiteralCharacterMatch(c);
				nfa.addTransition(state, new MatchTransitionPair(match, new Transition(nextState)));
			} 
			if (nextState==null) throw new IllegalStateException();
			state=nextState;
		}
		return nfa;
	}

}
