package com.pokerstats.pokerstatistics.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import com.pokerstats.pokerstatistics.service.UploadService;

@RestController
@RequestMapping("/upload")
public class UploadController {
  private final UploadService uploadService;

  public UploadController(UploadService uploadService) {
    this.uploadService = uploadService;
  }

  @CrossOrigin
  @PostMapping
  public void uploadFile(@RequestPart("file") MultipartFile file) {
    try {
      uploadService.uploadFile(file);
    } catch (Exception e) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "File upload failed", e);
    }
  }
}
