package br.insper.cursos.service;

import br.insper.cursos.cursos.Curso;
import br.insper.cursos.cursos.CursoRepository;
import br.insper.cursos.cursos.CursoService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class CursoServiceTest {

	@InjectMocks
	private CursoService cursoService;

	@Mock
	private CursoRepository cursoRepository;

	@Test
	void testCriarCursoComSucesso() {
		Curso curso = new Curso();
		curso.setId("1");
		curso.setTitulo("Curso Java");
		curso.setDescricao("Aprenda Java");
		curso.setCargaHoraria(40);
		curso.setInstrutor("Prof. Pedro");
		curso.setNomeUsuario("Pedro");
		curso.setEmailUsuario("pedro@teste.com");

		Mockito.when(cursoRepository.save(curso)).thenReturn(curso);

		Curso criado = cursoService.criarCurso(curso);

		Assertions.assertEquals("Curso Java", criado.getTitulo());
		Assertions.assertEquals("Aprenda Java", criado.getDescricao());
		Assertions.assertEquals(40, criado.getCargaHoraria());
		Assertions.assertEquals("Prof. Pedro", criado.getInstrutor());
		Assertions.assertEquals("Pedro", criado.getNomeUsuario());
		Assertions.assertEquals("pedro@teste.com", criado.getEmailUsuario());
	}

	@Test
	void testExcluirCurso() {
		String id = "1";

		cursoService.excluirCurso(id);

		Mockito.verify(cursoRepository).deleteById(id);
	}

	@Test
	void testListarTodosComCursos() {
		Curso curso1 = new Curso("1", "Java", "Desc", 20, "Prof. A", "User A", "a@a.com");
		Curso curso2 = new Curso("2", "Spring", "Desc", 30, "Prof. B", "User B", "b@b.com");

		List<Curso> cursos = Arrays.asList(curso1, curso2);

		Mockito.when(cursoRepository.findAll()).thenReturn(cursos);

		List<Curso> resultado = cursoService.listarTodos();

		Assertions.assertEquals(2, resultado.size());
		Assertions.assertEquals("Java", resultado.get(0).getTitulo());
		Assertions.assertEquals("Spring", resultado.get(1).getTitulo());
	}

	@Test
	void testListarTodosSemCursos() {
		Mockito.when(cursoRepository.findAll()).thenReturn(List.of());

		List<Curso> resultado = cursoService.listarTodos();

		Assertions.assertTrue(resultado.isEmpty());
	}

	@Test
	void testEqualsCursosMesmoId() {
		Curso curso1 = new Curso("1", "Java", "Desc", 20, "Prof", "User", "email@teste.com");
		Curso curso2 = new Curso("1", "Java", "Desc", 20, "Prof", "User", "email@teste.com");

		Assertions.assertEquals(curso1, curso2);
	}

	@Test
	void testEqualsCursosIdsDiferentes() {
		Curso curso1 = new Curso("1", "Java", "Desc", 20, "Prof", "User", "email@teste.com");
		Curso curso2 = new Curso("2", "Java", "Desc", 20, "Prof", "User", "email@teste.com");

		Assertions.assertNotEquals(curso1, curso2);
	}
}
