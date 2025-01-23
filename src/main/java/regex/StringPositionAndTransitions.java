package regex;

import java.util.List;

public class StringPositionAndTransitions {
	private int stringPosition;
	private List<State> transitions;
	
	public StringPositionAndTransitions(int stringPosition, List<State> transitions) {
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
	public List<State> getTransitions() {
		return transitions;
	}
	public void setTransitions(List<State> transitions) {
		this.transitions = transitions;
	}
	
	

}
