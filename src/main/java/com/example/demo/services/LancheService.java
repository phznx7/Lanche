package com.example.demo.services;

import com.example.demo.entities.Lanche;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class LancheService {
    private String filePath = "C:\\Users\\aluno.fsa\\ImagensLancheDestino\\";

    public boolean salvar(Lanche lanche) {
        Path path = Paths.get(lanche.getImagem());
        Path destinationPath = Paths.get(String.format("%s%d.%s", filePath, lanche.getCodigo(), getFileExtension(path)));

        if (Files.exists(path)) {
            try {
                Files.copy(path, destinationPath, StandardCopyOption.REPLACE_EXISTING);
                lanche.setImagem(destinationPath.toString());
                return true;
            } catch (IOException e) {
                return false;
            }
        }

        return false;
    }

    public boolean excluir(int id, Lanche lanche) {
        Path fileToDelete = Paths.get(String.format("%s%d.%s", filePath, lanche.getCodigo(), getFileExtension(Paths.get(lanche.getImagem()))));

        if (Files.exists(fileToDelete)) {
            try {
                Files.delete(fileToDelete);
                return true;
            } catch (IOException e) {
                throw new RuntimeException("Erro ao excluir a imagem do lanche: " + lanche.getCodigo(), e);
            }
        }

        return false;
    }

    private String getFileExtension(Path path) {
        String filename = path.getFileName().toString();
        int lastDotIndex = filename.lastIndexOf('.');
        return lastDotIndex == -1 ? "" : filename.substring(lastDotIndex + 1);
    }
}
