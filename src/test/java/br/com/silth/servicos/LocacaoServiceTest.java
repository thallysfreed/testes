package br.com.silth.servicos;

import br.com.silth.entidades.Filme;
import br.com.silth.entidades.Locacao;
import br.com.silth.entidades.Usuario;
import br.com.silth.exception.FilmeSemEstoqueException;
import br.com.silth.exception.LocadoraException;
import br.com.silth.matcher.MatchersProprios;
import br.com.silth.utils.DataUtils;
import org.hamcrest.CoreMatchers;
import org.hamcrest.CoreMatchers.*;
import org.junit.*;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;

public class LocacaoServiceTest {

    @Rule
    public ErrorCollector errorCollector = new ErrorCollector();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private LocacaoService service;

    @BeforeClass
    public static void setupClass(){

    }

    @AfterClass
    public static void destroyClass(){

    }

    @Before
    public void setup(){
        service = new LocacaoService();

    }

    @After
    public void destroy(){

    }

    @Test
    public void testarLocacao() throws Exception {
        Assume.assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));

        //cenario
        Usuario usuario = new Usuario("Usuario 1");
        Filme filme = new Filme("Filme 1", 2, 5.0);
        Filme filme2 = new Filme("Filme 2", 2, 0.0);

        //acao
        Locacao locacao = service.alugarFilme(usuario, Arrays.asList(filme, filme2));

        //verificacao

        errorCollector.checkThat(locacao.getValor(), is(equalTo(5.0)));
        errorCollector.checkThat(locacao.getDataLocacao(), MatchersProprios.eHoje(locacao.getDataLocacao()));
        errorCollector.checkThat(locacao.getDataRetorno(), MatchersProprios.eAmanha(locacao.getDataLocacao()));
    }

    @Test(expected = FilmeSemEstoqueException.class)
    public void testrLocacao_filmeSemEstoque() throws Exception {
        Usuario usuario = new Usuario("Usuario 1");
        Filme filme = new Filme("Filme 1", 0, 5.0);
        Filme filme2 = new Filme("Filme 2", 2, 5.0);

        service.alugarFilme(usuario, Arrays.asList(filme, filme2));
    }

    @Test
    public void testLocacao_usuarioVazio() throws FilmeSemEstoqueException{
        Filme filme = new Filme("A volta dos que não fotam", 1, 4.0);
        Filme filme2 = new Filme("Filme 2", 2, 5.0);
        try{
            service.alugarFilme(null,  Arrays.asList(filme, filme2));
            Assert.fail("Usuário deveria estar vazio");
        }catch (LocadoraException l){
            Assert.assertThat(l.getMessage(), is("Usuario vazio"));
        }
    }

    @Test
    public void testLocacao_filmeVazio() throws FilmeSemEstoqueException{
        Usuario usuario = new Usuario("Usuario 1");

        try{
            service.alugarFilme(usuario,  null);
            Assert.fail("Filme deveria estar vazio");
        }catch (LocadoraException l){
            Assert.assertThat(l.getMessage(), is("Filme vazio"));
        }
    }

    @Test
    public void testLocacao_filmeVazio2() throws FilmeSemEstoqueException, LocadoraException {
        Usuario usuario = new Usuario("Usuario 1");

        expectedException.expect(LocadoraException.class);
        expectedException.expectMessage("Filme vazio");

        service.alugarFilme(null,  null);
    }


    @Test
    public void deveTer25DescontoNoFilme3() throws FilmeSemEstoqueException, LocadoraException {
        Usuario usuario = new Usuario("Usuario 1");
        Filme filme = new Filme("Filme 1", 2, 10.0);
        Filme filme2 = new Filme("Filme 2", 2, 10.0);
        Filme filme3 = new Filme("Filme 2", 2, 10.0);

        Double esperado = 27.5d;

        Locacao locacao = service.alugarFilme(usuario, Arrays.asList(filme, filme2, filme3));

        Assert.assertThat(esperado, is(equalTo(locacao.getValor())));

    }

    @Test
    public void deveTer50DescontoNoFilme4() throws FilmeSemEstoqueException, LocadoraException {
        Usuario usuario = new Usuario("Usuario 1");
        Filme filme = new Filme("Filme 1", 2, 10.0);
        Filme filme2 = new Filme("Filme 2", 2, 10.0);
        Filme filme3 = new Filme("Filme 3", 2, 10.0);
        Filme filme4 = new Filme("Filme 4", 2, 10.0);

        Double esperado = 32.5d;

        Locacao locacao = service.alugarFilme(usuario, Arrays.asList(filme, filme2, filme3, filme4));

        Assert.assertThat(esperado, is(equalTo(locacao.getValor())));

    }

    @Test
    public void deveTer75DescontoNoFilme5() throws FilmeSemEstoqueException, LocadoraException {
        Usuario usuario = new Usuario("Usuario 1");
        Filme filme = new Filme("Filme 1", 2, 10.0);
        Filme filme2 = new Filme("Filme 2", 2, 10.0);
        Filme filme3 = new Filme("Filme 3", 2, 10.0);
        Filme filme4 = new Filme("Filme 4", 2, 10.0);
        Filme filme5 = new Filme("Filme 5", 2, 10.0);

        Double esperado = 35d;

        Locacao locacao = service.alugarFilme(usuario, Arrays.asList(filme, filme2, filme3, filme4, filme5));

        Assert.assertThat(esperado, is(equalTo(locacao.getValor())));

    }

    @Test
    public void deveTer100DescontoNoFilme6() throws FilmeSemEstoqueException, LocadoraException {
        Usuario usuario = new Usuario("Usuario 1");
        Filme filme = new Filme("Filme 1", 2, 10.0);
        Filme filme2 = new Filme("Filme 2", 2, 10.0);
        Filme filme3 = new Filme("Filme 3", 2, 10.0);
        Filme filme4 = new Filme("Filme 4", 2, 10.0);
        Filme filme5 = new Filme("Filme 5", 2, 10.0);
        Filme filme6 = new Filme("Filme 6", 2, 10.0);

        Double esperado = 35d;

        Locacao locacao = service.alugarFilme(usuario, Arrays.asList(filme, filme2, filme3, filme4, filme5, filme6));

        Assert.assertThat(esperado, is(equalTo(locacao.getValor())));

    }

    @Test
    public void deveDevolverNaSegundaAoAlugarNoSabado() throws FilmeSemEstoqueException, LocadoraException{
        Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));

        //cenario
        Usuario usuario = new Usuario("Usuario 1");
        List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 1, 5.0));

        //acao
        Locacao retorno = service.alugarFilme(usuario, filmes);

        //verificacao
        Assert.assertThat(retorno.getDataRetorno(), MatchersProprios.caiNumaSegunda());
    }
//    @Test
//    public void testrLocacao_filmeSemEstoque_2() {
//        LocacaoService service = new LocacaoService();
//        Usuario usuario = new Usuario("Usuario 1");
//        Filme filme = new Filme("Filme 1", 0, 5.0);
//
//        try {
//            service.alugarFilme(usuario, filme);
//            Assert.fail("Deveria ter lançado excesão");
//        }catch (Exception e){
//            Assert.assertThat(e.getMessage(), is("Filme sem estoque!"));
//        }
//    }
//
//    @Test
//    public void testrLocacao_filmeSemEstoque_3() throws Exception {
//        LocacaoService service = new LocacaoService();
//        Usuario usuario = new Usuario("Usuario 1");
//        Filme filme = new Filme("Filme 1", 0, 5.0);
//
//        expectedException.expect(Exception.class);
//        expectedException.expectMessage("Filme sem estoque!");
//
//        service.alugarFilme(usuario, filme);
//    }


}
