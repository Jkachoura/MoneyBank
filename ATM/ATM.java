import java.sql.*;
import java.time.*;
import java.time.format.*;

class ATM extends Thread {
    private String keypadInput;
    private SerialConnection sCon;
    private Keypad keypad;
    private ATMGUI agui;

    private String inputKeypad;
    String inputUID;
    String UIDfull;

    private String pincode;
    private int attempts;

    public ATM() {
        sCon = new SerialConnection();
        agui = new ATMGUI();
        keypad = new Keypad(sCon);
    }

    public void run() {
        ScanCard();
    }

    private void ScanCard() {
        agui.displayPanel("scanCardPanel");
        agui.scanCardPanel.add(agui.logoIcon);

        while (true) {
            Thread.yield();

            if (sCon.getInput() != null) {
                inputUID = sCon.getInput();
                if (inputUID.charAt(0) == 'U') {
                    while (inputUID.length() <= 8) {
                        sCon.clearInput();
                        while (sCon.getInput() == null) {
                            Thread.yield();
                        }
                        inputUID = inputUID + sCon.getInput();
                    }
                    UIDfull = inputUID.substring(1, 9);
                    break;
                }
                sCon.clearInput();
            }
        }
//        System.out.println(UIDfull);

        // Check if card isn't blocked
        if (checkCardBlockStatus(UIDfull)) {
            blockedCard();
        }
        enterPincode(getPincodeDatabase(UIDfull), UIDfull);
    }

    private void enterPincode(String correctPin, String UID) {
        agui.enterPin.setText("Enter your pincode");
        agui.PincodePanel.add(agui.logoIcon);
        agui.pinMessage.setText("");
        agui.displayPanel("PincodePanel");
        attempts = 0;
        while (true) {
            Thread.yield();

            keypadInput = keypad.getInput();
            if (keypadInput != null) {
                if (!keypadInput.equals("*") && !keypadInput.equals("#")) {
                    agui.pinMessage.setText("");
                    pincode = keypadInput;
                    agui.pinMessage.setText("*");
                    while (pincode.length() < 4) {
                        keypadInput = keypad.getInput();
                        if (keypadInput != null) {
                            pincode = pincode + keypadInput;
                            agui.pinMessage.setText(agui.pinMessage.getText() + "*");
                        }
                    }

                    if (pincode.equals(correctPin)) {
                        agui.enterPin.setText("Welcome to Money Bank!");
                        agui.pinMessage.setText(" ");
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        balance(UID);
                    } else if (attempts != 2) {
                        agui.enterPin.setText("Wrong pincode. Try again");
                        attempts++;
                        agui.pinMessage.setText(3 - attempts + " attempts left");
                    } else {
                        agui.enterPin.setText("Too many failed attempts");
                        agui.pinMessage.setText("Your card is blocked");
                        blockCard(UID);
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        ScanCard();
                    }
                }
            }
        }
    }

    private void balance(String UID) {
        agui.yourBalance.setText("Your balance is: " + checkBalance(UID));
        agui.displayPanel("BalancePanel");
        agui.BalancePanel.add(agui.logoIcon);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        receipt();
//        ScanCard();
    }

    private void receipt() {
        agui.displayPanel("receiptPanel");
        agui.receiptPanel.add(agui.logoIcon);
        while (true) {
            //Set up the variables and get the transactionID from the database
            Thread.yield();
            //Get keypad input
            keypadInput = keypad.getInput();
            if (keypadInput != null) {
                switch (keypadInput) {
                    //If D is pressed print out a receipt and dispense the money
                    case "D":
                        sCon.giveOutput(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mmdd/MM/yyyy")) + "1");
                        Thanks();
                        break;
                    //If * is pressed don't print out a receipt and dispense the money
                    case "*":
                        sCon.giveOutput("0");
                        Thanks();
                        break;
                }
            }
        }
    }

    private void Thanks() {
        agui.displayPanel("thanksPanel");
        agui.thanksPanel.add(agui.logoIcon);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ScanCard();
    }

    private void blockedCard() {
        agui.displayPanel("blockedCardPanel");
        agui.blockedCardPanel.add(agui.logoIcon);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ScanCard();
    }

    private String getPincodeDatabase(String UID) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/moneybank", "root", "MNBK22");
            Statement stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT PinCode FROM cards WHERE uid = '" + UID + "'");
            String pincode = null;
            while (rs.next()) {
                pincode = rs.getString(1);
            }
            con.close();
            return pincode;

        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    private void blockCard(String UID) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/moneybank", "root", "MNBK22");
            Statement stmt = con.createStatement();

            stmt.executeUpdate("UPDATE cards SET BlockStatus = 1 WHERE uid = '" + UID + "'");
            con.close();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private Boolean checkCardBlockStatus(String UID) {
        String blockStatus = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/moneybank", "root", "MNBK22");
            Statement stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT BlockStatus FROM cards WHERE uid = '" + UID + "'");
            while (rs.next()) {
                blockStatus = rs.getString(1);
            }
            con.close();

        } catch (Exception e) {
            System.out.println(e);
        }
        if (blockStatus.equals("1")) {
            return true;
        } else {
            return false;
        }
    }

    private String checkBalance(String UID) {
        String balance = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/moneybank", "root", "MNBK22");
            Statement stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT Balance FROM accounts\n" +
                    "JOIN cards ON cards.CardID = accounts.CardID\n" +
                    "WHERE cards.uid = " + "'" + UID + "'");
            while (rs.next()) {
                balance = rs.getString(1);
            }
            con.close();

        } catch (Exception e) {
            System.out.println(e);
        }
        return balance;
    }
}
