package br.com.silth.servicos;

import java.util.Date;
import java.util.List;

import br.com.silth.dao.LocacaoDao;
import br.com.silth.dao.LocacaoDaoImpl;
import br.com.silth.entidades.Filme;
import br.com.silth.entidades.Locacao;
import br.com.silth.entidades.Usuario;
import br.com.silth.exception.FilmeSemEstoqueException;
import br.com.silth.exception.LocadoraException;
import br.com.silth.utils.DataUtils;
import org.junit.Assert;
import org.junit.Test;

public class LocacaoService {

    private LocacaoDao locacaoDao;

    private SPCService spcService;

    private EmailService emailService;

    public Locacao alugarFilme(Usuario usuario, List<Filme> filmes) throws FilmeSemEstoqueException, LocadoraException {

        if (filmes == null || filmes.isEmpty()) {
            throw new LocadoraException("Filme vazio");
        }

        if (usuario == null || usuario.getNome() == null || usuario.getNome().isEmpty()) {
            throw new LocadoraException("Usuario vazio");
        }

        if(spcService.usuarioInadimplente(usuario)){
            throw new LocadoraException("Usuario Inadimplente");
        }

        Double valorTotal =0d;
        Locacao locacao = new Locacao();

        for (int i = 0; i < filmes.size(); i++) {
            if (filmes.get(i).getEstoque() == null || filmes.get(i).getEstoque() == 0) {
                throw new FilmeSemEstoqueException();
            }
            locacao.getFilmes().add(filmes.get(i));
            switch (i){
                case 2: {
                    valorTotal = valorTotal + (filmes.get(i).getPrecoLocacao() * 0.75);
                    break;
                    }
                case 3: {
                    valorTotal = valorTotal + (filmes.get(i).getPrecoLocacao() * 0.5);
                    break;
                    }
                case 4: {
                    valorTotal = valorTotal + (filmes.get(i).getPrecoLocacao() * 0.25);
                    break;
                }
                case 5: {
                    valorTotal = valorTotal + (filmes.get(i).getPrecoLocacao() * 0);
                    break;
                }

                default:
                    valorTotal = valorTotal + filmes.get(i).getPrecoLocacao();
            }
        }


        locacao.setUsuario(usuario);
        locacao.setDataLocacao(new Date());
        locacao.setValor(valorTotal);
        //Entrega no dia seguinte
        Date dataEntrega = new Date();
        dataEntrega = DataUtils.adicionarDias(dataEntrega, 1);
        locacao.setDataRetorno(dataEntrega);

        locacaoDao.salvar(locacao);

        return locacao;
    }

    public void notificarAtrasados(){
        List<Locacao> locacaoList = locacaoDao.obterLocacoesPendentes();

        for (Locacao locacao :
                locacaoList) {
            if(locacao.getDataRetorno().before(new Date())) {
                emailService.notificarAtraso(locacao.getUsuario());
            }
        }
    }
}