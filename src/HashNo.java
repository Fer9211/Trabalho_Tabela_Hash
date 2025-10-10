public class HashNo {
    private No[] tabelaHash;
    private int tamanho;
    private int colisoes;

    public HashNo(tamanho) {
        this.tamanho = tamanho;
        this.colisoes = 0;
        this.tabelaHash = new tabelaHash[tamanho];
    }

    public adicionar_valor_tabela(int valor){
        int id = conta_funcao_hash(valor);
        No novo_valor = new No(id, valor);

        if(tabelaHash[id] == null){
            tabelaHash[id] = novo_valor;
        }
        else{
            No primeiro = tabelaHash[id];

        }
    }
    //Formula da funcao = h(k) = m([k . A] mod 1)
    //Essa é a conta usada para encontrar o incie
    //A é uma constante, use a constante de Fibonacci hashing
    public int conta_funcao_hash(int valor, int tamanho){
        double a = (Math.sqrt(5) - 1) / 2;
        double resultado = valor * a;
        double parteFracionaria = resultado - Math.floor(resultado);
        return (int) (tamanho * parteFracionaria);
    }
}