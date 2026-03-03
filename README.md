# **Refleksi Modul 2**
Nama : Zhafira Uzma  
NPM : 2406495451  
Kelas : Adpro B

## Refleksi  
1. Prinsip yang telah saya terapkan setelah saya melakukan modifikasi pada kode saya ini adalah prinsip SRP,
LSP, dan DIP. Saya menerapkan SRP atau Single Responsibility Principle dengan memisahkan CarController dari
ProductController agar masing-masing kelas hanya memiliki satu tanggung jawab. Prinsip LSP atau Liskov
Substitution Principle saya terapkan dengan menghapus extends ProductController pada CarController karena
controller tidak seharusnya mewarisi controller lain agar tidak mewarisi routing yang bisa memicu error. Dip
atau Dependency Inversion Principle saya terapkan dengan membuat interface CarRepository dan CarService.
Sekarang, Controller bergantung pada interface CarService, dan Service bergantung pada interface CarRepository,
bukan langsung pada detail implementasinya. Untuk kedua prinsip lainnya yaitu OCP dan ISP sudah memenuhi pada
kode saya sebelum dimodifikasi.  

2. Manfaat menerapkan SOLID Principles pada kode saya :
- Penerapan DIP : Testability lebih mudah karena kode bergantung pada interface sehingga mudah membuat mock
database saat melakukan Unit Testing tanpa harus menyentuh database sungguhan.
- Penerapan LSP: Mencegah Error Bawaan karena dengan menghapus inheritance antar Controller, program dapat
mencegah Spring Boot kebingungan membaca endpoint. Spring tidak akan lagi melempar Ambiguous Mapping Exception
karena tidak ada dua kelas yang mengklaim URL path yang sama.
- Penerapan SRP: Mudah Dibaca dan Dikelola karena jika ada bug pada suatu fitur, cukup cari file class yang
memegang method fitur tersebut tanpa harus membuka satu file yang berisi baris kode yang panjang  

3. Hal yang akan terjadi jika tidak menerapkan SOLID Principles pada kode saya:
- Tight Coupling jika tidak menerapkan DIP karena apabila CarController di-inject langsung dengan CarServiceImpl,
maka setiap perubahan kecil pada constructor atau struktur internal CarServiceImpl bisa merusak CarController.
- Munculnya God Object jika tidak menerapkan SRP karena apabila endpoint untuk CarController tetap meng-extends 
ProductController.java, file tersebut akan menjadi sangat panjang dan kompleks. Jika proyek dikerjakan bersama
tim, risiko terjadinya merge conflict di Git akan sangat tinggi.
- Redundansi dan Perilaku Tidak Tertebak jika tidak menerapkan LSP karena saat CarController extends
ProductController, CarController diam-diam memiliki endpoint /product/create milik induknya. Jika pengguna
mengakses URL tersebut, sistem bisa mengeksekusi logika yang salah atau crash karena bentrok konfigurasi,
membuat keamanan dan alur aplikasi menjadi tidak tertebak.
