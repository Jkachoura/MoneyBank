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

    private String availableBills;
    private int availableTens;
    private int availableFifties;

    private int printingSleep;

    public ATM() {
        sCon = new SerialConnection();
        agui = new ATMGUI();
        keypad = new Keypad(sCon);
    }

    public void run() {
        scanCard();
//        menu("E3F6AB18");
//        withdrawMenu("E3F6AB18");
//        receipt("E3F6AB18", 20);
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

        while (true) {
            //Set up the variables and get the transactionID from the database
            Thread.yield();
            //Get keypad input
            keypadInput = keypad.getInput();
            if (keypadInput != null) {
                switch (keypadInput) {
                    //If A is pressed
                    case "A":
                        if (checkIfDebt(UID)) {
                            debtError(UID);
                            menu(UID);
                        } else {
                            withdrawConfirm(UID, 70);
                        }
                        break;
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
            debtError(UID);
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
                        withdrawConfirm(UID, 20);
                        break;
                    //If B is pressed withdraw €50
                    case "B":
                        withdrawConfirm(UID, 50);
                        break;
                    //If C is pressed withdraw €100
                    case "C":
                        withdrawConfirm(UID, 100);
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

    private void withdrawConfirm(String UID, int amount) {
        agui.displayPanel("withdrawConfirmPanel");
        agui.withdrawConfirmPanel.add(agui.logoIcon);
        agui.withdrawConfirmMessage.setText("Are you sure you want to withdraw €" + amount + "?");
        while (true) {
            Thread.yield();
            //Get keypad input
            keypadInput = keypad.getInput();
            if (keypadInput != null) {
                switch (keypadInput) {
                    //If D is pressed withdraw the amount
                    case "D":
                        withdraw(UID, amount);
                        break;
                    //If * is pressed don't withdraw the amount
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
        //TODO biljetkeuze-scherm toevoegen
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
        if (amount >= 50) {
            bilAmountFifty = amount / 50;
        } else {
            bilAmountFifty = 0;
        }
        billAmountTen = (amount - bilAmountFifty * 50) / 10;

        if (amount > 300) {
            exceedsLimit(UID, amount);
        }
        if (amount - (bilAmountFifty * 50) - (billAmountTen * 10) != 0 || amount == 0) {
            //ongeldig bedrag ingevoerd (geen tiental of 0)
            invalidAmount(UID, amount);
        }
        sCon.giveOutput("000000000000000000001" + billAmountTen + bilAmountFifty);
        readBillsAvailable();
        System.out.println("receivedBillsAvailable : " + availableBills);
//        System.out.println("received : " + printPossible);

        availableTens = Integer.parseInt(availableBills.substring(1, 3));
        System.out.println("Available tens: " + availableTens);
        availableFifties = Integer.parseInt(availableBills.substring(3, 5));
        System.out.println("Available fifites: " + availableFifties);

        if (billAmountTen > availableTens || bilAmountFifty > availableFifties) {
            if (amount <= 90 && bilAmountFifty > availableFifties && availableTens > 0) {
                if ((amount - availableFifties * 50) / 10 <= availableTens) {
                    sCon.giveOutput("000000000000000000001" + (amount - availableFifties * 50) / 10 + availableFifties);
                    printing(UID, amount);
                }
            }
            agui.displayPanel("insufBillsPanel");
            agui.insufBillsPanel.add(agui.logoIcon);
            if (billAmountTen > availableTens && bilAmountFifty > availableFifties) {
                agui.insufBills2.setText("Not enough €10 & €50 bills");
            } else if (billAmountTen > availableTens) {
                agui.insufBills2.setText("Not enough €10 bills");
            } else if (bilAmountFifty > availableFifties) {
                agui.insufBills2.setText("Not enough €50 bills");
            }
            //TODO print integers als 2 cijfers sturen zodat er meer dan 10 biljetten geprint kunnen worden

            while (true) {
                //niet genoeg saldo opties
                Thread.yield();
                keypadInput = keypad.getInput();
                if (keypadInput != null) {
                    switch (keypadInput) {
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
        } else {
            printing(UID, amount);
        }
    }

    private void invalidAmount(String UID, int amount) {
        agui.displayPanel("withdrawProcessScreen");
        agui.withdrawProcessScreen.add(agui.logoIcon);

        if (amount == 0) {
            agui.displayPanel("withdrawProcessScreen");
            agui.withdrawProcessScreen.add(agui.logoIcon);
            agui.withdrawStatusMessage.setText("€" + amount + " not a valid amount.");

            while (true) {
                //niet genoeg saldo opties
                Thread.yield();
                keypadInput = keypad.getInput();
                if (keypadInput != null) {
                    //If * is pressed go back to withdraw menu
                    if ("*".equals(keypadInput)) {
                        withdrawCustomAmount(UID);
                    }
                }
            }
        } else if (amount < 10) {
            amountRounded = 10;
        } else {
            amountRounded = amount - (amount - (bilAmountFifty * 50) - (billAmountTen * 10));
        }
        agui.displayPanel("withdrawSuggestionPanel");
        agui.withdrawSuggestionPanel.add(agui.logoIcon);
        agui.withdrawError.setText("€" + amount + " not a valid amount.");
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

    private void exceedsLimit(String UID, int amount) {
        agui.displayPanel("withdrawLimitPanel");
        agui.withdrawLimitPanel.add(agui.logoIcon);
        agui.withdrawLimitText.setText("€" + amount + " exceeds limit.");

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
                        withdraw(UID, 300);
                        break;
                }
            }
        }
    }

    private void printing(String UID, int amount) {
        agui.displayPanel("printingPanel");
        agui.printingPanel.add(agui.logoIcon);
        agui.printingMoney.setText("Printing " + "€" + amount);
        editBalance(UID, amount);
        printingSleep = (billAmountTen + bilAmountFifty) * 1200;
        try {
            Thread.sleep(printingSleep);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        receipt(UID, amount);
    }

    private void receipt(String UID, int amount) {
        //TODO arduino code witruimte toevoegen aan bonlayout (feed lines)
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
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        scanCard();
    }

    private void debtError(String UID) {
        agui.displayPanel("debtErrorPanel");
        agui.debtErrorPanel.add(agui.logoIcon);
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
                    //If * is abort transaction
                    case "*":
                        thanks();
                        break;
                }
            }
        }
    }

    private void readBillsAvailable() {
        while (true) {
            Thread.yield();
            if (sCon.getInput() != null) {
                availableBills = sCon.getInput();
                if (availableBills.charAt(0) == 'B') {
                    while (availableBills.length() < 6) {
                        sCon.clearInput();
                        while (sCon.getInput() == null) {
                            Thread.yield();
                        }
                        availableBills = availableBills + sCon.getInput();
                    }
                    availableBills = availableBills.substring(0, 6);
                    break;
                }
                sCon.clearInput();
            }
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
