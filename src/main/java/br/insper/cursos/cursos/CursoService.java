package br.insper.cursos.cursos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CursoService {

    @Autowired
    private CursoRepository cursoRepository;

    public Curso criarCurso(Curso curso) {
        return cursoRepository.save(curso);
    }

    public void excluirCurso(String id) {
        cursoRepository.deleteById(id);
    }

    public List<Curso> listarTodos() {
        return cursoRepository.findAll();
    }

}
