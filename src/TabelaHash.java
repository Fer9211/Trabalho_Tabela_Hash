public class TabelaHash {
    private No[] chave;
    private No[] registro;
    private int tamanho;

    public TabelaHash(int tamanho) {
        this.tamanho = tamanho;
        this.chave = new No[tamanho];
        this.registro = new No[tamanho];
    }

    public void imprimir_tabela_hash() {
        for (int i = 0; i < tamanho; i++) {
            System.out.print("Índice -> " + i + " | ");

            if (registro[i] == null) {
                System.out.println("Vazio");
            } else {
                No atual = registro[i];
                System.out.print("Chave = " + chave[i].getValor() + " | Registro = ");

                // Percorre a lista encadeada neste bucket
                while (atual != null) {
                    System.out.print(atual.getValor());
                    atual = atual.getProximo();
                    if (atual != null) {
                        System.out.print(" -> ");
                    }
                }
                System.out.println(); // pula linha após cada bucket
            }
        }
    }

    //Verifica se o local da insersão está vazio
    public boolean verificar_se_vazio(int bucket){
        if(chave[bucket] == null){
            return true;
        }
        return false;
    }

    //Retorna o tamanho da tabela hash
    public int getTamanho(){
        return tamanho;
    }

    //retorna o array registro
    public No[] getRegistro(){
        return registro;
    }

    //Retorna o array chave
    public No[] getChave(){
        return chave;
    }

    //Atualiza o array chave por inteiro
    public void setChave(No[] chave){
        this.chave = chave;
    }

    //Atualiza o array registro por inteiro
    public void setRegistro(No[] registro){
        this.registro = registro;
    }
}
