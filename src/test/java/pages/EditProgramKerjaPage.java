package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class EditProgramKerjaPage {
    private WebDriver driver;
    private WebDriverWait wait;

    private By judulInput = By.id("title");
    private By waktuInput = By.id("time");
    private By tanggalInput = By.id("date");
    private By lokasiInput = By.id("location");
    private By statusDropdown = By.id("status");
    private By rtInput = By.name("rt_id");
    private By deskripsiInput = By.id("description");
    private By simpanButton = By.xpath("//button[contains(text(),'SIMPAN')]");

    public EditProgramKerjaPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public void editForm(String judul, String tanggal, String waktu, String lokasi, String status, String rt, String deskripsi) {
        System.out.println("✏️ Mengedit form Program Kerja...");

        driver.findElement(judulInput).clear();
        driver.findElement(judulInput).sendKeys(judul);

        driver.findElement(waktuInput).clear();
        driver.findElement(waktuInput).sendKeys(waktu);

        driver.findElement(tanggalInput).clear();
        driver.findElement(tanggalInput).sendKeys(tanggal);

        driver.findElement(lokasiInput).clear();
        driver.findElement(lokasiInput).sendKeys(lokasi);

        // ✅ FIX: Gunakan Select untuk <select> dropdown
        Select statusSelect = new Select(driver.findElement(statusDropdown));
        statusSelect.selectByVisibleText(status); // contoh: "Progress", "Upcoming", dll

        driver.findElement(rtInput).clear();
        driver.findElement(rtInput).sendKeys(rt);

        driver.findElement(deskripsiInput).clear();
        driver.findElement(deskripsiInput).sendKeys(deskripsi);

        System.out.println("✅ Form edit berhasil diisi.");
    }

    public void clickSimpan() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(simpanButton));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
    }

    public void handleAlert() {
        try {
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());
            alert.accept();
            wait.until(ExpectedConditions.alertIsPresent()).accept();
            System.out.println("✅ Alert ditutup.");
        } catch (Exception e) {
            System.out.println("⚠️ Tidak ada alert muncul.");
        }
    }
}
