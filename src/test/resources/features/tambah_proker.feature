Feature: Tambah Program Kerja
  Sebagai seorang admin, saya ingin dapat menambahkan program kerja baru
  agar dapat mengelola kegiatan RT secara efektif.

  Scenario: Berhasil menambahkan program kerja baru
    Given Saya berada di halaman login
    When Saya login sebagai admin dengan email "adminrt1@gmail.com" dan password "password"
    And Saya navigasi ke halaman tambah program kerja
    And Saya mengisi form tambah program kerja dengan data yang valid
    And Saya menekan tombol simpan
    Then Program kerja baru berhasil ditambahkan dan saya kembali ke halaman daftar program kerja