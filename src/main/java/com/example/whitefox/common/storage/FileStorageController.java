package com.example.whitefox.common.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/storage")
public class FileStorageController {

    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        String fileDownloadUri = fileStorageService.storeFile(file);
        return ResponseEntity.ok(fileDownloadUri);
    }

    @PostMapping("/upload-multiple")
    public ResponseEntity<List<String>> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
        List<String> uris = java.util.Arrays.stream(files)
                .map(fileStorageService::storeFile)
                .collect(Collectors.toList());
        return ResponseEntity.ok(uris);
    }
}
