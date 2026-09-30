package com.empresa.propostas.app;

import com.empresa.propostas.builder.PropostaPadraoBuilder;
import com.empresa.propostas.director.DiretorPropostas;
import com.empresa.propostas.model.ItemProposta;
import com.empresa.propostas.model.PropostaComercial;
import com.empresa.propostas.singleton.ConfiguracaoComercial;

/**
 * Ponto de entrada da aplicação e único ponto de composição.
 *
 * Por que centralizar tudo aqui?
 * Esta é a única classe que conhece e chama ConfiguracaoComercial.getInstancia().
 * Ela extrai os valores necessários e os injeta no Builder e no Director.
 * Isso garante que nenhuma classe de domínio depende do Singleton diretamente,
 * mantendo as responsabilidades separadas e o código testável.
 */
public class Aplicacao {

    public static void main(String[] args) {

        // =====================================================================
        // BLOCO 1 — Demonstração do Singleton
        // =====================================================================

        // Ponto de composição: única chamada a getInstancia() em toda a aplicação.
        // Os valores extraídos aqui serão injetados nas classes que precisam deles.
        ConfiguracaoComercial config1 = ConfiguracaoComercial.getInstancia();
        ConfiguracaoComercial config2 = ConfiguracaoComercial.getInstancia();

        System.out.println("=== Verificação do Singleton ===");
        // == compara referências de memória, não conteúdo.
        // Se o Singleton funciona corretamente, config1 e config2
        // apontam para o mesmo objeto na heap — resultado esperado: true.
        System.out.println("Mesma instância (==)? " + (config1 == config2));
        System.out.println("Moeda padrão       : " + config1.getMoedaPadrao());
        System.out.println("Limite de desconto : " + config1.getLimiteMaximoDesconto() + "%");
        System.out.println();

        // Extração dos valores que serão injetados — nunca passamos o objeto
        // ConfiguracaoComercial inteiro para o Builder ou Director,
        // apenas os valores primitivos de que eles precisam.
        String moeda = config1.getMoedaPadrao();
        double limiteDesconto = config1.getLimiteMaximoDesconto();

        // =====================================================================
        // BLOCO 2 — Proposta básica via Director
        // =====================================================================

        // Builder e Director recebem os valores da configuração por injeção,
        // sem precisar conhecer ou chamar o Singleton internamente.
        PropostaPadraoBuilder builder = new PropostaPadraoBuilder(limiteDesconto);
        DiretorPropostas diretor = new DiretorPropostas(builder, moeda, limiteDesconto);

        PropostaComercial propostaBasica = diretor.criarPropostaBasica(
                "Tech Solutions Ltda.",
                "Ana Paula Ferreira",
                30,
                "Licença de Software Corporativo",
                5,
                1200.00
        );

        System.out.println("=== Proposta Básica (via Director) ===");
        System.out.println(propostaBasica);

        // =====================================================================
        // BLOCO 3 — Proposta personalizada via Builder diretamente
        // =====================================================================

        // O Builder foi reinicializado automaticamente após o construir()
        // chamado pelo Director — podemos usá-lo diretamente sem criar outro.
        PropostaComercial propostaPersonalizada = builder
                .iniciarProposta("Grupo Horizonte S.A.", "Carlos Eduardo Lima", 45)
                .definirMoeda(moeda)
                .adicionarItem(new ItemProposta("Consultoria em Arquitetura de Software", 10, 850.00))
                .adicionarItem(new ItemProposta("Desenvolvimento de API REST", 40, 620.00))
                .adicionarItem(new ItemProposta("Treinamento Técnico da Equipe", 8, 500.00))
                .aplicarDesconto(10.0)
                .definirObservacoes("Pagamento em até 3 parcelas. Início previsto para 15/10/2026.")
                .construir();

        System.out.println("=== Proposta Personalizada (via Builder direto) ===");
        System.out.println(propostaPersonalizada);

        // =====================================================================
        // BLOCO 4 — Tentativa de proposta inválida (sem itens)
        // =====================================================================

        System.out.println("=== Teste de Validação ===");

        try {
            // Proposta intencionalmente inválida: nenhum item adicionado.
            // O Builder deve rejeitar e lançar IllegalStateException.
            builder
                    .iniciarProposta("Cliente Inválido", "Responsável Teste", 15)
                    .definirMoeda(moeda)
                    .construir();

        } catch (IllegalStateException e) {
            // Capturamos apenas IllegalStateException — o tipo lançado pelo Builder
            // para violações de regra de negócio. Não usamos Exception genérico
            // para não engolir erros inesperados silenciosamente.
            System.out.println("Validação capturada corretamente:");
            System.out.println("  >> " + e.getMessage());
        }

        System.out.println();
        System.out.println("=== Execução encerrada com sucesso ===");
    }
}