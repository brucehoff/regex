package regex;

import java.util.List;

public class StringPositionAndTransitions {
	private int stringPosition;
	private List<Transition> transitions;
	
	public StringPositionAndTransitions(int stringPosition, List<Transition> transitions) {
		super();
		this.stringPosition = stringPosition;
		this.transitions = transitions;
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
		StringPositionAndTransitions other = (StringPositionAndTransitions) obj;
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
		return "StringPositionAndTransitions [stringPosition=" + stringPosition + ", transitions=" + transitions + "]";
	}


}
