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

public class TambahProkerTestNegative {

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
    public void test04_TambahProkerTanpaTanggal() {
        TambahProgramKerjaPage tambahProkerPage = new TambahProgramKerjaPage(driver, wait);

        // Isi form TANPA mengisi tanggal
        tambahProkerPage.fillForm(
                "Kerja Bakti RT 01",  // Judul
                "1",                  // RT
                "",                   // Tanggal kosong
                "09:00",              // Waktu
                "Lapangan",           // Lokasi
                "Membersihkan selokan"// Deskripsi
        );

        // Cek validitas tanggal sebelum klik simpan
        JavascriptExecutor js = (JavascriptExecutor) driver;
        Boolean isTanggalValid = (Boolean) js.executeScript(
                "return document.getElementById('date').checkValidity();"
        );
        String validationMsg = (String) js.executeScript(
                "return document.getElementById('date').validationMessage;"
        );

        Assert.assertFalse(isTanggalValid, "Field tanggal harus tidak valid saat kosong.");
        System.out.println("Pesan error browser: " + validationMsg);
        Assert.assertTrue(validationMsg.toLowerCase().contains("fill"), "Pesan error tidak sesuai harapan.");

        // Tidak perlu klik tombol simpan jika field tidak valid
        System.out.println("Tes 5 Berhasil: Validasi browser muncul saat tanggal kosong.");
    }

    @Test(priority = 5)
    public void test05_TambahProkerTanpaJudul() {
        TambahProgramKerjaPage tambahProkerPage = new TambahProgramKerjaPage(driver, wait);

        String tanggal = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));

        // Kosongkan input field judul secara eksplisit
        WebElement inputJudul = driver.findElement(By.id("title"));
        inputJudul.clear();
        // Isi form dengan judul kosong
        tambahProkerPage.fillForm(
                "",   // Judul kosong
                "1",
                tanggal,
                "11:30",
                "Yogyakarta",
                "Bersih desa"
        );

        // Validasi apakah input judul valid (HTML5 native validation)
        JavascriptExecutor js = (JavascriptExecutor) driver;
        Boolean isJudulValid = (Boolean) js.executeScript(
                "return document.getElementById('title').checkValidity();"
        );
        String validationMsg = (String) js.executeScript(
                "return document.getElementById('title').validationMessage;"
        );

        Assert.assertFalse(isJudulValid, "Field judul harus tidak valid saat kosong.");
        System.out.println("Pesan error browser: " + validationMsg);
        Assert.assertTrue(validationMsg.toLowerCase().contains("fill"), "Pesan error tidak sesuai harapan.");

        // Tidak klik tombol simpan karena form sudah tidak valid
        System.out.println("Tes 4 Berhasil: Validasi browser muncul saat judul kosong.");
    }


}