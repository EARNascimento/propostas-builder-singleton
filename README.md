# Propostas Comerciais com Builder e Singleton
Arquitetura e Padrões de Software - Aula 06  / Exercício prático

## Sumário

## Sumário

- [Estrutura do projeto](#estrutura-do-projeto)
- [Compilação](#compilação)
- [Execução](#execução)
- [Saída esperada](#saída-esperada)
- [Perguntas conceituais](#perguntas-conceituais)
  - [1. Qual problema do exercício foi resolvido pelo Builder?](#1-qual-problema-do-exercício-foi-resolvido-pelo-builder)
  - [2. Por que PropostaComercial não deve ser Singleton?](#2-por-que-propostacomercial-não-deve-ser-singleton)
  - [3. Qual é o escopo real da unicidade de ConfiguracaoComercial?](#3-qual-é-o-escopo-real-da-unicidade-de-configuracaocomercial)
  - [4. Por que o Director é útil neste exercício, mas não é obrigatório para toda proposta?](#4-por-que-o-director-é-útil-neste-exercício-mas-não-é-obrigatório-para-toda-proposta)
  - [5. Que dificuldade de teste surgiria se todas as classes chamassem ConfiguracaoComercial.getInstancia() internamente?](#5-que-dificuldade-de-teste-surgiria-se-todas-as-classes-chamassem-configuracaocomercialgetinstancia-internamente)
- [Diagrama UML](#diagrama-uml)

## Estrutura do Projeto
```txt
src/
└── com/
└── empresa/
└── propostas/
├── singleton/
│ └── ConfiguracaoComercial.java
├── model/
│ ├── ItemProposta.java
│ └── PropostaComercial.java
├── builder/
│ ├── PropostaBuilder.java
│ └── PropostaPadraoBuilder.java
├── director/
│ └── DiretorPropostas.java
└── app/
└── Aplicacao.java
```
## Compilação do Projeto


## Compilação

A partir da raiz do projeto (onde está a pasta `src`), execute:

```bash
find src -name "*.java" > fontes.txt
javac -d out @fontes.txt
```

Isso compila todos os arquivos `.java` encontrados recursivamente
e coloca os `.class` na pasta `out`.

## Execução

```bash
java -cp out com.empresa.propostas.app.Aplicacao
```

## Saída esperada

=== Verificação do Singleton ===  

Mesma instância (==)? true\
Moeda padrão : BRL\
Limite de desconto : 15.0%

=== Proposta Básica (via Director) ===  

=== Proposta Comercial ===  

Cliente : Tech Solutions Ltda.\
Responsável : Ana Paula Ferreira\
Validade : 30 dias\
Moeda : BRL\
Itens:

Licença de Software Corporativo | Qtd: 5 | Unit: 1200,00 | Subtotal: 6000,00\
Subtotal : 6000,00\
Total : BRL 6000,00\

=== Proposta Personalizada (via Builder direto) ===  

=== Proposta Comercial ===  

Cliente : Grupo Horizonte S.A.\
Responsável : Carlos Eduardo Lima\
Validade : 45 dias\
Moeda : BRL\
Itens:  

Consultoria em Arquitetura de Software | Qtd: 10 | Unit: 850,00 | Subtotal: 8500,00\
Desenvolvimento de API REST | Qtd: 40 | Unit: 620,00 | Subtotal: 24800,00\
Treinamento Técnico da Equipe | Qtd: 8 | Unit: 500,00 | Subtotal: 4000,00\
Observações : Pagamento em até 3 parcelas. Início previsto para 15/10/2026.\
Subtotal : 37300,00\
Desconto : 10,0% (-3730,00)\
Total : BRL 33570,00\

=== Teste de Validação ===  

Validação capturada corretamente:

A proposta deve possuir pelo menos um item.

=== Execução encerrada com sucesso ===  

## Perguntas para responder no README

1. Qual problema do exercício foi resolvido pelo Builder?

A construção de um objeto com campos obrigatórios, opcionais e com regras de validação que não dependem uma das outras. Sem o Builder, o código cliente precisaria chamar um construtor com sete ou mais parâmetros, sem nenhuma garantia de que os campos obrigatórios foram preenchidos ou que o desconto respeita o limite.
O Builder separa a montagem da validação e entrega um produto garantidamente válido e imutável.

2. Por que PropostaComercial não deve ser Singleton?

Porque o Singleton existe para garantir uma única instância de algo que representa um estado global e compartilhado. PropostaComercial representa um contrato específico com um cliente específico. Fazer dessa classe um Singleton iria impossibilitar a criação de mais de uma proposta por execução, o que foge do propósito da aplicação.

3. Qual é o escopo real da unicidade de ConfiguracaoComercial?

A unicidade vale apenas para uma JVM em execução. Se a aplicação for reiniciada, a instância é recriada do zero com os mesmos valores fixos. Em ambientes com várias JVMS, cada processo terá sua própria instância. O Singleton não garante unicidade, apenas dentro de um único processo Java.

4. Por que o Director é útil neste exercício, mas não é obrigatório para toda proposta?

O Director é útil quando uma sequência de montagem se repete com frequência e precisa ser padronizada, como a proposta básica de um item só.
O Builder é mais interessante para propostas com três itens distintos porque o Director engessaria a flexibilidae que o Builder oferece.

5. Que dificuldade de teste surgiria se todas as classes chamassem ConfiguracaoComercial.getInstancia() internamente?

O Singleton se tornaria uma dependência oculta e global de todas as classes. Em teste unitários, seria impossível substituir a configuração por valores diferentes sem modificar a classe ConfiguracaoComercial. Isso viola o princípio da inversão de dependência e torna os testes frágeis e interdependentes.

## Diagrama UML

![Diagrama UML da aplicação de propostas comerciais, em um diagrama de classes que relaciona ConfiguracaoComercial como Singleton, PropostaComercial e ItemProposta como modelos, PropostaBuilder e PropostaPadraoBuilder como builders, DiretorPropostas como director e Aplicacao como ponto de execução. O diagrama apresenta a estrutura técnica e as relações entre os componentes, sem conteúdo emocional relevante.](img/Diagrama UML_cfb.png)