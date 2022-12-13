import java.io.DataInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Random;

//This project Made by (Khaled Androu, Chris, Nicholas,Humayun, Yee and Ali).

 class KA
{
     private BigInteger p1;              //prime1
      private BigInteger q2;             //prime2
       private BigInteger M;            //modulus
        private BigInteger totient;     //totient
         private BigInteger e;          //public exponent
          private BigInteger d;         //private exponent
           private Random     ra;
            private int bitlength = 1024;

    public KA()
    {
         ra = new Random();
          p1 = BigInteger.probablePrime(bitlength, ra);
           q2 = BigInteger.probablePrime(bitlength, ra);
            M = p1.multiply(q2);
             totient = p1.subtract(BigInteger.ONE).multiply(q2.subtract(BigInteger.ONE));

        // Set e to a specific value rather than generating it randomly
        e = BigInteger.valueOf(65537);

         // Check whether e is relatively prime to totient(n)
    if (totient.gcd(e).compareTo(BigInteger.ONE) != 0) {

        // If not, choose a different value for e that is relatively prime to totient(n)
        e = BigInteger.probablePrime(bitlength / 2, ra);
        while (totient.gcd(e).compareTo(BigInteger.ONE) > 0 && e.compareTo(totient) < 0)
        {
            e.add(BigInteger.ONE);
        }
    }
    d = e.modInverse(totient);
}

    public KA(BigInteger e, BigInteger d, BigInteger M)
    {
         this.e = e;
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

        return (new BigInteger(message)).modPow(e, M).toByteArray();
    }

    // Decrypt message
    public byte[] decrypt(byte[] message)
    {

        return (new BigInteger(message)).modPow(d, M).toByteArray();
    }

}
