package br.com.silth.builders;

import br.com.silth.entidades.Filme;

public class FilmeBuilder {
    private Filme filme;

    public FilmeBuilder() {
    }

    public static FilmeBuilder umFilme(){
        FilmeBuilder builder = new FilmeBuilder();
        builder.filme = new Filme();

        builder.filme.setEstoque(5);
        builder.filme.setNome("Filme 1");
        builder.filme.setPrecoLocacao(10.0);

        return builder;
    }

    public FilmeBuilder comValor(Double valorFilme){
        filme.setPrecoLocacao(valorFilme);
        return this;
    }


    public FilmeBuilder comEstoque(Integer estoque){
        filme.setEstoque(estoque);
        return this;
    }

    public Filme agora(){
        return filme;
    }
}
