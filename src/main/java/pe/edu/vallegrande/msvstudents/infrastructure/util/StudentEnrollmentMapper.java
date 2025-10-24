package pe.edu.vallegrande.msvstudents.infrastructure.util;

import pe.edu.vallegrande.msvstudents.domain.model.StudentEnrollment;
import pe.edu.vallegrande.msvstudents.domain.enums.EnrollmentStatus;
import pe.edu.vallegrande.msvstudents.infrastructure.dto.request.CreateStudentEnrollmentRequest;
import pe.edu.vallegrande.msvstudents.infrastructure.dto.request.UpdateStudentEnrollmentRequest;
import pe.edu.vallegrande.msvstudents.infrastructure.dto.response.StudentEnrollmentResponse;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.UUID;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class StudentEnrollmentMapper {

    public static StudentEnrollment toEntity(CreateStudentEnrollmentRequest request) {
        StudentEnrollment enrollment = new StudentEnrollment();
        enrollment.setId(UUID.randomUUID().toString());
        enrollment.setStudentId(request.getStudentId());
        enrollment.setClassroomId(request.getClassroomId());
        // institutionId is set in the service from headers (no trust in client payload)
        enrollment.setEnrollmentDate(request.getEnrollmentDate());
        enrollment.setEnrollmentType(request.getEnrollmentType());
        enrollment.setStatus(EnrollmentStatus.ACTIVE);
        enrollment.setCreatedAt(LocalDateTime.now());
        enrollment.setUpdatedAt(LocalDateTime.now());

        // Generate QR code content as a simple JSON string
        String qrDataJson = String.format("{\"student_id\":\"%s\",\"classroom_id\":\"%s\"}",
                request.getStudentId(), request.getClassroomId());

        try {
            // QR generation with ZXing
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, StandardCharsets.UTF_8.name());
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);

            int width = 300;
            int height = 300;
            BitMatrix bitMatrix = new MultiFormatWriter().encode(qrDataJson, BarcodeFormat.QR_CODE, width, height, hints);

            ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
            byte[] pngData = pngOutputStream.toByteArray();

            String base64Qr = Base64.getEncoder().encodeToString(pngData);
            // store as data URI (png) so it's clear it's an image
            enrollment.setQrCode("data:image/png;base64," + base64Qr);
        } catch (Exception e) {
            // fallback: store the JSON string if QR generation fails
            enrollment.setQrCode(qrDataJson);
        }

        return enrollment;
    }

    public static StudentEnrollment updateEntity(StudentEnrollment existing, UpdateStudentEnrollmentRequest request) {
        if (request.getClassroomId() != null) {
            existing.setClassroomId(request.getClassroomId());
        }
        if (request.getEnrollmentType() != null) {
            existing.setEnrollmentType(request.getEnrollmentType());
        }
        if (request.getStatus() != null) {
            existing.setStatus(request.getStatus());
        }
        if (request.getTransferReason() != null) {
            existing.setTransferReason(request.getTransferReason());
        }
        existing.setUpdatedAt(LocalDateTime.now());
        return existing;
    }

    public static StudentEnrollmentResponse toResponse(StudentEnrollment enrollment) {
        return StudentEnrollmentResponse.builder()
                .id(enrollment.getId())
                .studentId(enrollment.getStudentId())
                .classroomId(enrollment.getClassroomId())
                .institutionId(enrollment.getInstitutionId())
                .enrollmentDate(enrollment.getEnrollmentDate())
                .enrollmentType(enrollment.getEnrollmentType())
                .status(enrollment.getStatus())
                .transferReason(enrollment.getTransferReason())
                .qrCode(enrollment.getQrCode())
                .createdAt(enrollment.getCreatedAt())
                .updatedAt(enrollment.getUpdatedAt())
                .build();
    }
}