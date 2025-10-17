/**
 * Interface que define o contrato para funções hash.
 * Cada implementação deve fornecer seu próprio método de cálculo.
 */
public interface HashFunction {
    /**
     * Calcula o índice (bucket) para um dado valor.
     *
     * @param valor O valor a ser hashado
     * @param tamanho O tamanho da tabela hash
     * @return O índice calculado (entre 0 e tamanho-1)
     */
    int calcularHash(int valor, int tamanho);

    /**
     * Retorna o nome da função hash para identificação nos resultados.
     *
     * @return Nome descritivo da função hash
     */
    String getNome();
}

