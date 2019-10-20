import java.math.BigInteger;

public class rsa_code {


    public static void main(String[] args) {
        BigInteger n = new BigInteger("256").pow(15);
//        BigInteger x = new BigInteger(n);
        System.out.println(findNearestPower256(n.add(new BigInteger("1"))));

    }

    /**
     * This algorithm calculates the value of k such that x is between 256^k and 256^(k+1). This algorithm first continually squares
     * 256 and stores the exponent to get an upper limit for the exponent then uses a binary search method to find the correct k.
     * This has a worst case scenario of O(log n) time because if the correct k is +/-1 from a power of 2 the algorithm will have to check
     * approximately log2 of k number of values for k
     * @param x the maximum value and set of octets can be
     * @return the value of k.
     */
    private static BigInteger findNearestPower256(BigInteger x) {
        BigInteger blockSize = new BigInteger("256");
        if (x.compareTo(blockSize) == 0) {
            return new BigInteger("1");
        }
        BigInteger lowerk = new BigInteger("1");
        BigInteger upperk = new BigInteger("1");


        while (blockSize.compareTo(x) < 0) {
            blockSize = blockSize.multiply(blockSize);
            upperk = upperk.multiply(new BigInteger("2"));
            lowerk = upperk.divide(new BigInteger("2"));
        }

        while ((upperk.subtract(lowerk)).compareTo(new BigInteger("1")) != 0) { // (upperk-lowerk) != 1
            BigInteger tempK;
            if (blockSize.compareTo(x) == 0) {
                return upperk;
            } else {
                tempK = (upperk.add(lowerk)).divide(new BigInteger("2"));// tempk = (upperk - lowerk) /2
                blockSize = get256toPowerK(tempK);
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
     * @param exponent the power of 256 that is trying to be found.
     * @return 256^exponent
     */
    private static BigInteger get256toPowerK (BigInteger exponent) {
        if (exponent.compareTo(new BigInteger("1")) == 0) {
            return new BigInteger("256");
        } else if ((exponent.mod(new BigInteger("2"))).compareTo(new BigInteger("0")) == 0) { //if exponent is even
            BigInteger k = get256toPowerK(exponent.divide(new BigInteger("2")));
            return k.multiply(k);
        } else {//if exponent is odd
            BigInteger k = get256toPowerK((exponent.subtract(new BigInteger("1"))).divide(new BigInteger("2")));
            return (k.multiply(k)).multiply(new BigInteger("256"));
        }
    }
}
