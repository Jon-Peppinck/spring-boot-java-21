package com.example.blog.controller;

import com.example.blog.service.FileStorageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/files")
public class FileController {

  @Autowired
  private FileStorageService fileStorageService;

  @PostMapping("/upload/image")
  public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
    try {
      String filename = fileStorageService.saveFile(file, "image");
      return ResponseEntity.ok("Image uploaded successfully: " + filename);
    } catch (IOException e) {
      return ResponseEntity.badRequest().body("Failed to upload image: " + e.getMessage());
    }
  }

  @PostMapping("/upload/pdf")
  public ResponseEntity<String> uploadPdf(@RequestParam("file") MultipartFile file) {
    try {
      String filename = fileStorageService.saveFile(file, "pdf");
      return ResponseEntity.ok("PDF uploaded successfully: " + filename);
    } catch (IOException e) {
      return ResponseEntity.badRequest().body("Failed to upload PDF: " + e.getMessage());
    }
  }

  @GetMapping("/download/image/{filename}")
  public ResponseEntity<byte[]> getImage(@PathVariable String filename) {
    try {
      byte[] fileData = fileStorageService.getFile(filename);
      return ResponseEntity.ok()
          .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
          .contentType(MediaType.IMAGE_JPEG)
          .body(fileData);
    } catch (IOException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @GetMapping("/download/pdf/{filename}")
  public ResponseEntity<byte[]> getPdf(@PathVariable String filename) {
    try {
      byte[] fileData = fileStorageService.getFile(filename);
      return ResponseEntity.ok()
          .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
          .contentType(MediaType.APPLICATION_PDF)
          .body(fileData);
    } catch (IOException e) {
      return ResponseEntity.notFound().build();
    }
  }
}
