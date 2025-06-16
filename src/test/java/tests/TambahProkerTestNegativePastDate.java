package tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.*;

import java.time.Duration;

public class TambahProkerTestNegativePastDate {

    private WebDriver driver;
    private WebDriverWait wait;
    private final String baseUrl = "http://sirtrw.vansite.cloud/";

    @BeforeClass
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            System.out.println("Menutup browser...");
            driver.quit();
            System.out.println("Browser ditutup.");
        }
    }

    @Test(priority = 1)
    public void test01_NavigasiKeHalamanLogin() {
        driver.get(baseUrl);
        LandingPage landingPage = new LandingPage(driver, wait);
        landingPage.clickMasuk();
        wait.until(ExpectedConditions.urlContains("/masuk"));
        Assert.assertTrue(driver.getCurrentUrl().contains("/masuk"));
        System.out.println("Tes 1 Berhasil: Navigasi ke halaman login sukses.");
    }

    @Test(priority = 2, dependsOnMethods = "test01_NavigasiKeHalamanLogin")
    public void test02_LoginKeDashboard() {
        LoginPage loginPage = new LoginPage(driver, wait);
        loginPage.loginAsAdmin("adminrt1@gmail.com", "password");
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loading-overlay")));
        WebElement menuProgramKerja = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//a[normalize-space()='Program Kerja']")));
        Assert.assertTrue(menuProgramKerja.isDisplayed(), "Menu Program Kerja tidak ditemukan setelah login.");
        System.out.println("Tes 2 Berhasil: Login ke dashboard sukses.");
    }

    @Test(priority = 3, dependsOnMethods = "test02_LoginKeDashboard")
    public void test03_NavigasiKeTambahProker() {
        AdminDashboardPage adminDashboard = new AdminDashboardPage(driver, wait);
        adminDashboard.goToProgramKerja();
        ProgramKerjaPage programKerjaPage = new ProgramKerjaPage(driver, wait);
        programKerjaPage.clickAddNew();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("title")));
        Assert.assertTrue(driver.getCurrentUrl().contains("/tambah-program-kerja"));
        System.out.println("Tes 3 Berhasil: Navigasi ke halaman tambah proker sukses.");
    }

    @Test(priority = 4, dependsOnMethods = "test03_NavigasiKeTambahProker")
    public void test04_BugReport_TambahDenganTanggalLewat() {
        TambahProgramKerjaPage tambahProkerPage = new TambahProgramKerjaPage(driver, wait);

        // --- Data untuk Skenario Bug ---
        // 1. Judul unik untuk identifikasi
        String judulProkerBug = "BUG REPORT - Proker Tanggal Lewat " + System.currentTimeMillis();
        // 2. Tanggal manual dari masa lalu dengan format
        String tanggalLewat = "01-01-2024";

        // --- Panggil `fillForm` dengan 6 argumen yang urutannya benar ---
        tambahProkerPage.fillForm(
                judulProkerBug,      // 1. Judul
                "1",                 // 2. Nomor RT/RW
                tanggalLewat,        // 3. Tanggal (Format yyyy-MM-dd)
                "09:30",             // 4. Waktu
                "Lokasi Laporan Bug",// 5. Lokasi
                "Deskripsi untuk laporan bug tanggal lewat." // 6. Deskripsi
        );

        tambahProkerPage.clickSimpan();

        // --- Logika untuk membuktikan bug ---
        // 1. Terima alert konfirmasi pertama
        wait.until(ExpectedConditions.alertIsPresent()).accept();

        // 2. Harapannya muncul alert SUKSES (inilah bug-nya)
        Alert successAlert = wait.until(ExpectedConditions.alertIsPresent());
        Assert.assertEquals(successAlert.getText(), "Program kerja berhasil ditambahkan!");
        successAlert.accept();

        // 3. Tunggu redirect setelah sukses
        wait.until(ExpectedConditions.urlContains("/program-kerja/admin"));

        // 4. Sengaja gagalkan tes untuk menandai BUG di laporan TestNG
        Assert.fail("BUG TERKONFIRMASI: Aplikasi berhasil menyimpan program kerja dengan tanggal yang sudah lewat.");
    }
}