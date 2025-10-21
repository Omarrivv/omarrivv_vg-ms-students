package pe.edu.vallegrande.msvstudents.infrastructure.util;

import pe.edu.vallegrande.msvstudents.domain.model.Student;
import pe.edu.vallegrande.msvstudents.domain.enums.Status;
import pe.edu.vallegrande.msvstudents.infrastructure.dto.request.CreateStudentRequest;
import pe.edu.vallegrande.msvstudents.infrastructure.dto.request.UpdateStudentRequest;
import pe.edu.vallegrande.msvstudents.infrastructure.dto.response.StudentResponse;

import java.time.LocalDateTime;
import java.util.UUID;

public class StudentMapper {

    public static Student toEntity(CreateStudentRequest request, String institutionId) {
        Student student = new Student();
        student.setId(UUID.randomUUID().toString());
        student.setInstitutionId(institutionId);
        student.setFirstName(request.getFirstName());
        student.setLastName(request.getLastName());
        student.setDocumentType(request.getDocumentType());
        student.setDocumentNumber(request.getDocumentNumber());
        student.setBirthDate(request.getBirthDate());
        student.setGender(request.getGender());
        student.setAddress(request.getAddress());
        student.setPhone(request.getPhone());
        student.setParentName(request.getParentName());
        student.setParentPhone(request.getParentPhone());
        student.setParentEmail(request.getParentEmail());
        student.setStatus(Status.ACTIVE);
        student.setCreatedAt(LocalDateTime.now());
        student.setUpdatedAt(LocalDateTime.now());
        return student;
    }

    public static Student updateEntity(Student existing, UpdateStudentRequest request) {
        if (request.getFirstName() != null) existing.setFirstName(request.getFirstName());
        if (request.getLastName() != null) existing.setLastName(request.getLastName());
        if (request.getDocumentType() != null) existing.setDocumentType(request.getDocumentType());
        if (request.getDocumentNumber() != null) existing.setDocumentNumber(request.getDocumentNumber());
        if (request.getBirthDate() != null) existing.setBirthDate(request.getBirthDate());
        if (request.getGender() != null) existing.setGender(request.getGender());
        if (request.getAddress() != null) existing.setAddress(request.getAddress());
        if (request.getPhone() != null) existing.setPhone(request.getPhone());
        if (request.getParentName() != null) existing.setParentName(request.getParentName());
        if (request.getParentPhone() != null) existing.setParentPhone(request.getParentPhone());
        if (request.getParentEmail() != null) existing.setParentEmail(request.getParentEmail());
        if (request.getStatus() != null) existing.setStatus(request.getStatus());
        existing.setUpdatedAt(LocalDateTime.now());
        return existing;
    }

    public static StudentResponse toResponse(Student student) {
        return StudentResponse.builder()
                .id(student.getId())
                .institutionId(student.getInstitutionId())
                .firstName(student.getFirstName())
                .lastName(student.getLastName())
                .documentType(student.getDocumentType())
                .documentNumber(student.getDocumentNumber())
                .birthDate(student.getBirthDate())
                .gender(student.getGender())
                .address(student.getAddress())
                .phone(student.getPhone())
                .parentName(student.getParentName())
                .parentPhone(student.getParentPhone())
                .parentEmail(student.getParentEmail())
                .status(student.getStatus())
                .createdAt(student.getCreatedAt())
                .updatedAt(student.getUpdatedAt())
                .build();
    }
}