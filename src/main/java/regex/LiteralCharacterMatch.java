package regex;

public class LiteralCharacterMatch implements CharacterMatch {
	private Character c;


	public LiteralCharacterMatch(Character c) {
		super();
		this.c = c;
	}



	@Override
	public boolean match(Character c) {
		return c.equals(this.c);
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((c == null) ? 0 : c.hashCode());
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
		LiteralCharacterMatch other = (LiteralCharacterMatch) obj;
		if (c == null) {
			if (other.c != null)
				return false;
		} else if (!c.equals(other.c))
			return false;
		return true;
	}



	@Override
	public String toString() {
		return c.toString();
	}
	
	

}
