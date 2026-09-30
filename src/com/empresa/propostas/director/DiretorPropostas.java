package com.empresa.propostas.director;

import com.empresa.propostas.builder.PropostaBuilder;
import com.empresa.propostas.model.ItemProposta;
import com.empresa.propostas.model.PropostaComercial;

/**
 * Coordena receitas de construção de propostas usando um PropostaBuilder.
 *
 * Por que o Director existe?
 * Ele encapsula sequências de construção que se repetem com frequência,
 * evitando que o código cliente (main) precise conhecer a ordem exata
 * das etapas para montar cada tipo de proposta.
 *
 * Por que o Director NÃO é obrigatório para toda proposta?
 * Porque ele é uma conveniência, não uma restrição. Propostas únicas ou
 * altamente personalizadas são melhor construídas diretamente pelo Builder,
 * sem passar por uma receita pré-definida. O Director só agrega valor
 * quando a sequência de montagem se repete e precisa ser padronizada.
 *
 * Por que moeda e limiteDesconto são recebidos aqui e não buscados
 * do Singleton internamente?
 * Pelo mesmo motivo do Builder: o Director não deve conhecer o Singleton.
 * Quem compõe o Director (o main) é responsável por injetar os valores
 * vindos da ConfiguracaoComercial, mantendo o ponto de composição único.
 */
public class DiretorPropostas {

    private final PropostaBuilder builder;
    private final String moeda;
    private final double limiteDesconto;

    /**
     * O Director recebe o Builder já configurado e os valores da
     * configuração comercial injetados externamente.
     * Isso garante que o Director pode ser usado com qualquer
     * implementação de PropostaBuilder sem alteração.
     */
    public DiretorPropostas(PropostaBuilder builder, String moeda, double limiteDesconto) {
        this.builder = builder;
        this.moeda = moeda;
        this.limiteDesconto = limiteDesconto;
    }

    /**
     * Receita 1: proposta básica com um único item padrão.
     *
     * Útil para orçamentos rápidos onde o cliente precisa de uma
     * referência de valor sem personalização. Sem desconto, sem observações.
     *
     * @param cliente        Nome do cliente da proposta.
     * @param responsavel    Nome do responsável comercial.
     * @param validadeEmDias Prazo de validade da proposta em dias.
     * @param itemDescricao  Descrição do serviço ou produto ofertado.
     * @param quantidade     Quantidade do item.
     * @param valorUnitario  Valor unitário do item.
     */
    public PropostaComercial criarPropostaBasica(
            String cliente,
            String responsavel,
            int validadeEmDias,
            String itemDescricao,
            int quantidade,
            double valorUnitario) {

        return builder
                .iniciarProposta(cliente, responsavel, validadeEmDias)
                .definirMoeda(moeda)
                .adicionarItem(new ItemProposta(itemDescricao, quantidade, valorUnitario))
                .construir();
    }

    /**
     * Receita 2: proposta completa com múltiplos itens, desconto e observações.
     *
     * Útil para propostas formais enviadas a clientes com negociação em curso.
     * O array de itens permite adicionar quantos forem necessários sem
     * sobrecarregar a assinatura do método com parâmetros fixos.
     *
     * @param cliente            Nome do cliente da proposta.
     * @param responsavel        Nome do responsável comercial.
     * @param validadeEmDias     Prazo de validade da proposta em dias.
     * @param itens              Array com todos os itens da proposta.
     * @param descontoPercentual Percentual de desconto a aplicar (0 a limiteDesconto).
     * @param observacoes        Texto livre com condições ou observações comerciais.
     */
    public PropostaComercial criarPropostaCompleta(
            String cliente,
            String responsavel,
            int validadeEmDias,
            ItemProposta[] itens,
            double descontoPercentual,
            String observacoes) {

        // Inicia a proposta com os campos obrigatórios
        builder.iniciarProposta(cliente, responsavel, validadeEmDias)
               .definirMoeda(moeda)
               .aplicarDesconto(descontoPercentual)
               .definirObservacoes(observacoes);

        // Adiciona cada item individualmente para respeitar a interface do Builder
        for (ItemProposta item : itens) {
            builder.adicionarItem(item);
        }

        return builder.construir();
    }
}