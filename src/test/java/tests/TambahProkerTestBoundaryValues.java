package tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TambahProkerTestBoundaryValues {

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

    /**
     * Skenario Boundary VALID: Dijalankan pertama (priority 4).
     * Tes ini diharapkan berhasil dan berakhir di halaman daftar proker.
     */
    @Test(priority = 4, dependsOnMethods = "test03_NavigasiKeTambahProker")
    public void test04_ValidBoundary_TambahDenganTanggalHariIni() {
        TambahProgramKerjaPage tambahProkerPage = new TambahProgramKerjaPage(driver, wait);

        String tanggalHariIni = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        String judulProkerValid = "TEST_BOUNDARY_VALID (Hari Ini) - " + System.currentTimeMillis();
        String deskripsiValid = "Input untuk tes boundary valid (tanggal hari ini)."; // Deskripsi disederhanakan

        tambahProkerPage.fillForm(
                judulProkerValid, "1", tanggalHariIni, "11:00", "Lokasi Boundary Valid", deskripsiValid
        );
        tambahProkerPage.clickSimpan();

        wait.until(ExpectedConditions.alertIsPresent()).accept();
        Alert successAlert = wait.until(ExpectedConditions.alertIsPresent());
        Assert.assertEquals(successAlert.getText(), "Program kerja berhasil ditambahkan!");
        successAlert.accept();
        wait.until(ExpectedConditions.urlContains("/program-kerja/admin"));

        System.out.println("Tes Boundary Valid Berhasil: Proker sukses dibuat dengan tanggal hari ini.");
        Assert.assertTrue(driver.getCurrentUrl().contains("/program-kerja/admin"));
    }

    /**
     * Skenario Boundary INVALID: Dijalankan kedua (priority 5).
     * Tes ini untuk laporan bug dan sengaja dibuat gagal.
     */
    @Test(priority = 5, dependsOnMethods = "test04_ValidBoundary_TambahDenganTanggalHariIni")
    public void test05_BugReport_TambahDenganTanggalLewat() {
        // Melanjutkan dari test04, kita navigasi lagi ke form tambah
        System.out.println("Memulai tes bug report dari halaman daftar proker...");
        ProgramKerjaPage programKerjaPage = new ProgramKerjaPage(driver, wait);
        programKerjaPage.clickAddNew();
        wait.until(ExpectedConditions.urlContains("/tambah-program-kerja"));

        TambahProgramKerjaPage tambahProkerPage = new TambahProgramKerjaPage(driver, wait);
        String judulProkerBug = "BUG REPORT - TEST_BOUNDARY_INVALID - Proker Kemarin " + System.currentTimeMillis();
        String tanggalKemarin = LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));

        tambahProkerPage.fillForm(
                judulProkerBug, "1", tanggalKemarin, "09:30", "Lokasi Bug", "Deskripsi bug."
        );
        tambahProkerPage.clickSimpan();

        wait.until(ExpectedConditions.alertIsPresent()).accept();
        Alert successAlert = wait.until(ExpectedConditions.alertIsPresent());
        Assert.assertEquals(successAlert.getText(), "Program kerja berhasil ditambahkan!");
        successAlert.accept();
        wait.until(ExpectedConditions.urlContains("/program-kerja/admin"));
        Assert.fail("BUG TERKONFIRMASI: Aplikasi berhasil menyimpan proker dengan tanggal kemarin.");
    }
}