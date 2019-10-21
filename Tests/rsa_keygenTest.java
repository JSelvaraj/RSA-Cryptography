import org.junit.Before;
import org.junit.Test;

import java.math.BigInteger;

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
    public void isPrimeCorrectInput() {
        assertTrue(rsa_keygen.isPrime(fn,x));
    }

    /**
     * This checks that isPrime returns false if the two inputs aren't coprime. I chose two random even numbers to test.
     */
    @Test
    public void isPrimeWrongInput() {
        assertFalse(rsa_keygen.isPrime(new BigInteger("25893239058"), new BigInteger("2593802")));
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
    public void readInValuesCorrect() {
        BigInteger[] array = {p, q, x};

    }




}