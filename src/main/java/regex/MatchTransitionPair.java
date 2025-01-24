package regex;

public class MatchTransitionPair {
	private CharacterMatch match;
	private Transition transition;
	
	public MatchTransitionPair(CharacterMatch match, Transition transition) {
		this.match = match;
		this.transition = transition;
	}
	
	public MatchTransitionPair(Character c, Transition transition) {
		this.match = new LiteralCharacterMatch(c);
		this.transition = transition;
	}

	public CharacterMatch getMatch() {
		return match;
	}

	public void setMatch(CharacterMatch match) {
		this.match = match;
	}

	public Transition getTransition() {
		return transition;
	}

	public void setTransition(Transition transition) {
		this.transition = transition;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((match == null) ? 0 : match.hashCode());
		result = prime * result + ((transition == null) ? 0 : transition.hashCode());
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
		MatchTransitionPair other = (MatchTransitionPair) obj;
		if (match == null) {
			if (other.match != null)
				return false;
		} else if (!match.equals(other.match))
			return false;
		if (transition == null) {
			if (other.transition != null)
				return false;
		} else if (!transition.equals(other.transition))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "MatchTransitionPair [match=" + match + ", transition=" + transition + "]";
	}
	
	

}
