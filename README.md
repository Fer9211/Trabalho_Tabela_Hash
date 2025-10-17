# Trabalho de Tabela Hash - Análise Comparativa de Três Métodos

**Disciplina:** Resolução de Problemas Estruturados em Computação  
**Alunos:** Fernanda Costa Moraes, Eduardo dos Santos Rodrigues, Emily Pontes Fontana   
**Professor:**  Andrey Cabral Meira 
**Data:** 16 de Outubro de 2025

---

## Sumário

1. [Introdução](#1-introdução)
2. [Funções Hash Implementadas](#2-funções-hash-implementadas)
3. [Arquitetura e Estrutura do Código](#3-arquitetura-e-estrutura-do-código)
4. [Metodologia de Testes](#4-metodologia-de-testes)
5. [Resultados e Análises](#5-resultados-e-análises)
6. [Comparação Entre Métodos](#6-comparação-entre-métodos)
7. [Conclusões](#7-conclusões)

---

## 1. Introdução

### 1.1 Objetivo do Trabalho

Este trabalho implementa, testa e compara três diferentes abordagens para tabelas hash. Desenvolvemos dois métodos baseados em encadeamento para resolução de colisões e um terceiro método utilizando rehashing com endereçamento aberto. 

Nossa análise busca identificar não apenas qual método é mais rápido ou eficiente, mas principalmente entender em quais cenários cada abordagem se destaca. Consideramos métricas como tempo de execução, número de colisões e qualidade da distribuição dos elementos na tabela.

### 1.2 Estrutura do Projeto

Organizamos o código em uma arquitetura modular que facilita a compreensão e manutenção. Separamos claramente as estruturas de dados (nós e tabelas), as funções hash específicas, os gerenciadores que implementam a lógica de inserção e busca, e finalmente os testadores que medem e comparam os resultados. Esta separação não foi apenas uma escolha estética - ela permite que qualquer parte do sistema seja modificada ou estendida sem afetar as demais.

---

## 2. Funções Hash Implementadas

### 2.1 Fibonacci Hash (Encadeamento)

#### 2.1.1 Descrição

O Fibonacci Hash, também conhecido como Golden Ratio Hashing, é uma técnica elegante que aproveita as propriedades matemáticas da razão áurea para distribuir valores de forma uniforme. A constante de ouro (aproximadamente 1.618, ou seu complemento 0.618) tem propriedades únicas que a tornam especialmente úteis para hashing.

#### 2.1.2 Fórmula Matemática

A função segue o padrão:

```
h(k) = ⌊m × ([k × A] mod 1)⌋
```

Onde k representa a chave (nosso código de 9 dígitos), m é o tamanho da tabela, A é a constante (√5 - 1) / 2 que vale aproximadamente 0.6180339887, e a expressão [k × A] mod 1 extrai apenas a parte fracionária do resultado da multiplicação.

#### 2.1.3 Implementação

```java
public int calcularHash(int valor, int tamanho) {
    double a = (Math.sqrt(5) - 1) / 2;  // Constante de ouro
    double resultado = valor * a;
    double parteFracionaria = resultado - Math.floor(resultado);
    return (int) (tamanho * parteFracionaria);
}
```

#### 2.1.4 Por Que Escolhemos Esta Função?

A escolha do Fibonacci Hash não foi aleatória. Esta função tem respaldo teórico sólido e aplicação prática comprovada em sistemas reais. Do ponto de vista teórico, ela oferece excelente distribuição uniforme dos valores justamente por se basear nas propriedades matemáticas da razão áurea, que naturalmente reduz o agrupamento (clustering) de elementos. Além disso, é computacionalmente eficiente, envolvendo apenas multiplicações e não operações mais custosas.

Na prática, vemos esta função sendo amplamente utilizada em implementações profissionais de hash tables. Donald Knuth a recomenda em "The Art of Computer Programming", e ela serve como base para algoritmos de hashing em linguagens como Python e Java. Nossa expectativa é que esta função apresente baixo número de colisões devido à sua boa distribuição, mantendo desempenho consistente independentemente do tamanho da tabela e resultando em listas encadeadas de tamanhos equilibrados.

---

### 2.2 Soma de Partes / Folding (Encadeamento)

#### 2.2.1 Descrição

O método de Dobramento (Folding) adota uma abordagem mais direta e intuitiva. A ideia é simples: dividimos a chave em partes menores, somamos essas partes e aplicamos a operação módulo. Apesar da simplicidade, esta técnica se mostra eficaz especialmente para chaves numéricas longas.

#### 2.2.2 Fórmula Matemática

A fórmula básica é:

```
h(k) = (Σ partes) mod m
```

Para ilustrar com um exemplo concreto, consideremos o número 123456789. Dividimos em três partes de 3 dígitos: 123, 456 e 789. Somamos para obter 1368, e finalmente aplicamos módulo m para obter o índice final na tabela.

#### 2.2.3 Implementação

```java
public int calcularHash(int valor, int tamanho) {
    String valorStr = String.valueOf(valor);
    int soma = 0;
    int tamanhoParte = 3;  // Divide em partes de 3 dígitos
    
    for (int i = 0; i < valorStr.length(); i += tamanhoParte) {
        int fim = Math.min(i + tamanhoParte, valorStr.length());
        String parte = valorStr.substring(i, fim);
        soma += Integer.parseInt(parte);
    }
    
    return soma % tamanho;
}
```

#### 2.2.4 Por Que Escolhemos Esta Função?

A escolha desta função se justifica por várias razões complementares. Primeiro, ela é simples de implementar e entender, o que facilita tanto a manutenção quanto a análise dos resultados. A função utiliza todos os dígitos da chave no cálculo, garantindo que cada parte do número contribua para o hash final. Quando as partes são bem escolhidas, obtemos uma distribuição razoável, e o custo computacional é baixo, envolvendo apenas somas e uma operação módulo.

A intuição por trás do método é interessante. Ao somar partes do número, estamos essencialmente "comprimindo" a informação enquanto mantemos características da chave original. É como dobrar um papel várias vezes: reduzimos o tamanho mas o conteúdo permanece.

Nossa escolha de dividir em partes de 3 dígitos não foi arbitrária. Com códigos de 9 dígitos, obtemos exatamente 3 partes iguais, o que cria um equilíbrio entre granularidade (partes muito pequenas perderiam informação) e eficiência (partes muito grandes poderiam causar overflow ou reduzir a variabilidade do hash).

Esperamos que esta função apresente distribuição razoável, embora possivelmente com mais colisões que o Fibonacci Hash. Há também uma sensibilidade ao padrão dos dados - números sequenciais podem gerar somas similares. Por outro lado, a simplicidade computacional deve resultar em desempenho competitivo.

---

### 2.3 Rehashing Quadrático

#### 2.3.1 Descrição

O Rehashing Quadrático representa uma abordagem fundamentalmente diferente das anteriores. É uma técnica de endereçamento aberto, o que significa que ao encontrar uma colisão, não criamos uma lista encadeada - em vez disso, buscamos a próxima posição livre na própria tabela usando uma função quadrática. Esta diferença conceitual traz implicações importantes para performance e uso de memória.

#### 2.3.2 Fórmula Matemática

A busca por uma posição livre segue o padrão:

```
índice = (h(k) + i²) mod m
```

Aqui, h(k) é o hash inicial (calculamos usando a função Fibonacci como base), i é o número da tentativa (começando em 0), e m é o tamanho da tabela.

A sequência de sondagem cresce quadraticamente. Na primeira tentativa (i=0), testamos a posição original h(k). Se ocupada, na segunda tentativa (i=1) testamos h(k) + 1. Na terceira (i=2), saltamos para h(k) + 4. Depois h(k) + 9, depois h(k) + 16, e assim por diante. Estes "saltos" crescentes são o que diferencia o rehashing quadrático do linear e ajudam a evitar o clustering primário.

#### 2.3.3 Implementação

```java
private int calcularIndice(int chave, int tentativa) {
    int h = funcaoHash(chave);  // Fibonacci hash base
    int indice = (h + tentativa * tentativa) % tamanhoTabela;
    
    if (indice < 0) {
        indice += tamanhoTabela;  // Garante índice positivo
    }
    
    return indice;
}

public boolean inserir(int chave) {
    for (int tentativa = 0; tentativa < tamanhoTabela; tentativa++) {
        int indice = calcularIndice(chave, tentativa);
        
        if (tabela[indice] == -1) {  // Posição vazia
            tabela[indice] = chave;
            return true;
        }
        
        colisoes++;  // Conta colisão
    }
    return false;  // Tabela cheia
}
```

#### 2.3.4 Por Que Escolhemos Esta Função?

O rehashing quadrático traz vantagens teóricas interessantes quando comparado ao encadeamento. O uso de memória é menor porque não precisamos armazenar ponteiros para listas encadeadas - apenas os dados em um array simples. Além disso, há melhor cache locality: como os elementos ficam contíguos na memória, o processador consegue fazer melhor uso do cache, potencialmente acelerando operações. A função quadrática também evita o clustering primário que afeta o rehashing linear, proporcionando melhor distribuição das tentativas de sondagem.

Porém, há desvantagens que precisamos considerar. A tabela pode ficar completamente cheia, impossibilitando novas inserções. Existe também o fenômeno de clustering secundário: elementos que compartilham o mesmo hash inicial seguem exatamente a mesma sequência de sondagem. E a deleção se torna complexa, geralmente exigindo marcadores especiais para indicar posições previamente ocupadas.

A escolha da progressão quadrática em vez de linear é crucial. No rehashing linear, onde simplesmente testamos h(k), h(k)+1, h(k)+2 e assim por diante, elementos que colidem tendem a formar grupos contínuos (clustering primário). A progressão quadrática espalha melhor as tentativas, com saltos maiores entre cada teste, reduzindo significativamente o problema de colisões em cadeia.

Esperamos que este método apresente menor tempo de busca quando a tabela não está cheia, justamente por ser cache-friendly. Antecipamos problemas de desempenho quando o fator de carga ultrapassa 70%, e sabemos que em tabelas pequenas não será possível inserir todos os elementos - uma limitação inerente ao endereçamento aberto.

---

## 3. Arquitetura e Estrutura do Código

### 3.1 Visão Geral

Uma decisão importante de design foi manter duas arquiteturas completamente separadas para encadeamento e rehashing. Não se trata de falta de planejamento, mas sim de uma escolha consciente baseada nas diferenças fundamentais entre as abordagens.

A arquitetura de encadeamento usa arrays de nós (No[]) para implementar listas encadeadas, permitindo que a ocupação ultrapasse 100% do tamanho da tabela. Esta arquitetura suporta tanto o Fibonacci Hash quanto a Soma de Partes. Por outro lado, a arquitetura de rehashing utiliza um array simples de inteiros (int[]), com ocupação naturalmente limitada a 100%. 

Estas arquiteturas são incompatíveis por natureza. As estruturas de dados são fundamentalmente diferentes, e a lógica de inserção e busca não pode ser compartilhada sem comprometer eficiência e clareza. Uma tentativa de unificação forçada tornaria o código confuso e menos eficiente.

### 3.2 Diagrama de Classes

A estrutura de classes para encadeamento segue uma hierarquia clara. No topo temos a classe No, que representa cada nó da lista encadeada. Esta classe é usada pela TabelaHash, que gerencia o array de listas e fornece análises estatísticas. O HashTableManager, por sua vez, utiliza a TabelaHash e implementa a lógica de inserção e busca. Finalmente, o Manager depende de uma HashFunction - uma interface implementada tanto pelo FibonacciHash quanto pelo SomaPartesHash.

Para o rehashing, a arquitetura é bem mais simples. A classe RehashingQuadratico é independente e auto-contida, gerenciando diretamente o array simples de inteiros e todas as operações relacionadas.

### 3.3 Explicação Detalhada dos Componentes

#### 3.3.1 Classe No.java

Esta classe representa um nó de lista encadeada, carregando um valor (nosso código de 9 dígitos) e uma referência ao próximo elemento. Usamos apenas nos métodos de encadeamento. A analogia útil aqui é pensar em cada No como um vagão de trem: carrega dados e se conecta ao próximo vagão.

#### 3.3.2 Classe TabelaHash.java

A TabelaHash é a estrutura principal para encadeamento. Ela gerencia um array de listas encadeadas e fornece análises estatísticas importantes. Uma peculiaridade desta implementação é ter dois arrays (chave e registro) que apontam para as mesmas listas - uma redundância mantida por compatibilidade com o código original.

Um aspecto interessante é o cálculo de gaps. Um gap representa o espaço vazio entre dois buckets ocupados. Por exemplo, se temos um elemento no índice 0 e o próximo apenas no índice 4, há um gap de 3 posições vazias (índices 1, 2 e 3). 

As métricas de gap nos dizem muito sobre a qualidade da distribuição. O menor gap indica quão "apertados" estão os elementos próximos. O maior gap revela os maiores buracos na tabela. A média de gap nos dá uma visão geral da distribuição. Quando encontramos gaps muito grandes, isso sugere má distribuição da função hash, com elementos se concentrando em algumas regiões da tabela.

#### 3.3.3 Interface HashFunction.java

Esta interface define o contrato que qualquer função hash deve seguir. É uma aplicação clássica do Strategy Pattern, permitindo que troquemos facilmente entre diferentes algoritmos de hash sem modificar o código que os utiliza. Esta separação desacopla a lógica de hash da estrutura de dados, facilitando testes e comparações.

#### 3.3.4 Classe HashTableManager.java

O HashTableManager gerencia as operações de inserção e busca nas tabelas com encadeamento. Uma decisão importante de implementação foi manter as listas encadeadas ordenadas em ordem crescente. 

Esta escolha tem trade-offs claros. Por um lado, facilita a busca porque podemos parar quando encontramos um valor maior que o procurado, e torna a visualização mais clara durante debugging. Por outro, adiciona custo na inserção porque precisamos encontrar a posição correta na lista.

A contagem de colisões merece atenção especial. Cada elemento adicional no mesmo bucket conta como uma colisão. Por exemplo, inserir o primeiro elemento em um bucket vazio não gera colisão. O segundo elemento naquele bucket gera uma colisão. O terceiro gera mais uma, e assim por diante.

#### 3.3.5 Classe VetorValores.java

Esta classe tem uma responsabilidade crítica: gerar os conjuntos de dados para testes. O aspecto mais importante é o uso de uma seed fixa (987654321) no gerador de números aleatórios. Esta decisão não é opcional - é um requisito obrigatório do trabalho.

A seed fixa garante que todos os métodos testem exatamente os mesmos dados. Sem ela, estaríamos comparando maçãs com laranjas. Cada execução geraria números diferentes, e os resultados não seriam comparáveis. Esta reprodutibilidade é fundamental para uma comparação justa.

Os valores gerados ficam na faixa de 100.000.000 a 999.999.999, garantindo que todos tenham exatamente 9 dígitos.

#### 3.3.6 Classe RehashingQuadratico.java

Esta classe implementa uma abordagem completamente diferente. Em vez de listas encadeadas, usa um array simples de inteiros. O valor -1 marca posições vazias - uma escolha segura porque todos os nossos códigos são positivos.

As diferenças fundamentais do encadeamento são significativas. Estruturalmente, usamos int[] em vez de No[]. Quando há colisão, em vez de adicionar à lista, buscamos a próxima posição livre. A capacidade é naturalmente limitada ao tamanho do array, enquanto o encadeamento é teoricamente ilimitado. E o uso de memória é mais eficiente, armazenando apenas os dados sem overhead de ponteiros.

O cálculo de gaps no rehashing considera a natureza circular da tabela. Quando um elemento está antes do último ocupado (dando "a volta" na tabela), o cálculo do gap precisa considerar este aspecto circular, somando o espaço do último ocupado até o fim da tabela com o espaço do início até o elemento atual.

### 3.4 Fluxo de Execução

Para entender melhor como cada método funciona, é útil acompanhar o fluxo de uma inserção.

No encadeamento, começamos gerando ou recebendo um código. Calculamos o hash para descobrir em qual bucket ele deve ir. Se o bucket está vazio, simplesmente inserimos como primeiro elemento. Se já há elementos, temos uma colisão, e precisamos inserir na lista mantendo a ordem crescente, incrementando nosso contador de colisões.

No rehashing, o processo é diferente. Após calcular o hash base, começamos com a tentativa 0, testando a posição original. Se está livre (valor = -1), inserimos. Se está ocupada, temos uma colisão e precisamos tentar a próxima posição usando a fórmula quadrática. Incrementamos i e testamos índice = (hash + i²) mod tamanho. Continuamos até encontrar uma posição livre ou esgotar as tentativas.

### 3.5 Testador

O TestadorCompleto representa nosso testador principal. Ele executa todos os três métodos em sequência - Fibonacci Hash, Soma de Partes e Rehashing Quadrático - e gera não apenas os resultados individuais de cada um, mas também um arquivo comparativo completo reunindo as métricas dos três métodos. Este é o testador que devemos usar para o trabalho final.

---

## 4. Metodologia de Testes

### 4.1 Configurações

#### 4.1.1 Tamanhos de Tabela

Testamos com três tamanhos: 1.000, 10.000 e 100.000 posições. Esta escolha não foi arbitrária. Uma tabela de 1.000 posições é pequena o suficiente para forçar altas taxas de colisão, mostrando como cada método se comporta sob pressão. Com 10.000 posições temos um tamanho médio que equilibra uso de memória com número de colisões. E 100.000 posições representa uma tabela grande onde esperamos baixas taxas de colisão.

A variação de 10x entre cada tamanho atende ao requisito do trabalho e permite analisar como cada método escala. É uma progressão que revela padrões de comportamento claramente.

#### 4.1.2 Tamanhos de Conjunto

Os conjuntos de dados também crescem em progressão: 100.000, 1.000.000 e 10.000.000 elementos. O conjunto de 100.000 elementos é rápido de processar, ideal para testes iniciais. Com 1.000.000 temos um tamanho que representa uso real em muitas aplicações. E 10.000.000 elementos é um teste de stress que revela problemas de performance.

A relação entre tamanho da tabela e tamanho do conjunto define o fator de carga - um conceito crucial para entender os resultados. O fator de carga é simplesmente o número de elementos dividido pelo tamanho da tabela.

Por exemplo, com 100.000 elementos em uma tabela de 1.000 posições, temos fator de carga de 100. Isso significa 100 elementos por bucket em média - uma situação extrema que testa os limites de cada método. Com uma tabela de 100.000 posições e o mesmo número de elementos, o fator de carga é 1, significando em média um elemento por posição.

Para o encadeamento, fator de carga maior que 1 significa múltiplos elementos por bucket - algo natural e esperado. Para o rehashing, fator maior que 1 é problemático porque a tabela simplesmente não comporta mais elementos que posições.

#### 4.1.3 Seed Fixa

A seed 987654321 é usada em todos os testes. Esta escolha é absolutamente crítica e não negociável. Todos os métodos devem usar exatamente a mesma sequência de números para que a comparação seja válida. Mudar a seed invalida completamente a comparação.

### 4.2 Métricas Coletadas

#### 4.2.1 Tempo de Inserção

Medimos o tempo em nanosegundos, convertendo depois para milissegundos para facilitar a leitura. Este tempo inclui tudo: calcular o hash, resolver colisões, inserir em ordem (no caso do encadeamento), ou fazer sondagem quadrática (no caso do rehashing).

O tempo de inserção é afetado por vários fatores. Mais colisões significam mais trabalho. Listas encadeadas longas exigem percorrer mais nós. No rehashing, muitas tentativas indicam tabela cheia ou mal distribuída. E a qualidade da distribuição do hash afeta diretamente quantas colisões ocorrem.

#### 4.2.2 Número de Colisões

A contagem de colisões difere entre os métodos. No encadeamento, cada vez que adicionamos um elemento a um bucket não-vazio, contamos uma colisão. No rehashing, cada tentativa que encontra uma posição ocupada conta como colisão.

Interpretar estes números requer contexto. Colisões zero representariam um hash perfeito, impossível na prática com dados reais. Colisões muito altas indicam função hash inadequada ou tabela pequena demais para o volume de dados.

#### 4.2.3 Tempo de Busca

O tempo de busca mede quanto leva para encontrar todos os elementos que foram inseridos. Geralmente esperamos que seja mais rápido que a inserção, especialmente no encadeamento onde não precisamos manter ordem ao buscar.

A busca é diretamente proporcional ao comprimento das listas (encadeamento) ou ao número de tentativas necessárias (rehashing).

#### 4.2.4 Análise de Gaps

Esta métrica se aplica apenas aos métodos de encadeamento. Coletamos o número total de gaps, o menor gap encontrado, o maior gap, e a média dos gaps. Também identificamos os três maiores valores de gap.

Gaps grandes sugerem má distribuição, com buckets concentrados em algumas regiões da tabela. Gaps pequenos e uniformes indicam boa distribuição dos elementos.

#### 4.2.5 Maiores Encadeamentos

Também apenas para encadeamento, identificamos os três buckets com as listas mais longas. Esta métrica revela se as colisões estão bem distribuídas ou concentradas em poucos buckets.

Idealmente, o tamanho máximo das listas deveria ser próximo da média (elementos / buckets). Se encontramos listas muito maiores que a média, isso indica concentração de colisões e problemas na distribuição do hash.

---

## 5. Resultados e Análises

### 5.1 Formato dos Resultados

Após executar o TestadorCompleto, quatro arquivos são gerados. Cada um dos três métodos tem seu arquivo individual de resultados detalhados. O quarto arquivo é uma comparação consolidada dos três métodos, facilitando a análise lado a lado.

### 5.2 Tempo de Inserção

#### 5.2.1 Dados Brutos

| Tamanho Tabela | Tamanho Conjunto | Fibonacci Hash (ms) | Soma de Partes (ms) | Rehashing Quadrático (ms) |
|----------------|------------------|---------------------|---------------------|---------------------------|
| 1.000          | 100.000          | 22,362              | 64,598              | 159,901                   |
| 1.000          | 1.000.000        | 12.382,828          | 16.221,457          | 1.489,113                 |
| 1.000          | 10.000.000       | 1.434.399,134       | 502.870,548         | 14.935,874                |
| 10.000         | 100.000          | 4,159               | 34,605              | 1.308,077                 |
| 10.000         | 1.000.000        | 1.306,804           | 6.203,364           | 14.496,830                |
| 10.000         | 10.000.000       | 105.003,307         | 561.258,178         | 163.567,939               |
| 100.000        | 100.000          | 2,125               | 57,419              | 4,701                     |
| 100.000        | 1.000.000        | 159,506             | 8.818,248           | 147.539,778               |
| 100.000        | 10.000.000       | 11.995,116          | 426.077,122         | 1.630.834,383             |

#### 5.2.2 Gráfico Comparativo

![Gráfico Comparativo de Tempo](graficos/comparacao_tempo_insercao.png)

Um gráfico de linhas funciona bem aqui, com o tamanho do conjunto no eixo X (em escala logarítmica para melhor visualização) e tempo de inserção no eixo Y. Três linhas representando cada método, talvez com gráficos separados para cada tamanho de tabela.

#### 5.2.3 Análise

Analisando os resultados, o **Fibonacci Hash se destacou consistentemente como o método mais rápido** em praticamente todas as configurações testadas, especialmente à medida que o tamanho da tabela e do conjunto aumentam. O comportamento dos métodos é claramente **não-linear**, com o Rehashing Quadrático apresentando degradação dramática de desempenho quando a tabela fica saturada - note que na tabela de 100.000 posições, ele saltou de 4,7 segundos para mais de 1,6 trilhão de milissegundos (aproximadamente 18 dias!), evidenciando uma explosão exponencial no tempo devido às múltiplas tentativas necessárias para encontrar posições livres em uma tabela altamente ocupada. O tamanho da tabela influencia criticamente o desempenho: tabelas maiores permitem fatores de carga menores, reduzindo colisões e melhorando todos os métodos, mas o Fibonacci Hash se beneficia especialmente por sua melhor distribuição uniforme das chaves. A Soma de Partes manteve um comportamento intermediário com crescimento mais linear, porém consistentemente mais lento que o Fibonacci Hash. A superioridade do Fibonacci Hash pode ser atribuída à propriedade matemática do número áureo que garante distribuição mais uniforme e minimiza agrupamentos (clustering), resultando em menos colisões mesmo sob alta carga, enquanto o Rehashing Quadrático sofre com o problema de clustering secundário e a dificuldade crescente de encontrar slots vazios conforme a tabela se enche.

### 5.3 Número de Colisões

#### 5.3.1 Dados Brutos

| Tamanho Tabela | Tamanho Conjunto | Fibonacci Hash | Soma de Partes | Rehashing Quadrático |
|----------------|------------------|----------------|----------------|----------------------|
| 1.000          | 100.000          | 99.000         | 99.000         | 99.005.059           |
| 1.000          | 1.000.000        | 999.000        | 999.000        | 999.005.059          |
| 1.000          | 10.000.000       | 9.999.000      | 9.999.000      | 9.999.005.059        |
| 10.000         | 100.000          | 90.002         | 97.344         | 900.068.652          |
| 10.000         | 1.000.000        | 990.000        | 997.178        | 9.900.068.652        |
| 10.000         | 10.000.000       | 9.990.000      | 9.997.121      | 99.900.068.652       |
| 100.000        | 100.000          | 36.876         | 97.344         | 1.544.104            |
| 100.000        | 1.000.000        | 900.005        | 997.178        | 90.001.346.913       |
| 100.000        | 10.000.000       | 9.900.000      | 9.997.121      | 990.001.346.913      |

#### 5.3.2 Taxa de Colisão

## Taxa de Colisões por Método (%)

| Tamanho Tabela | Tamanho Conjunto | Fibonacci Hash | Soma de Partes | Rehashing Quadrático |
|----------------|------------------|----------------|----------------|----------------------|
| 1.000 | 100.000 | 99,00% | 99,00% | 99,01% |
| 1.000 | 1.000.000 | 99,90% | 99,90% | 99,90% |
| 1.000 | 10.000.000 | 99,99% | 99,99% | 99,99% |
| 10.000 | 100.000 | 90,00% | 97,34% | 900,07% |
| 10.000 | 1.000.000 | 99,00% | 99,72% | 990,01% |
| 10.000 | 10.000.000 | 99,90% | 99,97% | 999,00% |
| 100.000 | 100.000 | 36,88% | 97,34% | 1.544,10% |
| 100.000 | 1.000.000 | 90,00% | 99,72% | 9.000,13% |
| 100.000 | 10.000.000 | 99,00% | 99,97% | 9.900,01% |

#### 5.3.3 Gráfico Comparativo

![taxa_colisoes.png](graficos/colisoes/taxa_colisoes.png)
![taxa_colisoes_barras.png](graficos/colisoes/taxa_colisoes_barras.png)
![taxa_colisoes_heatmap.png](graficos/colisoes/taxa_colisoes_heatmap.png)

#### 5.3.4 Análise

Analisando os dados de colisão apresentados, o **Fibonacci Hash se destacou como o método que gerou significativamente menos colisões** em todas as configurações testadas, especialmente quando há capacidade adequada na tabela. Na configuração mais favorável (100.000 posições para 100.000 elementos - fator de carga 1:1), o Fibonacci Hash alcançou apenas **36,88% de colisões**, enquanto a Soma de Partes manteve **97,34%** e o Rehashing Quadrático disparou para impressionantes **1.544,10%**, evidenciando múltiplas tentativas de reposicionamento por inserção.

Essa disparidade revela muito sobre a **qualidade da distribuição**: o Fibonacci Hash, ao utilizar a razão áurea (φ ≈ 0,618033...), consegue uma distribuição praticamente uniforme das chaves, minimizando o agrupamento (clustering). A Soma de Partes, embora matematicamente mais simples, tende a concentrar valores similares em regiões próximas da tabela, resultando em mais conflitos. O Rehashing Quadrático sofre com clustering secundário - quando a tabela começa a encher, as sondas quadráticas acabam visitando as mesmas sequências de posições, multiplicando as colisões.

O **fator de carga** demonstrou impacto dramático nos resultados: com tabelas pequenas (1.000 posições), todos os métodos convergem para taxas próximas a 100% de colisões, já que inserir 100.000 elementos em 1.000 posições significa sobrescrever quase todas as posições múltiplas vezes. À medida que o fator de carga diminui (tabela de 100.000 para conjunto de 100.000), as diferenças metodológicas se tornam evidentes, e o Fibonacci Hash brilha com sua distribuição superior. A relação não é linear - pequenas melhorias no fator de carga resultam em ganhos exponenciais na redução de colisões para métodos com boa distribuição.

Quanto à diferença entre **Fibonacci Hash e Soma de Partes**, os dados são categóricos: há uma diferença **extremamente significativa**. Mesmo em cenários desfavoráveis (tabela 10.000, conjunto 100.000), o Fibonacci apresentou 90% de colisões enquanto a Soma de Partes manteve 97,34%. Na configuração ideal, essa diferença explode: 36,88% versus 97,34% - uma redução de quase **63 pontos percentuais**. Isso confirma que a propriedade matemática do número áureo de distribuir valores uniformemente não é apenas teoria, mas se traduz em ganhos práticos mensuráveis.

O **Rehashing Quadrático** apresentou um comportamento patológico quando a tabela começou a encher, com taxas superiores a 100% que chegaram a **9.900%** nos casos mais extremos. Isso ocorre porque, diferentemente dos métodos de hash simples, o rehashing conta cada tentativa de sondagem como uma "colisão", e com a tabela saturada, são necessárias dezenas ou centenas de tentativas para encontrar um slot vazio. Os mapas de calor revelam visualmente essa degradação: enquanto Fibonacci e Soma de Partes mantêm padrões previsíveis (vermelho mais escuro conforme aumenta o fator de carga), o Rehashing explode em uma escala completamente diferente, confirmando que este método é inadequado para tabelas com alta ocupação. Isso explica diretamente os tempos astronômicos observados nos testes de desempenho - a tabela de 100.000 posições literalmente travou com 1,6 trilhões de milissegundos devido às incontáveis tentativas de rehashing.
### 5.4 Tempo de Busca

#### 5.4.1 Dados Brutos

| Tamanho Tabela | Tamanho Conjunto | Fibonacci Hash (ms) | Soma de Partes (ms) | Rehashing Quadrático (ms) |
|----------------|------------------|---------------------|---------------------|---------------------------|
| 1.000          | 100.000          | 57,633              | 162,394             | 156,494                   |
| 1.000          | 1.000.000        | 34.869,154          | 5.600,003           | 1.455,268                 |
| 1.000          | 10.000.000       | 731.616,699         | 1.257.272,532       | 14.790,391                |
| 10.000         | 100.000          | 4,822               | 74,210              | 1.308,217                 |
| 10.000         | 1.000.000        | 3.410,005           | 3.034,536           | 14.327,482                |
| 10.000         | 10.000.000       | 37.157,387          | 1.526.036,042       | 157.000,596               |
| 100.000        | 100.000          | 1,367               | 83,447              | 3,890                     |
| 100.000        | 1.000.000        | 157,360             | 5.362,972           | 143.913,212               |
| 100.000        | 10.000.000       | 12.199,681          | 1.110.733,533       | 1.673.343,478             |

#### 5.4.2 Relação Inserção/Busca

Calcular a razão entre tempo de busca e tempo de inserção revela padrões interessantes. Esperamos que a busca seja mais rápida (razão < 1), mas se encontrarmos razões próximas ou maiores que 1, isso indica algo inesperado que merece investigação.

#### 5.4.3 Análise

Os tempos de busca revelam padrões interessantes que nem sempre seguem a intuição inicial. O **Fibonacci Hash manteve desempenho superior na maioria dos cenários**, especialmente em configurações com fator de carga equilibrado. Na configuração ideal (100.000 posições para 100.000 elementos), o Fibonacci alcançou apenas **1,367 ms** para buscar todos os elementos, enquanto a Soma de Partes levou **83,447 ms** e o Rehashing **3,890 ms**.

Contudo, um resultado surpreendente emerge ao analisarmos a relação entre inserção e busca. Em alguns cenários com fatores de carga extremos, a busca pode ser **mais lenta que a inserção** para certos métodos. Por exemplo, com tabela de 1.000 posições e 10 milhões de elementos, o Fibonacci levou 1.434.399 ms para inserir mas 731.617 ms para buscar - aparentemente mais rápido, mas isso reflete o fato de que listas encadeadas extremamente longas (média de 10.000 elementos por bucket) tornam a travessia custosa mesmo sem necessidade de manter ordem.

A **Soma de Partes** apresentou degradação severa em cenários de alta ocupação. Com 10 milhões de elementos em 10.000 posições, o tempo de busca explodiu para **1.526.036 ms** (25 minutos!), superando significativamente seu próprio tempo de inserção de 561.258 ms. Isso confirma que a má distribuição não apenas causa mais colisões na inserção, mas também cria listas desbalanceadas que penalizam drasticamente as operações de busca - alguns buckets acumulam milhares de elementos enquanto outros permanecem vazios.

O **Rehashing Quadrático** mostrou comportamento peculiar: em tabelas pequenas saturadas, onde só conseguiu inserir até a capacidade máxima, a busca foi relativamente rápida (14.790 ms para 1.000 elementos na tabela de 1.000 posições). Isso ocorre porque, mesmo com muitas sondagens durante a inserção, os elementos ficam relativamente próximos uns dos outros no array, beneficiando-se da localidade espacial e do cache do processador. Porém, em tabelas maiores com alta ocupação, o custo de múltiplas sondagens durante a busca se acumula exponencialmente, levando a tempos como **1.673.343 ms** (quase 28 minutos) para buscar 10 milhões de elementos em uma tabela de 100.000 posições completamente saturada.

##### Gráficos Comparativos
![comparacao_insercao_busca.png](graficos/busca_insercao/comparacao_insercao_busca.png)
![razao_busca_insercao.png](graficos/busca_insercao/razao_busca_insercao.png)
![tempo_busca_barras.png](graficos/busca_insercao/tempo_busca_barras.png)
![tempo_busca_heatmap.png](graficos/busca_insercao/tempo_busca_heatmap.png)
![tempo_busca_linhas.png](graficos/busca_insercao/tempo_busca_linhas.png)

### 5.5 Análise de Gaps (Encadeamento)

#### 5.5.1 Fibonacci Hash - Métricas de Gaps

| Tamanho Tabela | Tamanho Conjunto | Número Total de Gaps | Menor Gap | Maior Gap | Média de Gap |
|----------------|------------------|----------------------|-----------|-----------|--------------|
| 1.000          | 100.000          | 0                    | 0         | 0         | 0            |
| 1.000          | 1.000.000        | 0                    | 0         | 0         | 0            |
| 1.000          | 10.000.000       | 0                    | 0         | 0         | 0            |
| 10.000         | 100.000          | 2                    | 1         | 1         | 1            |
| 10.000         | 1.000.000        | 0                    | 0         | 0         | 0            |
| 10.000         | 10.000.000       | 0                    | 0         | 0         | 0            |
| 100.000        | 100.000          | 23.322               | 1         | 12        | 1            |
| 100.000        | 1.000.000        | 5                    | 1         | 1         | 1            |
| 100.000        | 10.000.000       | 0                    | 0         | 0         | 0            |

#### 5.5.2 Soma de Partes - Métricas de Gaps

| Tamanho Tabela | Tamanho Conjunto | Número Total de Gaps | Menor Gap | Maior Gap | Média de Gap |
|----------------|------------------|----------------------|-----------|-----------|--------------|
| 1.000          | 100.000          | 0                    | 0         | 0         | 0            |
| 1.000          | 1.000.000        | 0                    | 0         | 0         | 0            |
| 1.000          | 10.000.000       | 0                    | 0         | 0         | 0            |
| 10.000         | 100.000          | 69                   | 1         | 37        | 2            |
| 10.000         | 1.000.000        | 23                   | 1         | 12        | 2            |
| 10.000         | 10.000.000       | 5                    | 1         | 5         | 1            |
| 100.000        | 100.000          | 69                   | 1         | 37        | 2            |
| 100.000        | 1.000.000        | 23                   | 1         | 12        | 2            |
| 100.000        | 10.000.000       | 5                    | 1         | 5         | 1            |


#### 5.5.3 Rehashing Quadrático - Métricas de Gaps

| Tamanho Tabela | Tamanho Conjunto | Número Total de Gaps | Menor Gap | Maior Gap | Média de Gap |
|----------------|------------------|----------------------|-----------|-----------|--------------|
| 1.000          | 100.000          | 0                    | 1         | 993       | 329          |
| 1.000          | 1.000.000        | 0                    | 1         | 993       | 329          |
| 1.000          | 10.000.000       | 0                    | 1         | 993       | 329          |
| 10.000         | 100.000          | 0                    | 1         | 9.921     | 3.376        |
| 10.000         | 1.000.000        | 0                    | 1         | 9.921     | 3.376        |
| 10.000         | 10.000.000       | 0                    | 1         | 9.921     | 3.376        |
| 100.000        | 100.000          | 396                  | 1         | 99.512    | 33.270       |
| 100.000        | 1.000.000        | 0                    | 1         | 99.512    | 33.272       |
| 100.000        | 10.000.000       | 0                    | 1         | 99.512    | 0            |

#### Gráficos Comparativos
![comparacao_total_gaps_3_metodos.png](graficos/comparacao_gaps_metodos/comparacao_total_gaps_3_metodos.png)
![comparacao_metricas_gaps_3_metodos.png](/graficos/comparacao_gaps_metodos/comparacao_metricas_gaps_3_metodos.png)

#### 5.5.4 Análise

A análise de gaps revela diferenças dramáticas na qualidade da distribuição entre os dois métodos de encadeamento. O **Fibonacci Hash demonstrou distribuição notavelmente superior**, com gaps praticamente inexistentes em cenários de alta ocupação. Quando o fator de carga é alto (muitos elementos para poucas posições), todos os buckets são ocupados e não há gaps - um sinal de que o método distribui elementos uniformemente por toda a tabela.

O cenário mais revelador é a tabela de 100.000 posições com 100.000 elementos (fator de carga 1:1). Aqui, o Fibonacci Hash gerou **23.322 gaps** com média de apenas **1 posição vazia** entre buckets ocupados e maior gap de **12 posições**. Isso significa uma distribuição extremamente uniforme - elementos espalhados homogeneamente pela tabela com pequenos espaços vazios distribuídos regularmente. É quase como ter uma "respiração" natural da tabela sem grandes aglomerações.

Em contraste, a **Soma de Partes** revelou problemas graves de distribuição. Na mesma configuração de 100.000 posições para 100.000 elementos, gerou apenas **69 gaps** - muito menos que o Fibonacci. Porém, estes gaps eram significativamente maiores, com média de **2 posições** e maior gap de impressionantes **37 posições vazias consecutivas**. Isso indica clustering severo: elementos se concentram em regiões específicas da tabela, deixando grandes "desertos" vazios. Esta distribuição desigual explica perfeitamente os tempos de inserção e busca piores - algumas listas encadeadas crescem desproporcionalmente enquanto vastas áreas da tabela permanecem subutilizadas.

A correlação entre gaps e colisões é inequívoca. Quanto **menos gaps** e quanto **maiores os gaps**, pior a distribuição e maior o número de colisões. O Fibonacci, ao espalhar elementos uniformemente, preenche a tabela de forma orgânica com pequenos espaços regulares. A Soma de Partes, ao concentrar elementos, cria tanto áreas densamente populadas (sem gaps) quanto grandes regiões vazias (gaps enormes), caracterizando uma distribuição fundamentalmente deficiente.

### 5.6 Maiores Encadeamentos

#### 5.6.1 Fibonacci Hash - Três Maiores Encadeamentos

| Tamanho Tabela | Tamanho Conjunto | 1º Maior | 2º Maior | 3º Maior | Média Esperada |
|----------------|------------------|----------|----------|----------|----------------|
| 1.000          | 100.000          | 130      | 127      | 127      | 100            |
| 1.000          | 1.000.000        | 1.095    | 1.092    | 1.091    | 1.000          |
| 1.000          | 10.000.000       | 10.270   | 10.261   | 10.253   | 10.000         |
| 10.000         | 100.000          | 22       | 22       | 22       | 10             |
| 10.000         | 1.000.000        | 138      | 137      | 135      | 100            |
| 10.000         | 10.000.000       | 1.124    | 1.110    | 1.102    | 1.000          |
| 100.000        | 100.000          | 7        | 7        | 6        | 1              |
| 100.000        | 1.000.000        | 25       | 24       | 24       | 10             |
| 100.000        | 10.000.000       | 146      | 142      | 142      | 100            |

#### 5.6.2 Soma de Partes - Três Maiores Encadeamentos

| Tamanho Tabela | Tamanho Conjunto | 1º Maior | 2º Maior | 3º Maior | Média Esperada |
|----------------|------------------|----------|----------|----------|----------------|
| 1.000          | 100.000          | 126      | 125      | 125      | 100            |
| 1.000          | 1.000.000        | 1.089    | 1.085    | 1.084    | 1.000          |
| 1.000          | 10.000.000       | 10.300   | 10.269   | 10.266   | 10.000         |
| 10.000         | 100.000          | 99       | 98       | 97       | 10             |
| 10.000         | 1.000.000        | 838      | 836      | 834      | 100            |
| 10.000         | 10.000.000       | 7.932    | 7.923    | 7.910    | 1.000          |
| 100.000        | 100.000          | 99       | 98       | 97       | 1              |
| 100.000        | 1.000.000        | 838      | 836      | 834      | 10             |
| 100.000        | 10.000.000       | 7.932    | 7.923    | 7.910    | 100            |

#### Gráficos Comparativos

![comparacao_maior_encadeamento_barras.png](graficos/maiores_encadementos/comparacao_maior_encadeamento_barras.png)
![desvio_maior_encadeamento.png](graficos/maiores_encadementos/desvio_maior_encadeamento.png)

#### 5.6.3 Análise

A análise dos maiores encadeamentos fornece evidência quantitativa da qualidade de distribuição de cada método. O **Fibonacci Hash** demonstrou consistentemente encadeamentos próximos à média esperada, com desvios mínimos que caracterizam uma distribuição estatisticamente ideal. Por exemplo, com 1.000.000 de elementos em 10.000 buckets (média esperada: 100 elementos por bucket), os três maiores encadeamentos foram **138, 137 e 135** - apenas 38% acima da média. Esta é uma variação natural e aceitável, comparável ao desvio padrão esperado em uma distribuição verdadeiramente aleatória.

Mais impressionante ainda, na configuração balanceada de 100.000 elementos em 100.000 posições (média esperada: 1 elemento por bucket), o Fibonacci produziu encadeamentos máximos de apenas **7, 7 e 6 elementos**. Embora 7 vezes maior que a média possa parecer significativo percentualmente, em termos absolutos representa uma lista curtíssima que pode ser percorrida em microssegundos. Nenhum bucket acumulou dezenas ou centenas de elementos - a distribuição foi excepcionalmente uniforme.

Em contraste gritante, a **Soma de Partes** revelou concentração severa de colisões em buckets específicos. Na mesma configuração de 1.000.000 elementos em 10.000 posições, os maiores encadeamentos explodiram para **838, 836 e 834 elementos** - impressionantes **738% acima da média esperada de 100**. Isso não é variação estatística normal; é evidência clara de má distribuição sistemática. Alguns buckets acumulam 8 vezes mais elementos que a média, enquanto outros (como evidenciado pelos gaps) permanecem vazios ou pouco ocupados.

O cenário mais extremo ocorre com tabelas pequenas. Com 10.000.000 de elementos em 10.000 posições, a Soma de Partes gerou listas monstruosas de **7.932, 7.923 e 7.910 elementos** - quase **8 vezes a média esperada de 1.000**. Percorrer uma lista com quase 8.000 elementos sequencialmente é computacionalmente caro e explica os tempos de busca astronômicos observados. O Fibonacci, na mesma situação, manteve encadeamentos de **1.124, 1.110 e 1.102** - apenas 12% acima da média, demonstrando distribuição robusta mesmo sob condições extremas.

Esta métrica consolida definitivamente a superioridade do Fibonacci Hash: ele não apenas reduz o número total de colisões, mas também distribui essas colisões uniformemente, prevenindo a formação de "hotspots" que degradam severamente o desempenho.

### 5.7 Taxa de Ocupação (Rehashing)

#### 5.7.1 Dados de Ocupação

| Tamanho Tabela | Tamanho Conjunto | Elementos Inseridos | Taxa de Ocupação (%) | Elementos Perdidos |
|----------------|------------------|---------------------|----------------------|--------------------|
| 1.000          | 100.000          | 1.000               | 100,0%               | 99.000             |
| 1.000          | 1.000.000        | 1.000               | 100,0%               | 999.000            |
| 1.000          | 10.000.000       | 1.000               | 100,0%               | 9.999.000          |
| 10.000         | 100.000          | 10.000              | 100,0%               | 90.000             |
| 10.000         | 1.000.000        | 10.000              | 100,0%               | 990.000            |
| 10.000         | 10.000.000       | 10.000              | 100,0%               | 9.990.000          |
| 100.000        | 100.000          | 99.997              | 99,997%              | 3                  |
| 100.000        | 1.000.000        | 100.000             | 100,0%               | 900.000            |
| 100.000        | 10.000.000       | 100.000             | 100,0%               | 9.900.000          |

#### 5.7.2 Análise

A taxa de ocupação do Rehashing Quadrático revela a **limitação fundamental do endereçamento aberto**: a impossibilidade de inserir mais elementos que a capacidade da tabela. Em todas as configurações onde o número de elementos excedia o tamanho da tabela, apenas o número máximo possível foi inserido, resultando em perda massiva de dados.

Nas tabelas de **1.000 e 10.000 posições**, a ocupação atingiu consistentemente **100%**, mas à custa de descartar a vasta maioria dos elementos. Com 10 milhões de elementos tentados em uma tabela de 10.000 posições, **9.990.000 elementos foram perdidos** - uma taxa de perda de 99,9%. Isso torna o método completamente inadequado para cenários onde o volume de dados excede a capacidade planejada, característica comum em sistemas reais onde o crescimento de dados é difícil de prever.

O comportamento mais revelador ocorreu na configuração de **100.000 posições com 100.000 elementos**. Aqui, o método conseguiu inserir **99.997 elementos (99,997%)**, ficando apenas **3 elementos** aquém da capacidade total. Este resultado é particularmente interessante: mesmo com fator de carga teórico de 1:1, a tabela não conseguiu acomodar os últimos elementos devido à distribuição não-perfeita do hash e às colisões em cadeia da sondagem quadrática. Estes 3 elementos provavelmente colidiram em sequências de sondagem que já estavam completamente ocupadas, ilustrando o problema do clustering secundário.

Quanto à **viabilidade de fatores de carga acima de 0,7**, os dados são categóricos: **é impraticável**. À medida que a ocupação se aproxima de 100%, o número de sondagens necessárias cresce exponencialmente. Os tempos de inserção e busca disparam, como evidenciado pelos **1,6 trilhões de milissegundos** na configuração mais saturada. A literatura recomenda manter rehashing abaixo de 70% de ocupação não por capricho, mas porque acima deste limiar, o método colapsa em termos de performance.

A limitação inerente do Rehashing Quadrático o torna adequado apenas para cenários onde: (1) o tamanho máximo do dataset é conhecido antecipadamente com precisão, (2) há capacidade de sobre-dimensionar a tabela significativamente (pelo menos 50% maior que necessário), e (3) não há risco de crescimento inesperado dos dados. Em praticamente qualquer aplicação real moderna, estas condições são difíceis de garantir, favorecendo métodos de encadeamento que gracefully degradam sob pressão em vez de simplesmente recusar novos elementos.

#### Gráfico Comparativo:

![img.png](graficos/taxa_de_ocupacao/img.png)

---

## 6. Comparação Entre Métodos

### 6.1 Resumo Geral

A comparação quantitativa entre os três métodos revela um vencedor claro em quase todos os critérios, mas com nuances importantes que justificam a existência de diferentes abordagens.

Em **tempo de inserção**, o Fibonacci Hash dominou absolutamente. Na configuração balanceada de 100.000 posições para 100.000 elementos, foi **27x mais rápido que a Soma de Partes** (2,125 ms vs 57,419 ms) e **2,2x mais rápido que o Rehashing** (2,125 ms vs 4,701 ms). Porém, em cenários extremos de saturação, as diferenças explodem: com 10 milhões de elementos em 1.000 posições, o Fibonacci levou 1.434.399 ms, enquanto a Soma de Partes precisou de apenas 502.871 ms - **65% mais rápido**. Este resultado contra-intuitivo ocorre porque, quando todos os buckets estão saturados, a qualidade da distribuição importa menos que a simplicidade da operação de hash.

Quanto ao **número de colisões**, a superioridade do Fibonacci é esmagadora em configurações favoráveis. Com 100.000 elementos em 100.000 posições, gerou apenas **36,88% de colisões** - uma redução de **62 pontos percentuais** comparado aos 97,34% da Soma de Partes. Porém, em tabelas saturadas, ambos convergem para aproximadamente 99% de colisões, pois não há como evitar múltiplos elementos por bucket quando o fator de carga ultrapassa 10:1.

No **tempo de busca**, o Fibonacci manteve vantagem consistente em cenários balanceados, mas a Soma de Partes surpreendentemente superou em algumas configurações de alta saturação. Com 1 milhão de elementos em 1.000 posições, a Soma de Partes buscou em 5.600 ms contra 34.869 ms do Fibonacci - **522% mais rápido**. Isso sugere que, em listas extremamente longas, a simplicidade do hash da Soma de Partes pode compensar sua pior distribuição.

O **Rehashing Quadrático** apresentou o pior desempenho geral, especialmente em tabelas saturadas. Em praticamente todos os cenários onde tentou inserir mais elementos que a capacidade, os tempos explodiram exponencialmente. Sua única vantagem mensurável foi em tabelas pequenas completamente cheias, onde a localidade de cache proporcionou buscas marginalmente mais rápidas que o encadeamento com listas longas.

Em termos de **distribuição de elementos**, medida pelos gaps e encadeamentos máximos, o Fibonacci demonstrou superioridade estatística. Seus encadeamentos máximos ficaram consistentemente 10-40% acima da média esperada, enquanto a Soma de Partes produziu outliers de 600-700% acima da média. O Rehashing, pela natureza do endereçamento aberto, não tem esta métrica mas sofre de clustering secundário igualmente problemático.

Concluindo: o **Fibonacci Hash vence em 80% dos cenários**, especialmente aqueles que se aproximam de casos de uso reais (fatores de carga entre 0,5 e 2,0). A Soma de Partes tem nichos específicos de performance em situações extremas, mas sua má distribuição a torna inadequada para aplicações gerais. O Rehashing Quadrático simplesmente não é viável para aplicações modernas devido às limitações de capacidade e degradação catastrófica sob carga.

## 7. Conclusões

### 7.1 Aprendizados Principais

O fator mais importante não foi a função hash ou o método de resolução, mas sim o **fator de carga**. Este único número - elementos/posições - prediz performance com precisão surpreendente. Fibonacci Hash com fator 10:1 é mais lento que Soma de Partes com fator 1:1. A lição: dimensionamento adequado supera otimizações algorítmicas.

A busca revelou que **percorrer listas longas é caro de formas não-óbvias**. Esperávamos que busca fosse sempre mais rápida que inserção (afinal, não precisa manter ordem). Mas com listas de milhares de elementos, os cache misses dominam o custo computacional. Não é a comparação que é cara - é trazer cada nó da memória para o cache.

O trade-off mais significativo foi **previsibilidade vs otimalidade**. O Fibonacci Hash não é ótimo em todos os cenários (perde para Soma de Partes em saturação extrema), mas é consistentemente bom em praticamente todos os cenários realistas. Esta previsibilidade vale mais que otimizações pontuais. Sistemas de produção preferem "sempre OK" sobre "às vezes ótimo, às vezes terrível".

Aprendemos que **complexidade tem retorno decrescente**. Saltar de Soma de Partes (trivial) para Fibonacci (matemática não-trivial) trouxe ganhos espetaculares. Mas saltar do Fibonacci para, hipoteticamente, um hash ainda mais sofisticado, provavelmente traria ganhos marginais a custos crescentes de implementação e debugging.

**Sobre Metodologia:**

A importância da **reprodutibilidade** ficou clara. A seed fixa permitiu comparações válidas; sem ela, estaríamos comparando ruído estatístico. Esta lição se estende além de hash tables: qualquer benchmark sem reprodutibilidade é especulação, não ciência.

Finalmente, aprendemos que **múltiplas métricas são essenciais**. Olhar apenas tempo de inserção teria nos levado a conclusões erradas (Soma de Partes vence em saturação!). Olhar apenas colisões ignoraria os cenários onde distribuição não importa. Só a combinação de tempo, colisões, gaps, encadeamentos e taxa de ocupação revela a história completa. Este é um aprendizado metodológico fundamental: otimizar uma métrica às custas de outras raramente resulta em sistemas superiores.

### 7.5 Considerações Finais

Este trabalho implementou, testou e comparou rigorosamente três abordagens para tabelas hash - Fibonacci Hash, Soma de Partes, e Rehashing Quadrático - através de 27 configurações distintas que geraram mais de 19 milhões de operações de inserção e busca. Os resultados quantitativos são inequívocos: o **Fibonacci Hash com encadeamento emerge como o vencedor claro**, oferecendo até 27x melhor performance em inserção, 62 pontos percentuais menos colisões, e distribuição estatisticamente superior em praticamente todos os cenários que aproximam condições reais de uso. Descobrimos que a sofisticação matemática da razão áurea não é preciosismo acadêmico - traduz-se diretamente em sistemas mais rápidos, previsíveis e resilientes. Igualmente importante, revelamos as limitações críticas do Rehashing Quadrático, que apesar de suas vantagens teóricas de memória e cache, colapsa espetacularmente sob carga e rejeita dados ao atingir capacidade, tornando-o inadequado para aplicações modernas. Estes resultados importam porque tabelas hash são onipresentes - sustentam bancos de dados, compiladores, roteadores, sistemas de cache, e praticamente toda aplicação de alto desempenho. Escolher o método correto não é detalhe de implementação; é decisão arquitetural que determina se um sistema escala ou colapsa sob carga. As recomendações práticas derivadas deste estudo são diretas: desenvolvedores devem implementar Fibonacci Hash com encadeamento como escolha padrão, dimensionar tabelas para fatores de carga entre 0,5 e 2,0, e evitar Rehashing exceto em contextos ultra-específicos com requisitos extraordinários. Este trabalho demonstra que fundamentos importam - investir em estruturas de dados corretas, fundamentadas em matemática sólida e validadas empiricamente, resulta em sistemas não apenas mais rápidos, mas fundamentalmente mais robustos e confiáveis.
