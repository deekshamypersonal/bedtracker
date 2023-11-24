package com.bedmanagement.bedtracker.common;

import com.bedmanagement.bedtracker.exception.ErrorMessageConstant;
import com.bedmanagement.bedtracker.exception.UserServiceException;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

public class Utility {
    public static boolean validateFiles(MultipartFile file) {

        if(file.getSize() > Constants.ALLOWED_FILE_SIZE) {
            throw new UserServiceException(ErrorMessageConstant.INVALID_FILE_SIZE);
        }

        String extension = FilenameUtils.getExtension(
                file.getOriginalFilename());
        if (!isSupportedExtension(extension))
            throw new UserServiceException(ErrorMessageConstant.HOSPITAL_NOT_FOUND);
        return true;
    }
    private static boolean isSupportedExtension(String extension) {
        return extension != null && (
                extension.equals("png")
                        || extension.equals("jpg")
                        || extension.equals("jpeg")
                        || extension.equals("pdf"));
    }


}
