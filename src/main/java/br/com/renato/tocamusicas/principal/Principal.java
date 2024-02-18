package br.com.renato.tocamusicas.principal;

import br.com.renato.tocamusicas.model.Artista;
import br.com.renato.tocamusicas.model.Musica;
import br.com.renato.tocamusicas.model.TipoArtista;
import br.com.renato.tocamusicas.repository.ArtistaRepository;
import br.com.renato.tocamusicas.service.ConsultaChatGPT;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Principal {
    private final ArtistaRepository repositorio;
    Scanner sc = new Scanner(System.in);

    public Principal(ArtistaRepository repositorio) {
        this.repositorio = repositorio;
    }

    public void exibeMenu() {

        var opcao = -1;
        while (opcao != 0) {
            var menu = """
                    1 - Cadastrar artistas
                    2 - Cadastrar músicas
                    3 - Listar músicas
                    4 - Buscar músicas por artistas
                    5 - Pesquisar dados sobre um artistas
                                    
                    0 - Sair                                 
                    """;

            System.out.println(menu);
            opcao = sc.nextInt();
            sc.nextLine();

            switch (opcao) {
                case 1:
                    cadastrarArtistas();
                    break;
                case 2:
                    cadastrarMusicas();
                    break;
                case 3:
                    listarMusicas();
                    break;
                case 4: 
                    buscarMusicasPorArtista();
                    break;
                case 5:
                    pesquisarDadosDoArtista();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida");
            }
        }
    }

    private void pesquisarDadosDoArtista() {
        System.out.println("Digite o nome do artista que está no banco que deseja fazer a pesquisa:  ");
        var nome = sc.nextLine();

        Optional<Artista> artista = repositorio.findByNomeContainingIgnoreCase(nome);

        if (artista.isPresent()){
            var resposta = ConsultaChatGPT.fazerPesquisa(nome);
            System.out.println(resposta.trim());
        } else {
            System.out.println("Este artista não está cadastrado no banco!");
        }
    }

    private void buscarMusicasPorArtista() {
        System.out.println("Digite o nome do artista: ");
        var nome = sc.nextLine();

        List<Musica> musicas = repositorio.buscaMusicaPorArtista(nome);

        musicas.forEach(System.out::println);
    }

    private void listarMusicas() {
        List<Artista> artistas = repositorio.findAll();

        artistas.forEach(a -> a.getMusicas().forEach(System.out::println));
    }

    private void cadastrarMusicas() {
        System.out.println("Cadastrar música de qual artisa? ");
        var nome = sc.nextLine();

        Optional<Artista> artista = repositorio.findByNomeContainingIgnoreCase(nome);

        if(artista.isPresent()){
            System.out.println("Qual o nome da música: ");
            var nomeMusica = sc.nextLine();

            Musica musica = new Musica(nomeMusica);
            musica.setArtista(artista.get());
            artista.get().getMusicas().add(musica);
            repositorio.save(artista.get());
        } else {
            System.out.println("Não foi encontrado esse artista no banco.");
        }
    }

    private void cadastrarArtistas() {
        var op = "S";

        while (op.equalsIgnoreCase("s")) {
            System.out.println("Digite o nome do artista: ");
            var nome = sc.nextLine();

            System.out.println("Digite qual o tipo do artista: (SOLO, DUPLA ou BANDA)");
            var tipo = sc.nextLine();

            TipoArtista tipoArtista = TipoArtista.valueOf(tipo.toUpperCase());

            Artista artista = new Artista(nome, tipoArtista);
            repositorio.save(artista);

            System.out.println("Deseja cadastrar novo artista? (S/N)");
            op = sc.nextLine();
        }
    }
}
