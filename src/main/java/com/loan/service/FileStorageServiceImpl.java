package com.loan.service;

import com.loan.exception.BaseException;
import com.loan.exception.ResultType;
import com.loan.repository.ApplicationRepository;
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

    private final ApplicationRepository applicationRepository;

    @Override
    public void save(Long applicationId,MultipartFile file) { // 지정된 경로로 파일을 복사하는 메소드 (같은 파일명이면 덮어쓰기)

        if(!isPresentApplication(applicationId)){
            throw new BaseException(ResultType.SYSTEM_ERROR);
        }

        try{
            String applicationPath = uploadPath.concat("/" + applicationId); // ../applicationId 로 생성해준다.
            Path directoryPath = Path.of(applicationPath);
            if(!Files.exists(directoryPath)){ // 만약 디렉토리가 없으면 디렉토리 생성
                Files.createDirectory(directoryPath);
            }

            Files.copy(file.getInputStream(), Paths.get(applicationPath).resolve(file.getOriginalFilename()));
        } catch (Exception e){
            throw new BaseException(ResultType.SYSTEM_ERROR);
        }

    }

    @Override
    public Resource load(Long applicationId,String fileName) { // 대출 신청 서류를 조회하는 메소드


        if(!isPresentApplication(applicationId)){
            throw new BaseException(ResultType.SYSTEM_ERROR);
        }
        String applicationPath = uploadPath.concat("/" + applicationId); // ../applicationId 로 생성해준다.


        try{
            Path file = Paths.get(applicationPath).resolve(fileName);

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
    public Stream<Path> loadAll(Long applicationId) {

        if(!isPresentApplication(applicationId)){
            throw new BaseException(ResultType.SYSTEM_ERROR);
        }
        String applicationPath = uploadPath.concat("/" + applicationId); // ../applicationId 로 생성해준다.

        try {
            return Files.walk(Paths.get(applicationPath),1).filter(path -> !path.equals(Paths.get(applicationPath))); // 해당 업로드패치 하위 위치에 있는 파일들만 조회
        }catch (Exception e){
            throw new BaseException(ResultType.SYSTEM_ERROR);
        }
    }

    @Override
    public void deleteAll(Long applicationId) {

        if(!isPresentApplication(applicationId)){
            throw new BaseException(ResultType.SYSTEM_ERROR);
        }
        String applicationPath = uploadPath.concat("/" + applicationId); // ../applicationId 로 생성해준다.

        FileSystemUtils.deleteRecursively(Paths.get(applicationPath).toFile()); // 경로에 있는 모든 파일 삭존
    }

    public boolean isPresentApplication(Long applicationId) { // 존재 하는 신청 정보인지 체크
        return applicationRepository.findById(applicationId).isPresent();
    }
}
