package com.springboot.demo.services;

import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class ImageStorageService implements IStorageService{
    private static final double FileLimit = 2.0;   //2Mb

    private final Path storageFolder = Paths.get("uploads");

    public ImageStorageService(){
        try{
            Files.createDirectories(storageFolder);
        } catch (IOException e) {
            throw new RuntimeException("Cannot create uploads folder",e);
        }
    }

    @Override
    public String storeFile(MultipartFile file) {
        try {
            //Validate the file
            if (file.isEmpty()){
                throw new RuntimeException("The uploaded file is empty");
            }
            if (!isImageFile(file)){
                throw new RuntimeException("You can only upload image file");
            }
            if (isExceedCapacity(file)){
                throw new RuntimeException("File must be <= 5Mb");
            }

            //Rename the file
            String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
            String generatedFileName = UUID.randomUUID().toString();
            generatedFileName = generatedFileName + "." + fileExtension;
            Path destinationPath = storageFolder.resolve(Paths.get(generatedFileName)).normalize().toAbsolutePath();

            if (!destinationPath.getParent().equals(this.storageFolder.toAbsolutePath())) {
                throw new RuntimeException(
                        "Cannot store file outside current directory.");
            }
            //Copy
            try (InputStream inputStream = file.getInputStream()){
                Files.copy(inputStream, destinationPath, StandardCopyOption.REPLACE_EXISTING);
            }
            return  generatedFileName;

        }catch (Exception exception){
            throw new RuntimeException("Fail in storing file (" + exception + ")", exception);
        }
    }

    @Override
    public Stream<Path> loadAllFiles() {
        try
        {
            return Files.walk(this.storageFolder, 1).filter(path -> !path.equals(storageFolder) && !path.toString().contains("._")).map(this.storageFolder::relativize);
        }catch (Exception e){
            throw new RuntimeException("Failed to load all files: " +e, e);
        }
    }

    @Override
    public byte[] readFileContent(String fileName) {
        try {
            Path file = storageFolder.resolve(fileName);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return StreamUtils.copyToByteArray(resource.getInputStream());
            }
            else {
                throw new RuntimeException(
                        "Could not read file: " + fileName);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteAllFiles() {

    }

    /**
     * Check if is image file
     * @param file input file
     * @return bool
     */
    private boolean isImageFile(MultipartFile file) {
        //Let install FileNameUtils
        String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
        assert fileExtension != null;
        return Arrays.asList(new String[] {"png","jpg","jpeg", "bmp"})
                .contains(fileExtension.trim().toLowerCase());
    }


    private boolean isExceedCapacity(MultipartFile file){
        double fileSizeInMb = file.getSize()/1_000_000.0;
        return (fileSizeInMb > FileLimit);
    }
}
