package com.empresa.propostas.builder;

import com.empresa.propostas.model.ItemProposta;
import com.empresa.propostas.model.PropostaComercial;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementação concreta do Builder para propostas comerciais padrão.
 *
 * Responsabilidades desta classe:
 * 1. Acumular os dados fornecidos passo a passo.
 * 2. Validar todos os campos e regras de negócio no momento de construir.
 * 3. Entregar um produto imutável e reinicializar o estado interno.
 */
public class PropostaPadraoBuilder implements PropostaBuilder {

    // Limite injetado no construtor — vem da ConfiguracaoComercial via main ou Director
    private final double limiteMaximoDesconto;

    // Estado interno acumulado durante a montagem
    private String cliente;
    private String responsavel;
    private int validadeEmDias;
    private List<ItemProposta> itens;
    private double descontoPercentual;
    private String observacoes;
    private String moeda;

    /**
     * O limite de desconto é a única dependência fixa do Builder.
     * Todos os outros dados variam a cada proposta construída.
     */
    public PropostaPadraoBuilder(double limiteMaximoDesconto) {
        this.limiteMaximoDesconto = limiteMaximoDesconto;
        reinicializar();
    }

    /**
     * Reinicializa o estado interno para valores neutros.
     * Chamado no construtor e automaticamente após cada construir()
     */
    private void reinicializar() {
        this.cliente = null;
        this.responsavel = null;
        this.validadeEmDias = 0;
        this.itens = new ArrayList<>();
        this.descontoPercentual = 0.0;
        this.observacoes = null;
        this.moeda = "BRL"; // valor neutro; será sobrescrito por definirMoeda()
    }

    @Override
    public PropostaBuilder iniciarProposta(String cliente, String responsavel, int validadeEmDias) {
        // Reinicializa antes de começar para garantir isolamento,
        reinicializar();
        this.cliente = cliente;
        this.responsavel = responsavel;
        this.validadeEmDias = validadeEmDias;
        return this;
    }

    @Override
    public PropostaBuilder adicionarItem(ItemProposta item) {
        if (item == null) {
            throw new IllegalArgumentException("Item não pode ser nulo.");
        }
        this.itens.add(item);
        return this;
    }

    @Override
    public PropostaBuilder aplicarDesconto(double percentual) {
        this.descontoPercentual = percentual;
        return this;
    }

    @Override
    public PropostaBuilder definirObservacoes(String observacoes) {
        this.observacoes = observacoes;
        return this;
    }

    @Override
    public PropostaBuilder definirMoeda(String moeda) {
        this.moeda = moeda;
        return this;
    }

    /**
     * Valida todos os campos obrigatórios e regras de negócio,
     * constrói o produto imutável e reinicializa o estado interno.
     */
    @Override
    public PropostaComercial construir() {
        validarCamposObrigatorios();
        validarItens();
        validarDesconto();

        // Produto construído com os dados acumulados e validados
        PropostaComercial proposta = new PropostaComercial(
                cliente,
                responsavel,
                validadeEmDias,
                itens,
                descontoPercentual,
                observacoes,
                moeda
        );

        // Reinicializa imediatamente após entregar o produto,
        // garantindo que o Builder está limpo para a próxima montagem
        reinicializar();

        return proposta;
    }

    // Validações internas — privadas, chamadas apenas por construir()
  
    private void validarCamposObrigatorios() {
        if (cliente == null || cliente.isBlank()) {
            throw new IllegalStateException("O cliente da proposta é obrigatório e não pode estar em branco.");
        }
        if (responsavel == null || responsavel.isBlank()) {
            throw new IllegalStateException("O responsável da proposta é obrigatório e não pode estar em branco.");
        }
        if (validadeEmDias <= 0) {
            throw new IllegalStateException("A validade da proposta deve ser maior que zero.");
        }
    }

    private void validarItens() {
        if (itens == null || itens.isEmpty()) {
            throw new IllegalStateException("A proposta deve possuir pelo menos um item.");
        }
    }

    private void validarDesconto() {
        if (descontoPercentual < 0) {
            throw new IllegalStateException("O desconto não pode ser negativo.");
        }
        if (descontoPercentual > limiteMaximoDesconto) {
            throw new IllegalStateException(
                    String.format("O desconto de %.1f%% excede o limite máximo permitido de %.1f%%.",
                            descontoPercentual, limiteMaximoDesconto));
        }
    }
}