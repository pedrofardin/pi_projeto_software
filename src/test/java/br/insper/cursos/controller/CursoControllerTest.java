package br.insper.cursos.controller;

import br.insper.cursos.cursos.Curso;
import br.insper.cursos.cursos.CursoController;
import br.insper.cursos.cursos.CursoService;
import br.insper.cursos.usuario.Usuario;
import br.insper.cursos.usuario.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class CursoControllerTest {

    @InjectMocks
    private CursoController cursoController;

    @Mock
    private CursoService cursoService;

    @Mock
    private UsuarioService usuarioService; // <- ESSA LINHA É NECESSÁRIA AGORA

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(cursoController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();

        this.objectMapper = new ObjectMapper();
    }

    @Test
    void testCriarCurso() throws Exception {
        Curso curso = new Curso("1", "Java", "Curso completo de Java", 40, "Prof. João", "João", "joao@email.com");

        // Mocka usuário ADMIN
        Usuario usuario = new Usuario();
        usuario.setEmail("joao@email.com");
        usuario.setNome("João");
        usuario.setPapel("ADMIN");

        Mockito.when(usuarioService.getUsuario("joao@email.com")).thenReturn(usuario);
        Mockito.when(cursoService.criarCurso(Mockito.any(Curso.class))).thenReturn(curso);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/curso")
                        .header("email", "joao@email.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(curso)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(curso)));
    }

    @Test
    void testExcluirCurso() throws Exception {
        // Mocka usuário ADMIN
        Usuario usuario = new Usuario();
        usuario.setEmail("admin@teste.com");
        usuario.setNome("Admin");
        usuario.setPapel("ADMIN");

        Mockito.when(usuarioService.getUsuario("admin@teste.com")).thenReturn(usuario);
        Mockito.doNothing().when(cursoService).excluirCurso("1");

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/curso/1")
                        .header("email", "admin@teste.com"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(cursoService).excluirCurso("1");
    }
}
