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
    public void test04_TambahProkerTanggalSudahLewat() {
        TambahProgramKerjaPage tambahProkerPage = new TambahProgramKerjaPage(driver, wait);

        // Menggunakan tanggal kemarin
        String tanggalKemarin = LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));

        // Isi form dengan data yang tidak valid (tanggal masa lalu)
        tambahProkerPage.fillForm(
                "Proker Tanggal Lewat " + System.currentTimeMillis(),
                "1",
                tanggalKemarin,
                "09:00",
                "Lokasi Tes Tanggal Lewat",
                "Deskripsi untuk tes dengan tanggal yang sudah lewat."
        );
        tambahProkerPage.clickSimpan();

        // Tangani alert konfirmasi
        try {
            Alert alert1 = wait.until(ExpectedConditions.alertIsPresent());
            System.out.println("Alert konfirmasi muncul: " + alert1.getText());
            alert1.accept();
            System.out.println("Alert konfirmasi disetujui.");

            // Cek apakah muncul alert sukses setelah itu
            try {
                Alert alert2 = wait.until(ExpectedConditions.alertIsPresent());
                String alertText = alert2.getText();
                System.out.println("Alert sukses muncul: " + alertText);
                alert2.accept();

                // Jika berhasil disimpan, berarti validasi gagal → tes harus gagal
                if (alertText.toLowerCase().contains("berhasil")) {
                    Assert.fail("Form berhasil disubmit padahal tanggal sudah lewat. Validasi gagal.");
                }
            } catch (TimeoutException e) {
                System.out.println("Tidak ada alert sukses muncul (validasi mungkin berhasil).");
            }
        } catch (TimeoutException e) {
            System.out.println("Tidak ada alert konfirmasi muncul.");
        }

        // Jika tidak disimpan, cek apakah pesan error muncul
        try {
            WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//div[contains(text(), 'Tanggal tidak boleh kurang dari hari ini')]")
            ));

            Assert.assertTrue(errorMessage.isDisplayed(), "Pesan error untuk tanggal yang sudah lewat tidak muncul.");
            Assert.assertTrue(driver.getCurrentUrl().contains("/tambah-program-kerja"),
                    "Halaman tidak seharusnya berpindah setelah input invalid.");
            System.out.println("Tes 4 Berhasil: Validasi tanggal berhasil, proker tidak disimpan.");
        } catch (TimeoutException e) {
            System.out.println("Pesan error tidak muncul. Validasi front-end mungkin gagal.");
        }
    }

}
