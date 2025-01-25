package regex;

public class RegexTokenAndPosition {
	RegexToken token;
	int position;
	public RegexTokenAndPosition(RegexToken token, int position) {
		super();
		this.token = token;
		this.position = position;
	}
	public RegexToken getToken() {
		return token;
	}
	public void setToken(RegexToken token) {
		this.token = token;
	}
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + position;
		result = prime * result + ((token == null) ? 0 : token.hashCode());
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
		RegexTokenAndPosition other = (RegexTokenAndPosition) obj;
		if (position != other.position)
			return false;
		if (token == null) {
			if (other.token != null)
				return false;
		} else if (!token.equals(other.token))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "RegexTokenAndPosition [token=" + token + ", position=" + position + "]";
	}
	
	
}
