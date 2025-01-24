package regex;

/*
 * Transition class contains (1) a state to transition to and 
 * (2) whether it's an epsilon transition (doesn't consume 
 * the input symbol).
 */
public class Transition {
	
	private State state;
	private boolean epsilon;
	
	/**
	 * Create a non-epsilon transition
	 * @param state
	 */
	public Transition(State state) {
		this.state = state;
		this.epsilon = false;
	}

	public Transition(State state, boolean epsilon) {
		this.state = state;
		this.epsilon = epsilon;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public boolean isEpsilon() {
		return epsilon;
	}

	public void setEpsilon(boolean epsilon) {
		this.epsilon = epsilon;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (epsilon ? 1231 : 1237);
		result = prime * result + ((state == null) ? 0 : state.hashCode());
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
		Transition other = (Transition) obj;
		if (epsilon != other.epsilon)
			return false;
		if (state == null) {
			if (other.state != null)
				return false;
		} else if (!state.equals(other.state))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Transition [state=" + state + ", epsilon=" + epsilon + "]";
	}
	
	

}
