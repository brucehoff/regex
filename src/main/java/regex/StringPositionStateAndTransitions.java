package regex;

import java.util.List;

public class StringPositionStateAndTransitions {
	private int stringPosition;
	private State state;
	private List<Transition> transitions;
	
	public StringPositionStateAndTransitions(int stringPosition, State state, List<Transition> transitions) {
		this.state=state;
		this.stringPosition = stringPosition;
		this.transitions = transitions;
	}
	public State getState() {
		return state;
	}
	public void setState(State state) {
		this.state = state;
	}
	public int getStringPosition() {
		return stringPosition;
	}
	public void setStringPosition(int stringPosition) {
		this.stringPosition = stringPosition;
	}
	public List<Transition> getTransitions() {
		return transitions;
	}
	public void setTransitions(List<Transition> transitions) {
		this.transitions = transitions;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((state == null) ? 0 : state.hashCode());
		result = prime * result + stringPosition;
		result = prime * result + ((transitions == null) ? 0 : transitions.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StringPositionStateAndTransitions other = (StringPositionStateAndTransitions) obj;
		if (state == null) {
			if (other.state != null)
				return false;
		} else if (!state.equals(other.state))
			return false;
		if (stringPosition != other.stringPosition)
			return false;
		if (transitions == null) {
			if (other.transitions != null)
				return false;
		} else if (!transitions.equals(other.transitions))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "StringPositionStateAndTransitions [stringPosition=" + stringPosition + ", state=" + state
				+ ", transitions=" + transitions + "]";
	}


}
