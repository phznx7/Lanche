package com.example.demo;

import com.example.demo.applications.LancheApplication;
import com.example.demo.entities.Lanche;
import com.example.demo.facade.LancheFacade;
import com.example.demo.repositories.LancheRepository;
import com.example.demo.services.LancheService;

import java.util.List;
import java.util.Scanner;  // Importação de Scanner para a leitura de entrada.

public class DemoApplication {
    private static LancheRepository lancheRepository;
    private static LancheService lancheService;
    private static LancheApplication lancheApplication;
    private static LancheFacade lancheFacade;

    private static void injetarDependencias() {
        lancheRepository = new LancheRepository();
        lancheService = new LancheService();
        lancheApplication = new LancheApplication(lancheService, lancheRepository);
        lancheFacade = new LancheFacade(lancheApplication);
    }

    public static void main(String[] args) {
        injetarDependencias();

        Scanner scanner = new Scanner(System.in);  // Usando Scanner para capturar as entradas.

        int codigo = 1;
        boolean quit = false;

        while (!quit) {
            System.out.println("""
                    1 - Cadastrar lanche
                    2 - Listar lanches
                    3 - Comprar lanche
                    4 - Excluir lanche
                    5 - Atualizar lanche
                    6 - Sair""");
            int op = Integer.parseInt(scanner.nextLine());  // Usando scanner para ler a entrada.

            switch (op) {
                case 1 -> {
                    System.out.println("Nome do lanche:");
                    String nome = scanner.nextLine();  // Lendo nome do lanche.
                    System.out.println("Preço:");
                    double preco = Double.parseDouble(scanner.nextLine());  // Lendo preço do lanche.
                    System.out.println("URL da imagem:");
                    String img_url = scanner.nextLine();  // Lendo URL da imagem.

                    lancheFacade.cadastrar(new Lanche(codigo++, nome, preco, img_url));
                    System.out.println("Lanche cadastrado com sucesso\n");
                }
                case 2 -> {
                    System.out.println("--------------------------------------\n CÓDIGO\tNOME\t\tPRECO\n--------------------------------------");
                    List<Lanche> lanches = lancheFacade.buscar();
                    for (Lanche l : lanches) {
                        System.out.println(l.getCodigo() + "\t\t" + l.getNome() + "\t\t" + l.getPreco() + "\n");
                    }
                }
                case 3 -> {
                    System.out.println("Digite o código do lanche:");
                    int cod = Integer.parseInt(scanner.nextLine());  // Lendo código do lanche.
                    System.out.println("Digite a quantidade:");
                    int qtd = Integer.parseInt(scanner.nextLine());  // Lendo quantidade.
                    Lanche lanche = lancheFacade.buscarPorCodigo(cod);

                    if (lanche != null) {
                        String total = String.format("%.2f", lancheFacade.calcularLanche(lanche, qtd));
                        System.out.println("Total: R$ " + total);
                    } else {
                        System.out.println("Lanche não encontrado!");
                    }
                }
                case 4 -> {
                    System.out.println("Digite o código do lanche para excluir:");
                    int cod = Integer.parseInt(scanner.nextLine());  // Lendo código do lanche para excluir.
                    boolean sucesso = lancheFacade.excluir(cod);
                    if (sucesso) {
                        System.out.println("Lanche excluído com sucesso!");
                    } else {
                        System.out.println("Erro ao excluir lanche.");
                    }
                }
                case 5 -> {
                    System.out.println("Digite o código do lanche para atualizar:");
                    int cod = Integer.parseInt(scanner.nextLine());  // Lendo código do lanche para atualizar.

                    // Verificar se o lanche existe
                    Lanche lanche = lancheFacade.buscarPorCodigo(cod);
                    if (lanche != null) {
                        System.out.println("Novo nome do lanche:");
                        String nome = scanner.nextLine();  // Lendo novo nome.
                        System.out.println("Novo preço do lanche:");
                        double preco = Double.parseDouble(scanner.nextLine());  // Lendo novo preço.
                        System.out.println("Nova URL da imagem:");
                        String img_url = scanner.nextLine();  // Lendo nova URL da imagem.

                        // Atualizar o lanche com os novos dados
                        lancheFacade.atualizar(cod, new Lanche(cod, nome, preco, img_url));
                        System.out.println("Lanche atualizado com sucesso!");
                    } else {
                        System.out.println("Lanche não encontrado!");
                    }
                }
                case 6 -> quit = true;
            }
        }
        scanner.close();  // Fechar o scanner após o uso.
    }
}
