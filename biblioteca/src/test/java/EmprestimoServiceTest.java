
import com.mycompany.biblioteca.dao.EmprestimoDAO;
import com.mycompany.biblioteca.exceptions.EmprestimoDuplicadoException;
import com.mycompany.biblioteca.exceptions.EmprestimoFinalizadoException;
import com.mycompany.biblioteca.exceptions.LimiteDeLivrosAlcancadoException;
import com.mycompany.biblioteca.exceptions.OrdenacaoEntreDatasIncompativel;
import com.mycompany.biblioteca.models.Cliente;
import com.mycompany.biblioteca.models.Emprestimo;
import com.mycompany.biblioteca.models.Livro;
import com.mycompany.biblioteca.services.EmprestimoService;
import com.mycompany.biblioteca.services.LivroService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 *
 * @author Erick Daniel
 */

public class EmprestimoServiceTest {
    @Mock
    private EmprestimoDAO emprestimoDAO;

    @Mock
    private LivroService livroService;

    @InjectMocks
    private EmprestimoService emprestimoService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testEmprestarLivroDisponivel() {
        Livro livro = new Livro(1,"O Pequeno Príncipe", "Antoine de Saint-Exupéry","Editora A",1945, 1);
        Cliente cliente = new Cliente(5,"Erick","Daniel","Rua R,  140, PR", "45999999999", LocalDate.of(1991,3,3));
        Emprestimo emprestimo = new Emprestimo();
        emprestimo.setLivro(livro);
        emprestimo.setDataEmprestimo(LocalDate.now());
        emprestimo.setDataDevolucao(LocalDate.now().plusDays(7));
        emprestimo.setCliente(cliente);

        when(livroService.obterLivroPorId(1)).thenReturn(livro);
        when(emprestimoDAO.buscarPorClienteID(5)).thenReturn(Collections.emptyList());

        emprestimoService.emprestar(emprestimo);
        Livro emprestado = livroService.obterLivroPorId(1);
        Assertions.assertEquals(0,emprestado.getExemplaresDisponiveis());
    }

    @Test
    public void testEmprestarLivroIndisponivel() {
        Livro livro = new Livro(1,"O Pequeno Príncipe", "Antoine de Saint-Exupéry","Editora A",1945, 1);
        Cliente cliente = new Cliente(5,"Erick","Daniel","Rua R,  140, PR", "45999999999", LocalDate.of(1991,3,3));
        livro.setExemplaresDisponiveis(0);

        Emprestimo emprestimo = new Emprestimo();
        emprestimo.setLivro(livro);
        emprestimo.setDataEmprestimo(LocalDate.now());
        emprestimo.setDataDevolucao(LocalDate.now().plusDays(7));
        emprestimo.setCliente(cliente);

        when(livroService.obterLivroPorId(1)).thenReturn(livro);

        assertThrows(LimiteDeLivrosAlcancadoException.class, () -> emprestimoService.emprestar(emprestimo));
    }

    @Test
    public void testEmprestarLivroDuplicado() {
        Livro livro = new Livro(1,"O Pequeno Príncipe", "Antoine de Saint-Exupéry","Editora A",1945, 1);
        Cliente cliente = new Cliente(5,"Erick","Daniel","Rua R,  140, PR", "45999999999", LocalDate.of(1991,3,3));
        livro.setExemplaresDisponiveis(3);

        Emprestimo emprestimo = new Emprestimo();
        emprestimo.setLivro(livro);
        emprestimo.setDataEmprestimo(LocalDate.now());
        emprestimo.setDataDevolucao(LocalDate.now().plusDays(7));
        emprestimo.setCliente(new Cliente(1,"Fulano","da Silva","Rua A,  140, RJ", "45989999999", LocalDate.of(1991,3,3)));

        when(livroService.obterLivroPorId(1)).thenReturn(livro);
        when(emprestimoDAO.buscarPorClienteID(1)).thenReturn(Collections.singletonList(emprestimo));

        assertThrows(EmprestimoDuplicadoException.class, () -> emprestimoService.emprestar(emprestimo));
    }

    @Test
    public void testDevolverLivro() {
        Livro livro = new Livro(1,"O Pequeno Príncipe", "Antoine de Saint-Exupéry","Editora A",1945, 1);
        Cliente cliente = new Cliente(5,"Erick","Daniel","Rua R,  140, PR", "45999999999", LocalDate.of(1991,3,3));
        livro.setExemplaresDisponiveis(2);

        Emprestimo emprestimo = new Emprestimo();
        emprestimo.setLivro(livro);
        emprestimo.setFinalizado(false);
        emprestimo.setCliente(cliente);
        emprestimo.setDataDevolucao(LocalDate.of(1991,3,13));
        emprestimo.setDataEmprestimo(LocalDate.of(1991,3,3));
        when(livroService.obterLivroPorId(1)).thenReturn(livro);

        emprestimoService.devolver(emprestimo);
        Livro devolvido = livroService.obterLivroPorId(1);
        assertEquals(3,devolvido.getExemplaresDisponiveis());
    }

    @Test void testeDevolverLivroJaDevolvido(){
        Livro livro = new Livro(1,"O Pequeno Príncipe", "Antoine de Saint-Exupéry","Editora A",1945, 1);
        livro.setExemplaresDisponiveis(2);

        Cliente cliente = new Cliente(5,"Erick","Daniel","Rua R,  140, PR", "45999999999", LocalDate.of(1991,3,3));

        Emprestimo emprestimo = new Emprestimo();
        emprestimo.setLivro(livro);
        emprestimo.setFinalizado(true);
        emprestimo.setCliente(cliente);
        emprestimo.setDataDevolucao(LocalDate.of(1991,3,13));
        emprestimo.setDataEmprestimo(LocalDate.of(1991,3,3));

        when(livroService.obterLivroPorId(1)).thenReturn(livro);

        assertThrows(EmprestimoFinalizadoException.class, () -> emprestimoService.devolver(emprestimo));
    }

    @Test void testeOrdenacaoDataSaidaDevolucao(){
        Livro livro = new Livro(1,"O Pequeno Príncipe", "Antoine de Saint-Exupéry","Editora A",1945, 1);
        livro.setExemplaresDisponiveis(2);

        Cliente cliente = new Cliente(5,"Erick","Daniel","Rua R,  140, PR", "45999999999", LocalDate.of(1991,3,3));

        Emprestimo emprestimo = new Emprestimo();
        emprestimo.setLivro(livro);
        emprestimo.setFinalizado(true);
        emprestimo.setCliente(cliente);
        emprestimo.setDataDevolucao(LocalDate.of(1991,3,3));
        emprestimo.setDataEmprestimo(LocalDate.of(1991,3,13));

        when(livroService.obterLivroPorId(1)).thenReturn(livro);

        assertThrows(OrdenacaoEntreDatasIncompativel.class, () -> emprestimoService.devolver(emprestimo));
    }
}
