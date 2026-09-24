package com.empresa.propostas.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Produto final gerado pelo Builder
 * Ele é imutável porque uma porposta entregue ao cliente não pode ser alterada depois de construída. Isso garante que o objeto que foi criado é exatamente o que o cliente vai ver.
 * Ele não é Singleton porque cada proposta é um contrato diferente.
 */

public final class PropostaComercial {

    private final String cliente;
    private final String responsavel;
    private final int validadeEmDias;
    private final List<ItemProposta> itens;
    private final double descontoPercentual;
    private final String observacoes;
    private final String moeda;

    PropostaComercial(
        String cliente,
        String responsavel,
        int validadeEmDias,
        List<ItemProposta> itens,
        double descontoPercentual,
        String observacoes,
        String moeda
    ){
        this.cliente = cliente;
        this.responsavel = responsavel;
        this.validadeEmDias = validadeEmDias;
        this.itens = Collections.unmodifiableList(new ArrayList<>(itens));
        this.descontoPercentual = descontoPercentual;
        this.observacoes = observacoes;
        this.moeda = moeda;
    }

    /*Soma os subtotais de todos os itens antes de aplicar o desconto */
    public double calcularSubtotal(){
        return itens.stream()
        .mapToDouble(ItemProposta::calcularSubtotal)
        .sum();
    }

    /*Valor absoluto do desconto aplicao ao subtotal , é calculado a partir do perentual informado na construção.*/
    public double calcularDesconto(){
        return calcularSubtotal() * (descontoPercentual / 100.0);
    }

    /*Total final: subtotal menos o desconto */

    public double calcularTotal(){
        return calcularSubtotal() - calcularDesconto();
    }

    /*Getters*/

    public String getCliente(){
        return cliente;
    }

    public String getResponsavel(){
        return responsavel;
    }

    public int getValidadeEmDias(){
        return validadeEmDias;
    }

    /*Retorna a lista já protegida */

    public List<ItemProposta> getItens(){
        return itens;
    }

    public double getDescontoPercentual(){
        return descontoPercentual;
    }

    public String getObservacoes(){
        return observacoes;
    }

    public String getMoeda(){
        return moeda;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("=== Proposta Comercial ===\n");
        sb.append(String.format("Cliente     : %s%n", cliente));
        sb.append(String.format("Responsável : %s%n", responsavel));
        sb.append(String.format("Validade    : %d dias%n", validadeEmDias));
        sb.append(String.format("Moeda       : %s%n", moeda));

        sb.append("Itens:\n");
        itens.forEach(item -> sb.append(item.toString()).append("\n"));

        if (observacoes != null && !observacoes.isBlank()) {
            sb.append(String.format("Observações : %s%n", observacoes));
        }

        sb.append(String.format("Subtotal    : %.2f%n", calcularSubtotal()));

        if (descontoPercentual > 0) {
            sb.append(String.format("Desconto    : %.1f%% (-%.2f)%n",
                    descontoPercentual, calcularDesconto()));
        }

        sb.append(String.format("Total       : %s %.2f%n", moeda, calcularTotal()));

        return sb.toString();
    }
}