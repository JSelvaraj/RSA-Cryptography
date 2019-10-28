import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static junit.framework.TestCase.assertTrue;

public class rsa_codeTest {

    @Test
    public void pq100txt() {
        String[] keygenArgs = {"p01-src/test/pq100", "pub", "priv"};
        rsa_keygen.main(keygenArgs);
        String[] encodeArgs = {"pub", "p01-src/test/udhr_eng.txt", "c1"};
        rsa_code.main(encodeArgs);
        String[] decodeArgs = {"-d", "priv", "c1", "p1"};
        rsa_code.main(decodeArgs);
        try {
            assertTrue(FileUtils.contentEquals(new File("p01-src/test/udhr_eng.txt"), new File("p1")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void pq500txt() {
        String[] keygenArgs = {"p01-src/test/pq500", "pub", "priv"};
        rsa_keygen.main(keygenArgs);
        String[] encodeArgs = {"pub", "p01-src/test/udhr_eng.txt", "c1"};
        rsa_code.main(encodeArgs);
        String[] decodeArgs = {"-d", "priv", "c1", "p1"};
        rsa_code.main(decodeArgs);
        try {
            assertTrue(FileUtils.contentEquals(new File("p01-src/test/udhr_eng.txt"), new File("p1")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void pq2000txt() {
        String[] keygenArgs = {"p01-src/test/pq2000", "pub", "priv"};
        rsa_keygen.main(keygenArgs);
        String[] encodeArgs = {"pub", "p01-src/test/udhr_eng.txt", "c1"};
        rsa_code.main(encodeArgs);
        String[] decodeArgs = {"-d", "priv", "c1", "p1"};
        rsa_code.main(decodeArgs);
        try {
            assertTrue(FileUtils.contentEquals(new File("p01-src/test/udhr_eng.txt"), new File("p1")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void pq100pic() {
        String[] keygenArgs = {"p01-src/test/pq100", "pub", "priv"};
        rsa_keygen.main(keygenArgs);
        String[] encodeArgs = {"pub", "p01-src/test/pic.jpg", "c1"};
        rsa_code.main(encodeArgs);
        String[] decodeArgs = {"-d", "priv", "c1", "pic.jpg"};
        rsa_code.main(decodeArgs);
        try {
            assertTrue(FileUtils.contentEquals(new File("p01-src/test/pic.jpg"), new File("pic.jpg")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void pq500pic() {
        String[] keygenArgs = {"p01-src/test/pq500", "pub", "priv"};
        rsa_keygen.main(keygenArgs);
        String[] encodeArgs = {"pub", "p01-src/test/pic.jpg", "c1"};
        rsa_code.main(encodeArgs);
        String[] decodeArgs = {"-d", "priv", "c1", "pic.jpg"};
        rsa_code.main(decodeArgs);
        try {
            assertTrue(FileUtils.contentEquals(new File("p01-src/test/pic.jpg"), new File("pic.jpg")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void pq2000pic() {
        String[] keygenArgs = {"p01-src/test/pq2000", "pub", "priv"};
        rsa_keygen.main(keygenArgs);
        String[] encodeArgs = {"pub", "p01-src/test/pic.jpg", "c1"};
        rsa_code.main(encodeArgs);
        String[] decodeArgs = {"-d", "priv", "c1", "pic.jpg"};
        rsa_code.main(decodeArgs);
        try {
            assertTrue(FileUtils.contentEquals(new File("p01-src/test/pic.jpg"), new File("pic.jpg")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void pq100bin() {
        String[] keygenArgs = {"p01-src/test/pq100", "pub", "priv"};
        rsa_keygen.main(keygenArgs);
        String[] encodeArgs = {"pub", "p01-src/test/blah.bin", "c1"};
        rsa_code.main(encodeArgs);
        String[] decodeArgs = {"-d", "priv", "c1", "p1"};
        rsa_code.main(decodeArgs);
        try {
            assertTrue(FileUtils.contentEquals(new File("p01-src/test/blah.bin"), new File("p1")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void pq500bin() {
        String[] keygenArgs = {"p01-src/test/pq500", "pub", "priv"};
        rsa_keygen.main(keygenArgs);
        String[] encodeArgs = {"pub", "p01-src/test/blah.bin", "c1"};
        rsa_code.main(encodeArgs);
        String[] decodeArgs = {"-d", "priv", "c1", "p1"};
        rsa_code.main(decodeArgs);
        try {
            assertTrue(FileUtils.contentEquals(new File("p01-src/test/blah.bin"), new File("p1")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void pq2000bin() {
        String[] keygenArgs = {"p01-src/test/pq2000", "pub", "priv"};
        rsa_keygen.main(keygenArgs);
        String[] encodeArgs = {"pub", "p01-src/test/blah.bin", "c1"};
        rsa_code.main(encodeArgs);
        String[] decodeArgs = {"-d", "priv", "c1", "p1"};
        rsa_code.main(decodeArgs);
        try {
            assertTrue(FileUtils.contentEquals(new File("p01-src/test/blah.bin"), new File("p1")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    @AfterClass
    public static void cleanup() {
        FileUtils.deleteQuietly(new File("priv"));
        FileUtils.deleteQuietly(new File("pub"));
        FileUtils.deleteQuietly(new File("out1"));
        FileUtils.deleteQuietly(new File("out2"));
        FileUtils.deleteQuietly(new File("c1"));
        FileUtils.deleteQuietly(new File("pic.jpg"));
//        FileUtils.deleteQuietly(new File("p1"));
    }

}