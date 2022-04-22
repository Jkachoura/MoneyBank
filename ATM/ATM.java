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

    private String pinAmount;

    private int billAmountTen;
    private int bilAmountFifty;
    private int amountRounded;

    private String balance;

    public ATM() {
        sCon = new SerialConnection();
        agui = new ATMGUI();
        keypad = new Keypad(sCon);
    }

    public void run() {
        scanCard();
//        withdraw("E3F6AB18", 40);
    }

    private void scanCard() {
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

        // Check if card isn't blocked
        if (checkCardBlockStatus(UIDfull)) {
            blockedCard();
        }
        enterPincode(getPincodeDatabase(UIDfull), UIDfull);
    }

    private void enterPincode(String correctPin, String UID) {
        agui.enterPin.setText("Enter your pincode");
        agui.pincodePanel.add(agui.logoIcon);
        agui.pinMessage.setText("");
        agui.displayPanel("pincodePanel");

        attempts = 0;
        while (true) {
            Thread.yield();
            keypadInput = keypad.getInput();
            if (keypadInput != null) {
                if (!keypadInput.equals("A") && !keypadInput.equals("B") && !keypadInput.equals("C") && !keypadInput.equals("D") && !keypadInput.equals("*") && !keypadInput.equals("#")) {
                    agui.pinMessage.setText("");
                    pincode = keypadInput;
                    agui.pinMessage.setText("*");
                    while (true) {
                        keypadInput = keypad.getInput();
                        if (keypadInput != null) {
                            if (keypadInput.equals("D")) {
                                break;
                            } else if (keypadInput.equals("#") && pincode.length() != 0) {
                                pincode = pincode.substring(0, pincode.length() - 1);
                                agui.pinMessage.setText(agui.pinMessage.getText().substring(0, agui.pinMessage.getText().length() - 1));
                            } else if (!keypadInput.equals("A") && !keypadInput.equals("B") &&
                                    !keypadInput.equals("C") && !keypadInput.equals("*") &&
                                    !keypadInput.equals("#") && pincode.length() < 4) {
                                pincode = pincode + keypadInput;
                                agui.pinMessage.setText(agui.pinMessage.getText() + "*");
                            } else if (keypadInput.equals("*")) {
                                thanks();
                            }
                        }
                    }

                    if (pincode.equals(correctPin)) {
                        menu(UID);
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
                        scanCard();
                    }
                } else if (keypadInput.equals("*")) {
                    thanks();
                }
            }
        }
    }

    private void menu(String UID) {
        agui.displayPanel("menuPanel");
        agui.menuPanel.add(agui.logoIcon);

        //TODO menu GUI
        // snelpin opties laten zien (niet pas in withdraw menu)
        // User story: Als een gebruiker wil ik een gebruikersvriendelijke User Interface hebben, zodat ik gemakkelijk kan pinnen.

        while (true) {
            //Set up the variables and get the transactionID from the database
            Thread.yield();
            //Get keypad input
            keypadInput = keypad.getInput();
            if (keypadInput != null) {
                switch (keypadInput) {
                    //If D is pressed
                    case "B":
                        balance(UID);
                        break;
                    case "C":
                        withdrawMenu(UID);
                        break;
                    //If * is pressed
                    case "*":
                        thanks();
                        break;
                }
            }
        }
    }

    private void balance(String UID) {
        agui.yourBalance.setText("Your balance is: €" + checkBalance(UID));
        agui.displayPanel("BalancePanel");
        agui.BalancePanel.add(agui.logoIcon);
        while (true) {
            keypadInput = keypad.getInput();
            if (keypadInput != null) {
                switch (keypadInput) {
                    //If * is pressed don't print out a receipt and dispense the money
                    case "*":
                        menu(UID);
                        break;
                }
            }
        }
    }

    private void withdrawMenu(String UID) {
        if (checkIfDebt(UID)) {
            debtError();
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            menu(UID);
        }
        agui.displayPanel("withdrawMenuPanel");
        agui.withdrawMenuPanel.add(agui.logoIcon);
        while (true) {
            //Set up the variables and get the transactionID from the database
            Thread.yield();
            //Get keypad input
            keypadInput = keypad.getInput();
            if (keypadInput != null) {
                switch (keypadInput) {
                    //If A is pressed withdraw €20
                    case "A":
                        withdraw(UID, 20);
                        break;
                    //If B is pressed withdraw €50
                    case "B":
                        withdraw(UID, 50);
                        break;
                    //If C is pressed withdraw €100
                    case "C":
                        withdraw(UID, 100);
                        break;
                    //If D is pressed go to custom withdraw method
                    case "D":
                        withdrawCustomAmount(UID);
                        break;
                    //If * is pressed go back to menu
                    case "*":
                        menu(UID);
                        break;

                }
            }
        }
    }

    private void withdrawCustomAmount(String UID) {
        agui.displayPanel("withdrawPanel");
        agui.withdrawPanel.add(agui.logoIcon);
        agui.withdrawAmountCustom.setText("");
        Thread.yield();
        pinAmount = null;
        while (true) {
            keypadInput = keypad.getInput();
            if (keypadInput != null) {
                if (!keypadInput.equals("A") && !keypadInput.equals("B") && !keypadInput.equals("C") && !keypadInput.equals("D") && !keypadInput.equals("*") && !keypadInput.equals("#")) {
                    if (pinAmount == null) {
                        pinAmount = keypadInput;
                        agui.withdrawAmountCustom.setText("€" + pinAmount);
                    } else if (pinAmount.length() < 3) {
                        pinAmount = pinAmount + keypadInput;
                        agui.withdrawAmountCustom.setText(agui.withdrawAmountCustom.getText() + keypadInput);
                    }
                } else if (keypadInput.equals("D")) {
                    break;
                } else if (keypadInput.equals("*")) {
                    withdrawMenu(UID);
                } else if (keypadInput.equals("#") && pinAmount.length() != 0) {
                    pinAmount = pinAmount.substring(0, pinAmount.length() - 1);
                    agui.withdrawAmountCustom.setText(agui.withdrawAmountCustom.getText().substring(0, agui.withdrawAmountCustom.getText().length() - 1));
                }
            }
        }
        withdraw(UID, Integer.parseInt(pinAmount));
    }

    private void withdraw(String UID, int amount) {
        // Check of er genoeg saldo is
        if (!checkSufficientBalance(UID, amount)) {
            agui.displayPanel("withdrawInsufOptionsPanel");
            agui.withdrawInsufOptionsPanel.add(agui.logoIcon);
            while (true) {
                //niet genoeg saldo opties
                Thread.yield();
                keypadInput = keypad.getInput();
                if (keypadInput != null) {
                    switch (keypadInput) {
                        //If B is pressed check balance
                        case "B":
                            balance(UID);
                            break;
                        //If C is pressed enter custom amount
                        case "C":
                            withdrawCustomAmount(UID);
                            break;
                        //If * is abort transaction
                        case "*":
                            thanks();
                            break;

                    }
                }
            }
        }

        //bereken hoeveel biljetten nodig zijn
        if (amount >= 50) bilAmountFifty = amount / 50;
        billAmountTen = (amount - bilAmountFifty * 50) / 10;

        //TODO maximum pinbaar bedrag
        // (deze sprint)

        if (amount - (bilAmountFifty * 50) - (billAmountTen * 10) != 0 || amount == 0) {
            //ongeldig bedrag ingevoerd (geen tiental of 0)
            invalidAmount(UID, amount);
        } else {
            agui.displayPanel("printingPanel");
            agui.printingPanel.add(agui.logoIcon);
            agui.printingMoney.setText("Printing " + "€" + amount);
            editBalance(UID, amount);
            sCon.giveOutput("000000000000000000001" + billAmountTen + bilAmountFifty);
            try {
                Thread.sleep(6000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            receipt(UID, amount);
        }

        //TODO error scherm niet genoeg biljetten aanwezig (volgende sprint)
        // User Story: Als een gebruiker wil rekening houden met welke biljetten nog beschikbaar zijn, zodat ik kan weten hoeveel geld ik kan pinnen.
    }

    private void invalidAmount(String UID, int amount) {
        agui.displayPanel("withdrawProcessScreen");
        agui.withdrawProcessScreen.add(agui.logoIcon);
        agui.withdrawStatusMessage.setText("€" + amount + " not a valid amount.");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        if (amount == 0) {
            agui.displayPanel("withdrawProcessScreen");
            agui.withdrawProcessScreen.add(agui.logoIcon);
            agui.withdrawStatusMessage.setText("€" + amount + " not a valid amount.");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            withdrawCustomAmount(UID);
        } else if (amount < 10) {
            amountRounded = 10;
        } else {
            amountRounded = amount - (amount - (bilAmountFifty * 50) - (billAmountTen * 10));
        }
        agui.displayPanel("withdrawSuggestionPanel");
        agui.withdrawSuggestionPanel.add(agui.logoIcon);
        agui.withdrawSuggestionText2.setText("€" + amountRounded + " instead?");
        //ander bedrag suggestie opties scherm
        while (true) {
            Thread.yield();
            keypadInput = keypad.getInput();
            if (keypadInput != null) {
                switch (keypadInput) {
                    //If * is pressed go back to withdraw men
                    case "*":
                        withdrawMenu(UID);
                        break;
                    //If D is pressed withdraw the suggested amount
                    case "D":
                        withdraw(UID, amountRounded);
                        break;
                }
            }
        }
    }

    private void receipt(String UID, int amount) {
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
                        sCon.giveOutput(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mmdd/MM/yyyy")) +
                                getIBAN(UID).substring(14, 18) + "1" + "000" + amount);
                        thanks();
                        break;
                    //If * is pressed don't print out a receipt and dispense the money
                    case "*":
                        sCon.giveOutput("0");
                        thanks();
                        break;
                }
            }
        }
    }

    private void thanks() {
        agui.displayPanel("thanksPanel");
        agui.thanksPanel.add(agui.logoIcon);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        scanCard();
    }

    private void blockedCard() {
        agui.displayPanel("blockedCardPanel");
        agui.blockedCardPanel.add(agui.logoIcon);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        scanCard();
    }

    private void debtError() {
        //TODO debt scherm niet meer getimed maken maar een scherm met back/abort button
        // User story: Als een gebruiker wil ik een gebruikersvriendelijke User Interface hebben, zodat ik gemakkelijk kan pinnen.
        agui.displayPanel("debtErrorPanel");
        agui.debtErrorPanel.add(agui.logoIcon);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // --------------------------------------------------------------------------------------------------------
    // DATABASE SQL METHODS


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

    private void editBalance(String UID, int amount) {
        int newBalance = Integer.parseInt(checkBalance(UID)) - amount;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/moneybank", "root", "MNBK22");
            Statement stmt = con.createStatement();

            stmt.executeUpdate("UPDATE accounts  \n" +
                    "JOIN cards ON cards.CardID = accounts.CardID\n" +
                    "SET Balance = " + newBalance + " WHERE cards.uid = " + "'" + UID + "'");

            con.close();

        } catch (Exception e) {
            System.out.println(e);
        }
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

    private String checkBalance(String UID) {
        balance = null;
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

    private String getIBAN(String UID) {
        String IBAN = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/moneybank", "root", "MNBK22");
            Statement stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT IBAN FROM accounts\n" +
                    "JOIN cards ON cards.CardID = accounts.CardID\n" +
                    "WHERE cards.uid = " + "'" + UID + "'");
            while (rs.next()) {
                IBAN = rs.getString(1);
            }
            con.close();

        } catch (Exception e) {
            System.out.println(e);
        }
        return IBAN;
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
        assert blockStatus != null;
        return blockStatus.equals("1");
    }

    private boolean checkIfDebt(String UID) {
        if (Integer.parseInt(checkBalance(UID)) < 0) {
            return true;
        } else {
            return false;
        }
    }

    private boolean checkSufficientBalance(String UID, int withdrawAmount) {
        if (Integer.parseInt(checkBalance(UID)) >= withdrawAmount) {
            return true;
        } else {
            return false;
        }
    }
}
