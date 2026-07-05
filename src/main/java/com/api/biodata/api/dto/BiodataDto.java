package com.api.biodata.api.dto;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.ToString;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Schema(
        name = "Biodata",
        description = "DTO untuk request dan response data biodata"
)
public class BiodataDto
{
    @Schema(
            description = "ID unik biodata",
            example = "344f4139-bbb7-443d-a96e-c975be15e5af"
    )
    private String id;
    @Schema(
            description = "Nomor Induk Mahasiswa",
            example = "20230001",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String nim;
    @Schema(
            description = "Nama lengkap mahasiswa",
            example = "Mohammad Sulaeman",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String fullName;
    @Schema(
            description = "Foto dalam format Base64 atau URL gambar",
            example = "9j/4AAQSk..."
    )
    private String photo;
    @Schema(
            description = "Alamat email",
            example = "mohammad.sulaeman@hotmail.com"
    )
    private String email;
    @Schema(
            description = "Nomor telepon",
            example = "0812345678903"
    )
    private String phone;
    @Schema(
            description = "Tempat dan tanggal lahir",
            example = "Jakarta, 23 Juni 1990"
    )
    private String dob;

    @Schema(
            description = "Alamat lengkap",
            example = "Bekasi Timur, Kota Bekasi, Jawa Barat"
    )
    private String address;
}