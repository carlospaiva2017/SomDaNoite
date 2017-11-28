package br.com.fafeltech.somdanoite.helper;

import java.util.HashMap;

public class ParseErros {

    private HashMap<Integer, String> erros;

    public ParseErros() {
        this.erros = new HashMap<>();
        erros.put(202, "Usuário já existe, favor escolha outro nome de usuário.");
        erros.put(201, "Favor preencher a senha!");
        erros.put(200, "Favor preencher nome de usuário");
        erros.put(203, "E-mail já utilizado por outro usuário. Por favor informe outro e-mail.");
        erros.put(204, "Favor preencher endereço de e-mail.");
    }

    public String getErro (int codErro) {
        return this.erros.get(codErro);
    }
}
