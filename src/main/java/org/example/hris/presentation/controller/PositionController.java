package org.example.hris.presentation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.hris.application.dto.position.PositionCreateRequest;
import org.example.hris.application.dto.position.PositionResponse;
import org.example.hris.application.dto.position.PositionUpdateRequest;
import org.example.hris.application.service.PositionService;
import org.example.hris.domain.model.Position;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/positions")
@RequiredArgsConstructor
public class PositionController {

    private final PositionService positionService;

    @PostMapping
    public ResponseEntity<PositionResponse> create(@Valid @RequestBody PositionCreateRequest request) {
        Position position = Position.builder()
                .namaJabatan(request.getNamaJabatan())
                .build();

        Position created = positionService.createPosition(position);
        return ResponseEntity.ok(toResponse(created));
    }

    @GetMapping
    public ResponseEntity<List<PositionResponse>> findAll() {
        List<PositionResponse> responses = positionService.getAllPositions()
                .stream()
                .map(this::toResponse)
                .toList();

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PositionResponse> findById(@PathVariable UUID id) {
        Position position = positionService.getPositionById(id);
        return ResponseEntity.ok(toResponse(position));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PositionResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody PositionUpdateRequest request
    ) {
        Position updated = Position.builder()
                .namaJabatan(request.getNamaJabatan())
                .build();

        Position saved = positionService.updatePosition(id, updated);
        return ResponseEntity.ok(toResponse(saved));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        positionService.deletePosition(id);
        return ResponseEntity.noContent().build();
    }

    private PositionResponse toResponse(Position position) {
        return PositionResponse.builder()
                .id(position.getId())
                .namaJabatan(position.getNamaJabatan())
                .build();
    }
}