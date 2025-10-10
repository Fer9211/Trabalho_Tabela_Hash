public class HashNo {
    private No[] tabelaHash;
    private int tamanho;
    private int colisoes;

    public HashNo(int tamanho) {
        this.tamanho = tamanho;
        this.colisoes = 0;
        this.tabelaHash = new No[tamanho];
    }

    public void adicionar_valor_tabela(int valor){
        int id = conta_funcao_hash(valor, tamanho);
        No novo_valor = new No(id, valor);

        if(tabelaHash[id] == null){
            tabelaHash[id] = novo_valor;
        }
        else{
            No atual = tabelaHash[id];
            while(atual.getProximo() != null){
                atual = atual.getProximo();
            }
            atual.setProximo(novo_valor);
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

    public void imprimir_tabela(){
        for(int i = 0; i < tamanho; i++){
            No valor = tabelaHash[i];
            if(valor == null){
                System.out.println("Slot " + i + " vazio");
            }
            else{
                No atual = valor;
                while (atual != null){
                    System.out.println("Id = " + atual.getId() + "| Valor = " + atual.getValor());
                    atual = atual.getProximo();
                }
            }
        }
    }
    public static void main(String[] args) {
        int[] valores = {128945376, 903176452, 572684913, 416839725, 739520186, 285617439, 654392871, 871236549, 398761254, 520948367};

        HashNo tabela_10 = new HashNo(9);
        for(int i = 0; i < 9; i++){  // usa 0 até 8 para não estourar o array
            tabela_10.adicionar_valor_tabela(valores[i]);
        }

        tabela_10.imprimir_tabela();  // opcional: para ver o resultado
    }
}
