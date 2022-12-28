import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.DataInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

public class RSAGUI extends JFrame implements ActionListener {

  // Declare GUI components
  private JTextField input,display_p1,display_q2,display_M,display_phi,display_d,display_enstrings,display_destrings,display_decrypt;
  private JButton btnEncrypt;
  private BigInteger p1;              //prime1
    private BigInteger q2;             //prime2
    private BigInteger M;            //modulus
    private BigInteger phi;     //totient
    private static final BigInteger ENCRYPTION_EXPONENT = BigInteger.valueOf(65537);          //public exponent
    private BigInteger d;         //private exponent
    private int bitlength = 512; //512 length for p and q
	private String string_p1,string_q2 ,string_M,string_phi,string_d ;//string variable to store coverted string
   
 
	
  public RSAGUI() {
    // Set up the window
    super("RSA Ky Generation:This project Made by (Khaled Androu, Chris, Nicholas,Humayun, Yee and Ali).");
    setSize(1000, 1000);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLayout(new FlowLayout());

    // Create the text fields and button
    input = new JTextField();
	input.setPreferredSize(new Dimension(300,50));
    display_p1 = new JTextField();
	display_p1.setPreferredSize(new Dimension(300,200));
	display_q2 = new JTextField();
	display_q2.setPreferredSize(new Dimension(300,200));
	display_M = new JTextField();
	display_M.setPreferredSize(new Dimension(300,200));
	display_phi = new JTextField();
	display_phi.setPreferredSize(new Dimension(300,200));
	display_d = new JTextField();
	display_d.setPreferredSize(new Dimension(300,200));
	display_enstrings = new JTextField();
	display_enstrings.setPreferredSize(new Dimension(300,50));
	display_destrings = new JTextField();
	display_destrings.setPreferredSize(new Dimension(300,50));
	display_decrypt = new JTextField();
	display_decrypt.setPreferredSize(new Dimension(300,50));
	
	
    // result field is read-only
	display_decrypt.setEditable(false); 
	display_enstrings.setEditable(false); 
	display_destrings.setEditable(false);
	display_p1.setEditable(false); 
	display_q2.setEditable(false);
	display_M.setEditable(false);
	display_phi.setEditable(false);
	display_d.setEditable(false);
   btnEncrypt = new JButton("Encrypt");
   btnEncrypt.addActionListener(this); // listen for button clicks
    // Add the components to the window
	add(display_p1);
	add(display_q2);
	add(display_q2);
	add(display_M);
	add(display_phi);
	add(display_d);
	add(display_enstrings);
	add(display_destrings);
	add(input);
    add(display_decrypt);
	add(btnEncrypt);
	
	

  }
  
  public RSAGUI(BigInteger d, BigInteger M)
    {

          this.d = d;
           this.M = M;
    }

  // This method is called when the button is clicked
  public void actionPerformed(ActionEvent e) {
    
	String teststring;
    // Read the values from the text fields
    teststring = input.getText();
	
    // Perform the the calculation
	//prevent reuse of random number
          p1 = BigInteger.probablePrime(bitlength, new SecureRandom());
          q2 = BigInteger.probablePrime(bitlength, new SecureRandom());
          M = p1.multiply(q2);
          phi = p1.subtract(BigInteger.ONE).multiply(q2.subtract(BigInteger.ONE));

        // Set e to a specific value rather than generating it randomly


        /*
         // Check whether e is relatively prime to totient(n)
    if (phi.gcd(e).compareTo(BigInteger.ONE) != 0) {

        // If not, choose a different value for e that is relatively prime to totient(n)
        e = BigInteger.probablePrime(bitlength / 2, ra);
        while (phi.gcd(e).compareTo(BigInteger.ONE) > 0 && e.compareTo(totient) < 0)
        {
            e.add(BigInteger.ONE);
        }
    }
    d = e.modInverse(phi);
         */

        // Use an encryption exponent e = 65537 and ensure that it is relatively prime to phi(n)
        while (!phi.gcd(ENCRYPTION_EXPONENT).equals(BigInteger.ONE)){
            // If it is not, go back to Step 1 and generate new values for p and q
            p1 = BigInteger.probablePrime(bitlength, new SecureRandom());
            q2 = BigInteger.probablePrime(bitlength, new SecureRandom());
            M = p1.multiply(q2);
            phi = p1.subtract(BigInteger.ONE).multiply(q2.subtract(BigInteger.ONE));
        }
// Compute the value for the decryption exponent d, which is the multiplicative inverse of e mod phi(n)
         d = ENCRYPTION_EXPONENT.modInverse(phi);
        string_p1 = p1.toString();
        string_q2 = q2.toString();
        string_M = M.toString();
        string_phi = phi.toString();
        string_d = d.toString();
	// encrypt
    byte[] encrypted = encrypt(teststring.getBytes());
	// decrypt
	byte[] decrypted = decrypt(encrypted);
    // Display the result in the result field
	
    display_decrypt.setText("Decrypted: " + new String(decrypted));
	display_p1.setText("p1:"+string_p1);
	display_q2.setText("q2:"+string_q2);
	display_M.setText("M:"+string_M);
	display_phi.setText("phi:"+ string_phi);
	display_d.setText("d:"+ string_d);
	display_enstrings.setText("String in Bytes: " + bytesToString(teststring.getBytes()));
	display_destrings.setText("Decrypting Bytes: " + bytesToString(decrypted));
  }
  
  //Convert to string
    private static String bytesToString(byte[] encrypted)
    {
        String test = "";
        for (byte b : encrypted)
        {
            test += Byte.toString(b);
        }
        return test;
    }

    // Encrypt message
    public byte[] encrypt(byte[] message)
    {

        return (new BigInteger(message)).modPow(ENCRYPTION_EXPONENT, M).toByteArray();
    }

    // Decrypt message
    public byte[] decrypt(byte[] message)
    {

        return (new BigInteger(message)).modPow(d, M).toByteArray();
    }


  public static void main(String[] args) {
    RSAGUI gui = new RSAGUI();
    gui.setVisible(true);
  }
}
