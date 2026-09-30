package com.empresa.propostas.singleton;

/**
 * Singleton que representa a configuração comercial global da aplicação.
 *
 * Por que Singleton aqui?
 * A configuração (moeda e limite de desconto) deve ser a mesma para toda a JVM
 * durante a execução. Não faz sentido criar instâncias diferentes com valores
 * distintos — isso quebraria a consistência das propostas geradas.
 *
 * Técnica escolhida: Initialization-on-demand holder idiom.
 * A instância só é criada quando getInstancia() é chamado pela primeira vez.
 * A JVM garante que a inicialização da classe interna é thread-safe sem
 * necessidade de synchronized ou volatile.
 */
public class ConfiguracaoComercial {

    private final String moedaPadrao;
    private final double limiteMaximoDesconto;

    /**
     * Construtor privado — impede que qualquer outro código instancie
     * ConfiguracaoComercial diretamente, garantindo que só existe uma instância.
     */
    private ConfiguracaoComercial() {
        this.moedaPadrao = "BRL";
        this.limiteMaximoDesconto = 15.0;
    }

    private static final class Holder {
        private static final ConfiguracaoComercial INSTANCIA = new ConfiguracaoComercial();
    }

    /**
     * Ponto de acesso global à instância única.
     */
    public static ConfiguracaoComercial getInstancia() {
        return Holder.INSTANCIA;
    }

    public String getMoedaPadrao() {
        return moedaPadrao;
    }

    public double getLimiteMaximoDesconto() {
        return limiteMaximoDesconto;
    }

    @Override
    public String toString() {
        return "ConfiguracaoComercial{moeda='" + moedaPadrao + "', limiteDesconto=" + limiteMaximoDesconto + "%}";
    }
}