package com.empresa.propostas.singleton;

/**
 * Singleton representa a configuração comercial global da aplicação.
 * 
 * O Singleton vai aqui porque a configuração dee ser a mesma para toda a JVM durante a execução. Não posso criar instâncias diferentes com valores diferentes.
 * Eu garanto que ela só é criada quado getInstancia() é chamada.
 */

public class ConfiguracaoComercial {
    private final String moedaPadrao;
    private final double limiteMaximoDesconto;

    private ConfiguracaoComercial() {
        this.moedaPadrao = "BRL";
        this.limiteMaximoDesconto = 15.0;
    }
}

/**
 * Holder é a classe interna carregada somente na primeira chamada.
 */

private static final class Holder {
    private static final ConfiguracaoComercial INSTANCIA = new ConfiguracaoComercial;
}

public static ConfiguracaoComercial getInstancia(){
    return Holder.INSTANCIA;
}

public String getMoedaPadrao(){
    return moedaPadrao;
}

public double getLimiteMaximoDesconto(){
    return limiteMaximoDesconto;
}

@Override
public String toString(){
    return "ConfiguracaoComercial{moeda='" + moedaPadrao + "', limiteDesconto=" + limiteMaximoDesconto + "%}";
}