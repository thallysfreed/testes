package br.com.silth.servicos;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.com.silth.dao.LocacaoDao;
import br.com.silth.entidades.Filme;
import br.com.silth.entidades.Locacao;
import br.com.silth.entidades.Usuario;
import br.com.silth.exception.FilmeSemEstoqueException;
import br.com.silth.exception.LocadoraException;
import br.com.silth.utils.DataUtils;

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

        Boolean negativado = false;

        try {
            negativado = spcService.usuarioInadimplente(usuario);
        } catch (Exception e) {
            throw new LocadoraException("Problemas com SPC, tente novamente");
        }

        if(negativado){
            throw new LocadoraException("Usuario Inadimplente");
        }

        Locacao locacao = new Locacao();

        locacao.setFilmes(filmes);
        locacao.setValor(getValorLocacao(filmes));
        locacao.setUsuario(usuario);
        locacao.setDataLocacao(new Date());

        //Entrega no dia seguinte
        Date dataEntrega = new Date();
        dataEntrega = DataUtils.adicionarDias(dataEntrega, 1);
        if(DataUtils.verificarDiaSemana(dataEntrega, Calendar.SUNDAY)) {
            dataEntrega = DataUtils.adicionarDias(dataEntrega, 1);
        }

        locacao.setDataRetorno(dataEntrega);

        locacaoDao.salvar(locacao);

        return locacao;
    }

    private Double getValorLocacao(List<Filme> filmes) throws FilmeSemEstoqueException {
        Double valorTotal = 0d;
        for (int i = 0; i < filmes.size(); i++) {
            if (filmes.get(i).getEstoque() == null || filmes.get(i).getEstoque() == 0) {
                throw new FilmeSemEstoqueException();
            }
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
        return valorTotal;
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

    public void provogarLocacao(Locacao locacao, int dias){
        Locacao novaLocacao = new Locacao();
        novaLocacao.setUsuario(locacao.getUsuario());
        novaLocacao.setFilmes(locacao.getFilmes());
        novaLocacao.setDataLocacao(new Date());
        novaLocacao.setDataRetorno(DataUtils.obterDataComDiferencaDias(dias));
        novaLocacao.setValor(locacao.getValor() * dias);

        locacaoDao.salvar(novaLocacao);
    }
}