package br.com.silth.servicos;

import java.util.Date;

import br.com.silth.entidades.Filme;
import br.com.silth.entidades.Locacao;
import br.com.silth.entidades.Usuario;
import br.com.silth.exception.FilmeSemEstoqueException;
import br.com.silth.exception.LocadoraException;
import br.com.silth.utils.DataUtils;
import org.junit.Assert;
import org.junit.Test;

public class LocacaoService {
	
	public Locacao alugarFilme(Usuario usuario, Filme filme) throws FilmeSemEstoqueException, LocadoraException {

		if(filme == null ){
			throw new LocadoraException("Filme vazio");
		}

		if(usuario == null || usuario.getNome() == null || usuario.getNome().isEmpty()){
			throw new LocadoraException("Usuario vazio");
		}

		if(filme.getEstoque() == null || filme.getEstoque() == 0){
			throw new FilmeSemEstoqueException();
		}

		Locacao locacao = new Locacao();
		locacao.setFilme(filme);
		locacao.setUsuario(usuario);
		locacao.setDataLocacao(new Date());
		locacao.setValor(filme.getPrecoLocacao());

		//Entrega no dia seguinte
		Date dataEntrega = new Date();
		dataEntrega = DataUtils.adicionarDias(dataEntrega, 1);
		locacao.setDataRetorno(dataEntrega);
		
		//Salvando a locacao...	
		//TODO adicionar método para salvar
		
		return locacao;
	}


}