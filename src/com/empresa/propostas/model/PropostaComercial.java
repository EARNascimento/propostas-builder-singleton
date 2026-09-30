package com.empresa.propostas.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Produto final imutável gerado pelo Builder.
 *
 * Por que imutável?
 * Uma proposta comercial entregue ao cliente não deve poder ser alterada
 * depois de construída — isso representaria uma falha de integridade comercial.
 * A imutabilidade garante que o objeto que saiu do Builder é exatamente
 * o que o cliente vai ver, sem risco de alteração acidental posterior.
 *
 * Por que NÃO é Singleton?
 * Porque cada proposta representa um contrato diferente, com cliente,
 * itens e valores distintos. O Singleton existe para unicidade de configuração
 * global — aqui precisamos do oposto: múltiplas instâncias independentes.
 */
public final class PropostaComercial {

    private final String cliente;
    private final String responsavel;
    private final int validadeEmDias;
    private final List<ItemProposta> itens;
    private final double descontoPercentual;
    private final String observacoes;
    private final String moeda;

    /**
     * Construtor de acesso restrito ao pacote.
     * Somente o Builder (que está no pacote builder, não em model) poderia
     * precisar de acesso, mas como usamos um construtor package-private aqui
     * e o Builder chama via instanciação direta, isso fica claro no design.
     *
     * Na prática, restringir ao pacote já impede que código externo
     * construa uma PropostaComercial sem passar pelo Builder.
     *
     * A lista recebida é copiada defensivamente: mesmo que o Builder
     * modifique sua lista interna depois de entregar a proposta,
     * esta instância permanece íntegra.
     */
    public PropostaComercial(
            String cliente,
            String responsavel,
            int validadeEmDias,
            List<ItemProposta> itens,
            double descontoPercentual,
            String observacoes,
            String moeda) {

        this.cliente = cliente;
        this.responsavel = responsavel;
        this.validadeEmDias = validadeEmDias;
        // Cópia defensiva: protege esta instância de alterações externas na lista original
        this.itens = Collections.unmodifiableList(new ArrayList<>(itens));
        this.descontoPercentual = descontoPercentual;
        this.observacoes = observacoes;
        this.moeda = moeda;
    }
    /**
     * Soma os subtotais de todos os itens antes de aplicar qualquer desconto.
     */
    public double calcularSubtotal() {
        return itens.stream()
                .mapToDouble(ItemProposta::calcularSubtotal)
                .sum();
    }

    /**
     * Valor absoluto do desconto aplicado sobre o subtotal.
     */
    public double calcularDesconto() {
        return calcularSubtotal() * (descontoPercentual / 100.0);
    }

    /**
     * Total final: subtotal menos o desconto.
     */
    public double calcularTotal() {
        return calcularSubtotal() - calcularDesconto();
    }

    public String getCliente() {
        return cliente;
    }

    public String getResponsavel() {
        return responsavel;
    }

    public int getValidadeEmDias() {
        return validadeEmDias;
    }

    public List<ItemProposta> getItens() {
        return itens;
    }

    public double getDescontoPercentual() {
        return descontoPercentual;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public String getMoeda() {
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