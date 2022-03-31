//package ATMreceiptest;
class ATM extends Thread{
    private String choice;
    private SerialInterface serialInterface;
    private Keypad keypad;
    private ATMScreen as;

    public ATM(){
        serialInterface = new SerialInterface("COM7");
        as = new ATMScreen();
        keypad = new Keypad(serialInterface);
    }

    public void run(){
        Receipt();
    }

    private void Receipt() {
        as.displayPanel("Receipt");
		while(true){
            //Set up the variables and get the transactionID from the database
    	    Thread.yield();
    	    //Get keypad input
    	    choice = keypad.getInput();
		    if (choice != null) {
			    switch(choice) {
			    //If D is pressed print out a receipt and dispense the money
			    case "D":
				    serialInterface.giveOutput("1");
				    Thanks();
			    break;
			    //If * is pressed don't print out a receipt and dispense the money
			    case "*":
				    serialInterface.giveOutput("0");
				    Thanks();
			    break;
			    }   
            }
		}
    }

    private void Thanks() {
    	as.displayPanel("Thanks");
    	try{
            Thread.sleep(7000);
        }
        catch(InterruptedException e){
            e.printStackTrace();
        }
    	
        Receipt();
    }
}
