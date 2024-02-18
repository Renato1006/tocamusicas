package br.com.renato.tocamusicas.service;

import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.service.OpenAiService;

public class ConsultaChatGPT {
    public static String fazerPesquisa(String texto){
        OpenAiService service = new OpenAiService("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");

        CompletionRequest requisicao = CompletionRequest.builder()
                .model("gpt-3.5-turbo-instruct")
                .prompt("me fale sobre o artista: " + texto)
                .maxTokens(1000)
                .temperature(0.7)
                .build();

        var resposta = service.createCompletion(requisicao);
        return resposta.getChoices().get(0).getText();
    }
}
