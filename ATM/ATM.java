import java.sql.*;
import java.time.*;
import java.time.format.*;

class ATM extends Thread {
    private String keypadInput;
    private SerialConnection sCon;
    private Keypad keypad;
    private ATMGUI agui;

    String inputIBAN;
    String IBANfull;

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
//        enterPincode("GL03MNBK12345670");
//        withdrawConfirm("GL03MNBK12345678", 70);
//        receipt("GL03MNBK12345678", 20);
//        balance("GL03MNBK12345678", 5678);
//        getPincodeDatabase("GL03MNBK12345678");
//        printing("GL03MNBK12345678", 100, 5678);
    }

    private void scanCard() {
        agui.displayPanel("scanCardPanel");
        agui.scanCardPanel.add(agui.logoIcon);

        while (true) {
            Thread.yield();

            if (sCon.getInput() != null) {
                inputIBAN = sCon.getInput();
                if (inputIBAN.charAt(0) == 'I') {
                    while (inputIBAN.length() <= 16) {
                        sCon.clearInput();
                        while (sCon.getInput() == null) {
                            Thread.yield();
                        }
                        inputIBAN = inputIBAN + sCon.getInput();
                    }
                    IBANfull = inputIBAN.substring(1, 17);
                    break;
                }
                sCon.clearInput();
            }
        }
        if (IBANfull.charAt(0) == 0x00) {
            //No IBAN found on card (block 1)
            scanCard();
        }
        enterPincode(IBANfull);
    }

    private void enterPincode(String IBAN) {
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
                    String checkPincode = Post_JSON.getBalance(IBAN, Integer.parseInt(pincode));
                    if (checkPincode == null) {
                        agui.enterPin.setText("Something went wrong.");
                        agui.pinMessage.setText("Try again later.");
                        try {
                            Thread.sleep(4000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    if (checkPincode.equals("Account does not exist")) {
                        agui.enterPin.setText("Account does not exist");
                        agui.pinMessage.setText("Aborting transaction...");
                        try {
                            Thread.sleep(4000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        scanCard();
                    } else if (checkPincode.equals("Blocked")) {
                        agui.enterPin.setText("Too many failed attempts");
                        agui.pinMessage.setText("Your card is blocked");
                        blockedCard();
                        try {
                            Thread.sleep(4000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else if (checkPincode.equals("Wrong body")) {
                        agui.enterPin.setText("Something went wrong.");
                        agui.pinMessage.setText("Try again later");
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        scanCard();
                    } else if (checkPincode.substring(0, 6).equals("Succes")) {
                        menu(IBAN, Integer.parseInt(pincode));
                    } else if (checkPincode.substring(0, 9).equals("Incorrect")) {
                        agui.enterPin.setText("Wrong pincode. Try again");
                        attempts = Integer.parseInt(checkPincode.substring(9, 10));
                        agui.pinMessage.setText(attempts + " attempts left");
                    }
                } else if (keypadInput.equals("*")) {
                    thanks();
                }
            }
        }
    }

    private void menu(String IBAN, int pinCode) {
        agui.displayPanel("menuPanel");
        agui.menuPanel.add(agui.logoIcon);

        while (true) {
            Thread.yield();
            //Get keypad input
            keypadInput = keypad.getInput();
            if (keypadInput != null) {
                switch (keypadInput) {
                    //If A is pressed withdraw 70
                    case "A":
                        if (checkIfDebt(IBAN, pinCode)) {
                            debtError(IBAN, pinCode);
                            menu(IBAN, pinCode);
                        } else {
                            withdrawConfirm(IBAN, 70, pinCode);
                        }
                        break;
                    //If B is pressed check balance
                    case "B":
                        balance(IBAN, pinCode);
                        break;
                    //If C is pressed go to withdraw menu
                    case "C":
                        withdrawMenu(IBAN, pinCode);
                        break;
                    //If * is pressed abort
                    case "*":
                        thanks();
                        break;
                }
            }
        }
    }

    private void balance(String IBAN, int pinCode) {
        agui.yourBalance.setText("Your balance is: €" + checkBalance(IBAN, pinCode));
        agui.displayPanel("balancePanel");
        agui.balancePanel.add(agui.logoIcon);
        while (true) {
            keypadInput = keypad.getInput();
            if (keypadInput != null) {
                switch (keypadInput) {
                    //If * is pressed go back to menu
                    case "*":
                        menu(IBAN, pinCode);
                        break;
                }
            }
        }
    }

    private void withdrawMenu(String IBAN, int pinCode) {
        if (checkIfDebt(IBAN, pinCode)) {
            debtError(IBAN, pinCode);
            menu(IBAN, pinCode);
        }
        agui.displayPanel("withdrawMenuPanel");
        agui.withdrawMenuPanel.add(agui.logoIcon);
        while (true) {
            Thread.yield();
            //Get keypad input
            keypadInput = keypad.getInput();
            if (keypadInput != null) {
                switch (keypadInput) {
                    //If A is pressed withdraw €20
                    case "A":
                        withdrawConfirm(IBAN, 20, pinCode);
                        break;
                    //If B is pressed withdraw €50
                    case "B":
                        withdrawConfirm(IBAN, 50, pinCode);
                        break;
                    //If C is pressed withdraw €100
                    case "C":
                        withdrawConfirm(IBAN, 100, pinCode);
                        break;
                    //If D is pressed go to withdraw custom amount screen
                    case "D":
                        withdrawCustomAmount(IBAN, pinCode);
                        break;
                    //If * is pressed go back to menu
                    case "*":
                        menu(IBAN, pinCode);
                        break;

                }
            }
        }
    }

    private void withdrawConfirm(String IBAN, int amount, int pinCode) {
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
                        withdraw(IBAN, amount, pinCode);
                        break;
                    //If * is pressed don't withdraw the amount and go back to menu
                    case "*":
                        menu(IBAN, pinCode);
                        break;
                }
            }
        }
    }

    private void withdrawCustomAmount(String IBAN, int pinCode) {
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
                    withdrawMenu(IBAN, pinCode);
                } else if (keypadInput.equals("#") && pinAmount.length() != 0) {
                    pinAmount = pinAmount.substring(0, pinAmount.length() - 1);
                    agui.withdrawAmountCustom.setText(agui.withdrawAmountCustom.getText().substring(0, agui.withdrawAmountCustom.getText().length() - 1));
                }
            }
        }
        withdraw(IBAN, Integer.parseInt(pinAmount), pinCode);
    }

    private void withdraw(String IBAN, int amount, int pinCode) {
        // Check of er genoeg saldo is
        if (!checkSufficientBalance(IBAN, amount, pinCode)) {
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
                            balance(IBAN, pinCode);
                            break;
                        //If C is pressed enter custom amount
                        case "C":
                            withdrawCustomAmount(IBAN, pinCode);
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
            exceedsLimit(IBAN, amount, pinCode);
        }
        if (amount - (bilAmountFifty * 50) - (billAmountTen * 10) != 0 || amount == 0) {
            //ongeldig bedrag ingevoerd (geen tiental of 0)
            invalidAmount(IBAN, amount, pinCode);
        }
        sCon.giveOutput("000000000000000000001" + billAmountTen + bilAmountFifty);
        readBillsAvailable();
        System.out.println("receivedBillsAvailable : " + availableBills);

        availableTens = Integer.parseInt(availableBills.substring(1, 3));
        System.out.println("Available tens: " + availableTens);
        availableFifties = Integer.parseInt(availableBills.substring(3, 5));
        System.out.println("Available fifites: " + availableFifties);

        if (billAmountTen > availableTens || bilAmountFifty > availableFifties) {
            if (amount <= 90 && bilAmountFifty > availableFifties && availableTens > 0) {
                if ((amount - availableFifties * 50) / 10 <= availableTens) {
                    sCon.giveOutput("000000000000000000001" + (amount - availableFifties * 50) / 10 + availableFifties);
                    printing(IBAN, amount, pinCode);
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

            while (true) {
                //niet genoeg saldo opties
                Thread.yield();
                keypadInput = keypad.getInput();
                if (keypadInput != null) {
                    switch (keypadInput) {
                        //If C is pressed enter custom amount
                        case "C":
                            withdrawCustomAmount(IBAN, pinCode);
                            break;
                        //If * is abort transaction
                        case "*":
                            thanks();
                            break;

                    }
                }
            }
        } else {
            printing(IBAN, amount, pinCode);
        }
    }

    private void invalidAmount(String IBAN, int amount, int pinCode) {
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
                        withdrawCustomAmount(IBAN, pinCode);
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
                        withdrawMenu(IBAN, pinCode);
                        break;
                    //If D is pressed withdraw the suggested amount
                    case "D":
                        withdraw(IBAN, amountRounded, pinCode);
                        break;
                }
            }
        }
    }

    private void exceedsLimit(String IBAN, int amount, int pinCode) {
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
                        withdrawMenu(IBAN, pinCode);
                        break;
                    //If D is pressed withdraw the suggested amount
                    case "D":
                        withdraw(IBAN, 300, pinCode);
                        break;
                }
            }
        }
    }

    private void printing(String IBAN, int amount, int pinCode) {
        agui.displayPanel("printingPanel");
        agui.printingPanel.add(agui.logoIcon);
        agui.printingMoney.setText("");
        agui.printingMessage.setText("");
        String withdrawOutput = editBalance(IBAN, amount, pinCode);
        if (withdrawOutput == null) {
            agui.printingMoney.setText("Something went wrong.");
            agui.printingMessage.setText("Try again later.");
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            scanCard();
        } else if (withdrawOutput.equals("Account does not exist")) {
            agui.printingMoney.setText("Account does not exist");
            agui.printingMessage.setText("Aborting transaction...");
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            scanCard();
        } else if (withdrawOutput.equals("Blocked")) {
            agui.printingMoney.setText("This card is blocked");
            agui.printingMessage.setText("Contact the helpdesk.");
            blockedCard();
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else if (withdrawOutput.equals("Wrong body")) {
            agui.printingMoney.setText("Something went wrong.");
            agui.printingMessage.setText("Try again later");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            scanCard();
        } else if (withdrawOutput.substring(0, 6).equals("Succes")) {
            agui.printingMoney.setText("Printing " + "€" + amount);
        } else if (withdrawOutput.substring(0, 9).equals("Incorrect")) {
            agui.printingMoney.setText("Something went wrong.");
            agui.printingMessage.setText("Try again later");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            scanCard();
        }
        printingSleep = (billAmountTen + bilAmountFifty) * 1200;
        try {
            Thread.sleep(printingSleep);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        receipt(IBAN, amount);
    }

    private void receipt(String IBAN, int amount) {
        agui.displayPanel("receiptPanel");
        agui.receiptPanel.add(agui.logoIcon);
        while (true) {
            Thread.yield();
            //Get keypad input
            keypadInput = keypad.getInput();
            if (keypadInput != null) {
                switch (keypadInput) {
                    //If D is pressed print out a receipt and dispense the money
                    case "D":
                        sCon.giveOutput(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mmdd/MM/yyyy")) +
                                IBAN.substring(12, 16) + "1" + "000" + amount);
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

    private void debtError(String IBAN, int pinCode) {
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
                        balance(IBAN, pinCode);
                        break;
                    //If * is pressed abort transaction
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


//    private void blockCard(String IBAN) {
//        try {
//            Post_JSON.blockCard(IBAN);
//        } catch (Exception e) {
//            System.out.println(e);
//        }
//    }

    private String editBalance(String IBAN, int amount, int pinCode) {
        try {
            return Post_JSON.withdraw(IBAN, amount, pinCode);
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

//    private String getPincodeDatabase(String IBAN) {
//        try {
//            String pinCode = null;
//            pincode = Post_JSON.pinCheck(IBAN);
//            return pincode;
//        } catch (Exception e) {
//            System.out.println(e);
//        }
//        return null;
//    }

    private String checkBalance(String IBAN, int pinCode) {
        try {
            balance = null;
            balance = Post_JSON.getBalance(IBAN, pinCode);
            if (balance == null) {
                return null;
            } else {
                balance = balance.substring(6, balance.length() - 1);
                return balance;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

//    private Boolean checkCardBlockStatus(String IBAN) {
//        String blockStatus = null;
//        try {
//            blockStatus = Post_JSON.checkBlockStatus(IBAN);
//            return blockStatus.equals("1");
//        } catch (Exception e) {
//            System.out.println(e);
//        }
//        return null;
//    }

    private boolean checkIfDebt(String IBAN, int pinCode) {
        if (Integer.parseInt(checkBalance(IBAN, pinCode)) < 0) {
            return true;
        } else {
            return false;
        }
    }

    private boolean checkSufficientBalance(String IBAN, int withdrawAmount, int pinCode) {
        if (Integer.parseInt(checkBalance(IBAN, pinCode)) >= withdrawAmount) {
            return true;
        } else {
            return false;
        }
    }
}
