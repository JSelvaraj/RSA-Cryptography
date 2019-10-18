import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigInteger;
import java.util.Scanner;

public class rsa_keygen {


    public static void main(String[] args) {
//        rsa_keygen x =new rsa_keygen();
        BigInteger test_val_1 = new BigInteger("15");
        BigInteger[] values = readInValues(args[0]);

        System.out.println(isPrime(new BigInteger("15"), new BigInteger("3"), new BigInteger("5")));
    }

    /**
     * This method checks whether 2 numbers, x and (p-1)(q-1) are coprime
     * @param x operand 1
     * @param p operand 2
     * @param q operand 3
     * @return true if e and (p-1)(q-1) are coprime and false if not
     */
    public static boolean isPrime(BigInteger x, BigInteger p, BigInteger q) {
        BigInteger operand_2 = (p.subtract(new BigInteger("1"))).multiply(q.subtract(new BigInteger("1")));
        if(gcd(x, operand_2).equals(new BigInteger("1"))) {
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
    private static BigInteger gcd(BigInteger a, BigInteger b) {
        if (a.equals(new BigInteger("0"))) {
            return b ;
        }
        return gcd(b.mod(a), a);
    }

    /**
     * This reads in a file containing 3 numbers, into an array of 3 BigIntegers
     * @param infile The input file containing
     * @return
     */
    private static BigInteger[] readInValues(String infile) {
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
