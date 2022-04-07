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

    //Pincode Screen
    public JPanel PincodePanel;
    public JLabel enterPin = new JLabel("Enter your pincode");
    public JLabel pinMessage = new JLabel("");

    //Menu screen
    public JPanel menuPanel;
    //Balance screen
    public JPanel BalancePanel;
    public JLabel yourBalance = new JLabel("Your Balance is ");

    //Receipt screen
    public JPanel receiptPanel;

    //Thanks Screen
    public JPanel thanksPanel;
    private JLabel ThanksFor = new JLabel("Thank you for choosing Money Bank!");
    private JLabel lblNiceday = new JLabel("Have a nice day!");

    //Blocked card screen
    public JPanel blockedCardPanel;
    private JLabel thisCardBlocked = new JLabel("This card is blocked");

    // logo test
    private ImageIcon logoImage;
    private ImageIcon logoImageIcon;
    public JLabel logo;
    public JLabel logoIcon;

    public ATMGUI() {
        setContentPane(screenPanel);
        setResizable(false);
        setAlwaysOnTop(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1382, 864);
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
        PincodePanel = new JPanel();
        PincodePanel.setLayout(null);
        PincodePanel.setBackground(Color.decode("#15202B"));
        Windows.add(PincodePanel, "PincodePanel");

        enterPin.setHorizontalAlignment(JLabel.CENTER);
        enterPin.setVerticalAlignment(JLabel.CENTER);
        enterPin.setFont(new Font("Circular Std Bold", Font.PLAIN, 100));
        enterPin.setForeground(Color.decode("#FFFFFF"));
        enterPin.setBounds(0, 0, 1382, 864);
        PincodePanel.add(enterPin);

        pinMessage.setHorizontalAlignment(JLabel.CENTER);
        pinMessage.setVerticalAlignment(JLabel.CENTER);
        pinMessage.setFont(new Font("Circular Std Bold", Font.PLAIN, 100));
        pinMessage.setForeground(Color.decode("#8899A6"));
        pinMessage.setBounds(0, 200, 1382, 864);
        PincodePanel.add(pinMessage);

        logoIcon.setHorizontalAlignment(JLabel.CENTER);
        logoIcon.setVerticalAlignment(JLabel.TOP);
        logoIcon.setBounds(0, 0, 1382, 864);
        PincodePanel.add(logoIcon);

        //Menu screen
        menuPanel = new JPanel();
        menuPanel.setLayout(null);
        menuPanel.setBackground(Color.decode("#15202B"));
        Windows.add(menuPanel, "menuPanel");

        JLabel label_21 = new JLabel("B", SwingConstants.CENTER);
        label_21.setOpaque(true);
        label_21.setForeground(Color.decode("#FFFFFF"));
        label_21.setFont(new Font("Circular Std Bold", Font.PLAIN, 40));
        label_21.setBackground(Color.decode("#22303C"));
        label_21.setBounds(741, 462, 50, 75);
        menuPanel.add(label_21);

        JLabel label_26 = new JLabel("C", SwingConstants.CENTER);
        label_26.setVerticalAlignment(SwingConstants.BOTTOM);
        label_26.setOpaque(true);
        label_26.setForeground(Color.decode("#FFFFFF"));
        label_26.setFont(new Font("Circular Std Bold", Font.PLAIN, 50));
        label_26.setBackground(Color.decode("#22303C"));
        label_26.setBounds(741, 357, 50, 75);
        menuPanel.add(label_26);

        JButton btnBalance = new JButton("Balance");
        btnBalance.setForeground(Color.WHITE);
        btnBalance.setFont(new Font("Circular Std Bold", Font.PLAIN, 25));
        btnBalance.setFocusPainted(false);
        btnBalance.setBackground(Color.decode("#22303C"));
        btnBalance.setBounds(591, 462, 150, 75);
        menuPanel.add(btnBalance);

        JButton btnWithdraw = new JButton("Withdraw");
        btnWithdraw.setForeground(Color.WHITE);
        btnWithdraw.setFont(new Font("Circular Std Bold", Font.PLAIN, 25));
        btnWithdraw.setFocusPainted(false);
        btnWithdraw.setBackground(Color.decode("#22303C"));
        btnWithdraw.setBounds(591, 357, 150, 75);
        menuPanel.add(btnWithdraw);

        // JLabel label_20 = new JLabel("D", SwingConstants.CENTER);
        // label_20.setOpaque(true);
        // label_20.setForeground(Color.decode("#FFFFFF"));
        // label_20.setFont(new Font("Circular Std Bold", Font.PLAIN, 40));
        // label_20.setBackground(Color.decode("#22303C"));
        // label_20.setBounds(1250, 700, 50, 75);
        // menuPanel.add(label_20);

        JLabel label_25 = new JLabel("*", SwingConstants.CENTER);
        label_25.setVerticalAlignment(SwingConstants.BOTTOM);
        label_25.setOpaque(true);
        label_25.setForeground(Color.decode("#FFFFFF"));
        label_25.setFont(new Font("Circular Std Bold", Font.PLAIN, 50));
        label_25.setBackground(Color.decode("#22303C"));
        label_25.setBounds(75, 700, 50, 75);
        menuPanel.add(label_25);

        // JButton btnBack = new JButton("Back");
        // btnBack.setForeground(Color.WHITE);
        // btnBack.setFont(new Font("Circular Std Bold", Font.PLAIN, 25));
        // btnBack.setFocusPainted(false);
        // btnBack.setBackground(Color.decode("#018786"));
        // btnBack.setBounds(1100, 700, 150, 75);
        // menuPanel.add(btnBack);

        JButton btnAbort = new JButton("Abort");
        btnAbort.setForeground(Color.WHITE);
        btnAbort.setFont(new Font("Circular Std Bold", Font.PLAIN, 25));
        btnAbort.setFocusPainted(false);
        btnAbort.setBackground(Color.decode("#B00020"));
        btnAbort.setBounds(125, 700, 150, 75);
        menuPanel.add(btnAbort);


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

        BalancePanel.add(logoIcon);


        //Receipt
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

        receiptPanel.add(logoIcon);


        //End screen
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

        blockedCardPanel.add(logoIcon);

        setVisible(true);
    }

    public void displayPanel(String pnl) {
        cardLayout.show(Windows, pnl);
        screenPanel.repaint();
    }
}

