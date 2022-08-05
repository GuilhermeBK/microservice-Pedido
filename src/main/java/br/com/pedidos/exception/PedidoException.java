package br.com.pedidos.exception;

public class PedidoException extends RuntimeException {

    private String mensagem;

    private Throwable throwable;

    public PedidoException (String mensagem, Throwable throwable){
        this.mensagem = mensagem;
        this.throwable = throwable;
    }

    public PedidoException (String mensagem){
        this.mensagem = mensagem;
    }
}
