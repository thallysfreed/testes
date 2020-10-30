package br.com.silth.servicos;

import br.com.silth.entidades.Filme;
import br.com.silth.entidades.Locacao;
import br.com.silth.entidades.Usuario;
import br.com.silth.exception.FilmeSemEstoqueException;
import br.com.silth.exception.LocadoraException;
import br.com.silth.utils.DataUtils;
import org.hamcrest.CoreMatchers;
import org.hamcrest.CoreMatchers.*;
import org.junit.*;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import java.util.Arrays;
import java.util.Date;

import static org.hamcrest.CoreMatchers.*;

public class LocacaoServiceTest {

    @Rule
    public ErrorCollector errorCollector = new ErrorCollector();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private LocacaoService service;

    private static Integer count;

    @BeforeClass
    public static void setupClass(){
        count = 0;
    }

    @AfterClass
    public static void destroyClass(){

    }

    @Before
    public void setup(){
        service = new LocacaoService();
        count++;
    }

    @After
    public void destroy(){
        System.out.println(count);
    }

    @Test
    public void testarLocacao() throws Exception {
        //cenario
        Usuario usuario = new Usuario("Usuario 1");
        Filme filme = new Filme("Filme 1", 2, 5.0);
        Filme filme2 = new Filme("Filme 2", 2, 0.0);

        //acao
        Locacao locacao = service.alugarFilme(usuario, Arrays.asList(filme, filme2));

        //verificacao

        errorCollector.checkThat(locacao.getValor(), is(equalTo(5.0)));
        errorCollector.checkThat(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
        errorCollector.checkThat(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)), is(true));
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
