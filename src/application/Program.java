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
                String menu = """
                        1. Cadastrar um novo pet
                        2. Alterar os dados de um pet cadastrado
                        3. Deletar um pet cadastrado
                        4. Listar todos os pets cadastrados
                        5. Listar pets cadastrados por critério
                        6. Sair
                        """;
                System.out.print(menu);

                System.out.print("Insira uma opção: ");
                opcao = sc.nextInt();
                System.out.print("");
            } while (opcao < 1 || opcao > 6);
        } catch (InputMismatchException e) {
            System.out.println("Você deve digitar apenas números: " + e.getMessage());
        }

        sc.nextLine();

        switch (opcao) {
            case 1: // Cadastrar pet
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
                    file2 = new File(folder, LocalDateTime.now().format(df) + "-" + pet.getNome().toUpperCase().replaceAll(" ", "") + ".TXT");
                    try (FileWriter fw = new FileWriter(file2); BufferedWriter bw = new BufferedWriter(fw)) {
                        salvarArquivo(pet, bw);
                    } catch (IOException e) {
                        System.out.println("Erro! " + e.getMessage());
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                }
                break;
            case 2: {
                alterarPet(sc, df);
                break;
            }
            case 3: {
                int id = 0;
                String[] criterios = criteriosFiltro(sc);
                String[] arquivos = filtrarPets(Integer.parseInt(criterios[0]), Integer.parseInt(criterios[1]), Integer.parseInt(criterios[2]), criterios[3], criterios[4], Integer.parseInt(criterios[5]));

                deletarPet(sc, arquivos, id);
                break;
            }
            case 4:
                listarPets();
                break;
            case 5: {
                String[] criterios = criteriosFiltro(sc);

                filtrarPets(Integer.parseInt(criterios[0]), Integer.parseInt(criterios[1]), Integer.parseInt(criterios[2]), criterios[3], criterios[4], Integer.parseInt(criterios[5]));
                break;
            }
            case 6:
                break;
            default:
                System.out.println("Você digitou uma opção inválida, tente novamente.");
        }

    }

    public static Pet criarPet(String[] dados) {
        ValidaPet.validarPet(dados);
        System.out.print("Pet criado com sucesso.");
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

    public static void listarPets() {
        File folder = new File("petsCadastrados");
        File[] files = folder.listFiles();

        if (files != null) {
            int contador = 1;
            for (File arquivo : files) {
                try (FileReader fr = new FileReader(arquivo.getAbsoluteFile()); BufferedReader br = new BufferedReader(fr)) {
                    String line;
                    System.out.print(contador);
                    while ((line = br.readLine()) != null) {
                        System.out.print(line.replaceAll("\\s*\\d+\\s*-\\s*", " - "));
                    }
                    contador++;
                    System.out.println();
                } catch (IOException e) {
                    System.out.println("Erro!" + e.getMessage());
                }
            }
        } else {
            System.out.print("Nenhum pet cadastrado encontrado nos arquivos.");
        }
    }

    public static String[] filtrarPets(int criterio1, int criterio2, int qtdFiltros, String filtroValor, String filtroValor2, int filtroPet) {
        File folder = new File("petsCadastrados");
        File[] files = folder.listFiles();
        String[] caminhosPetsFiltrados = new String[0];

        if (files != null) {
            caminhosPetsFiltrados = new String[files.length];
            int petsFiltrados = 0;
            for (File file : files) {
                File arquivo = new File(file.getAbsolutePath());
                try (FileReader fr = new FileReader(arquivo); BufferedReader br = new BufferedReader(fr)) {
                    String line;
                    StringBuilder sb = new StringBuilder();

                    while ((line = br.readLine()) != null) {
                        String petInfo = line.replaceAll("\\s*\\d+\\s*-\\s*", " - ").trim();
                        sb.append(petInfo).append(" ");
                    }

                    boolean filtrado1 = true;
                    boolean filtrado2 = true;

                    if (filtroPet == 1 && !sb.toString().contains("Cachorro")) {
                        filtrado1 = false;
                    } else if (filtroPet != 1 && !sb.toString().contains("Gato")) {
                        filtrado1 = false;
                    }

                    if (criterio1 == 1 && !sb.toString().contains(filtroValor)) {
                        filtrado1 = false;
                    } else if (criterio1 == 2 && !sb.toString().contains(filtroValor)) {
                        filtrado1 = false;
                    } else if (criterio1 == 3 && !sb.toString().contains(filtroValor)) {
                        filtrado1 = false;
                    } else if (criterio1 == 4 && !sb.toString().contains(filtroValor)) {
                        filtrado1 = false;
                    } else if (criterio1 == 5 && !sb.toString().contains(filtroValor)) {
                        filtrado1 = false;
                    } else if (criterio1 == 6 && !sb.toString().contains(filtroValor)) {
                        filtrado1 = false;
                    }

                    if (qtdFiltros == 2) {
                        if (criterio2 == 1 && !sb.toString().contains(filtroValor2)) {
                            filtrado2 = false;
                        } else if (criterio2 == 2 && !sb.toString().contains(filtroValor2)) {
                            filtrado2 = false;
                        } else if (criterio2 == 3 && !sb.toString().contains(filtroValor2)) {
                            filtrado2 = false;
                        } else if (criterio2 == 4 && !sb.toString().contains(filtroValor2)) {
                            filtrado2 = false;
                        } else if (criterio2 == 5 && !sb.toString().contains(filtroValor2)) {
                            filtrado2 = false;
                        } else if (criterio2 == 6 && !sb.toString().contains(filtroValor2)) {
                            filtrado2 = false;
                        }
                    }

                    if (qtdFiltros == 1) {
                        if (filtrado1) {
                            caminhosPetsFiltrados[petsFiltrados] = arquivo.getAbsolutePath();
                            petsFiltrados++;
                            System.out.println(petsFiltrados + sb.toString().trim());
                        }
                    } else {
                        if (filtrado1 && filtrado2) {
                            caminhosPetsFiltrados[petsFiltrados] = arquivo.getAbsolutePath();
                            petsFiltrados++;
                            System.out.println(petsFiltrados + sb.toString().trim());
                        }
                    }

                    sb.setLength(0);
                } catch (IOException e) {
                    System.out.println("Erro!" + e.getMessage());
                }
            }

            if (petsFiltrados == 0) {
                System.out.print("Sem resultados! Nenhum pet foi encontrado.");
            }

        } else {
            System.out.println("Nenhum pet cadastrado encontrado nos arquivos.");
        }
        return caminhosPetsFiltrados;
    }

    public static String[] criteriosFiltro(Scanner sc) {
        String menu = """
                Escolha um critério para a busca
                1 - Nome e/ou sobrenome
                2 - Gênero
                3 - Idade
                4 - Peso
                5 - Raça
                6 - Endereço
                """;
        int filtroPet = 0;
        int criterio1 = 0;
        int criterio2 = 0;
        int qtdFiltros = 0;
        do {
            System.out.print("Escolha o tipo de pet a ser filtrado\n");
            System.out.print("""
                    1 - Cachorro
                    2 - Gato
                    """);
            System.out.print("Insira uma opção: ");
            filtroPet = sc.nextInt();
        } while (filtroPet < 1 || filtroPet > 2);

        do {
            System.out.print("Escolha a quantidade de critérios\n");
            System.out.print("1 - Um critério\n");
            System.out.print("2 - Dois critérios\n");
            System.out.print("Insira a opção: ");
            qtdFiltros = sc.nextInt();
        } while (qtdFiltros < 1 || qtdFiltros > 2);

        do {
            System.out.print(menu);
            System.out.print("Insira a opção: ");
            criterio1 = sc.nextInt();
        } while (criterio1 < 1 || criterio1 > 6);

        if (qtdFiltros == 2) {
            do {
                System.out.print(menu);
                System.out.print("Insira a opção: ");
                criterio2 = sc.nextInt();
            } while (criterio2 < 1 || criterio2 > 6 || criterio2 == criterio1);
        }
        String filtroValor = "";
        String filtroValor2 = "";
        sc.nextLine(); // limpando buffer do int criterio

        System.out.print("Insira o valor a ser buscado para o critério: ");
        filtroValor = sc.nextLine();

        if (qtdFiltros == 2) {
            System.out.print("Insira o valor a ser buscado para o critério 2: ");
            filtroValor2 = sc.nextLine();
        }
        return new String[]{String.valueOf(criterio1), String.valueOf(criterio2), String.valueOf(qtdFiltros), filtroValor, filtroValor2, String.valueOf(filtroPet)};
    }

    public static void deletarPet(Scanner sc, String[] arquivos, int id) {
        do {
            System.out.print("\nInsira o ID do pet que deseja deletar: ");
            id = sc.nextInt();
        } while (id < 1 || id > arquivos.length || arquivos[id - 1] == null);

        File arquivoPet = new File(arquivos[id - 1]);
        if (arquivoPet.delete()) {
            System.out.print("O pet foi deletado");
        } else {
            System.out.print("Pet não encontrado.");
        }
    }

    public static void alterarPet(Scanner sc, DateTimeFormatter df) {
        int id = 0;
        int escolha = 0;
        String alteracao = "";
        boolean valido = false;
        int linhaArquivo = 0;
        File arquivoPet;
        String[] linhas;
        StringBuilder sb = new StringBuilder();
        String[] criterios = criteriosFiltro(sc);
        String[] arquivos = filtrarPets(Integer.parseInt(criterios[0]), Integer.parseInt(criterios[1]), Integer.parseInt(criterios[2]), criterios[3], criterios[4], Integer.parseInt(criterios[5]));

        do {
            System.out.print("Insira o ID do pet que deseja alterar: ");
            id = sc.nextInt();
        } while (id < 1 || id > arquivos.length || arquivos[id - 1] == null);

        do {
            System.out.print("""
                    Escolha o campo que será alterado
                    1 - Nome
                    2 - Endereço
                    3 - Idade
                    4 - Peso
                    5 - Raça
                    """);
            System.out.print("Insira a opção: ");
            escolha = sc.nextInt();
        } while (escolha < 1 || escolha > 5);

        sc.nextLine(); // limpando buffer
        do {
            sb.setLength(0);
            if (escolha == 4) {
                System.out.print("\nI - Número da casa: ");
                String numero = sc.nextLine();
                System.out.print("II - Cidade: ");
                String cidade = sc.nextLine();
                System.out.print("III - Rua: ");
                String rua = sc.nextLine();
                alteracao = rua + "," + numero + "," + cidade;
            } else {
                System.out.print("Insira a alteração: ");
                alteracao = sc.nextLine();
            }

            arquivoPet = new File(arquivos[id - 1]);

            try (FileReader fr = new FileReader(arquivoPet); BufferedReader br = new BufferedReader(fr)) {
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line).append(System.lineSeparator());
                }
            } catch (IOException e) {
                System.out.print("Erro!" + e.getMessage());
            }

            linhas = sb.toString().split(System.lineSeparator());

            try {
                switch (escolha) {
                    case 1:
                        linhaArquivo = 1;
                        ValidaPet.validaNome(alteracao);
                        File dest = new File(arquivoPet.getParentFile(), LocalDateTime.now().format(df) + "-" + alteracao.toUpperCase().replaceAll(" ", "") + ".TXT");

                        if (!arquivoPet.renameTo(dest)) {
                            System.out.print("Erro ao renomear arquivo");
                        } else {
                            arquivoPet = dest;
                        }
                        break;
                    case 2:
                        linhaArquivo = 4;
                        ValidaPet.validaEndereco(alteracao);
                        break;
                    case 3:
                        ValidaPet.validaIdade(alteracao);
                        alteracao = alteracao + " anos";
                        linhaArquivo = 5;
                        break;
                    case 4:
                        ValidaPet.validaPeso(alteracao);
                        alteracao = alteracao + " kg";
                        linhaArquivo = 6;
                        break;
                    case 5:
                        ValidaPet.validaRaca(alteracao);
                        linhaArquivo = 7;
                        break;
                }
                valido = true;
            } catch (IllegalArgumentException e) {
                System.out.print("Erro!" + e.getMessage());
            }
        } while (!valido);

        for (int i = 0; i < linhas.length; i++) {
            if (linhas[i].startsWith(String.valueOf(linhaArquivo))) {
                linhas[i] = linhaArquivo + " - " + alteracao;
            }
        }

        try (FileWriter fw = new FileWriter(arquivoPet); BufferedWriter bw = new BufferedWriter(fw)) {
            for (String linha : linhas) {
                bw.write(linha);
                bw.newLine();
            }
            bw.flush();
        } catch (IOException e) {
            System.out.println("Erro!" + e.getMessage());
        }

    }
}
