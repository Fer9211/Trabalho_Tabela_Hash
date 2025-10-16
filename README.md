
# 📂 Projeto de Tabelas Hash: PJBL

4° U Noite
- Eduardo Rodrigues
- Emily Fontana
- Fernanda Moraes

## ⚠️⚠️ Instruções para o código!!
**Estruturas permitidas:**
- Vetores (`int[]`, `float[]`, `boolean[]`)
- Estruturas de nó (para listas encadeadas)
- Tipos primitivos (`int`, `float`, `boolean`)
- `String`
- Matrizes (`[][]`) — **não é permitido usar funções prontas de manipulação**
- `Random` com seed

**Bibliotecas permitidas:**
- Leitura de dados (`Scanner`, `BufferedReader`)
- Escrita de dados (`PrintWriter`, `FileWriter`)
- Medição de tempo (ms, µs ou ns)

---

## 1. Descrição do Projeto

Este projeto contempla a resolução do PJBL Hash - Resolução de Problemas Estrruturados em Computação

- Funções

1. **Encadeamento**  
   Utiliza listas encadeadas para armazenar múltiplos valores em um mesmo índice da tabela.

2. **Rehashing Quadrático (Quadratic Probing)**  
   Utiliza sondagem quadrática para encontrar uma nova posição livre na tabela, evitando o uso de listas.

O objetivo é analisar desempenho, colisões, gaps e encadeamentos de cada técnica em diferentes tamanhos de tabelas e conjuntos de dados.

---

#### 2. Estrutura das Classes

- **No**: representa um nó da lista encadeada com valor e referência ao próximo.  
- **Registro**: mantém arrays `registro` e `chave` para cada bucket, métodos para inserção e análise de gaps/encadeamentos.  
- **VetorValores**: gera conjuntos de números aleatórios de 9 dígitos, com seed fixa para reproducibilidade.  
- **Encadeamento**: realiza inserções, buscas, cálculo de colisões e medição de tempo.
- **RehashingQuadrático**: realiza inserções, buscas, cálculo de colisões e medição de tempo. - serve como compartivo em relação as estatísticas geradas



## 3. Tamanhos dos Conjuntos de Teste

- **Tamanhos das tabelas hash:** 1.000 | 10.000 | 100.000  
- **Tamanhos dos conjuntos de dados:** 100.000 | 1.000.000 | 10.000.000  

Todos os vetores de dados são gerados aleatoriamente com **seed fixa** para garantir resultados consistentes.

---

## 4. Resultados

Os resultados foram salvos nos arquivos:

- `resultados_encadeamento.txt`  
- `resultados_rehashing.txt`

A partir dos resultados obtidos, geramos gráficos com os dados adquiridos




## 5. Comparativo Final 📊


## 6. Como Executar

1.entre no arquivo src

```bash
cd src
```

2. Compile todas as classes:

```bash
javac *.java

```
3. Execute as classes.

```bash
java Main

```




