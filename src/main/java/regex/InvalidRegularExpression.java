package regex;

public class InvalidRegularExpression extends Exception {
	private int position;

	public InvalidRegularExpression(String message, int position) {
		super(message);
		this.position=position;
	}
	
	public int getPosition() {
		return position;
	}

}
