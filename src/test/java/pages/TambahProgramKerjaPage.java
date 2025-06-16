package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TambahProgramKerjaPage {
    private WebDriver driver;
    private WebDriverWait wait;

    private By judulInput = By.id("title");
    private By rtRwInput = By.id("rt_rw_id");
    private By tanggalInput = By.id("date");
    private By waktuInput = By.id("time");
    private By lokasiInput = By.id("location");
    private By deskripsiInput = By.id("description");
    private By simpanButton = By.xpath("//button[normalize-space()='SIMPAN']");

    public TambahProgramKerjaPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public void fillForm(String judul, String rt, String tanggal, String waktu, String lokasi, String deskripsi) {
        System.out.println("Mengisi form Tambah Program Kerja...");
        driver.findElement(judulInput).sendKeys(judul);
        driver.findElement(rtRwInput).clear();
        driver.findElement(rtRwInput).sendKeys(rt);
        driver.findElement(tanggalInput).sendKeys(tanggal);
        driver.findElement(waktuInput).sendKeys(waktu);
        driver.findElement(lokasiInput).sendKeys(lokasi);
        driver.findElement(deskripsiInput).sendKeys(deskripsi);
        System.out.println("Form berhasil diisi.");
    }

    public void clickSimpan() {
        System.out.println("Mengklik tombol Simpan...");
        WebElement simpanBtn = driver.findElement(simpanButton);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", simpanBtn);
        System.out.println("Tombol Simpan berhasil diklik.");
    }

    public void handleAlert() {
        System.out.println("Menunggu dan menerima alert konfirmasi 'Yakin?'...");
        wait.until(ExpectedConditions.alertIsPresent()).accept();
        System.out.println("Alert konfirmasi pertama diterima.");

        System.out.println("Menunggu dan menerima alert notifikasi 'Berhasil Ditambahkan'...");
        wait.until(ExpectedConditions.alertIsPresent()).accept();
        System.out.println("Alert notifikasi kedua diterima.");
    }
}