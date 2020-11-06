package br.com.silth.servicos;

import br.com.silth.builders.FilmeBuilder;
import br.com.silth.builders.LocacaoBuilder;
import br.com.silth.builders.UsuarioBuilder;
import br.com.silth.dao.LocacaoDao;
import br.com.silth.dao.LocacaoDaoImpl;
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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.verification.VerificationMode;

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

    @InjectMocks
    private LocacaoService service;

    @Mock
    private LocacaoDao locacaoDao;

    @Mock
    private SPCService spcService;

    @Mock
    private EmailService emailService;

    @BeforeClass
    public static void setupClass(){

    }

    @AfterClass
    public static void destroyClass(){

    }

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void destroy(){

    }

    @Test
    public void testarLocacao() throws Exception {
        Assume.assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));



        //acao
        Locacao locacao = service.alugarFilme(UsuarioBuilder.umUsuario().agora(), Arrays.asList(FilmeBuilder.umFilme().comValor(2.5).agora(), FilmeBuilder.umFilme().comValor(2.5).agora()));

        //verificacao
        errorCollector.checkThat(locacao.getValor(), is(equalTo(5.0)));
        errorCollector.checkThat(locacao.getDataLocacao(), MatchersProprios.eHoje(locacao.getDataLocacao()));
        errorCollector.checkThat(locacao.getDataRetorno(), MatchersProprios.eAmanha(locacao.getDataLocacao()));
    }

    @Test(expected = FilmeSemEstoqueException.class)
    public void testrLocacao_filmeSemEstoque() throws Exception {
        service.alugarFilme(UsuarioBuilder.umUsuario().agora(), Arrays.asList(FilmeBuilder.umFilme().agora(), FilmeBuilder.umFilme().comEstoque(0).agora()));
    }

    @Test
    public void testLocacao_usuarioVazio() throws FilmeSemEstoqueException{
        try{
            service.alugarFilme(null,  Arrays.asList(FilmeBuilder.umFilme().agora()));
            Assert.fail("Usuário deveria estar vazio");
        }catch (LocadoraException l){
            Assert.assertThat(l.getMessage(), is("Usuario vazio"));
        }
    }

    @Test
    public void testLocacao_filmeVazio() throws FilmeSemEstoqueException{

        try{
            service.alugarFilme(UsuarioBuilder.umUsuario().agora(),  null);
            Assert.fail("Filme deveria estar vazio");
        }catch (LocadoraException l){
            Assert.assertThat(l.getMessage(), is("Filme vazio"));
        }
    }

    @Test
    public void testLocacao_filmeVazio2() throws FilmeSemEstoqueException, LocadoraException {
        expectedException.expect(LocadoraException.class);
        expectedException.expectMessage("Filme vazio");

        service.alugarFilme(null,  null);
    }


    @Test
    public void deveTer25DescontoNoFilme3() throws FilmeSemEstoqueException, LocadoraException {
        Double esperado = 27.5d;

        Locacao locacao = service.alugarFilme(UsuarioBuilder.umUsuario().agora(), Arrays.asList(FilmeBuilder.umFilme().agora(), FilmeBuilder.umFilme().agora(), FilmeBuilder.umFilme().agora()));

        Assert.assertThat(esperado, is(equalTo(locacao.getValor())));

    }

    @Test
    public void deveTer50DescontoNoFilme4() throws FilmeSemEstoqueException, LocadoraException {
        Filme filme = new Filme("Filme 1", 2, 10.0);
        Filme filme2 = new Filme("Filme 2", 2, 10.0);
        Filme filme3 = new Filme("Filme 3", 2, 10.0);
        Filme filme4 = new Filme("Filme 4", 2, 10.0);

        Double esperado = 32.5d;

        Locacao locacao = service.alugarFilme(UsuarioBuilder.umUsuario().agora(), Arrays.asList(FilmeBuilder.umFilme().agora(), FilmeBuilder.umFilme().agora(), filme3, FilmeBuilder.umFilme().agora()));

        Assert.assertThat(esperado, is(equalTo(locacao.getValor())));

    }

    @Test
    public void deveTer75DescontoNoFilme5() throws FilmeSemEstoqueException, LocadoraException {
        Double esperado = 35d;

        Locacao locacao = service.alugarFilme(UsuarioBuilder.umUsuario().agora(), Arrays.asList(FilmeBuilder.umFilme().agora(), FilmeBuilder.umFilme().agora(), FilmeBuilder.umFilme().agora(), FilmeBuilder.umFilme().agora(), FilmeBuilder.umFilme().agora()));

        Assert.assertThat(esperado, is(equalTo(locacao.getValor())));

    }

    @Test
    public void deveTer100DescontoNoFilme6() throws FilmeSemEstoqueException, LocadoraException {
        Double esperado = 35d;

        Locacao locacao = service.alugarFilme(UsuarioBuilder.umUsuario().agora(), Arrays.asList(FilmeBuilder.umFilme().agora(),FilmeBuilder.umFilme().agora(),FilmeBuilder.umFilme().agora(),FilmeBuilder.umFilme().agora(),FilmeBuilder.umFilme().agora(),FilmeBuilder.umFilme().agora()));

        Assert.assertThat(esperado, is(equalTo(locacao.getValor())));

    }

    @Test
    public void deveDevolverNaSegundaAoAlugarNoSabado() throws FilmeSemEstoqueException, LocadoraException{
        Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));

        //cenario
        List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().agora());

        //acao
        Locacao retorno = service.alugarFilme(UsuarioBuilder.umUsuario().agora(), filmes);

        //verificacao
        Assert.assertThat(retorno.getDataRetorno(), MatchersProprios.caiNumaSegunda());
    }

    @Test
    public void deveLancarExpceptionUsuarioInadimplente() throws FilmeSemEstoqueException {
        List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().agora());
        Usuario usuario = UsuarioBuilder.umUsuario().agora();

        Mockito.when(spcService.usuarioInadimplente(usuario)).thenReturn(true);

        try {
            service.alugarFilme(usuario, filmes);
            Assert.fail();
        }catch (LocadoraException locadoraException){
            Assert.assertThat(locadoraException.getMessage(), is("Usuario Inadimplente"));
        }

        Mockito.verify(spcService).usuarioInadimplente(usuario);
    }

    @Test
    public void deveEnviarEmailParaLocacoesAtrasadas(){

        Usuario usuario = UsuarioBuilder.umUsuario().agora();
        Usuario usuario2 = UsuarioBuilder.umUsuario().comNome("Usuario 2").agora();
        Usuario usuario3 = UsuarioBuilder.umUsuario().comNome("Usuario 3").agora();
        Usuario usuario4 = UsuarioBuilder.umUsuario().comNome("Usuario 4").agora();
        List<Locacao> locacaoList = Arrays.asList(LocacaoBuilder.umLocacao().comUsuario(usuario).agora(),
                LocacaoBuilder.umLocacao().atrasada().comUsuario(usuario2).agora(),
                LocacaoBuilder.umLocacao().atrasada().comUsuario(usuario3).agora(),
                LocacaoBuilder.umLocacao().atrasada().comUsuario(usuario3).agora(),
                LocacaoBuilder.umLocacao().comUsuario(usuario4).agora());

        Mockito.when(locacaoDao.obterLocacoesPendentes()).thenReturn(locacaoList);

        service.notificarAtrasados();

        Mockito.verify(emailService).notificarAtraso(usuario2);
        Mockito.verify(emailService, Mockito.atLeastOnce()).notificarAtraso(usuario3);
        Mockito.verifyNoMoreInteractions(emailService);
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
