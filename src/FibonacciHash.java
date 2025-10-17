/**
 * Implementação da função hash usando Fibonacci Hashing.
 * Formula: h(k) = m * ([k * A] mod 1)
 * onde A é a constante de ouro (golden ratio): (√5 - 1) / 2
 */
public class FibonacciHash implements HashFunction {

    @Override
    public int calcularHash(int valor, int tamanho) {
        double a = (Math.sqrt(5) - 1) / 2; // Constante de ouro
        double resultado = valor * a;
        double parteFracionaria = resultado - Math.floor(resultado);
        return (int) (tamanho * parteFracionaria);
    }

    @Override
    public String getNome() {
        return "Fibonacci Hash (Golden Ratio)";
    }
}
