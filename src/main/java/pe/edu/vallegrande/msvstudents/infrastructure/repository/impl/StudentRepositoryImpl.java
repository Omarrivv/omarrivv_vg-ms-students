package pe.edu.vallegrande.msvstudents.infrastructure.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import pe.edu.vallegrande.msvstudents.domain.model.Student;
import pe.edu.vallegrande.msvstudents.infrastructure.repository.StudentRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class StudentRepositoryImpl implements StudentRepository {

    private final ReactiveMongoTemplate mongoTemplate;

    @Override
    public Mono<Student> save(Student student) {
        return mongoTemplate.save(student);
    }

    @Override
    public Mono<Student> findById(String id) {
        return mongoTemplate.findById(id, Student.class);
    }

    @Override
    public Flux<Student> findAll() {
        return mongoTemplate.findAll(Student.class);
    }

    @Override
    public Flux<Student> findByInstitutionId(String institutionId) {
        Query query = Query.query(Criteria.where("institutionId").is(institutionId));
        return mongoTemplate.find(query, Student.class);
    }

    @Override
    public Mono<Boolean> existsById(String studentId) {
        Query query = Query.query(Criteria.where("id").is(studentId));
        return mongoTemplate.exists(query, Student.class);
    }

    @Override
    public Mono<Student> findByDocumentNumberAndInstitutionId(String documentNumber, String institutionId) {
        Query query = Query.query(
            Criteria.where("documentNumber").is(documentNumber)
                    .and("institutionId").is(institutionId)
        );
        return mongoTemplate.findOne(query, Student.class);
    }
}