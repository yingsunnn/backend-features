package ying.backend_features.utils;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import static junitparams.JUnitParamsRunner.$;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Created by ying on 2017-04-14.
 */
@RunWith(JUnitParamsRunner.class)
public class AuthenticationUtilsTest {
    private AuthenticationUtils authenticationUtils;

    @Before
    public void init() throws UnsupportedEncodingException {
        authenticationUtils = new AuthenticationUtils();
    }

    @Test
    @Parameters({"64", "128"})
    public void testGetSalt(int length) {

        try {
            byte[] actualSalt = AuthenticationUtils.getSalt(length);
            String saltStr = Base64.getEncoder().encodeToString(actualSalt);
            byte[] expactedSalt = Base64.getDecoder().decode(saltStr);

            assertEquals(byteArr2String(expactedSalt), byteArr2String(actualSalt));
        } catch (NoSuchAlgorithmException e) {
            fail();
        }
    }

    private String byteArr2String(byte[] bs) {
        StringBuilder bytesStr = new StringBuilder();
        for (byte b : bs) {
            bytesStr.append(b).append(" ");
        }
        return bytesStr.toString();
    }

    @Test
    @Parameters(method = "testGetSecurePasswordParameters")
    public void testGetSecurePassword(String password) {
        try {
            authenticationUtils.getSecurePassword(password, authenticationUtils.getSalt(64));
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private Object testGetSecurePasswordParameters() {
        return $(
                $("123456"),
                $("ewrwrwr")
        );
    }

}
