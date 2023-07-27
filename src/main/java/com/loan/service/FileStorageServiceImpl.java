package com.loan.service;

import com.loan.exception.BaseException;
import com.loan.exception.ResultType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor

public class FileStorageServiceImpl implements FileStorageService{

    @Value("${spring.servlet.multipart.location}")
    private String uploadPath;

    @Override
    public void save(MultipartFile file) { // 지정된 경로로 파일을 복사하는 메소드 (같은 파일명이면 덮어쓰기)
        try{
            Files.copy(file.getInputStream(), Paths.get(uploadPath).resolve(file.getOriginalFilename()),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e){
            throw new BaseException(ResultType.SYSTEM_ERROR);
        }

    }

    @Override
    public Resource load(String fileName) { // 대출 신청 서류를 조회하는 메소드

        try{
            Path file = Paths.get(uploadPath).resolve(fileName);

            Resource resource = new UrlResource(file.toUri());

            if(resource.isReadable() || resource.exists()){
                return resource;
            }else {
                throw new BaseException(ResultType.NOT_EXIST);
            }
        } catch (Exception e) {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        }
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(Paths.get(uploadPath),1).filter(path -> !path.equals(Paths.get(uploadPath))); // 해당 업로드패치 하위 위치에 있는 파일들만 조회
        }catch (Exception e){
            throw new BaseException(ResultType.SYSTEM_ERROR);
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(Paths.get(uploadPath).toFile()); // 경로에 있는 모든 파일 삭제
    }
}
