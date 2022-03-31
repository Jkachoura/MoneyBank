public class Keypad implements InputDevice{

	private String keypadKeys[] = {	"1", "2", "3", "A",
									"4", "5", "6", "B",
									"7", "8", "9", "C",
									"*", "0", "#",	"D" };
	
	SerialInterface com;
	
	Keypad(SerialInterface com){
		this.com = com;
	}
	
	public String getInput() {
		String input = com.getInput();
		
		if (input != null) {
			if (checkValidKey(input)) {
				com.clearInput();
				return input;
			}	else {
				return null;
			}
		}
		
		return null;
	}
	
	private boolean checkValidKey(String key){
		for (String keypadKey : keypadKeys) {
			if (key.equals(keypadKey)) return true;
		}
		return false;
	}
}



