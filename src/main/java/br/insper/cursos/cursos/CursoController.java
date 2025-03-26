package br.insper.cursos.cursos;

import br.insper.cursos.usuario.Usuario;
import br.insper.cursos.usuario.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("api/curso")
public class CursoController {

    @Autowired
    private CursoService cursoService;

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    public Curso criarCurso(@RequestBody Curso curso,
                            @RequestHeader(name = "email") String email) {
        Usuario usuario = usuarioService.getUsuario(email);

        if (!"ADMIN".equalsIgnoreCase(usuario.getPapel())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Apenas ADMIN pode cadastrar cursos.");
        }

        return cursoService.criarCurso(curso);
    }

    @DeleteMapping("/{id}")
    public void deletarCurso(@PathVariable String id,
                             @RequestHeader(name = "email") String email) {
        Usuario usuario = usuarioService.getUsuario(email);

        if (!"ADMIN".equalsIgnoreCase(usuario.getPapel())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Apenas ADMIN pode deletar cursos.");
        }

        cursoService.excluirCurso(id);
    }

    @GetMapping
    public Iterable<Curso> listarTodos() {
        return cursoService.listarTodos();
    }
}
