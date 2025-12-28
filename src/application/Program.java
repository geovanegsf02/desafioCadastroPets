package application;

import entities.Pet;
import enums.GeneroPet;
import enums.TipoPet;
import utils.ValidaPet;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.Locale;
import java.util.Scanner;

public class Program {
    public static void main(String[] args) {
        Locale.setDefault(Locale.US);
        Scanner sc = new Scanner(System.in);
        Pet pet = null;
        File file = new File("formulary.txt");
        File file2 = null;
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmm");
        String[] obj = new String[7];
        int opcao = 0;

        try {
            do {
                String menu = "1. Cadastrar novo pet\n" +
                        "2. Alterar os dados do pet cadastrado\n" +
                        "3. Deletar um pet cadastrado\n" +
                        "4. Listar todos os pets cadastrados\n" +
                        "5. Listar pets por algum critério (idade, nome, raça)\n" +
                        "6. Sair\n";
                System.out.print(menu);

                System.out.print("Insira uma opção: ");
                opcao = sc.nextInt();
                System.out.print("");
            }
            while (opcao < 1 || opcao > 6);
        } catch (InputMismatchException e) {
            System.out.println("Você deve digitar apenas números: " + e.getMessage());
        }

        sc.nextLine();

        switch (opcao) {
            case 1:
                try (FileReader fr = new FileReader(file); BufferedReader br = new BufferedReader(fr)) {
                    String line;
                    int i = 0;
                    while ((line = br.readLine()) != null) {
                        System.out.print(line + " ");

                        if (line.startsWith("4 -")) {
                            System.out.print("\nI - Número da casa: ");
                            String numero = sc.nextLine();
                            System.out.print("II - Cidade: ");
                            String cidade = sc.nextLine();
                            System.out.print("III - Rua: ");
                            String rua = sc.nextLine();
                            String endereco = rua + "," + numero + "," + cidade;
                            obj[i] = endereco;
                        } else {
                            String valor = sc.nextLine();
                            obj[i] = valor;
                        }
                        i++;

                    }
                } catch (IOException e) {
                    System.out.print("Erro: " + e.getMessage());
                }

                try {
                    File folder = new File("petsCadastrados");
                    folder.mkdir();
                    pet = criarPet(obj);
                    file2 = new File(folder,LocalDateTime.now().format(df) + "-" + pet.getNome().toUpperCase().replaceAll(" ", "") + ".TXT");
                    try (FileWriter fw = new FileWriter(file2); BufferedWriter bw = new BufferedWriter(fw)) {
                        salvarArquivo(pet, bw);
                    } catch (IOException e) {
                        System.out.println("Erro! " + e.getMessage());
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                }


                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
            case 6:
                break;
            default:
                System.out.println("Você digitou uma opção inválida, tente novamente.");
        }

    }

    public static Pet criarPet(String[] dados) {
        ValidaPet.validarPet(dados);
        return new Pet(dados[0], // nome
                TipoPet.valueOf(dados[1]), //TipoPet
                GeneroPet.valueOf(dados[2]), // GeneroPet
                dados[3], // endereço
                dados[4], // idade
                dados[5], // peso
                dados[6]); // raça
    }

    public static void salvarArquivo(Pet pet, BufferedWriter bw) throws IOException {
        bw.write(1 + " - " + pet.getNome());
        bw.newLine();
        bw.write(2 + " - " + pet.getTipo());
        bw.newLine();
        bw.write(3 + " - " + pet.getGenero());
        bw.newLine();
        bw.write(4 + " - " + pet.getEndereco());
        bw.newLine();
        bw.write(5 + " - " + pet.getIdade() + " anos");
        bw.newLine();
        bw.write(6 + " - " + pet.getPeso() + "kg");
        bw.newLine();
        bw.write(7 + " - " + pet.getRaca());
        bw.flush();
    }
}
