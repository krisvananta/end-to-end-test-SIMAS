package tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.AdminDashboardPage;
import pages.LoginPage;
import pages.ProgramKerjaPage;
import pages.TambahProgramKerjaPage;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TambahProkerTest {

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
            driver.close();
            driver.quit();
            System.out.println("Browser ditutup.");
        }
    }

    @Test(priority = 1)
    public void test01_NavigasiKeHalamanLogin() {
        driver.get(baseUrl);
        WebElement masukButton = driver.findElement(By.linkText("Masuk"));
        try {
            masukButton.click();
        } catch (org.openqa.selenium.ElementClickInterceptedException e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", masukButton);
        }
        wait.until(ExpectedConditions.urlContains("/masuk"));
        Assert.assertTrue(driver.getCurrentUrl().contains("/masuk"));
        System.out.println("✅ Tes 1 Berhasil: Navigasi ke halaman login sukses.");
    }

    @Test(priority = 2, dependsOnMethods = "test01_NavigasiKeHalamanLogin")
    public void test02_LoginKeDashboard() {
        LoginPage loginPage = new LoginPage(driver, wait);
        loginPage.loginAsAdmin("adminrt1@gmail.com", "password");

        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loading-overlay")));

        // Verifikasi dengan menunggu menu 'Program Kerja' muncul. Ini lebih andal.
        WebElement menuProgramKerja = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[normalize-space()='Program Kerja']")));
        Assert.assertTrue(menuProgramKerja.isDisplayed(), "Menu Program Kerja tidak ditemukan setelah login.");

        System.out.println("✅ Tes 2 Berhasil: Login ke dashboard sukses.");
    }

    @Test(priority = 3, dependsOnMethods = "test02_LoginKeDashboard")
    public void test03_NavigasiKeTambahProker() {
        AdminDashboardPage adminDashboard = new AdminDashboardPage(driver, wait);
        adminDashboard.goToProgramKerja();

        ProgramKerjaPage programKerjaPage = new ProgramKerjaPage(driver, wait);
        programKerjaPage.clickAddNew();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("title")));
        Assert.assertTrue(driver.getCurrentUrl().contains("/tambah-program-kerja"));
        System.out.println("✅ Tes 3 Berhasil: Navigasi ke halaman tambah proker sukses.");
    }

    @Test(priority = 4, dependsOnMethods = "test03_NavigasiKeTambahProker")
    public void test04_ProsesTambahHinggaSelesai() {
        TambahProgramKerjaPage tambahProkerPage = new TambahProgramKerjaPage(driver, wait);
        String tanggalHariIni = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        String judulProker = "Proker Sukses Final " + System.currentTimeMillis();
        tambahProkerPage.fillForm(
                judulProker, "1", tanggalHariIni, "11:30", "Yogyakarta", "Deskripsi final."
        );
        tambahProkerPage.clickSimpan();
        tambahProkerPage.handleAlert(); // Handle 2 alert

        wait.until(ExpectedConditions.urlContains("/program-kerja/admin"));

        ProgramKerjaPage programKerjaPage = new ProgramKerjaPage(driver, wait);
        programKerjaPage.clickUpcomingTab();

        // Jeda singkat untuk melihat hasil
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("✅ Tes 4 Berhasil: Proses tambah proker dan pindah ke upcoming sukses.");
    }
}