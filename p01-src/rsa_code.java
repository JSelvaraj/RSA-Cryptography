import java.io.*;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Scanner;


public class rsa_code {

    FileOutputStream outputStream;
    FileInputStream inputStream;


    public rsa_code(String infile, String outfile) {
        try {
            inputStream = new FileInputStream(infile);
            outputStream = new FileOutputStream(outfile);
        } catch (IOException e) {
            System.err.println("Message not Found\n");
            System.exit(-1);
        }
    }

    public static void main(String[] args) {
        rsa_code rsa_code;
        BigInteger[] values;
        switch (args.length) {
            case 3:
                if (args[0].equals("-d")) {
                    System.out.println("Usage: rsa-code [-d] keyfile message output");
                    System.exit(-1);
                }
                rsa_code = new rsa_code(args[1], args[2]);
                values = rsa_code.readInValues(args[0]);
                rsa_code.encode(values[0], rsa_code.findNearestPower256(values[0]), values[1]);
                break;
            case 4:
                if (!args[0].equals("-d")) {
                    System.out.println("Usage: rsa-code [-d] keyfile message output");
                    System.exit(-1);
                }
                rsa_code = new rsa_code(args[2], args[3]);
                values = rsa_code.readInValues(args[1]);
                rsa_code.decode(values[0], rsa_code.findNearestPower256(values[0]), values[1]);
                break;
            default:
                System.out.println("Usage: rsa-code [-d] keyfile message output");
                System.exit(-1);
                break;
        }
    }

    /**
     * This algorithm calculates the value of k such that x is between 256^k and 256^(k+1). This algorithm first continually squares
     * 256 and stores the exponent to get an upper limit for the exponent then uses a binary search method to find the correct k.
     * This has a worst case scenario of O(log n) time because if the correct k is +/-1 from a power of 2 the algorithm will have to check
     * approximately log2 of k number of values for k
     *
     * @param x the maximum value and set of octets can be
     * @return the value of k.
     */
    public int findNearestPower256(BigInteger x) {
        BigInteger blockSize = new BigInteger("256");
        if (x.compareTo(blockSize) == 0) {
            return 1;
        }
        int lowerk = 1;
        int upperk = 1;

        /* This establishes an upper limit and a lower limit to find k*/
        while (blockSize.compareTo(x) < 0) { // if blocksize <= x
            blockSize = blockSize.multiply(blockSize);
            upperk = upperk * 2;
        }
        lowerk = upperk / 2;

        /* a modified binary search algorithm to find k */
        while ((upperk - lowerk) != 1) { // while the difference between the limits isn't one.
            int tempK;
            if (blockSize.compareTo(x) == 0) { // if blockSize == x
                return upperk;
            } else {
                tempK = (upperk + lowerk) / 2; // tempk = (upperk - lowerk) /2
                blockSize = repeatedSquaring(tempK);
            }
            int compare = blockSize.compareTo(x);
            /* This compares the current power of 256 to k and adjusts the limit according to whether the power its larger, smaller or equal to */
            if (compare >= 1) {
                upperk = tempK;
            } else if (compare <= -1) {
                lowerk = tempK;
            } else {
                return tempK;
            }
        }
        return lowerk;
    }

    /**
     * This algorithm calculates powers of 256 used repeated squaring to reduce the number of calculations required
     *
     * @param exponent the power of 256 that is trying to be found.
     * @return 256^exponent
     */
    public BigInteger repeatedSquaring(int exponent) {
        if (exponent == 1) {
            return new BigInteger("256");
        } else if ((exponent % 2) == 0) { //if exponent is even
            BigInteger k = repeatedSquaring(exponent / 2);
            return k.multiply(k);
        } else { //if exponent is odd
            BigInteger k = repeatedSquaring((exponent - 1) / 2);
            return (k.multiply(k)).multiply(new BigInteger("256"));
        }
    }


    /**
     * @param N        the mod
     * @param k        the number of plaintext bytes encrypted at a time
     * @param exponent the public key number
     */
    public void encode(BigInteger N, int k, BigInteger exponent) {
        try {
            byte[] array = new byte[k];
            int count = inputStream.read(array);
            while (count != -1) {
                /* this ensures that when it gets tot he last block of plaintext, it only encrypts the correct bytes */
                array = Arrays.copyOfRange(array, 0, count);

                BigInteger plaintextBlock = new BigInteger(1, array);
                BigInteger ciphertext = modularExponentiation(plaintextBlock, exponent, N);
                byte[] cipertextBlock = modularExponentiation(plaintextBlock, exponent, N).toByteArray();

                /* This part writes the a 8 byte 'count' block with the count of plaintext blocks encoded */
                long longcount = (long) count;
                ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
                buffer.putLong(longcount);
                byte[] size = buffer.array();
                for (int i = 7; i >= 0; i--) {
                    outputStream.write(size[i]);
                }

                /* This part writes the ciphertext block to k + 1 bytes in outfile */
                if (cipertextBlock.length <= k + 1) {
                    outputStream.write(new byte[(k + 1) - cipertextBlock.length]);
                } else {
                    outputStream.write(cipertextBlock, 1,cipertextBlock.length);
                }
                outputStream.write(cipertextBlock);

                /* resets the byte array and reads a new set of plaintext */
                array = new byte[k];
                count = inputStream.read(array);
            }
//            outputStream.write('\n');
        } catch (IOException e) {
            System.err.println("Message not Found\n");
            System.exit(-1);
        }
    }

    /**
     * This function recursively calculates a^exponent mod mod. If the exponent is odd it recursively calls itself again with one less exponent
     * the result is then multiplied with a. If the exponent is even it recursively calls itself but divides the exponent by 2
     *
     * @param a        the number being powered
     * @param exponent the number 'a' is being powered to
     * @param mod      the modulus
     * @return a^exponent mod 'mod'
     */
    private BigInteger modularExponentiation(BigInteger a, BigInteger exponent, BigInteger mod) {


        BigInteger result = new BigInteger("1");

        while ( exponent.compareTo(new BigInteger("0")) > 0) {
            /* If there's a 1 in the bit string the result multiplied by a power of a */
            if ((exponent.and(new BigInteger("1")).compareTo(new BigInteger("1"))) == 0) {
                result = (result.multiply(a)).mod(mod);
            }
            /* Checks moves along the bit string representing the exponent */
            exponent = exponent.shiftRight(1);
            /* Because each 1 represents a higher power of 2, the initial value has to be squared */
            a = (a.multiply(a)).mod(mod);
        }
        return result;



//        if (exponent.compareTo(new BigInteger("1")) == 0) { // base case 1: if exponent == 1
//            return a;
//        } else if (exponent.compareTo(new BigInteger("0")) == 0) { // base case 2: if exponent == 0
//            return new BigInteger("1");
//        } else if ((exponent.mod(new BigInteger("2"))).compareTo(new BigInteger("0")) == 0) { //if exponent is even
//            BigInteger k = modularExponentiation(a, exponent.divide(new BigInteger("2")), mod);
//            return (k.multiply(k)).mod(mod);
//        } else { //if exponent is odd
//            BigInteger k = modularExponentiation(a, (exponent.subtract(new BigInteger("1"))).divide(new BigInteger("2")), mod);
//            return (((k.multiply(k)).mod(mod)).multiply(a)).mod(mod);
//        }
    }

    /**
     * @param N        The mod
     * @param exponent the private key number
     */
    public void decode(BigInteger N, int k, BigInteger exponent) {
        try {
            /* Reads the first 8 bytes to get the k value */

            byte[] kArray = new byte[8];
            byte[] reversekArray = new byte[8];
            byte[] intkArray;
            int count = inputStream.read(kArray);
            for (int i = 7; i >= 0; i--) {
                reversekArray[7-i] = kArray[i];
            }
//            ArrayUtils.reverse(kArray);
            if (count < 8) {
                System.err.println("Improperly Formatted input\n");
                System.exit(-1);
            }
            /* converts k from an 8 byte long to a 4 byte int  */
            ByteBuffer buffer = ByteBuffer.allocate(4);
            intkArray = Arrays.copyOfRange(reversekArray, 4, 8);
            buffer.put(intkArray);
            int expectedNumberOfbytes = buffer.getInt(0);

            while (count > -1) {
                /* Reads the first k bytes, decodes it and checks that the decoding is valid */
                byte[] cipherArray = new byte[k + 1];
                inputStream.read(cipherArray);
                BigInteger ciphertextBlock = new BigInteger(1, cipherArray);
                /* Decodes the first k blocks using modular exponentiation */
                BigInteger plaintext = modularExponentiation(ciphertextBlock, exponent, N);
                byte[] plaintextBytes = plaintext.toByteArray();
                if (plaintext.compareTo(new BigInteger("0")) == 0) {
                    plaintextBytes = new byte[expectedNumberOfbytes];
                }
                else if (plaintextBytes[0] == 0 && plaintextBytes.length > expectedNumberOfbytes) {
                    plaintextBytes = Arrays.copyOfRange(plaintextBytes, 1, plaintextBytes.length); // sometimes byte[1] is a negative number because byte[0] was 1, this translates to a 0 block in the plaintext bytes

                }
                if (plaintext.compareTo(N) > -1 ||
                        plaintextBytes.length > expectedNumberOfbytes ||
                        ciphertextBlock.compareTo(N) > -1) { // if plaintext > N
                    System.err.println("Incorrect input");
                    System.exit(-1);
                }
                outputStream.write(new byte[expectedNumberOfbytes-plaintextBytes.length]);
                outputStream.write(plaintextBytes);

                /* reads the next count block */
                count = inputStream.read(kArray);
                for (int i = 7; i >= 0; i--) {
                    reversekArray[7-i] = kArray[i];
                }
                buffer = ByteBuffer.allocate(4); // resets the buffer
                intkArray = Arrays.copyOfRange(reversekArray, 4, 8);
                buffer.put(intkArray);
                expectedNumberOfbytes = buffer.getInt(0);

            }

//            outputStream.write('\n');
            outputStream.close();
        } catch (IOException e) {
            System.err.println("Improperly Formatted input\n");
            System.exit(-1);
        }

    }

    /**
     * this is a helper function that reads in the keyfile and gives an array containing the mod and the public/private key
     * separated by a whitespace.
     *
     * @param infile the keyfile.
     * @return an array containing the N value and the exponent value
     */
    private BigInteger[] readInValues(String infile) {
        try {
            String[] numbers = new String[2];
            BigInteger[] values = new BigInteger[2];
            FileReader reader = new FileReader(infile);
            Scanner scanner = new Scanner(reader);
            for (int i = 0; i < 2; i++) {
                numbers[i] = scanner.next();
                values[i] = new BigInteger(numbers[i]);
            }
            if (values[0].compareTo(new BigInteger("256")) < 0 || values[1].compareTo(new BigInteger("0")) < 0) { // if N < 256 or exponent is negative
                System.err.println("Input error: N too small or exponent is negative");
                System.exit(-1);
            }
            return values;
        } catch (FileNotFoundException e) {
            System.err.println("File not Found\n");
            System.exit(-1);
        }
        return null;

    }

}


