import java.io.*;
import java.math.BigInteger;
import java.util.Scanner;

public class rsa_keygen {

    public static void main(String[] args) {
        if (args.length < 3) {
            System.err.println("Usage:  rsa_keygen infile outfile1 outfile2");
            System.exit(-1);
        }
        rsa_keygen keygen = new rsa_keygen();
//        BigInteger test_val_1 = new BigInteger("15");
//        BigInteger test_val_2 = new BigInteger("3");
//        BigInteger test_val_3 = new BigInteger("5");
        BigInteger[] values = keygen.readInValues(args[0]);
        BigInteger p = values[0];
        BigInteger q = values[1];
        BigInteger x = values[2];
        BigInteger n = p.multiply(q);
        BigInteger fn = (p.subtract(new BigInteger("1"))).multiply(q.subtract(new BigInteger("1")));
        if (!keygen.isPrime(fn, x)) {
            System.err.println("Unsuitable exponent\n");
            System.exit(-1);
        }
        BigInteger y = keygen.modInverse( x, fn);
        keygen.printValues(n, x, y, args[1], args[2]);
        System.exit(0);


//        System.out.println(x.modInverse(fn) + "done");
//        System.out.println(keygen.modInverse(x, fn));

//        System.out.println(x.multiply(keygen.modInverse(x, fn)).mod(fn));
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
    public BigInteger[] readInValues(String infile) {
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
            System.err.println("Unsuitable exponent\n");
            System.exit(-1);
        }
        return null;
    }

    /**
     * This method should identify the modular inverse of a number using extended euclidean algorithm.
     * i.e. it finds a^-1 mod b
     * @param a the number whose modular inverse needs to be found
     * @param mod the mod value
     * @return the modular inverse of a
     */
    public BigInteger modInverse(BigInteger a, BigInteger mod) {
        BigInteger originalMod =  new BigInteger(mod.toByteArray());
        BigInteger y = new BigInteger("0");
        BigInteger x = new BigInteger(("1")); // will hold the modular inverse

        if (mod.equals(new BigInteger("1"))) {
            return new BigInteger("0");
        }
        /* this while loop iteratively does the euclidean algorithm while updating
        a value 'x' that will eventually be the modular inverse of a */
        while (a.compareTo(new BigInteger("1")) == 1) {
            BigInteger quotient = a.divide(mod);
            BigInteger t = mod;

            mod = a.mod(mod);
            a = t;
            t = y;

            y = x.subtract(quotient.multiply(y));
            x = t;
        }
        if (x.compareTo(new BigInteger("0")) == -1) {
            x = x.add(originalMod);
        }
        return x;
    }

    /**
     * This method will write the various calculated values to a files named in the original terminal command.
     * @param pq the value of p * q
     * @param x the value x
     * @param y the value y
     * @param outfile1 the directory of the file that will contain pq and x
     * @param outfile2 the directory of the file that will contain pq and y
     */
    public void printValues(BigInteger pq, BigInteger x, BigInteger y, String outfile1, String outfile2) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(outfile1));
            BufferedWriter writer2 = new BufferedWriter(new FileWriter(outfile2));
            writer.write(pq.toString() + " " + x.toString());
            writer2.write(pq.toString() + " " + y.toString());
            writer.close();
            writer2.close();
        } catch (IOException e) {

        }

    }


}
