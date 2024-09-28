package com.springboot.estore.Estore.services.impl;

import com.springboot.estore.Estore.exceptions.BadApiRequest;
import com.springboot.estore.Estore.services.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);
    @Override
    public String uploadFile(MultipartFile file, String path) throws IOException {
        String originalFilename = file.getOriginalFilename();
        logger.info(originalFilename);
        String fileName = UUID.randomUUID().toString();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileNameWithExtension = fileName + extension;
        String fullPathWithFileName = path + fileNameWithExtension;
        if(extension.equalsIgnoreCase(".png") || extension.equalsIgnoreCase(".jpg") ||
         extension.equalsIgnoreCase(".jpeg"))
        {
            File folder = new File(path);
            if(!folder.exists())
            {
                folder.mkdirs();
            }
            Files.copy(file.getInputStream(), Paths.get(fullPathWithFileName));
            return  fileNameWithExtension;
        }
        else{
            throw new BadApiRequest("File with this "+extension+" not allowed");
        }
    }

    @Override
    public InputStream getResource(String path, String name) throws FileNotFoundException {

        String fullpath = path + name;
        InputStream inputStream = new FileInputStream(fullpath);
        return inputStream;
    }
}
