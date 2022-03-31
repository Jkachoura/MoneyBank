
import java.awt.Point;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Font;
import java.awt.CardLayout;
import javax.swing.JButton;


public class ATMScreen extends JFrame{
    private JPanel screenPanel = new JPanel();
    private JPanel Windows = new JPanel();
    private CardLayout cardLayout = new CardLayout(0,0);


    
    //Thanks Screen
    private JLabel ThanksFor = new JLabel("Thank you for choosing Money Bank!");
    private JLabel lblNiceday = new JLabel("Have a nice day!");
    
    
    public ATMScreen(){
        setContentPane(screenPanel);
        setResizable(false);
        setAlwaysOnTop(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1920, 1080);
        setUndecorated(true);;
        setCursor( getToolkit().createCustomCursor(new BufferedImage( 1, 1, BufferedImage.TYPE_INT_ARGB ), new Point(), null ) );
        getContentPane().setLayout(null);
    
        
      
        
    
        Windows.setBounds(0, 0, 1920, 1080);
        getContentPane().add(Windows);
        Windows.setLayout(cardLayout);
        
        
        
        //Receipt
        
        JPanel Receipt = new JPanel();
        Receipt.setLayout(null);
        Windows.add(Receipt, "Receipt");
        
        JLabel label_11 = new JLabel("D", SwingConstants.CENTER);
        label_11.setOpaque(true);
        label_11.setForeground(Color.WHITE);
        label_11.setFont(new Font("Tahoma", Font.PLAIN, 40));
        label_11.setBackground(Color.LIGHT_GRAY);
        label_11.setBounds(1650, 900, 50, 75);
        Receipt.add(label_11);
        
        JLabel label_12 = new JLabel("*", SwingConstants.CENTER);
        label_12.setVerticalAlignment(SwingConstants.BOTTOM);
        label_12.setOpaque(true);
        label_12.setForeground(Color.WHITE);
        label_12.setFont(new Font("Tahoma", Font.PLAIN, 50));
        label_12.setBackground(Color.LIGHT_GRAY);
        label_12.setBounds(200, 900, 50, 75);
        Receipt.add(label_12);
        
        
        JButton btnYes = new JButton("Yes");
        btnYes.setForeground(Color.WHITE);
        btnYes.setFont(new Font("Tahoma", Font.PLAIN, 25));
        btnYes.setFocusPainted(false);
        btnYes.setBackground(new Color(0, 153, 51));
        btnYes.setBounds(1500, 900, 150, 75);
        Receipt.add(btnYes);
        
        JButton btnNo = new JButton("No");
        btnNo.setForeground(Color.WHITE);
        btnNo.setFont(new Font("Tahoma", Font.PLAIN, 25));
        btnNo.setFocusPainted(false);
        btnNo.setBackground(new Color(255, 51, 51));
        btnNo.setBounds(250, 900, 150, 75);
        Receipt.add(btnNo);
        
        JLabel Receiptyn = new JLabel("Would you like a receipt?");
        Receiptyn.setForeground(new Color(0, 0, 0));
        Receiptyn.setHorizontalAlignment(SwingConstants.CENTER);
        Receiptyn.setFont(new Font("Tahoma", Font.PLAIN, 70));
        Receiptyn.setBounds(0, 550, 1920, 86);
        Receipt.add(Receiptyn);
        
        
        //End screen
        
        JPanel Thanks = new JPanel();
        Thanks.setLayout(null);
        Windows.add(Thanks, "Thanks");
        
        
        ThanksFor.setFont(new Font("Tahoma", Font.PLAIN, 70));
        ThanksFor.setForeground(new Color(0, 0, 0));
        ThanksFor.setHorizontalAlignment(SwingConstants.CENTER);
        ThanksFor.setBounds(0, 550, 1920, 111);
        Thanks.add(ThanksFor);
        
        
        lblNiceday.setFont(new Font("Tahoma", Font.PLAIN, 60));
        lblNiceday.setForeground(new Color(0, 0, 0));
        lblNiceday.setHorizontalAlignment(SwingConstants.CENTER);
        lblNiceday.setBounds(0, 750, 1920, 81);
        Thanks.add(lblNiceday);
        
        

        setVisible(true);

    }

    public void displayPanel(String pnl) {
        cardLayout.show(Windows , pnl);
        screenPanel.repaint();
    }

}
