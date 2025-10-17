public class FibonacciHash implements HashFunction {

    @Override
    public int calcularHash(int valor, int tamanho) {
        double a = (Math.sqrt(5) - 1) / 2;
        double resultado = valor * a;
        double parteFracionaria = resultado - Math.floor(resultado);
        return (int) (tamanho * parteFracionaria);
    }

    @Override
    public String getNome() {
        return "Fibonacci Hash (Golden Ratio)";
    }
}