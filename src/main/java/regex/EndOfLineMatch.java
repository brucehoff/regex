package regex;

public class EndOfLineMatch implements CharacterMatch {

	@Override
	public boolean match(Character c) {
		return true;
	}
	
	public int hashCode() {return 1;}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "EOL";
	}
	
	

}
