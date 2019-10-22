import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

class rsa_codeTest {

    @Test
    void findNearestPower256() {
    }

    @Test
    void modularExponentiation() {
    }

    @Test
    void encode() {
        rsa_code rsa_code = new rsa_code("infile", "outfile");
        rsa_code.encode(new BigInteger("78899"), 2, new BigInteger("5"));
    }

    @Test
    void decode() {
        rsa_code rsa_code = new rsa_code("outfile", "result");
        rsa_code.decode(new BigInteger("78899"), new BigInteger("62669"));
    }


}