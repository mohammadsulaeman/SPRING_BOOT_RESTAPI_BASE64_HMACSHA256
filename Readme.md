# Biodata API

REST API untuk manajemen data biodata menggunakan Spring Boot dan PostgreSQL.

## Tech Stack

* Java 17
* Spring Boot 4.0.7
* Spring MVC
* Spring Data JPA
* Bean Validation
* PostgreSQL
* Maven
* Lombok
* MapStruct
* OpenAPI (Swagger)
* Gson
* Apache Commons IO

## Features

* CRUD Biodata
* Upload dan akses file gambar
* Validasi request menggunakan Jakarta Validation
* Dokumentasi API menggunakan Swagger UI
* Mapping DTO menggunakan MapStruct
* Database PostgreSQL
* Auto reload menggunakan Spring Boot DevTools

## API Documentation

Swagger UI dapat diakses melalui:

```text
http://localhost:8080/swagger-ui.html
```

atau

```text
http://localhost:8080/swagger-ui/index.html
```

## Requirements

* Java 17+
* Maven 3.9+
* PostgreSQL 15+

# Security Schema
Menggunakan HmacSha256 dengan mengirimkan token ke dalam Header Postman


# Pembuatan Token HMACSHA256 Online
- Pergi Ke https://www.devglan.com/online-tools/hmac-sha256-online
- Generate HMAC :
  - Pada Enter Plain Text to Compute Hash di isi dengan :  data fullname yang di kirim pada postman
  - Pada Enter the Secret Key di isi dengan : BIODATA_2023
  - Pada Select Cryptographic Hash Function : SHA-256
  - Pada Output MAC Format pilih : Base64
  - Lalu Klik Generate HMAC
  - Token Yang Di dapat di gunakan untuk header token pada postman

# Pembuatan Base64 pada Images Online
- Pergi Ke https://base64.guru/converter/encode/image
- Pada Local File : isi dengan gambar yang ingin di ubah ke bentuk byte
- Lalu Klik Encode Image to Base64
- Hasil Yang Di Dapat di taruh di body photo pada saat postman

## Postman Collection

Download collection:

[CRUD Header Token hmca256.postman_collection.json](../../../DOCUMENT/CRUD%20Header%20Token%20hmca256.postman_collection.json)

## Build Project

```bash
mvn clean install
```

## Run Application

```bash
mvn spring-boot:run
```





