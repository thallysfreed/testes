package br.com.silth.servicos;

import br.com.silth.entidades.Usuario;

public interface EmailService {
    public void notificarAtraso(Usuario usuario);
}
