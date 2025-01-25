package regex;

import static regex.NFA.AFTER_PATTERN;
import static regex.NFA.START_OF_PATTERN;
import static regex.NFA.START_STATE;
import static regex.NFA.TERMINAL_STATE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RegexParser {
	
	private static final char STAR = '*';
	private static final char PLUS = '+';
	private static final char QUESTION_MARK = '?';
	private static final char WILDCARD = '.';
	private static final char BACKSLASH = '\\';
	private static final WildcardCharacterMatch WILDCARD_MATCH = new WildcardCharacterMatch();
	private static final EndOfLineMatch END_OF_LINE_MATCH = new EndOfLineMatch();
	private static final List<Character> ALL_CONTROLS = Arrays.asList(new Character[] {STAR, PLUS, QUESTION_MARK, WILDCARD, BACKSLASH});
	private static final List<Character> METACHARACTERS = Arrays.asList(new Character[] {STAR, PLUS, QUESTION_MARK, WILDCARD});
	private static final List<RegexMetacharacter> MODIFIERS = 
			Arrays.asList(new RegexMetacharacter[] {RegexMetacharacter.STAR, RegexMetacharacter.PLUS, RegexMetacharacter.QUESTION_MARK});
	
	
	private static final List<MatchTransitionPair> createNonGreedyWildcardTransitionList(State currentState, State nextState) {
		return Arrays.asList(new MatchTransitionPair[] {
				// epsilon transition.  The rule matches anything
				new MatchTransitionPair(WILDCARD_MATCH, new Transition(nextState, true)),
				new MatchTransitionPair(WILDCARD_MATCH, new Transition(currentState)),
				// match end of line. (Alternative is to add a special char' on the end of the input string
				new MatchTransitionPair(END_OF_LINE_MATCH, new Transition(nextState, true))
		});
	}
	
	public static List<RegexTokenAndPosition> tokenizeString(String s) throws InvalidRegularExpression {
		List<RegexTokenAndPosition> result = new ArrayList<RegexTokenAndPosition>();
		for (int i=0; i<s.length(); i++) {
			char c = s.charAt(i); // the current character
			if (BACKSLASH==c) {
				if (i==s.length()-1) throw new InvalidRegularExpression("Cannot end with \\.", i);
				Character next = s.charAt(i+1);
				if (ALL_CONTROLS.contains(next)) {
					i++;
					result.add(new RegexTokenAndPosition(new RegexLiteral(next), i));					
				} else {
					throw new IllegalStateException("Attempt to escape a non-metacharacter at position "+i);
				}
			} else {
				if (METACHARACTERS.contains(c)) {
					RegexMetacharacter meta = null;
					switch (c) {
					case STAR:
						meta = RegexMetacharacter.STAR; 
						break;
					case PLUS:
						meta = RegexMetacharacter.PLUS; 
						break;
					case WILDCARD:
						meta = RegexMetacharacter.WILDCARD; 
						break;
					case QUESTION_MARK:
						meta = RegexMetacharacter.QUESTION_MARK; 
						break;
					default:
						throw new IllegalStateException("Unexpected "+c);
					}
					result.add(new RegexTokenAndPosition(meta, i));
				} else {
					result.add(new RegexTokenAndPosition(new RegexLiteral(c), i));
				}
			}
		}
		return result;
	}

	/**
	 * Parse a regular expression String and return an NFA
	 * @param regexString the regular expression to parse
	 * @return
	 */
	public static NFA parseRegex(String regexString) throws InvalidRegularExpression {
		NFA nfa = new NFA();
		List<RegexTokenAndPosition> re = tokenizeString(regexString);
		// we start by adding 'non-greedy' wildcards (like '.*') to the beginning and end of the patterns
		nfa.addTransition(START_STATE, createNonGreedyWildcardTransitionList(START_STATE, START_OF_PATTERN));
		nfa.addTransition(AFTER_PATTERN, createNonGreedyWildcardTransitionList(AFTER_PATTERN, TERMINAL_STATE));
		if (re.size()==0) { // special case: no rules
			nfa.addTransition(START_STATE, new MatchTransitionPair(END_OF_LINE_MATCH, new Transition(AFTER_PATTERN, true)));
			return nfa;
		}
		State state = START_OF_PATTERN;
		int nextStateId=0; // a convenient generator for unique state ids
		for (int i=0; i<re.size(); i++) {
			RegexTokenAndPosition current = re.get(i);
			RegexTokenAndPosition next = i<re.size()-1 ? re.get(i+1) : null;
			State nextState = null;
			CharacterMatch match;
			if (MODIFIERS.contains(current.getToken())) {
				throw new InvalidRegularExpression("Illegal metacharacter "+current.getToken()+" at position "+current.getPosition(), current.getPosition());
			} else if (current.getToken()==RegexMetacharacter.WILDCARD ) {
				match = WILDCARD_MATCH;
			} else {
				match = new LiteralCharacterMatch(((RegexLiteral)current.getToken()).getC());
			}
			if (next!=null && MODIFIERS.contains(next.getToken())) {
				i++;
				nextState = i<re.size()-1 ? new State(""+(nextStateId++)) : AFTER_PATTERN;	
				if (next.getToken().equals(RegexMetacharacter.STAR)) { // *
					nfa.addTransition(state, Arrays.asList(new MatchTransitionPair[] {
							new MatchTransitionPair(match, new Transition(state)),
							// epsilon transition.  The rule matches anything
							new MatchTransitionPair(WILDCARD_MATCH, new Transition(nextState, true)),
							// match end of line. (Alternative is to add a special char' on the end of the input string)
							new MatchTransitionPair(END_OF_LINE_MATCH, new Transition(nextState, true))
					}));
				} else if (next.getToken().equals(RegexMetacharacter.QUESTION_MARK)) { // ?
					nfa.addTransition(state, Arrays.asList(new MatchTransitionPair[] {
							new MatchTransitionPair(match, new Transition(nextState)),
							// epsilon transition.  The rule matches anything
							new MatchTransitionPair(WILDCARD_MATCH, new Transition(nextState, true)),
							// match end of line. (Alternative is to add a special char' on the end of the input string)
							new MatchTransitionPair(END_OF_LINE_MATCH, new Transition(nextState, true))
					}));
				} else if (next.getToken().equals(RegexMetacharacter.PLUS)) { // +
					nfa.addTransition(state, Arrays.asList(new MatchTransitionPair[] {
							new MatchTransitionPair(match, new Transition(state)),
							new MatchTransitionPair(match, new Transition(nextState))
					}));
				} else {
					throw new IllegalStateException("Unexpected token "+next.getToken());
				}
			} else { // no modifier character
				nextState = i<re.size()-1 ? new State(""+(nextStateId++)) : AFTER_PATTERN;	
				nfa.addTransition(state, new MatchTransitionPair(match, new Transition(nextState)));
			} 
			if (nextState==null) throw new IllegalStateException();
			state=nextState;
		}
		return nfa;
	}

}
