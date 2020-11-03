package br.com.silth.servicos;

import br.com.silth.entidades.Filme;
import br.com.silth.entidades.Locacao;
import br.com.silth.entidades.Usuario;
import br.com.silth.exception.FilmeSemEstoqueException;
import br.com.silth.exception.LocadoraException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;


@RunWith(Parameterized.class)
public class CalculoValorLocacaoTest {

    private LocacaoService service;

    @Parameterized.Parameter
    public List<Filme> filmes;

    @Parameterized.Parameter(value = 1)
    public Double valorLocacao;

    @Before
    public void setup() {
        service = new LocacaoService();
    }

    private static Filme filme = new Filme("Filme 1", 2, 10.0);
    private static Filme filme2 = new Filme("Filme 2", 2, 10.0);
    private static Filme filme3 = new Filme("Filme 3", 2, 10.0);
    private static Filme filme4 = new Filme("Filme 4", 2, 10.0);
    private static Filme filme5 = new Filme("Filme 5", 2, 10.0);
    private static Filme filme6 = new Filme("Filme 6", 2, 10.0);

    @Parameterized.Parameters
    public static Collection<Object[]> getParameters() {
        return Arrays.asList(new Object[][]{{Arrays.asList(filme), 10.0},
                                            {Arrays.asList(filme, filme2), 20.0},
                                            {Arrays.asList(filme, filme2, filme3), 27.5},
                                            {Arrays.asList(filme, filme2, filme3, filme4), 32.5},
                                            {Arrays.asList(filme, filme2, filme3, filme4, filme5), 35.0},
                                            {Arrays.asList(filme, filme2, filme3, filme4, filme5, filme6), 35.0}});
    }

    @Test
    public void deveTer75DescontoNoFilme5() throws FilmeSemEstoqueException, LocadoraException {
        Locacao locacao = service.alugarFilme(new Usuario("Teste"), filmes);

        Assert.assertThat(valorLocacao, is(equalTo(locacao.getValor())));
    }
}
