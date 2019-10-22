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
                rsa_code.decode(values[0], values[1]);
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

        while (blockSize.compareTo(x) < 0) {
            blockSize = blockSize.multiply(blockSize);
            upperk = upperk * 2;
            lowerk = upperk / 2;
        }

        while ((upperk - lowerk) != 1) { // (upperk-lowerk) != 1
            int tempK;
            if (blockSize.compareTo(x) == 0) {
                return upperk;
            } else {
                tempK = (upperk + lowerk) / 2; // tempk = (upperk - lowerk) /2
                blockSize = repeatedSquaring(tempK);
            }
            int compare = blockSize.compareTo(x);
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
                BigInteger plaintextBlock = new BigInteger(1, array);
                byte[] cipertextBlock = modularExponentiation(plaintextBlock, exponent, N).toByteArray();


                /* This part writes the a 64 bit block with the count of plaintext blocks encoded */
                long longcount = (long) count;
                ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
                buffer.putLong(longcount);
                outputStream.write(buffer.array());


                /* This part writes the ciphertext block to k + 1 bytes in outfile */
                outputStream.write(new byte[(k + 1) - cipertextBlock.length]);
                outputStream.write(cipertextBlock);



                /* resets the byte array and  */
                array = new byte[k];
                count = inputStream.read(array);
                if (count < k && count != -1) { // If the last set of octets is smaller than k it ensure the byte array doesn't have trailing 0's
                    array = Arrays.copyOfRange(array, 0, count);
                }
            }
        } catch (IOException e) {
            System.err.println("Message not Found\n");
            System.exit(-1);
        }
    }

    /**
     * This function recursively calculates a^exponent mod mod. If the exponent is odd it recursively calls itself again with one less exponent
     * the result is then multiplied with a. If the exponent is even it recursively calls itself but divides the exponent by 2
     *
     * @param a
     * @param exponent
     * @param mod
     * @return
     */
    private BigInteger modularExponentiation(BigInteger a, BigInteger exponent, BigInteger mod) {
        if (exponent.compareTo(new BigInteger("1")) == 0) {
            return a;
        } else if ((exponent.mod(new BigInteger("2"))).compareTo(new BigInteger("0")) == 0) { //if exponent is even
            BigInteger k = modularExponentiation(a, exponent.divide(new BigInteger("2")), mod);
            return (k.multiply(k)).mod(mod);
        } else { //if exponent is odd
            BigInteger k = modularExponentiation(a, (exponent.subtract(new BigInteger("1"))).divide(new BigInteger("2")), mod);
            return (((k.multiply(k)).mod(mod)).multiply(a)).mod(mod);
        }
    }

    /**
     * @param N        The mod
     * @param exponent the private key number
     */
    public void decode(BigInteger N, BigInteger exponent) {
        try {
            byte[] kArray = new byte[8];
            byte[] intkArray;
            int count = inputStream.read(kArray); //reads in first 64bit count block
            ByteBuffer buffer = ByteBuffer.allocate(4);
            intkArray = Arrays.copyOfRange(kArray, 4, 8);
            buffer.put(intkArray);
            int expectedNumberOfbytes = buffer.getInt(0);
            /* The first instance of k should be the (length - 1)
            of each ciphertext blocks (even the last one) */
            int k = expectedNumberOfbytes;


            while (count > -1) {
                if (count < 8) {
                    System.err.println("Improperly Formatted input\n");
                    System.exit(-1);
                }
                byte[] cipherArray = new byte[k + 1];
                inputStream.read(cipherArray);
                BigInteger ciphertextBlock = new BigInteger(1, cipherArray);
                BigInteger plaintext = modularExponentiation(ciphertextBlock, exponent, N);
                byte[] plaintextBytes = plaintext.toByteArray();
                if (plaintext.compareTo(N) > -1 ||
                        plaintextBytes.length > expectedNumberOfbytes ||
                        ciphertextBlock.compareTo(N) > -1) { // if plaintext > N
                    System.err.println("Incorrect input");
                    System.exit(-1);
                }
                outputStream.write(plaintextBytes);

                count = inputStream.read(kArray);

                buffer = ByteBuffer.allocate(4);
                intkArray = Arrays.copyOfRange(kArray, 4, 8);
                buffer.put(intkArray);
                expectedNumberOfbytes = buffer.getInt(0);

            }

        } catch (IOException e) {
            System.err.println("Improperly Formatted input\n");
            System.exit(-1);
        }
    }

    /**
     * this is a helper function that reads in the keyfile and gives an array containing the mod and the public/private key
     * separated by a whitespace.
     * @param infile
     * @return
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
                System.err.println("Input error");

            }
            return values;
        } catch (FileNotFoundException e) {
            System.err.println("File not Found\n");
            System.exit(-1);
        }
        return null;

    }

}


