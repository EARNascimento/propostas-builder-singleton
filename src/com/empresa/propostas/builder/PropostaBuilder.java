package com.empresa.propostas.builder;

import com.empresa.propostas.model.ItemProposta;
import com.empresa.propostas.model.PropostaComercial;

/**
 * Contrato das etapas de construção de uma PropostaComercial.
 * 
 * O métodos vão retornar PropostaBuilder para que o encadeamento funcione com qualquer implementação.
 */

public interface PropostaBuilder {

    //Inicia uma nova montagem, limpando o estado anterior.
    PropostaBuilder iniciarProposta(String cliente, String responsavel, int validadeEmDias);

    //Adiciona um item à proposta em construção, pode ser chamado para compor a lista de itens
    PropostaBuilder adicionarItem(ItemProposta item);

    //Define o percentual de desconto a ser aplicado sobre o subtotal.
    PropostaBuilder aplicarDesconto(double percentual);

    //Define as observações.
    PropostaBuilder definirObservacoes(String observacoes);

    //Define a modea que será exibida.
    PropostaBuilder definirMoeda(String moeda);

    //Valida todos os campos, aplica as regras e entrega o produto.
    PropostaComercial construir();
}