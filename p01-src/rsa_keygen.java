import java.io.*;
import java.math.BigInteger;
import java.util.Scanner;

public class rsa_keygen {

    public static void main(String[] args) {
        if (args.length < 3) {
            System.err.println("Usage:  rsa_keygen infile outfile1 outfile2");
            System.exit(-1);
        }
        BigInteger[] values = new BigInteger[0];
        try {
            values = readInValues(args[0]);
        } catch (FileNotFoundException e) {
            System.err.println("File not Found\n");
            System.exit(-1);
        }
        BigInteger p = values[0];
        BigInteger q = values[1];
        BigInteger privatekey = values[2];
        BigInteger n = p.multiply(q);
        BigInteger fn = (p.subtract(new BigInteger("1"))).multiply(q.subtract(new BigInteger("1"))); // (p-1)(q-1)
        if (!isCoprime(fn, privatekey)) {
            System.err.println("Unsuitable exponent\n");
            System.exit(-1);
        }
        BigInteger publickey = modInverse(privatekey, fn);
        printValues(n, privatekey, publickey, args[1], args[2]);
        System.exit(0);
    }

    /**
     * This method checks whether 2 numbers, x and (p-1)(q-1) are coprime
     *
     * @param x the exponent
     * @param n this value is equal to (p-1)(q-1)
     * @return true if e and (p-1)(q-1) are coprime and false if not
     */
    public static boolean isCoprime(BigInteger n, BigInteger x) {
        return gcd(x, n).equals(new BigInteger("1"));
    }


    /**
     * applies Euclid's algorithm recursively to determine the greatest common divisor between the parameters a and b
     *
     * @param a operand 1
     * @param b (p-1)(q-1)
     * @return The greatest common divisor between a and b
     */
    private static BigInteger gcd(BigInteger a, BigInteger b) {
        if (a.equals(new BigInteger("0"))) {
            return b;
        }
        return gcd(b.mod(a), a);
    }

    /**
     * This reads in a file containing 3 numbers, into an array of 3 BigIntegers
     *
     * @param infile The input file containing p, q and x
     * @return a BigInteger array containing the 3 numbers from an input file. Null on a failure.
     */
    public static BigInteger[] readInValues(String infile) throws FileNotFoundException {

        String[] numbers = new String[3];
        BigInteger[] values = new BigInteger[3];
        FileReader reader = new FileReader(infile);
        Scanner scanner = new Scanner(reader);
        for (int i = 0; i < 3; i++) {
            numbers[i] = scanner.next();
            values[i] = new BigInteger(numbers[i]);
        }
        return values;

    }

    /**
     * This method should identify the modular inverse of a number using extended euclidean algorithm.
     * i.e. it finds a^-1 mod b
     *
     * It assumes the exponent and mod are coprime otherwise it will enter an infinite loop.
     *
     * @param exponent   the number whose modular inverse needs to be found
     * @param mod the mod value
     * @return the modular inverse of a
     */
    public static BigInteger modInverse(BigInteger exponent, BigInteger mod) {
        BigInteger originalMod = new BigInteger(mod.toByteArray());
        BigInteger y = new BigInteger("0");
        BigInteger x = new BigInteger(("1")); // will hold the modular inverse

        if (mod.equals(new BigInteger("1"))) {
            return new BigInteger("0");
        }
        /* this while loop iteratively does the euclidean algorithm while updating
        a value 'x' that will eventually be the modular inverse of a */
        while (exponent.compareTo(new BigInteger("1")) > 0) {
            BigInteger quotient = exponent.divide(mod);
            BigInteger t = mod;

            mod = exponent.mod(mod);
            exponent = t;
            t = y;

            y = x.subtract(quotient.multiply(y));
            x = t;
        }
        if (x.compareTo(new BigInteger("0")) < 0) {
            x = x.add(originalMod);
        }
        return x;
    }

    /**
     * This method will write the various calculated values to a files named in the original terminal command.
     *
     * @param pq       the value of p * q
     * @param x        the value x
     * @param y        the value y
     * @param outfile1 the directory of the file that will contain pq and x
     * @param outfile2 the directory of the file that will contain pq and y
     */
    public static void printValues(BigInteger pq, BigInteger x, BigInteger y, String outfile1, String outfile2) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(outfile1));
            BufferedWriter writer2 = new BufferedWriter(new FileWriter(outfile2));
            writer.write(pq.toString() + " " + x.toString());
            writer2.write(pq.toString() + " " + y.toString());
            writer.close();
            writer2.close();
        } catch (IOException e) {
            System.err.println("Files did not print correctly");
        }

    }


}
