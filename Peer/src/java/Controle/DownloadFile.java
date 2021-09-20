/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controle;

import Modelo.Arquivo;
import aplicacao.TelaLista;
import aplicacao.TelaLog;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Raphael Felipe
 */
public class DownloadFile {
    public String hash, ip, port;

    public DownloadFile() {}
    
    public DownloadFile(String hash, String ip, String port) {
        this.hash = hash;
        this.ip = ip;
        this.port = port;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

   public void saveDownloadFile(int[] vetor_principal, Arquivo arquivo, TelaLog telaLog){
        byte[] vetor_final = new byte[vetor_principal.length];

        for(int i = 0; i < vetor_principal.length; i++){
            vetor_final[i] = (byte) vetor_principal[i];
        }
        telaLog.logArea.append("verificando...");
        System.out.println("verificando...");
        try {
            if(new TorrentFilesManage().getHashCode(vetor_final).equals(arquivo.getHashArquivo())){
                new TorrentFilesManage().createFileFromByteArray("C://ThorEnt//" + arquivo.getNome(), vetor_final);
                System.out.println("ok");
                System.out.println("salvo!");
            }else{
                telaLog.logArea.append("Hash incorreto");
                telaLog.logArea.append("Hash esperado: " + arquivo.getHashArquivo());
                telaLog.logArea.append("Hash do arquivo baixado: " + new TorrentFilesManage().getHashCode(vetor_final));
                System.out.println("Hash incorreto");
                System.out.println("Hash esperado: " + arquivo.getHashArquivo());
                System.out.println("Hash do arquivo baixado: " + new TorrentFilesManage().getHashCode(vetor_final));
            }
            //new TorrentFilesManage().createFileFromByteArray("C://ThorEnt//testando.jpg", vetor_final);
        } catch (Exception ex) {
            telaLog.logArea.append("Salvar arquivo: " + ex.getMessage());
            System.out.println("Salvar arquivo: " + ex.getMessage());
            Logger.getLogger(TelaLista.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
}
