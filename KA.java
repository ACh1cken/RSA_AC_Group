import java.io.DataInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

//This project Made by (Khaled Androu, Chris, Nicholas,Humayun, Yee and Ali).

 public class KA
{
    private BigInteger p1;              //prime1
    private BigInteger q2;             //prime2
    private BigInteger M;            //modulus
    private BigInteger phi;     //totient
    private static final BigInteger ENCRYPTION_EXPONENT = BigInteger.valueOf(65537);          //public exponent
    private BigInteger d;         //private exponent
    private int bitlength = 512; //512 length for p and q


    public KA()
    {
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
        System.out.println("P "+p1.toString());
        System.out.println("Q "+q2.toString());
        System.out.println("M "+M.toString());
        System.out.println("Phi "+phi.toString());
        System.out.println("D "+d.toString());


}

    public KA(BigInteger d, BigInteger M)
    {

          this.d = d;
           this.M = M;
    }

    @SuppressWarnings("deprecation")
    public static void main(String[] args) throws IOException
    {
        KA rsa = new KA();
        DataInputStream in = new DataInputStream(System.in);
        String teststring;
        System.out.println("Enter the plain text:");
        teststring = in.readLine();
        System.out.println("Encrypting String: " + teststring);
        System.out.println("String in Bytes: "
                + bytesToString(teststring.getBytes()));

        // encrypt
        byte[] encrypted = rsa.encrypt(teststring.getBytes());

        // decrypt
        byte[] decrypted = rsa.decrypt(encrypted);

        System.out.println("Decrypting Bytes: " + bytesToString(decrypted));
        System.out.println("Decrypted String: " + new String(decrypted));
    }

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

}
