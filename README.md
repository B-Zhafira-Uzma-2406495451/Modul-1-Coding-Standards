# **Refleksi Modul 1**  
Nama : Zhafira Uzma  
NPM : 2406495451  
Kelas : Adpro B  

## Refleksi 1
### Clean Code Principles :
1. Meaningful Names: Saya memilih nama variabel, metode, dan kelas yang mencerminkan fungsinya secara spesifik, contohnya seperti atribut productName untuk nama produk atau methods findAll() untuk mengambil
   semua data.
2. Single Responsibility Principle (SRP): Saya memastikan tiap komponen hanya memiliki satu tugas saja dalam menjalankan program saya. Contohnya pada kode saya adalah ProductController hanya untuk alur request,
   ProductRepository untuk menangani penyimpanan data, dan ProductService untuk pemanggilan methods pada ProductRepository.
3. Separation of Concerns: Saya memisahkan methods untuk model, methods untuk program, dan UI melalui pola desain MVC.
4. Don't Repeat Yourself (DRY): Saya memastikan methods untuk mengolah model diletakkan di dalam ProductRepository, sehingga methods itu tidak ada duplikasi pada bagian service atau controller.

### Secure Coding Practices
1. Penggunaan Unpredictable ID atau UUID untuk mencegah serangan enumerasi data (1, 2, 3) dari pihak luar
2. Menampilkan notifikasi konfirmasi ketika ingin menghapus data sehingga ketika user ragu untuk menghapus data namun terlanjur meng-click button delete, masih diberikan kesempatan untuk memastikan kembali data
   yang ingin dihapus
3. Tidak menampilkan ID product pada tampilan web dan memastikan function edit tidak dapat mengubah ID product (hanya nama product dan quantity-nya saja yang bisa diubah

### Improvement Space
1. Belum ada validasi input untuk product di sisi server. Hal ini dapat diperbaiki dengan menambahkan anotasi validasi seperti @NotBlank dan @Min(0) untuk memastikan data yang masuk selalu valid.
2. Belum ada penanganan untuk objek Null. Hal ini dapat diperbaiki dengan membungkus hasil pencarian menggunakan Optional<Product> atau memberikan logika pengecekan di level service.


## Refleksi 2
### Question 1
Setelah melakukan unit testing, saya merasa lega dan percaya diri terhadap kestablian dan keamanan kode saya. Menurut saya, untuk memastikan program kita memerlukan unit test dengan coverage yang tinggi.
Namun, coverage yang tinggi atau sempurna (100%) tidak selalu berarti kalau program yang kita buat tidak memiliki bug atau error. Oleh karena itu, unit test harus mencakup positive, negative, dan edge case
serta untuk jumlah unit test yang diperlukan adalah tidak menentu (selama mencakup ketiga case tersebut maka unit test sudah cukup)

### Question 2

