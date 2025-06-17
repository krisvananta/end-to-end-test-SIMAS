package tests;

import io.cucumber.java.en.*;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import pages.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TambahProkerSteps {

    private WebDriver driver;
    private WebDriverWait wait;
    private final String baseUrl = "http://sirtrw.vansite.cloud/";

    @Given("Saya berada di halaman login")
    public void saya_berada_di_halaman_login() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        driver.get(baseUrl);

        LandingPage landingPage = new LandingPage(driver, wait);
        landingPage.clickMasuk();
        wait.until(ExpectedConditions.urlContains("/masuk"));
    }

    @When("Saya login sebagai admin dengan email {string} dan password {string}")
    public void saya_login_sebagai_admin_dengan_email_dan_password(String email, String password) {
        LoginPage loginPage = new LoginPage(driver, wait);
        loginPage.loginAsAdmin(email, password);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loading-overlay")));
        WebElement menuProgramKerja = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//a[normalize-space()='Program Kerja']")));
        Assert.assertTrue(menuProgramKerja.isDisplayed(), "Menu Program Kerja tidak ditemukan setelah login.");
    }

    @And("Saya navigasi ke halaman tambah program kerja")
    public void saya_navigasi_ke_halaman_tambah_program_kerja() {
        AdminDashboardPage adminDashboard = new AdminDashboardPage(driver, wait);
        adminDashboard.goToProgramKerja();

        ProgramKerjaPage programKerjaPage = new ProgramKerjaPage(driver, wait);
        programKerjaPage.clickAddNew();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("title")));
        Assert.assertTrue(driver.getCurrentUrl().contains("/tambah-program-kerja"));
    }

    @And("Saya mengisi form tambah program kerja dengan data yang valid")
    public void saya_mengisi_form_tambah_program_kerja_dengan_data_yang_valid() {
        TambahProgramKerjaPage tambahProkerPage = new TambahProgramKerjaPage(driver, wait);
        String tanggalHariIni = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        String judulProker = "Proker Sukses Final BDD " + System.currentTimeMillis();

        tambahProkerPage.fillForm(
                judulProker,
                "1",
                tanggalHariIni,
                "11:30",
                "Yogyakarta",
                "Deskripsi final dari BDD test."
        );
    }

    @And("Saya menekan tombol simpan")
    public void saya_menekan_tombol_simpan() {
        TambahProgramKerjaPage tambahProkerPage = new TambahProgramKerjaPage(driver, wait);
        tambahProkerPage.clickSimpan();
        tambahProkerPage.handleAlert();
    }

    @Then("Program kerja baru berhasil ditambahkan dan saya kembali ke halaman daftar program kerja")
    public void program_kerja_baru_berhasil_ditambahkan() {
        wait.until(ExpectedConditions.urlContains("/program-kerja/admin"));
        Assert.assertTrue(driver.getCurrentUrl().contains("/program-kerja/admin"));
        System.out.println("Skenario BDD Berhasil: Proses tambah proker sukses.");
        if (driver != null) {
            driver.quit();
        }
    }
}