package org.example.tests;


import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.LoginPage;
import utils.BaseTest;

public class DashboardTest extends BaseTest {

    @BeforeMethod
    public void loginFirst() throws InterruptedException {
        driver.get("http://localhost:8000/login");

        LoginPage login = new LoginPage(driver);
        login.loginAs("admin@simas.test", "password123"); // sesuaikan dengan test credential

        Thread.sleep(2000); // waktu tunggu untuk proses login + redirect
    }

    @Test(priority = 1)
    public void testDashboardHeader() {
        DashboardPage dashboard = new DashboardPage(driver);
        Assert.assertTrue(dashboard.isGreetingDisplayed(), "Greeting tidak muncul");
        Assert.assertFalse(dashboard.getAdminName().isEmpty(), "Nama admin kosong");
    }

    @Test(priority = 2)
    public void testDataWargaLoaded() {
        DashboardPage dashboard = new DashboardPage(driver);
        Assert.assertTrue(dashboard.isWargaLoaded(), "Data warga masih loading");
    }

    @Test(priority = 3)
    public void testKritikSaranLoaded() {
        DashboardPage dashboard = new DashboardPage(driver);
        Assert.assertTrue(dashboard.isKritikLoaded(), "Kritik dan Saran masih loading");
    }

    @Test(priority = 4)
    public void testPembayaranLoaded() {
        DashboardPage dashboard = new DashboardPage(driver);
        Assert.assertTrue(dashboard.isPembayaranLoaded(), "Pembayaran masih loading");
    }

    @Test(priority = 5)
    public void testFinancialDataVisible() {
        DashboardPage dashboard = new DashboardPage(driver);
        Assert.assertTrue(dashboard.isFinancialAmountVisible(), "Pemasukan / Pengeluaran masih loading");
    }

    @Test(priority = 6)
    public void testChartsRendered() {
        DashboardPage dashboard = new DashboardPage(driver);
        Assert.assertTrue(dashboard.isPieChartVisible(), "Pie chart tidak tampil");
        Assert.assertTrue(dashboard.isBarChartVisible(), "Bar chart tidak tampil");
        Assert.assertTrue(dashboard.isLineChartVisible(), "Line chart tidak tampil");
    }
}
