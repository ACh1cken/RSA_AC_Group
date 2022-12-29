/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


import java.math.BigInteger;
import java.security.SecureRandom;

/**
 *
 * @author Khaled Androu, Chris, Nicholas,Humayun, Yee and Ali
 */
class KA {
    public  BigInteger p1;              //prime1
    public  BigInteger q2;             //prime2
    private  BigInteger M;            //modulus
    public  BigInteger phi;     //totient
    private static final BigInteger ENCRYPTION_EXPONENT = BigInteger.valueOf(65537);          //public exponent
    private  BigInteger d;         //private exponent
    private  int bitlength = 512; //512 length for p and q


    public void generateKeys()
    {
        //prevent reuse of random number
          p1 = BigInteger.probablePrime(bitlength, new SecureRandom());
          q2 = BigInteger.probablePrime(bitlength, new SecureRandom());
          //incase p=q
          while(p1.equals(q2)){
              q2 = BigInteger.probablePrime(bitlength, new SecureRandom());
          }
          M = p1.multiply(q2);
          phi = p1.subtract(BigInteger.ONE).multiply(q2.subtract(BigInteger.ONE));

 
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
}

    //flush keys and prepare for generation for another rsa encryption and not reuse old keys
    public void flushKeys(){
        p1 = null;
        q2 = null;
        M = null;
        phi = null;
        d = null;
    }

    //convet byes to string
    public static String bytesToString(byte[] encrypted)
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