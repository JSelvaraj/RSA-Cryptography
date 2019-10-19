import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigInteger;
import java.util.Scanner;

public class rsa_keygen {


    public static void main(String[] args) {
        rsa_keygen x =new rsa_keygen();
//        BigInteger test_val_1 = new BigInteger("15");
//        BigInteger test_val_2 = new BigInteger("3");
//        BigInteger test_val_3 = new BigInteger("5");
        BigInteger[] values = x.readInValues(args[0]);
        BigInteger p = values[0];
        BigInteger q = values[1];
        BigInteger n = p.multiply(q);
        BigInteger fn = (p.subtract(new BigInteger("1"))).multiply(q.subtract(new BigInteger("1")));
        System.out.println(x.isPrime(fn,values[2]));
//        System.out.println(x.isPrime((test_val_1), test_val_2, test_val_3));
    }

    /**
     * This method checks whether 2 numbers, x and (p-1)(q-1) are coprime
     * @param x operand 1
     * @param n this value is equal to (p-1)(q-1)
     * @return true if e and (p-1)(q-1) are coprime and false if not
     */
    public  boolean isPrime(BigInteger n, BigInteger x) {
        if(gcd(x, n).equals(new BigInteger("1"))) {
            return true;
        }
        return false;
    }

    /**
     * applies Euclid's algorithm recursively to determine the greatest common divisor between the parameters a and b
     * @param a operand 1
     * @param b (p-1)(q-1)
     * @return The greatest common divisor between a and b
     */
    private BigInteger gcd(BigInteger a, BigInteger b) {
        if (a.equals(new BigInteger("0"))) {
            return b ;
        }
        return gcd(b.mod(a), a);
    }

    /**
     * This reads in a file containing 3 numbers, into an array of 3 BigIntegers
     * @param infile The input file containing p, q and x
     * @return a BigInteger array containing the 3 numbers from an input file. Null on a failure.
     */
    private BigInteger[] readInValues(String infile) {
        try {
            String[] numbers = new String[3];
            BigInteger[] values = new BigInteger[3];
            FileReader reader = new FileReader(infile);
            Scanner scanner = new Scanner(reader);
            for (int i = 0; i < 3; i++) {
                numbers[i] = scanner.next();
                values[i] = new BigInteger(numbers[i]);
            }
            return values;
        } catch (FileNotFoundException e) {
            System.out.println("Unsuitable exponent\n");
            System.exit(-1);
        }
        return null;
    }

}
