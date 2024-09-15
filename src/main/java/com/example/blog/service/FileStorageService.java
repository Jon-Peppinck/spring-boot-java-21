package com.example.blog.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageService {
  private final Path rootLocation = Paths.get("uploads");

  public FileStorageService() throws IOException {
    if (!Files.exists(rootLocation)) {
      Files.createDirectories(rootLocation);
    }
  }

  public String saveFile(MultipartFile file, String fileType) throws IOException {
    String extension = getFileExtension(file.getOriginalFilename());
    String filename = UUID.randomUUID().toString() + "." + extension;

    if (fileType.equalsIgnoreCase("image") && !isValidImageFile(extension)) {
      throw new IOException("Invalid image file type.");
    }
    if (fileType.equalsIgnoreCase("pdf") && !"pdf".equalsIgnoreCase(extension)) {
      throw new IOException("Invalid PDF file type.");
    }

    Files.copy(file.getInputStream(), this.rootLocation.resolve(filename));
    return filename;
  }

  public byte[] getFile(String filename) throws IOException {
    Path filePath = this.rootLocation.resolve(filename);
    return Files.readAllBytes(filePath);
  }

  private String getFileExtension(String filename) {
    return filename.substring(filename.lastIndexOf(".") + 1);
  }

  private boolean isValidImageFile(String extension) {
    return "jpg".equalsIgnoreCase(extension) || "jpeg".equalsIgnoreCase(extension);
  }
}
