import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;


public class ATMGUI extends JFrame {
    private JPanel screenPanel = new JPanel();
    private JPanel Windows = new JPanel();
    private CardLayout cardLayout = new CardLayout(0, 0);

    //Card Screen
    public JPanel scanCardPanel;
    private JLabel ScanCardTo = new JLabel("Scan your card to begin");

    public JLabel dLabelPin;
    public JButton btnYesPin;
    public JLabel starLabelPin;

    //Pincode Screen
    public JPanel pincodePanel;
    public JLabel enterPin = new JLabel("Enter your pincode");
    public JLabel pinMessage = new JLabel("");
    public JButton btnAbortPin;
    public JButton btnDeletePin;
    public JLabel hashLabelPin;

    //Menu screen
    public JPanel menuPanel;
    public JLabel welcomeMenu = new JLabel("Welcome to Money Bank!");

    //Balance screen
    public JPanel BalancePanel;
    public JLabel yourBalance = new JLabel("Your Balance is ");

    //Withdraw menu screen
    public JPanel withdrawMenuPanel;

    //Withdraw okay/ error screen
    public JPanel withdrawProcessScreen;
    public JLabel withdrawError = new JLabel("");
    public JLabel withdrawStatusMessage = new JLabel("");

    //Withdraw custom amount screen
    public JPanel withdrawPanel;
    public JLabel withdrawMessage = new JLabel("Enter withdraw amount: ");
    public JLabel withdrawAmountCustom = new JLabel("");

    //Withdraw Insufficient Balance Options screen
    public JPanel withdrawInsufOptionsPanel;
    private JLabel withdrawInsufBal = new JLabel("Insufficient balance.");

    //Withdraw suggestion screen
    public JPanel withdrawSuggestionPanel;
    public JLabel withdrawSuggestionText = new JLabel("Would you like to withdraw");
    public JLabel withdrawSuggestionText2;

    //printing money Screen
    public JPanel printingPanel;
    public JLabel printingMoney = new JLabel("");

    //Receipt screen
    public JPanel receiptPanel;

    //Thanks Screen
    public JPanel thanksPanel;
    private JLabel ThanksFor = new JLabel("Thank you for choosing Money Bank!");
    private JLabel lblNiceday = new JLabel("Have a nice day!");

    //Blocked card screen
    public JPanel blockedCardPanel;
    private JLabel thisCardBlocked = new JLabel("This card is blocked");

    //Debt error screen
    public JPanel debtErrorPanel;
    private JLabel debtErrorMessage = new JLabel("Account is in debt.");
    private JLabel debtErrorMessage2 = new JLabel("Unable to withdraw money.");

    // logo test
    private ImageIcon logoImage;
    private ImageIcon logoImageIcon;
    public JLabel logo;
    public JLabel logoIcon;

    public ATMGUI() {
        setContentPane(screenPanel);
        setResizable(false);
        setAlwaysOnTop(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1383, 864);
        setUndecorated(true);
        setCursor(getToolkit().createCustomCursor(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB), new Point(), null));
        getContentPane().setLayout(null);

        Windows.setBounds(0, 0, 1382, 864);
        getContentPane().add(Windows);
        Windows.setLayout(cardLayout);

        // background: "#15202B"
        // card: "#192734"
        // hover color: "#22303C"
        // primary text: "#FFFFFF"
        // secondary text: "#8899A6"
        // delete button: #FFA000


        //logo test
        try {
            logoImage = new ImageIcon(getClass().getResource("logo transparent 1000x500.png"));
            logo = new JLabel(logoImage);
            logoImageIcon = new ImageIcon(getClass().getResource("logo icon topleftcorner.png"));
            logoIcon = new JLabel(logoImageIcon);
        } catch (Exception e) {
            System.out.println("Image cannot be found");
        }


        //ScanCard screen
        scanCardPanel = new JPanel();
        scanCardPanel.setLayout(null);
        scanCardPanel.setBackground(Color.decode("#15202B"));
        Windows.add(scanCardPanel, "scanCardPanel");

        ScanCardTo.setHorizontalAlignment(JLabel.CENTER);
        ScanCardTo.setVerticalAlignment(JLabel.CENTER);
        ScanCardTo.setFont(new Font("Circular Std Bold", Font.PLAIN, 100));
        ScanCardTo.setForeground(Color.decode("#8899A6"));
        ScanCardTo.setBounds(0, 300, 1382, 864);
        scanCardPanel.add(ScanCardTo);

        logo.setHorizontalAlignment(JLabel.CENTER);
        logo.setVerticalAlignment(JLabel.TOP);
        logo.setBounds(0, 0, 1382, 864);
        scanCardPanel.add(logo);

        //Pincode screen
        pincodePanel = new JPanel();
        pincodePanel.setLayout(null);
        pincodePanel.setBackground(Color.decode("#15202B"));
        Windows.add(pincodePanel, "pincodePanel");

        enterPin.setHorizontalAlignment(JLabel.CENTER);
        enterPin.setVerticalAlignment(JLabel.CENTER);
        enterPin.setFont(new Font("Circular Std Bold", Font.PLAIN, 100));
        enterPin.setForeground(Color.decode("#FFFFFF"));
        enterPin.setBounds(0, 0, 1382, 864);
        pincodePanel.add(enterPin);

        pinMessage.setHorizontalAlignment(JLabel.CENTER);
        pinMessage.setVerticalAlignment(JLabel.CENTER);
        pinMessage.setFont(new Font("Circular Std Bold", Font.PLAIN, 100));
        pinMessage.setForeground(Color.decode("#8899A6"));
        pinMessage.setBounds(0, 200, 1382, 864);
        pincodePanel.add(pinMessage);

        dLabelPin = new JLabel("D", SwingConstants.CENTER);
        dLabelPin.setOpaque(true);
        dLabelPin.setForeground(Color.decode("#FFFFFF"));
        dLabelPin.setFont(new Font("Circular Std Bold", Font.PLAIN, 40));
        dLabelPin.setBackground(Color.decode("#22303C"));
        dLabelPin.setBounds(1250, 700, 50, 75);
        pincodePanel.add(dLabelPin);

        btnYesPin = new JButton("Confirm");
        btnYesPin.setForeground(Color.WHITE);
        btnYesPin.setFont(new Font("Circular Std Bold", Font.PLAIN, 25));
        btnYesPin.setFocusPainted(false);
        btnYesPin.setBackground(Color.decode("#018786"));
        btnYesPin.setBounds(1100, 700, 150, 75);
        pincodePanel.add(btnYesPin);

        starLabelPin = new JLabel("*", SwingConstants.CENTER);
        starLabelPin.setVerticalAlignment(SwingConstants.BOTTOM);
        starLabelPin.setOpaque(true);
        starLabelPin.setForeground(Color.decode("#FFFFFF"));
        starLabelPin.setFont(new Font("Circular Std Bold", Font.PLAIN, 50));
        starLabelPin.setBackground(Color.decode("#22303C"));
        starLabelPin.setBounds(75, 700, 50, 75);
        pincodePanel.add(starLabelPin);

        btnAbortPin = new JButton("Abort");
        btnAbortPin.setForeground(Color.WHITE);
        btnAbortPin.setFont(new Font("Circular Std Bold", Font.PLAIN, 25));
        btnAbortPin.setFocusPainted(false);
        btnAbortPin.setBackground(Color.decode("#B00020"));
        btnAbortPin.setBounds(125, 700, 150, 75);
        pincodePanel.add(btnAbortPin);

        hashLabelPin = new JLabel("#", SwingConstants.CENTER);
        hashLabelPin.setOpaque(true);
        hashLabelPin.setForeground(Color.decode("#FFFFFF"));
        hashLabelPin.setFont(new Font("Circular Std Bold", Font.PLAIN, 40));
        hashLabelPin.setBackground(Color.decode("#22303C"));
        hashLabelPin.setBounds(850, 700, 50, 75);
        pincodePanel.add(hashLabelPin);

        btnDeletePin = new JButton("Delete");
        btnDeletePin.setForeground(Color.WHITE);
        btnDeletePin.setFont(new Font("Circular Std Bold", Font.PLAIN, 25));
        btnDeletePin.setFocusPainted(false);
        btnDeletePin.setBackground(Color.decode("#FFA000"));
        btnDeletePin.setBounds(900, 700, 150, 75);
        pincodePanel.add(btnDeletePin);

        logoIcon.setHorizontalAlignment(JLabel.CENTER);
        logoIcon.setVerticalAlignment(JLabel.TOP);
        logoIcon.setBounds(0, 0, 1382, 864);


        //Menu screen
        menuPanel = new JPanel();
        menuPanel.setLayout(null);
        menuPanel.setBackground(Color.decode("#15202B"));
        Windows.add(menuPanel, "menuPanel");

        welcomeMenu.setHorizontalAlignment(JLabel.CENTER);
        welcomeMenu.setVerticalAlignment(JLabel.CENTER);
        welcomeMenu.setFont(new Font("Circular Std Bold", Font.PLAIN, 100));
        welcomeMenu.setForeground(Color.decode("#FFFFFF"));
        welcomeMenu.setBounds(0, -200, 1382, 864);
        menuPanel.add(welcomeMenu);

        JLabel aLabelMenu = new JLabel("A", SwingConstants.CENTER);
        aLabelMenu.setOpaque(true);
        aLabelMenu.setForeground(Color.decode("#FFFFFF"));
        aLabelMenu.setFont(new Font("Circular Std Bold", Font.PLAIN, 40));
        aLabelMenu.setBackground(Color.decode("#22303C"));
        aLabelMenu.setBounds(741, 364, 50, 75);
        menuPanel.add(aLabelMenu);

        JLabel bLabelMenu = new JLabel("B", SwingConstants.CENTER);
        bLabelMenu.setOpaque(true);
        bLabelMenu.setForeground(Color.decode("#FFFFFF"));
        bLabelMenu.setFont(new Font("Circular Std Bold", Font.PLAIN, 40));
        bLabelMenu.setBackground(Color.decode("#22303C"));
        bLabelMenu.setBounds(741, 469, 50, 75);
        menuPanel.add(bLabelMenu);

        JLabel cLabelMenu = new JLabel("C", SwingConstants.CENTER);
        cLabelMenu.setVerticalAlignment(SwingConstants.BOTTOM);
        cLabelMenu.setOpaque(true);
        cLabelMenu.setForeground(Color.decode("#FFFFFF"));
        cLabelMenu.setFont(new Font("Circular Std Bold", Font.PLAIN, 50));
        cLabelMenu.setBackground(Color.decode("#22303C"));
        cLabelMenu.setBounds(741, 574, 50, 75);
        menuPanel.add(cLabelMenu);

        JButton btn70W = new JButton("Pin €70");
        btn70W.setForeground(Color.WHITE);
        btn70W.setFont(new Font("Circular Std Bold", Font.PLAIN, 25));
        btn70W.setFocusPainted(false);
        btn70W.setBackground(Color.decode("#22303C"));
        btn70W.setBounds(591, 364, 150, 75);
        menuPanel.add(btn70W);

        JButton btnBalanceMenu = new JButton("Balance");
        btnBalanceMenu.setForeground(Color.WHITE);
        btnBalanceMenu.setFont(new Font("Circular Std Bold", Font.PLAIN, 25));
        btnBalanceMenu.setFocusPainted(false);
        btnBalanceMenu.setBackground(Color.decode("#22303C"));
        btnBalanceMenu.setBounds(591, 469, 150, 75);
        menuPanel.add(btnBalanceMenu);

        JButton btnWithdrawMenu = new JButton("Withdraw");
        btnWithdrawMenu.setForeground(Color.WHITE);
        btnWithdrawMenu.setFont(new Font("Circular Std Bold", Font.PLAIN, 25));
        btnWithdrawMenu.setFocusPainted(false);
        btnWithdrawMenu.setBackground(Color.decode("#22303C"));
        btnWithdrawMenu.setBounds(591, 574, 150, 75);
        menuPanel.add(btnWithdrawMenu);

        JLabel starLabelMenu = new JLabel("*", SwingConstants.CENTER);
        starLabelMenu.setVerticalAlignment(SwingConstants.BOTTOM);
        starLabelMenu.setOpaque(true);
        starLabelMenu.setForeground(Color.decode("#FFFFFF"));
        starLabelMenu.setFont(new Font("Circular Std Bold", Font.PLAIN, 50));
        starLabelMenu.setBackground(Color.decode("#22303C"));
        starLabelMenu.setBounds(75, 700, 50, 75);
        menuPanel.add(starLabelMenu);

        JButton btnAbortMenu = new JButton("Abort");
        btnAbortMenu.setForeground(Color.WHITE);
        btnAbortMenu.setFont(new Font("Circular Std Bold", Font.PLAIN, 25));
        btnAbortMenu.setFocusPainted(false);
        btnAbortMenu.setBackground(Color.decode("#B00020"));
        btnAbortMenu.setBounds(125, 700, 150, 75);
        menuPanel.add(btnAbortMenu);

        //Balance screen
        BalancePanel = new JPanel();
        BalancePanel.setLayout(null);
        BalancePanel.setBackground(Color.decode("#15202B"));
        Windows.add(BalancePanel, "BalancePanel");

        yourBalance.setHorizontalAlignment(JLabel.CENTER);
        yourBalance.setVerticalAlignment(JLabel.CENTER);
        yourBalance.setFont(new Font("Circular Std Bold", Font.PLAIN, 100));
        yourBalance.setForeground(Color.decode("#FFFFFF"));
        yourBalance.setBounds(0, 0, 1382, 864);
        BalancePanel.add(yourBalance);

        JLabel starLabelBalance = new JLabel("*", SwingConstants.CENTER);
        starLabelBalance.setVerticalAlignment(SwingConstants.BOTTOM);
        starLabelBalance.setOpaque(true);
        starLabelBalance.setForeground(Color.decode("#FFFFFF"));
        starLabelBalance.setFont(new Font("Circular Std Bold", Font.PLAIN, 50));
        starLabelBalance.setBackground(Color.decode("#22303C"));
        starLabelBalance.setBounds(75, 700, 50, 75);
        BalancePanel.add(starLabelBalance);

        JButton btnBackBalance = new JButton("Back");
        btnBackBalance.setForeground(Color.WHITE);
        btnBackBalance.setFont(new Font("Circular Std Bold", Font.PLAIN, 25));
        btnBackBalance.setFocusPainted(false);
        btnBackBalance.setBackground(Color.decode("#B00020"));
        btnBackBalance.setBounds(125, 700, 150, 75);
        BalancePanel.add(btnBackBalance);


        //Withdraw menu screen
        withdrawMenuPanel = new JPanel();
        withdrawMenuPanel.setLayout(null);
        withdrawMenuPanel.setBackground(Color.decode("#15202B"));
        Windows.add(withdrawMenuPanel, "withdrawMenuPanel");

        JLabel aLabelWM = new JLabel("A", SwingConstants.CENTER);
        aLabelWM.setOpaque(true);
        aLabelWM.setForeground(Color.decode("#FFFFFF"));
        aLabelWM.setFont(new Font("Circular Std Bold", Font.PLAIN, 40));
        aLabelWM.setBackground(Color.decode("#22303C"));
        aLabelWM.setBounds(741, 257, 50, 75);
        withdrawMenuPanel.add(aLabelWM);

        JLabel bLabelWM = new JLabel("B", SwingConstants.CENTER);
        bLabelWM.setOpaque(true);
        bLabelWM.setForeground(Color.decode("#FFFFFF"));
        bLabelWM.setFont(new Font("Circular Std Bold", Font.PLAIN, 40));
        bLabelWM.setBackground(Color.decode("#22303C"));
        bLabelWM.setBounds(741, 357, 50, 75);
        withdrawMenuPanel.add(bLabelWM);

        JLabel cLabelWM = new JLabel("C", SwingConstants.CENTER);
        cLabelWM.setVerticalAlignment(SwingConstants.BOTTOM);
        cLabelWM.setOpaque(true);
        cLabelWM.setForeground(Color.decode("#FFFFFF"));
        cLabelWM.setFont(new Font("Circular Std Bold", Font.PLAIN, 50));
        cLabelWM.setBackground(Color.decode("#22303C"));
        cLabelWM.setBounds(741, 462, 50, 75);
        withdrawMenuPanel.add(cLabelWM);

        JLabel dLabelWM = new JLabel("D", SwingConstants.CENTER);
        dLabelWM.setVerticalAlignment(SwingConstants.BOTTOM);
        dLabelWM.setOpaque(true);
        dLabelWM.setForeground(Color.decode("#FFFFFF"));
        dLabelWM.setFont(new Font("Circular Std Bold", Font.PLAIN, 50));
        dLabelWM.setBackground(Color.decode("#22303C"));
        dLabelWM.setBounds(741, 567, 50, 75);
        withdrawMenuPanel.add(dLabelWM);

        JButton btn20WM = new JButton("Pin €20");
        btn20WM.setForeground(Color.WHITE);
        btn20WM.setFont(new Font("Circular Std Bold", Font.PLAIN, 25));
        btn20WM.setFocusPainted(false);
        btn20WM.setBackground(Color.decode("#22303C"));
        btn20WM.setBounds(591, 257, 150, 75);
        withdrawMenuPanel.add(btn20WM);

        JButton btn50WM = new JButton("Pin €50");
        btn50WM.setForeground(Color.WHITE);
        btn50WM.setFont(new Font("Circular Std Bold", Font.PLAIN, 25));
        btn50WM.setFocusPainted(false);
        btn50WM.setBackground(Color.decode("#22303C"));
        btn50WM.setBounds(591, 357, 150, 75);
        withdrawMenuPanel.add(btn50WM);

        JButton btn100WM = new JButton("Pin €100");
        btn100WM.setForeground(Color.WHITE);
        btn100WM.setFont(new Font("Circular Std Bold", Font.PLAIN, 25));
        btn100WM.setFocusPainted(false);
        btn100WM.setBackground(Color.decode("#22303C"));
        btn100WM.setBounds(591, 462, 150, 75);
        withdrawMenuPanel.add(btn100WM);

        JButton btnCustomWM = new JButton("Custom");
        btnCustomWM.setForeground(Color.WHITE);
        btnCustomWM.setFont(new Font("Circular Std Bold", Font.PLAIN, 25));
        btnCustomWM.setFocusPainted(false);
        btnCustomWM.setBackground(Color.decode("#22303C"));
        btnCustomWM.setBounds(591, 567, 150, 75);
        withdrawMenuPanel.add(btnCustomWM);

        JLabel starLabelWM = new JLabel("*", SwingConstants.CENTER);
        starLabelWM.setVerticalAlignment(SwingConstants.BOTTOM);
        starLabelWM.setOpaque(true);
        starLabelWM.setForeground(Color.decode("#FFFFFF"));
        starLabelWM.setFont(new Font("Circular Std Bold", Font.PLAIN, 50));
        starLabelWM.setBackground(Color.decode("#22303C"));
        starLabelWM.setBounds(75, 700, 50, 75);
        withdrawMenuPanel.add(starLabelWM);

        JButton btnAbortWM = new JButton("Back");
        btnAbortWM.setForeground(Color.WHITE);
        btnAbortWM.setFont(new Font("Circular Std Bold", Font.PLAIN, 25));
        btnAbortWM.setFocusPainted(false);
        btnAbortWM.setBackground(Color.decode("#B00020"));
        btnAbortWM.setBounds(125, 700, 150, 75);
        withdrawMenuPanel.add(btnAbortWM);


        //Withdraw custom amount screen
        withdrawPanel = new JPanel();
        withdrawPanel.setLayout(null);
        withdrawPanel.setBackground(Color.decode("#15202B"));
        Windows.add(withdrawPanel, "withdrawPanel");

        withdrawMessage.setHorizontalAlignment(JLabel.CENTER);
        withdrawMessage.setVerticalAlignment(JLabel.CENTER);
        withdrawMessage.setFont(new Font("Circular Std Bold", Font.PLAIN, 100));
        withdrawMessage.setForeground(Color.decode("#FFFFFF"));
        withdrawMessage.setBounds(0, 0, 1382, 864);
        withdrawPanel.add(withdrawMessage);

        withdrawAmountCustom.setHorizontalAlignment(JLabel.CENTER);
        withdrawAmountCustom.setVerticalAlignment(JLabel.CENTER);
        withdrawAmountCustom.setFont(new Font("Circular Std Bold", Font.PLAIN, 100));
        withdrawAmountCustom.setForeground(Color.decode("#FFFFFF"));
        withdrawAmountCustom.setBounds(0, 200, 1382, 864);
        withdrawPanel.add(withdrawAmountCustom);

        JLabel dLabelCustom = new JLabel("D", SwingConstants.CENTER);
        dLabelCustom.setOpaque(true);
        dLabelCustom.setForeground(Color.decode("#FFFFFF"));
        dLabelCustom.setFont(new Font("Circular Std Bold", Font.PLAIN, 40));
        dLabelCustom.setBackground(Color.decode("#22303C"));
        dLabelCustom.setBounds(1250, 700, 50, 75);
        withdrawPanel.add(dLabelCustom);

        JButton btnYes2 = new JButton("Confirm");
        btnYes2.setForeground(Color.WHITE);
        btnYes2.setFont(new Font("Circular Std Bold", Font.PLAIN, 25));
        btnYes2.setFocusPainted(false);
        btnYes2.setBackground(Color.decode("#018786"));
        btnYes2.setBounds(1100, 700, 150, 75);
        withdrawPanel.add(btnYes2);

        JLabel hashLabelCustom = new JLabel("#", SwingConstants.CENTER);
        hashLabelCustom.setOpaque(true);
        hashLabelCustom.setForeground(Color.decode("#FFFFFF"));
        hashLabelCustom.setFont(new Font("Circular Std Bold", Font.PLAIN, 40));
        hashLabelCustom.setBackground(Color.decode("#22303C"));
        hashLabelCustom.setBounds(850, 700, 50, 75);
        withdrawPanel.add(hashLabelCustom);

        JButton btnDelete = new JButton("Delete");
        btnDelete.setForeground(Color.WHITE);
        btnDelete.setFont(new Font("Circular Std Bold", Font.PLAIN, 25));
        btnDelete.setFocusPainted(false);
        btnDelete.setBackground(Color.decode("#FFA000"));
        btnDelete.setBounds(900, 700, 150, 75);
        withdrawPanel.add(btnDelete);

        JLabel label_70 = new JLabel("*", SwingConstants.CENTER);
        label_70.setVerticalAlignment(SwingConstants.BOTTOM);
        label_70.setOpaque(true);
        label_70.setForeground(Color.decode("#FFFFFF"));
        label_70.setFont(new Font("Circular Std Bold", Font.PLAIN, 50));
        label_70.setBackground(Color.decode("#22303C"));
        label_70.setBounds(75, 700, 50, 75);
        withdrawPanel.add(label_70);

        JButton btnBack3 = new JButton("Back");
        btnBack3.setForeground(Color.WHITE);
        btnBack3.setFont(new Font("Circular Std Bold", Font.PLAIN, 25));
        btnBack3.setFocusPainted(false);
        btnBack3.setBackground(Color.decode("#B00020"));
        btnBack3.setBounds(125, 700, 150, 75);
        withdrawPanel.add(btnBack3);


        //Withdraw process screen
        withdrawProcessScreen = new JPanel();
        withdrawProcessScreen.setLayout(null);
        withdrawProcessScreen.setBackground(Color.decode("#15202B"));
        Windows.add(withdrawProcessScreen, "withdrawProcessScreen");

        withdrawStatusMessage.setHorizontalAlignment(JLabel.CENTER);
        withdrawStatusMessage.setVerticalAlignment(JLabel.CENTER);
        withdrawStatusMessage.setFont(new Font("Circular Std Bold", Font.PLAIN, 100));
        withdrawStatusMessage.setForeground(Color.decode("#FFFFFF"));
        withdrawStatusMessage.setBounds(0, 0, 1382, 864);
        withdrawProcessScreen.add(withdrawStatusMessage);


        //Withdraw Insufficient Balance Options screen
        withdrawInsufOptionsPanel = new JPanel();
        withdrawInsufOptionsPanel.setLayout(null);
        withdrawInsufOptionsPanel.setBackground(Color.decode("#15202B"));
        Windows.add(withdrawInsufOptionsPanel, "withdrawInsufOptionsPanel");

        withdrawInsufBal.setHorizontalAlignment(JLabel.CENTER);
        withdrawInsufBal.setVerticalAlignment(JLabel.CENTER);
        withdrawInsufBal.setFont(new Font("Circular Std Bold", Font.PLAIN, 100));
        withdrawInsufBal.setForeground(Color.decode("#FFFFFF"));
        withdrawInsufBal.setBounds(0, 0, 1382, 864);
        withdrawInsufOptionsPanel.add(withdrawInsufBal);

        JLabel bLabelInsufOptions = new JLabel("B", SwingConstants.CENTER);
        bLabelInsufOptions.setOpaque(true);
        bLabelInsufOptions.setForeground(Color.decode("#FFFFFF"));
        bLabelInsufOptions.setFont(new Font("Circular Std Bold", Font.PLAIN, 40));
        bLabelInsufOptions.setBackground(Color.decode("#22303C"));
        bLabelInsufOptions.setBounds(801, 700, 50, 75);
        withdrawInsufOptionsPanel.add(bLabelInsufOptions);

        JLabel cLabelInsufOptions = new JLabel("C", SwingConstants.CENTER);
        cLabelInsufOptions.setVerticalAlignment(SwingConstants.BOTTOM);
        cLabelInsufOptions.setOpaque(true);
        cLabelInsufOptions.setForeground(Color.decode("#FFFFFF"));
        cLabelInsufOptions.setFont(new Font("Circular Std Bold", Font.PLAIN, 50));
        cLabelInsufOptions.setBackground(Color.decode("#22303C"));
        cLabelInsufOptions.setBounds(1257, 700, 50, 75);
        withdrawInsufOptionsPanel.add(cLabelInsufOptions);

        JButton btnBalanceInsufOptions = new JButton("Check your balance");
        btnBalanceInsufOptions.setHorizontalAlignment(JButton.CENTER);
        btnBalanceInsufOptions.setForeground(Color.WHITE);
        btnBalanceInsufOptions.setFont(new Font("Circular Std Bold", Font.PLAIN, 25));
        btnBalanceInsufOptions.setFocusPainted(false);
        btnBalanceInsufOptions.setBackground(Color.decode("#22303C"));
        btnBalanceInsufOptions.setBounds(531, 700, 270, 75);
        withdrawInsufOptionsPanel.add(btnBalanceInsufOptions);

        JButton btnWithdrawInsufOptions = new JButton("Enter different amount");
        btnWithdrawInsufOptions.setForeground(Color.WHITE);
        btnWithdrawInsufOptions.setFont(new Font("Circular Std Bold", Font.PLAIN, 25));
        btnWithdrawInsufOptions.setFocusPainted(false);
        btnWithdrawInsufOptions.setBackground(Color.decode("#22303C"));
        btnWithdrawInsufOptions.setBounds(957, 700, 300, 75);
        withdrawInsufOptionsPanel.add(btnWithdrawInsufOptions);

        JLabel starLabelInsufOptions = new JLabel("*", SwingConstants.CENTER);
        starLabelInsufOptions.setVerticalAlignment(SwingConstants.BOTTOM);
        starLabelInsufOptions.setOpaque(true);
        starLabelInsufOptions.setForeground(Color.decode("#FFFFFF"));
        starLabelInsufOptions.setFont(new Font("Circular Std Bold", Font.PLAIN, 50));
        starLabelInsufOptions.setBackground(Color.decode("#22303C"));
        starLabelInsufOptions.setBounds(75, 700, 50, 75);
        withdrawInsufOptionsPanel.add(starLabelInsufOptions);

        JButton btnAbortInsufOptions = new JButton("Cancel transaction");
        btnAbortInsufOptions.setForeground(Color.WHITE);
        btnAbortInsufOptions.setFont(new Font("Circular Std Bold", Font.PLAIN, 25));
        btnAbortInsufOptions.setFocusPainted(false);
        btnAbortInsufOptions.setBackground(Color.decode("#B00020"));
        btnAbortInsufOptions.setBounds(125, 700, 280, 75);
        withdrawInsufOptionsPanel.add(btnAbortInsufOptions);

        //Withdraw suggestion screen
        withdrawSuggestionPanel = new JPanel();
        withdrawSuggestionPanel.setLayout(null);
        withdrawSuggestionPanel.setBackground(Color.decode("#15202B"));
        Windows.add(withdrawSuggestionPanel, "withdrawSuggestionPanel");

        withdrawError.setHorizontalAlignment(JLabel.CENTER);
        withdrawError.setVerticalAlignment(JLabel.CENTER);
        withdrawError.setFont(new Font("Circular Std Bold", Font.PLAIN, 75));
        withdrawError.setForeground(Color.decode("#8899A6"));
        withdrawError.setBounds(0, -250, 1382, 864);
        withdrawSuggestionPanel.add(withdrawError);

        JLabel dLabelWSug = new JLabel("D", SwingConstants.CENTER);
        dLabelWSug.setOpaque(true);
        dLabelWSug.setForeground(Color.decode("#FFFFFF"));
        dLabelWSug.setFont(new Font("Circular Std Bold", Font.PLAIN, 40));
        dLabelWSug.setBackground(Color.decode("#22303C"));
        dLabelWSug.setBounds(1250, 700, 50, 75);
        withdrawSuggestionPanel.add(dLabelWSug);

        JLabel starLabelWSug = new JLabel("*", SwingConstants.CENTER);
        starLabelWSug.setVerticalAlignment(SwingConstants.BOTTOM);
        starLabelWSug.setOpaque(true);
        starLabelWSug.setForeground(Color.decode("#FFFFFF"));
        starLabelWSug.setFont(new Font("Circular Std Bold", Font.PLAIN, 50));
        starLabelWSug.setBackground(Color.decode("#22303C"));
        starLabelWSug.setBounds(75, 700, 50, 75);
        withdrawSuggestionPanel.add(starLabelWSug);

        JButton yesButttonWSug = new JButton("Yes");
        yesButttonWSug.setForeground(Color.WHITE);
        yesButttonWSug.setFont(new Font("Circular Std Bold", Font.PLAIN, 25));
        yesButttonWSug.setFocusPainted(false);
        yesButttonWSug.setBackground(Color.decode("#018786"));
        yesButttonWSug.setBounds(1100, 700, 150, 75);
        withdrawSuggestionPanel.add(yesButttonWSug);

        JButton noButtonWSug = new JButton("No");
        noButtonWSug.setForeground(Color.WHITE);
        noButtonWSug.setFont(new Font("Circular Std Bold", Font.PLAIN, 25));
        noButtonWSug.setFocusPainted(false);
        noButtonWSug.setBackground(Color.decode("#B00020"));
        noButtonWSug.setBounds(125, 700, 150, 75);
        withdrawSuggestionPanel.add(noButtonWSug);

        withdrawSuggestionText.setForeground(Color.decode("#FFFFFF"));
        withdrawSuggestionText.setHorizontalAlignment(SwingConstants.CENTER);
        withdrawSuggestionText.setFont(new Font("Circular Std Bold", Font.PLAIN, 100));
        withdrawSuggestionText.setBounds(0, -50, 1382, 864);
        withdrawSuggestionPanel.add(withdrawSuggestionText);

        withdrawSuggestionText2 = new JLabel("");
        withdrawSuggestionText2.setForeground(Color.decode("#FFFFFF"));
        withdrawSuggestionText2.setHorizontalAlignment(SwingConstants.CENTER);
        withdrawSuggestionText2.setFont(new Font("Circular Std Bold", Font.PLAIN, 100));
        withdrawSuggestionText2.setBounds(0, 100, 1382, 864);
        withdrawSuggestionPanel.add(withdrawSuggestionText2);

        // Printing panel
        printingPanel = new JPanel();
        printingPanel.setLayout(null);
        printingPanel.setBackground(Color.decode("#15202B"));
        Windows.add(printingPanel, "printingPanel");

        printingMoney.setFont(new Font("Circular Std Bold", Font.PLAIN, 70));
        printingMoney.setForeground(Color.decode("#FFFFFF"));
        printingMoney.setHorizontalAlignment(SwingConstants.CENTER);
        printingMoney.setBounds(0, 0, 1382, 864);
        printingPanel.add(printingMoney);

        //Receipt screen
        receiptPanel = new JPanel();
        receiptPanel.setLayout(null);
        receiptPanel.setBackground(Color.decode("#15202B"));
        Windows.add(receiptPanel, "receiptPanel");

        JLabel label_11 = new JLabel("D", SwingConstants.CENTER);
        label_11.setOpaque(true);
        label_11.setForeground(Color.decode("#FFFFFF"));
        label_11.setFont(new Font("Circular Std Bold", Font.PLAIN, 40));
        label_11.setBackground(Color.decode("#22303C"));
        label_11.setBounds(1250, 700, 50, 75);
        receiptPanel.add(label_11);

        JLabel label_12 = new JLabel("*", SwingConstants.CENTER);
        label_12.setVerticalAlignment(SwingConstants.BOTTOM);
        label_12.setOpaque(true);
        label_12.setForeground(Color.decode("#FFFFFF"));
        label_12.setFont(new Font("Circular Std Bold", Font.PLAIN, 50));
        label_12.setBackground(Color.decode("#22303C"));
        label_12.setBounds(75, 700, 50, 75);
        receiptPanel.add(label_12);

        JButton btnYes = new JButton("Yes");
        btnYes.setForeground(Color.WHITE);
        btnYes.setFont(new Font("Circular Std Bold", Font.PLAIN, 25));
        btnYes.setFocusPainted(false);
        btnYes.setBackground(Color.decode("#018786"));
        btnYes.setBounds(1100, 700, 150, 75);
        receiptPanel.add(btnYes);

        JButton btnNo = new JButton("No");
        btnNo.setForeground(Color.WHITE);
        btnNo.setFont(new Font("Circular Std Bold", Font.PLAIN, 25));
        btnNo.setFocusPainted(false);
        btnNo.setBackground(Color.decode("#B00020"));
        btnNo.setBounds(125, 700, 150, 75);
        receiptPanel.add(btnNo);

        JLabel Receiptyn = new JLabel("Would you like a receipt?");
        Receiptyn.setForeground(Color.decode("#FFFFFF"));
        Receiptyn.setHorizontalAlignment(SwingConstants.CENTER);
        Receiptyn.setFont(new Font("Circular Std Bold", Font.PLAIN, 100));
        Receiptyn.setBounds(0, 0, 1382, 864);
        receiptPanel.add(Receiptyn);


        //Thanks screen
        thanksPanel = new JPanel();
        thanksPanel.setLayout(null);
        thanksPanel.setBackground(Color.decode("#15202B"));
        Windows.add(thanksPanel, "thanksPanel");

        ThanksFor.setFont(new Font("Circular Std Bold", Font.PLAIN, 70));
        ThanksFor.setForeground(Color.decode("#FFFFFF"));
        ThanksFor.setHorizontalAlignment(SwingConstants.CENTER);
        ThanksFor.setBounds(0, 0, 1382, 864);
        thanksPanel.add(ThanksFor);

        lblNiceday.setFont(new Font("Circular Std Bold", Font.PLAIN, 60));
        lblNiceday.setForeground(Color.decode("#8899A6"));
        lblNiceday.setHorizontalAlignment(SwingConstants.CENTER);
        lblNiceday.setBounds(0, 200, 1382, 864);
        thanksPanel.add(lblNiceday);


        //Blocked Card screen
        blockedCardPanel = new JPanel();
        blockedCardPanel.setLayout(null);
        blockedCardPanel.setBackground(Color.decode("#15202B"));
        Windows.add(blockedCardPanel, "blockedCardPanel");

        thisCardBlocked.setHorizontalAlignment(JLabel.CENTER);
        thisCardBlocked.setVerticalAlignment(JLabel.CENTER);
        thisCardBlocked.setFont(new Font("Circular Std Bold", Font.PLAIN, 100));
        thisCardBlocked.setForeground(Color.decode("#FFFFFF"));
        thisCardBlocked.setBounds(0, 0, 1382, 864);
        blockedCardPanel.add(thisCardBlocked);


        //Debt Error screen
        debtErrorPanel = new JPanel();
        debtErrorPanel.setLayout(null);
        debtErrorPanel.setBackground(Color.decode("#15202B"));
        Windows.add(debtErrorPanel, "debtErrorPanel");

        debtErrorMessage.setHorizontalAlignment(JLabel.CENTER);
        debtErrorMessage.setVerticalAlignment(JLabel.CENTER);
        debtErrorMessage.setFont(new Font("Circular Std Bold", Font.PLAIN, 100));
        debtErrorMessage.setForeground(Color.decode("#FFFFFF"));
        debtErrorMessage.setBounds(0, 150, 1382, 864);
        debtErrorPanel.add(debtErrorMessage);

        debtErrorMessage2.setHorizontalAlignment(JLabel.CENTER);
        debtErrorMessage2.setVerticalAlignment(JLabel.CENTER);
        debtErrorMessage2.setFont(new Font("Circular Std Bold", Font.PLAIN, 100));
        debtErrorMessage2.setForeground(Color.decode("#FFFFFF"));
        debtErrorMessage2.setBounds(0, -50, 1382, 864);
        debtErrorPanel.add(debtErrorMessage2);

        setVisible(true);
    }

    public void displayPanel(String pnl) {
        cardLayout.show(Windows, pnl);
        screenPanel.repaint();
    }
}
