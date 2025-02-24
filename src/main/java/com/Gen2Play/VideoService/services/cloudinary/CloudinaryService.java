package com.Gen2Play.VideoService.services.cloudinary;

import com.Gen2Play.VideoService.model.dto.cloudinary.CloudinaryUploadResponse;
import com.Gen2Play.VideoService.model.entity.enumeration.MediaTypeEnum;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import io.github.cdimascio.dotenv.Dotenv;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLEncoder;
import java.io.UnsupportedEncodingException;

import java.io.IOException;
import java.util.Map;

import com.Gen2Play.VideoService.configuration.EnvConfig;

@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;
    private final String cloudName = EnvConfig.getEnv("CLOUDINARY_CLOUD_NAME");
    private final String apiKey = EnvConfig.getEnv("CLOUDINARY_API_KEY");
    private final String apiSecret = EnvConfig.getEnv("CLOUDINARY_API_SECRET");

    // Lấy thông tin Cloudinary từ application.properties
    public CloudinaryService() {
        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret,
                "secure", true
        ));
    }

    //upload video to Cloudinary
    @SuppressWarnings({"rawtypes"})
    public CloudinaryUploadResponse uploadVideo(MultipartFile videoFile) {
        CloudinaryUploadResponse result = new CloudinaryUploadResponse();
        try {
            // Upload video lên Cloudinary
            Map uploadResult = cloudinary.uploader().upload(videoFile.getBytes(),
                    ObjectUtils.asMap(
                            "resource_type", "video", // Chỉ định là video
                            "folder", "uploaded_videos" // Lưu vào thư mục "uploaded_videos"
                    ));
            // Lấy các tag liên quan đến nội dung video
            // TODO: find ai to auto-tags
            // TODO: GET FPS
            // Lấy URL của video đã upload
            String sourceLink = uploadResult.get("secure_url").toString();
            // Lấy thông tin độ phân giải
            int width = (int) uploadResult.get("width");
            int height = (int) uploadResult.get("height");
            // Lấy tên của video (public_id)
            String videoName = getFileNameWithoutExtension(videoFile.getOriginalFilename());
            // Lấy định dạng của video (format)
            String videoFormat = uploadResult.get("format").toString();
            //fix link to get view link
            String viewLink = getCloudinaryPlayerUrl(sourceLink, videoFormat);
            result.setName(videoName);
            result.setSourceLink(sourceLink);
            result.setResolution(width + "x" + height);
            result.setViewLink(viewLink);
            if (MediaTypeEnum.isValidFormat(videoFormat)) {
                result.setFormat(MediaTypeEnum.valueOf(videoFormat.toUpperCase()));
            } else {
                result.setFormat(MediaTypeEnum.OTHER);
            }
            //return result
            return result;
        } catch (IOException e) {
            throw new RuntimeException("Error uploading video: " + e.getMessage());
        }
    }

    private String getCloudinaryPlayerUrl(String mp4Url, String format) throws UnsupportedEncodingException {
        // Sử dụng regex để lấy public_id từ URL mp4
        String regex = "/upload/v\\d+/(.+?)\\." + format;
        String publicId = null;
        if (mp4Url.matches(".*" + regex + ".*")) {
            publicId = mp4Url.replaceAll(".*" + regex, "$1");
        }
        if (publicId == null) {
            return null;
        }
        // Tạo link Cloudinary Player
        return "https://player.cloudinary.com/embed/?public_id=" + URLEncoder.encode(publicId, "UTF-8") + "&cloud_name=" + cloudName + "&profile=cld-default";
    }

    private String getFileNameWithoutExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf(".");
        if (lastDotIndex == -1) {
            return fileName; // Trường hợp không có phần mở rộng
        }
        return fileName.substring(0, lastDotIndex);
    }
}
