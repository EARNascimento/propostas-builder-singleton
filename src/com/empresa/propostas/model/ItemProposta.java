package com.empresa.propostas.model;

/**
 * Representa um item dentro de uma proposta comercial.
 * Vou manter ele imutável porque o PropostaComercial também é, deixarei padronizado.
 * Ele não vai ter setters porque um ItemProposta não pode ser alterado.
 */

public final class ItemProposta{
    private final String descricao;
    private final int quantidade;
    private final double valorUnitario;

    public ItemProposta(String descricao, int quantidade, double valorUnitario){
        if (descricao == null || descricao.isBlank()){
            throw new IllegalArgumentException("A descrição do item não pode estar em branco.");
        }
        if(quantidade <= 0) {
            throw new IllegalArgumentException("A quantidade do item deve ser maior que zero.");
        }
        if(valorUnitario < 0){
            throw new IllegalArgumentException("O valor unitário do item não pode ser negativo");
        }

        this.descricao = descricao;
        this.quantidade = quantidade;
        this.valorUnitario = valorUnitario;
    }

    public String getDescricao(){
        return descricao;
    }

    public int getQuantidade(){
        return quantidade;
    }

    public double getValorUnitario(){
        return valorUnitario;
    }

    public double calcularSubtotal(){
        return quantidade * valorUnitario;
    }

    @Override
    public String toString(){
        return String.format(" -%s | Qtd: %d | Unit: %.2f | Subtotal: %.2f",
            descricao, quantidade, valorUnitario, calcularSubtotal());
    }
}

