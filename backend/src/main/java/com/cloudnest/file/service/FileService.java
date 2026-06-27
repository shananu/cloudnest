package com.cloudnest.file.service;

import com.cloudnest.file.dto.request.RenameFileRequest;
import com.cloudnest.file.dto.response.DownloadFileResponse;
import com.cloudnest.file.dto.response.FileResponse;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;
import java.io.IOException;
import java.util.List;

public interface FileService {

        FileResponse upload(
                        MultipartFile file,
                        UUID folderId,
                        Authentication authentication) throws IOException;

        List<FileResponse> getMyFiles(Authentication authentication);

        DownloadFileResponse download(UUID fileId, Authentication authentication) throws IOException;

        void delete(UUID fileId, Authentication authentication) throws IOException;

        FileResponse rename(
                        UUID fileId,
                        RenameFileRequest request,
                        Authentication authenticationx);

        List<FileResponse> getFilesInFolder(
                        UUID folderId,
                        Authentication authentication);
}