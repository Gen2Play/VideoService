package com.Gen2Play.VideoService.services.tags;

import com.google.api.gax.longrunning.OperationFuture;
import com.google.cloud.videointelligence.v1.AnnotateVideoProgress;
import com.google.cloud.videointelligence.v1.AnnotateVideoRequest;
import com.google.cloud.videointelligence.v1.AnnotateVideoResponse;
import com.google.cloud.videointelligence.v1.VideoIntelligenceServiceClient;
import com.google.cloud.videointelligence.v1.Feature;

public class GoogleCloudVideo {
    public static void getVideoTags(String videoUri) throws Exception {
        // Tạo client của Video Intelligence API
        try (VideoIntelligenceServiceClient client = VideoIntelligenceServiceClient.create()) {
            // Cấu hình yêu cầu
            AnnotateVideoRequest request = AnnotateVideoRequest.newBuilder()
                    .setInputUri(videoUri)
                    .addFeatures(Feature.LABEL_DETECTION)  // Nhận diện nhãn (tags)
                    .build();

            // Gửi yêu cầu API và nhận kết quả trả về
            OperationFuture<AnnotateVideoResponse, AnnotateVideoProgress> future = client.annotateVideoAsync(request);

            // Chờ kết quả và lấy ra các kết quả nhận diện
            AnnotateVideoResponse response = future.get();

            // Lấy các tags từ kết quả
            response.getAnnotationResultsList().forEach(result -> {
                result.getSegmentLabelAnnotationsList().forEach(segmentLabelAnnotation -> {
                    System.out.println("Tag: " + segmentLabelAnnotation.getEntity().getDescription());
                });
            });
        }
    }
}
