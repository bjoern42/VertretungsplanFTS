package MyApps.VertretungsplanFTSMobile.model;

public class InvalidLoginException extends Exception {
	private static final long serialVersionUID = 1L;

	public InvalidLoginException(){
		super("Invalid Login");
	}
}
