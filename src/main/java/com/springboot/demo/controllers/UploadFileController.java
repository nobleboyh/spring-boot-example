package com.springboot.demo.controllers;

import com.springboot.demo.models.ResponseObject;
import com.springboot.demo.services.IStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "api/upload-file")
public class UploadFileController {
    @Autowired
    private IStorageService storageService;

    @PostMapping(value = "")
    ResponseEntity<ResponseObject> uploadFile(@RequestParam("file")MultipartFile file){
        try{
            String generatedName = storageService.storeFile(file);
            return ResponseEntity.ok().body(new ResponseObject("OK", "Upload file successfully", generatedName));
        }catch (Exception err){
            return  ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(new ResponseObject("Failed","Cannot upload file: " + err, ""));
        }
    }

    @GetMapping(value = "/{fileName:.+}")
    public ResponseEntity<byte[]> getImage(@PathVariable String fileName){
        try {
            byte[] bytes = storageService.readFileContent(fileName);
            return ResponseEntity
                    .ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(bytes);
        }catch (Exception exception) {
            return ResponseEntity.noContent().build();
        }
    }

    //How to load all uploaded files ?
    @GetMapping("")
    public ResponseEntity<ResponseObject> getUploadedFiles() {
        try {
            List<String> urls = storageService.loadAllFiles()
                    .map(path -> {
                        //convert fileName to url(send request "readDetailFile")
                        return MvcUriComponentsBuilder.fromMethodName(UploadFileController.class,
                                "getImage", path.getFileName().toString()).build().toUri().toString();
                    })
                    .collect(Collectors.toList());
            return ResponseEntity.ok(new ResponseObject("ok", "List files successfully", urls));
        }catch (Exception exception) {
            return ResponseEntity.ok(new
                    ResponseObject("failed", "List files failed" + exception, new String[] {}));
        }
    }
}
