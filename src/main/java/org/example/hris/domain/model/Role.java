package org.example.hris.domain.model;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    private UUID id;
    private String namaRole;
}