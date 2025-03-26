package br.insper.cursos.cursos;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CursoRepository extends MongoRepository<Curso, String> {
}
