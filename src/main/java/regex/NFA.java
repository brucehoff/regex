package regex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class NFA {
	public static final State START_STATE = new State("START");
	
	public static final State TERMINAL_STATE = new State("END");
	
	public static final State START_OF_PATTERN = new State("START_OF_PATTERN");
	
	public static final State AFTER_PATTERN = new State("AFTER_PATTERN");
	
	
	/*
	 * nfa maps a state to the list of possible transitions
	 */
	Map<State, List<MatchTransitionPair>> nfa;
	
	public NFA() {
		nfa = new HashMap<State, List<MatchTransitionPair>>();
	}
	
	/**
	 * Add a transition to the NFA.  The order of the transition states
	 * passed in is the order in which they will be applied during execution.
	 */
	public void addTransition(State state, List<MatchTransitionPair> transitions) {
		nfa.put(state, transitions);
	}
	
	public void addTransition(State state,  MatchTransitionPair transition) {
		nfa.put(state, Arrays.asList(new MatchTransitionPair[] {transition}));
	}
	
	
	/**
	 * Apply the NFA to an input string.
	 * @param s the input string
	 */
	public StartAndEnd parse(String s) throws NoRegularExpressionMatchException {
		// every character in 's' will be associates with some state from the regular expression
		// this is important to determine where the sought pattern starts and ends
		State[] characterStates = new State[s.length()+1];
		State state = START_STATE;
		Stack<StringPositionStateAndTransitions> backtrackStack = new Stack<StringPositionStateAndTransitions>();
		int i=0;
		while (i<s.length() || !state.equals(TERMINAL_STATE)) {
			characterStates[i] = state;
			List<Transition> matchingTransitions = new ArrayList<Transition>();
			// If we have characters to parse and are not in a terminal state,
			// look up the next transition in the NFA.
			// Otherwise we'll just try to backtrack
			if (!state.equals(TERMINAL_STATE)) {
				if (!nfa.containsKey(state)) throw new IllegalStateException("No transition for "+state);
				for (MatchTransitionPair mtp : nfa.get(state)) {
					if ((i<s.length() && mtp.getMatch().match(s.charAt(i))) ||
							(i>=s.length() && mtp.getTransition().isEpsilon() && mtp.getMatch() instanceof EndOfLineMatch)) {
						matchingTransitions.add(mtp.getTransition());
					}
				}
			}
			// matchingTransitions is the list of Transitions which are valid for the current input character
			if (matchingTransitions.isEmpty()) {
				//  No matching transitions.  Can we backtrack?
				if (backtrackStack.empty()) {
					throw new NoRegularExpressionMatchException("Parser failed at character "+i);
				}
				StringPositionStateAndTransitions spt = backtrackStack.pop();
				i = spt.getStringPosition();
				matchingTransitions = spt.getTransitions();
				state = spt.getState();
				// System.out.println("Backtracking to position "+i+" in "+s+". We have "+matchingTransitions.size()+" more rule(s) to try.");
				characterStates[i]=state;
			}
			Transition transition=matchingTransitions.get(0);
			if (matchingTransitions.size()>1) {
				backtrackStack.push(new StringPositionStateAndTransitions(i, state, dropFirstElement(matchingTransitions)));
			}
			state = transition.getState();
			// an epsilon-transition does not consume the next character, but a non-epsilon transition does.
			if (!transition.isEpsilon()) {
				i++;
			}
		}
		// now we need to find where the pattern starts and ends
		int start = 0; // first char after START state or 0 if no character matches START state
		int end = -1;
		for (int j=0; j<characterStates.length; j++) {
			if (START_STATE.equals(characterStates[j])) {
				start = j+1; // this will be the first character position that matches some state of the regex
			}
			if (end<0 && AFTER_PATTERN.equals(characterStates[j])) {
				end = j; // this will be the first character position after the regex
			}
		}
		// edge case: empty string
		if (s.length()==0) {
			start=0;
		}
		return new StartAndEnd(start, end);
	}
	
	static <T> List<T> dropFirstElement(List<T> list) {
		List<T> result = new ArrayList<T>();
		for (int i = 1; i<list.size(); i++) {
			result.add(list.get(i));
		}
		return result;
	}
	
	/**
	 * Clear the states from 'from' to end of array
	 * @param characterStates
	 * @param from
	 */
	static void clearCharacterStates(State[] characterStates, int from) {
		for (int i=from; i<characterStates.length; i++) {
			characterStates[i]=null;
		}
	}
	
	public String toString() {
		return nfa.toString();
	}
}
