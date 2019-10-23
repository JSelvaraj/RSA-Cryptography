import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigInteger;
import java.util.Scanner;
import static org.junit.Assert.*;

public class rsa_keygenTest {

    BigInteger p, q, x, y, n, fn;
    @Before
    public void setupVariables() {
         p = new BigInteger("3810509");
         q = new BigInteger("3810511");
         x = new BigInteger("3811163");
         n = p.multiply(q);
         fn = (p.subtract(new BigInteger("1"))).multiply(q.subtract(new BigInteger("1")));
         y = x.modInverse(fn);
    }

    /**
     * This tests checks whether the isPrime method correctly identifies x coprime with fn. The above numbers were taken
     * from a list of primes.
     */
    @Test
    public void isCoprimeCorrectInput() {

//        byte[] array = new byte[3];
//        array[0] = 1;
//        array[1] = 5;
//        array[2] = 1;
////        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
////        buffer.put(array);
////        buffer.flip();
////        BigInteger i = new BigInteger(buffer.array());
////        System.out.println(buffer.getLong());
//
//        try {
//            FileOutputStream writer = new FileOutputStream("infile");
//            writer.write(array);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


        assertTrue(rsa_keygen.isCoprime(fn,x));
    }

    /**
     * This checks that isPrime returns false if the two inputs aren't coprime. I chose two random even numbers to test.
     */
    @Test
    public void isCoprimeWrongInput() {
        assertFalse(rsa_keygen.isCoprime(new BigInteger("25893239058"), new BigInteger("2593802")));
    }

    /**
     * This test checks that modInverse returns the correct value. I used the modInverse obtained by the java.Math library
     * to compare to the modInverse obtained by the method.
     */
    @Test
    public void modInverseCorrect() {
        assertEquals(rsa_keygen.modInverse(x, fn), y);
    }

    /**
     * This tests that the readInValues method can correctly a given text file. The text file test1 will be alongside
     * this test class.
     */
    @Test
    public void readInValuesCorrect() throws FileNotFoundException {
        BigInteger[] array = {p, q, x};
        assertEquals(rsa_keygen.readInValues("p01-src/test1")[0], array[0]);
        assertEquals(rsa_keygen.readInValues("p01-src/test1")[1], array[1]);
        assertEquals(rsa_keygen.readInValues("p01-src/test1")[2], array[2]);
    }

    /**
     * This function checks that the correct error is thrown if the input file doesn't exist.
     * @throws FileNotFoundException
     */
    @Test(expected = FileNotFoundException.class)
    public void readInValuesNoFile() throws FileNotFoundException {


        rsa_keygen.readInValues("noFileExists");
    }

    /**
     * This checks that the printValues file correctly prints the numbers to text files.
     * @throws FileNotFoundException
     */
    @Test
    public void outputFiles() throws FileNotFoundException {
        rsa_keygen.printValues(n, x, y, "out1", "out2");
        Scanner scanner1 = new Scanner(new FileReader("out1"));
        BigInteger pq = new BigInteger(scanner1.next());
        BigInteger publickey = new BigInteger(scanner1.next());
        scanner1.close();
        assertEquals(pq, n);
        assertEquals(publickey, x);
        Scanner scanner2 = new Scanner(new FileReader("out2"));
        pq = new BigInteger(scanner2.next());
        BigInteger privatekey = new BigInteger(scanner2.next());
        assertEquals(pq, n);
        assertEquals(y, privatekey);
    }




}