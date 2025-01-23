package regex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class NFA {
	
	/*
	 * nfa maps a state, input pair to the list of possible transitions
	 */
	private Map<StateInputPair, List<State>> nfa;
	
	public NFA() {
		nfa = new HashMap<StateInputPair, List<State>>();
	}
	
	/**
	 * Add a transition to the NFA.  The order of the transition states
	 * passed in is the order in which they will be applied during execution.
	 * @param state
	 * @param input
	 * @param transitions
	 */
	public void addTransition(State state, Character input, List<State> transitions) {
		nfa.put(new StateInputPair(state, input), transitions);
	}
	
	public void addTransition(State state, Character input, State transition) {
		nfa.put(new StateInputPair(state, input), Arrays.asList(new State[] {transition}));
	}
	
	/**
	 * Apply the NFA to an input string.
	 * @param s the input string
	 */
	public void parse(String s) {
		State state = State.START_STATE;
		Stack<StringPositionAndTransitions> backtrackStack = new Stack<StringPositionAndTransitions>();
		for (int i=0; i<s.length(); i++) {
			List<State> transitions = nfa.get(new StateInputPair(state, s.charAt(i)));
			if (transitions==null) {
				//  cannot continue.  Can we backtrack?
				if (backtrackStack.empty()) {
					throw new RuntimeException("Parser failed at character "+i);
				}
				StringPositionAndTransitions spt = backtrackStack.pop();
				i = spt.getStringPosition();
				transitions = spt.getTransitions();
				// System.out.println("Backtracking to position "+i+" in "+s+". We have "+transitions.size()+" more rule(s) to try.");
			}
			state=transitions.get(0);
			if (transitions.size()>1) {
				backtrackStack.push(new StringPositionAndTransitions(i, dropFirstElement(transitions)));
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
