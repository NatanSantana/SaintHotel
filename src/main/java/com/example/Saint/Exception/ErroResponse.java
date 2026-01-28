package com.example.Saint.Exception;

public class ErroResponse {

    private int status;
    private String mensagem;

    public ErroResponse(int status, String mensagem) {
        this.status = status;
        this.mensagem = mensagem;
    }

    public int getStatus() {
        return status;
    }

    public String getMensagem() {
        return mensagem;
    }
}
