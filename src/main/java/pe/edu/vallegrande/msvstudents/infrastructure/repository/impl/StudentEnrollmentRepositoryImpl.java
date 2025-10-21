package pe.edu.vallegrande.msvstudents.infrastructure.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import pe.edu.vallegrande.msvstudents.domain.model.StudentEnrollment;
import pe.edu.vallegrande.msvstudents.infrastructure.repository.StudentEnrollmentRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class StudentEnrollmentRepositoryImpl implements StudentEnrollmentRepository {

    private final ReactiveMongoTemplate mongoTemplate;

    @Override
    public Mono<StudentEnrollment> save(StudentEnrollment enrollment) {
        return mongoTemplate.save(enrollment);
    }

    @Override
    public Mono<StudentEnrollment> findById(String id) {
        return mongoTemplate.findById(id, StudentEnrollment.class);
    }

    @Override
    public Flux<StudentEnrollment> findByInstitutionId(String institutionId) {
        Query query = Query.query(Criteria.where("institution_id").is(institutionId));
        return mongoTemplate.find(query, StudentEnrollment.class);
    }

    @Override
    public Mono<Boolean> existsById(String enrollmentId) {
        Query query = Query.query(Criteria.where("id").is(enrollmentId));
        return mongoTemplate.exists(query, StudentEnrollment.class);
    }

    @Override
    public Mono<Boolean> existsByQrCode(String qrCode) {
        Query query = Query.query(Criteria.where("qrCode").is(qrCode));
        return mongoTemplate.exists(query, StudentEnrollment.class);
    }

    @Override
    public Flux<StudentEnrollment> findByClassroomIdIn(List<String> classroomIds) {
        Query query = Query.query(Criteria.where("classroomId").in(classroomIds));
        return mongoTemplate.find(query, StudentEnrollment.class);
    }
}