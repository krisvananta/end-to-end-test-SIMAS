package org.example.tests;


import org.testng.Assert;
import org.testng.annotations.Test;
import pages.LoginPage;
import utils.BaseTest;

public class LoginTest extends BaseTest {

    @Test
    public void loginWithValidCredentials_shouldSucceed() throws InterruptedException {
        driver.get("http://localhost:8000/login");

        LoginPage login = new LoginPage(driver);
        login.loginAs("admin@simas.test", "password123"); // Sesuaikan kredensial test Anda

        Thread.sleep(2000); // Tunggu response dari axios dan redirection

        Assert.assertTrue(login.getSuccessMessage().contains("Login berhasil"), "Pesan sukses tidak muncul");
    }

    @Test
    public void loginWithInvalidCredentials_shouldShowError() {
        driver.get("http://localhost:8000/login");

        LoginPage login = new LoginPage(driver);
        login.loginAs("admin@simas.test", "salahpassword");

        Assert.assertTrue(login.getErrorMessage().length() > 0, "Pesan error tidak tampil");
    }
}
