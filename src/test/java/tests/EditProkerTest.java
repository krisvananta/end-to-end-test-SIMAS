package tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.*;

import java.time.Duration;

public class EditProkerTest {

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
            driver.quit();
            System.out.println("🔒 Browser ditutup setelah test.");
        }
    }

    @Test(priority = 1)
    public void test01_NavigasiKeHalamanLogin() {
        driver.get(baseUrl);

        // ✅ Gunakan LandingPage
        LandingPage landingPage = new LandingPage(driver, wait);
        landingPage.clickMasuk();

        wait.until(ExpectedConditions.urlContains("/masuk"));
        Assert.assertTrue(driver.getCurrentUrl().contains("/masuk"));
        System.out.println("✅ Navigasi ke halaman login sukses.");
    }

    @Test(priority = 2, dependsOnMethods = "test01_NavigasiKeHalamanLogin")
    public void test02_LoginKeDashboard() {
        LoginPage loginPage = new LoginPage(driver, wait);
        loginPage.loginAsAdmin("adminrt1@gmail.com", "password");

        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loading-overlay")));

        WebElement menuProgramKerja = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//a[normalize-space()='Program Kerja']")));
        Assert.assertTrue(menuProgramKerja.isDisplayed());
        System.out.println("✅ Login ke dashboard berhasil.");
    }

    @Test(priority = 3, dependsOnMethods = "test02_LoginKeDashboard")
    public void test03_NavigasiKeHalamanEdit() {
        AdminDashboardPage dashboard = new AdminDashboardPage(driver, wait);
        dashboard.goToProgramKerja();

        // ✅ Bisa dibuatkan helper di ProgramKerjaPage kalau mau lebih POM
        WebElement editButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("(//a[contains(@href,'/edit')])[1]")
        ));
        editButton.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("title")));
        Assert.assertTrue(driver.getCurrentUrl().contains("/edit"));
        System.out.println("✅ Berhasil masuk halaman Edit Program Kerja.");
    }

    @Test(priority = 4, dependsOnMethods = "test03_NavigasiKeHalamanEdit")
    public void test04_EditDataProker() {
        EditProgramKerjaPage editPage = new EditProgramKerjaPage(driver, wait);

        String newJudul = "Edit Proker Otomatis " + System.currentTimeMillis();

        editPage.editForm(
                newJudul,
                "28/06/2025",
                "10:00",
                "Jakarta Timur",
                "Progress",
                "1",
                "Deskripsi hasil edit otomatis oleh test."
        );

        editPage.clickSimpan();
        editPage.handleAlert();

        wait.until(ExpectedConditions.urlContains("/program-kerja/admin"));
        Assert.assertTrue(driver.getCurrentUrl().contains("/program-kerja/admin"));

        System.out.println("✅ Program Kerja berhasil diedit dan kembali ke dashboard.");
    }
}
