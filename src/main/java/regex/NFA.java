package regex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class NFA {
	
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
	public void parse(String s) {
		State state = State.START_STATE;
		Stack<StringPositionAndTransitions> backtrackStack = new Stack<StringPositionAndTransitions>();
		int i=0;
		while (i<s.length()) {
			List<Transition> matchingTransitions = new ArrayList<Transition>();
			for (MatchTransitionPair mtp : nfa.get(state)) {
				if (mtp.getMatch().match(s.charAt(i))) {
					matchingTransitions.add(mtp.getTransition());
				}
			}
			// matchingTransitions is the list of Transitions which are valid for the current input character
			if (matchingTransitions.isEmpty()) {
				//  No matching transitions.  Can we backtrack?
				if (backtrackStack.empty()) {
					throw new RuntimeException("Parser failed at character "+i);
				}
				StringPositionAndTransitions spt = backtrackStack.pop();
				i = spt.getStringPosition();
				matchingTransitions = spt.getTransitions();
				System.out.println("Backtracking to position "+i+" in "+s+". We have "+matchingTransitions.size()+" more rule(s) to try.");
			}
			Transition transition=matchingTransitions.get(0);
			if (matchingTransitions.size()>1) {
				backtrackStack.push(new StringPositionAndTransitions(i, dropFirstElement(matchingTransitions)));
			}
			state = transition.getState();
			// an epsilon-transition does not consume the next character, but a non-epsilon transition does.
			if (!transition.isEpsilon()) {
				i++;
			}
		}
		if (!state.equals(State.TERMINAL_STATE)) {
			throw new RuntimeException("Parser failed.");
		}
	}
	
	static <T> List<T> dropFirstElement(List<T> list) {
		List<T> result = new ArrayList<T>();
		for (int i = 1; i<list.size(); i++) {
			result.add(list.get(i));
		}
		return result;
	}
	
	public String toString() {
		return nfa.toString();
	}
}
