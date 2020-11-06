package br.com.silth.dao;

import br.com.silth.entidades.Locacao;

import java.util.List;

public interface LocacaoDao {

    public void salvar(Locacao locacao);

    List<Locacao> obterLocacoesPendentes();

}
