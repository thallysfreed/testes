package br.com.silth.servicos;

import br.com.silth.entidades.Usuario;

public class SPCServiceImpl implements SPCService{
    @Override
    public boolean usuarioInadimplente(Usuario usuario) {
        return false;
    }
}
