package br.com.silth.servicos;

import br.com.silth.entidades.Filme;
import br.com.silth.entidades.Locacao;
import br.com.silth.entidades.Usuario;
import br.com.silth.utils.DataUtils;
import org.hamcrest.CoreMatchers;
import org.hamcrest.CoreMatchers.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

import static org.hamcrest.CoreMatchers.*;

public class LocacaoServiceTest {

    @Test
    public void testarLocacao() {
        //cenario
        LocacaoService service = new LocacaoService();
        Usuario usuario = new Usuario("Usuario 1");
        Filme filme = new Filme("Filme 1", 2, 5.0);

        //acao
        Locacao locacao = service.alugarFilme(usuario, filme);

        //verificacao

        Assert.assertThat(locacao.getValor(), is(equalTo(5.0)));
        Assert.assertThat(locacao.getValor(), is(not(6.0)));
        Assert.assertThat(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
        Assert.assertThat(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)), is(true));
    }
}
