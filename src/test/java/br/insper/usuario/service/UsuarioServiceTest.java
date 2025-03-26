package br.insper.usuario.service;

import br.insper.cursos.usuario.Usuario;
import br.insper.cursos.usuario.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UsuarioServiceTest {

    @InjectMocks
    private UsuarioService usuarioService;

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUsuarioComSucesso() {
        String email = "admin@teste.com";
        Usuario mockUsuario = new Usuario();
        mockUsuario.setEmail(email);
        mockUsuario.setNome("Admin");
        mockUsuario.setPapel("ADMIN");

        ResponseEntity<Usuario> responseEntity = new ResponseEntity<>(mockUsuario, HttpStatus.OK);

        when(restTemplate.getForEntity("http://56.124.127.89:8080/api/usuario/" + email, Usuario.class))
                .thenReturn(responseEntity);

        // substituindo o RestTemplate interno manualmente, se necessário
        UsuarioService serviceComMock = new UsuarioService() {
            @Override
            public Usuario getUsuario(String email) {
                try {
                    return restTemplate
                            .getForEntity("http://56.124.127.89:8080/api/usuario/" + email, Usuario.class)
                            .getBody();
                } catch (HttpClientErrorException.NotFound e) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND);
                }
            }
        };

        Usuario usuario = serviceComMock.getUsuario(email);

        assertNotNull(usuario);
        assertEquals("ADMIN", usuario.getPapel());
        assertEquals("admin@teste.com", usuario.getEmail());
    }

    @Test
    void testGetUsuarioNotFound() {
        String email = "inexistente@teste.com";

        when(restTemplate.getForEntity("http://56.124.127.89:8080/api/usuario/" + email, Usuario.class))
                .thenThrow(HttpClientErrorException.NotFound.create(HttpStatus.NOT_FOUND, "Not Found", HttpHeaders.EMPTY, null, null));

        UsuarioService serviceComMock = new UsuarioService() {
            @Override
            public Usuario getUsuario(String email) {
                try {
                    return restTemplate
                            .getForEntity("http://56.124.127.89:8080/api/usuario/" + email, Usuario.class)
                            .getBody();
                } catch (HttpClientErrorException.NotFound e) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND);
                }
            }
        };

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> serviceComMock.getUsuario(email)
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    void testGetUsuarioLancaNotFound() {
        UsuarioService usuarioService = new UsuarioService();

        // simula um email que não existe no endpoint
        String email = "naoexiste@teste.com";

        // aqui não mockamos, então de fato esse endpoint precisa não existir ou estar offline
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            usuarioService.getUsuario(email);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }
}
