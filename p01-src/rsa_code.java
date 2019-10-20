import java.math.BigInteger;

public class rsa_code {


    public static void main(String[] args) {
        BigInteger x = new BigInteger("16777215");
        System.out.println(findNearestPower256(x));

    }

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
