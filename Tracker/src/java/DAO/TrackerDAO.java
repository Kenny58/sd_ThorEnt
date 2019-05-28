/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;


import controle.Banco;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import modelo.Arquivo;
import modelo.Peer;

/**
 *
 * @author Helbert Monteiro
 */
public class TrackerDAO {
    
    private String            sql;
    private PreparedStatement preparedStatement;
    private ResultSet         resultSet;
    private boolean           retorno;
    
    public boolean inserirPeer(Peer peer){
        try{
            if(!verificaIp(peer.getIp())){
                sql               = "insert into peer (ip) values (?)";
                preparedStatement = Banco.getPreparedStatement(sql);
                preparedStatement.setString(1, peer.getIp());
                if(preparedStatement.executeUpdate() > 0){
                    retorno = inserirArquivo(peer);
                }
            }else{
                retorno = atualizaListaArquivos(peer);
            }
        }catch(SQLException erro){
            System.out.println("Inserir Peer: " + erro.getMessage());
            retorno = false;
        }
        return retorno;
    }
    
    private boolean verificaIp(String ip){
        sql               = "select ip from peer where ip = ?";
        preparedStatement = Banco.getPreparedStatement(sql);
        try{
            preparedStatement.setString(1, ip);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return true;
            }else{
                return false;
            }
        }catch(SQLException erro){
            System.out.println("Verificar IP: " + erro.getMessage());
            return false;
        }
    }
    
    private boolean atualizaListaArquivos(Peer peer){
        sql               = "delete from arquivo where ip = ?";
        preparedStatement = Banco.getPreparedStatement(sql);
        try{
            preparedStatement.setString(1, peer.getIp());
            if(preparedStatement.executeUpdate()> 0){
                retorno = inserirArquivo(peer);
            }
        }catch(SQLException erro){
            System.out.println("Atualizar Lista de Arquivos: " + erro.getMessage());
            retorno = false;
        }
        return retorno;
    }
    
    private boolean inserirArquivo(Peer peer){
        for(int i = 0; i < peer.getArquivos().size(); i++){
            sql = "insert into arquivo (hashArquivo, nome, tamanhoArquivo, tamanhoVetor, ip) values (?, ?, ?, ?, ?)";
            preparedStatement = Banco.getPreparedStatement(sql);
            try{
                preparedStatement.setString(1, peer.getArquivos().get(i).getHashArquivo());
                preparedStatement.setString(2, peer.getArquivos().get(i).getNome());
                preparedStatement.setDouble(3, peer.getArquivos().get(i).getTamanhoArquivo());
                preparedStatement.setInt   (4, peer.getArquivos().get(i).getTamanhoVetor());
                preparedStatement.setString(5, peer.getIp());
                if(preparedStatement.executeUpdate() > 0){
                    retorno = true;
                }
            }catch(SQLException erro){
                System.out.println("Inserir Arquivo: " + erro.getMessage());
                retorno = false;
                break;
            }
        }
        return retorno;
    }
    
    public List<Arquivo> listarArquivo(){
        List<String> hashs = new ArrayList<>();
        hashs = listarHash();
        List<Arquivo> arquivos = new ArrayList<>();
        try{
            for(int i = 0; i < hashs.size(); i++){
                sql = "select * from arquivo where hashArquivo = ?";
                preparedStatement = Banco.getPreparedStatement(sql);
                try{
                    preparedStatement.setString(1, hashs.get(i));
                    resultSet = preparedStatement.executeQuery();
                    List<String> peers = new ArrayList<>();
                    while(resultSet.next()){
                        peers.add(resultSet.getString("ip"));
                    }
                    
                    resultSet.previous();
                    Arquivo arquivo = new Arquivo();
                    arquivo.setHashArquivo   (resultSet.getString("hashArquivo"));
                    arquivo.setNome          (resultSet.getString("nome"));
                    arquivo.setTamanhoArquivo(resultSet.getDouble("tamanhoArquivo"));
                    arquivo.setTamanhoVetor  (resultSet.getInt   ("tamanhoVetor"));
                    arquivo.setPeers         (peers);

                    arquivos.add(arquivo);

                }catch(SQLException erro){
                    System.out.println("Lista Arquivos: " + erro.getMessage());
                }
            }
            return arquivos;
        }catch(Exception erro){
            System.out.println("Lista Arquivos(for): " + erro.getMessage());
            return null;
        }
    }
    
    private List<String> listarHash(){
        List<String> hashs = new ArrayList<>();
        sql = "select distinct hashArquivo from Arquivo";
        preparedStatement = Banco.getPreparedStatement(sql);
        try{
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                hashs.add(resultSet.getString("hashArquivo"));
            }
            return hashs;
        }catch(SQLException erro){
            System.out.println("Lista Hashs: " + erro.getMessage());
            return null;
        }
    }
    
    private List<String> listarIp(ResultSet resultSet) throws SQLException{
        List<String> peers = new ArrayList<>();
        while(resultSet.next()){
            peers.add(resultSet.getString("ip"));
        }
        return peers;
    }
    
}
