package com.example.logonrm.assistenciaapp.model;

import java.util.Date;

/**
 * Created by vvilas on 05/09/2017.
 */

public class Item {

    private int codigo;
    private int codigoFuncionario;
    private boolean finalizado;
    private String descricao;

    public Item(int codigo, int codigoFuncionario, boolean finalizado, String descricao) {
        this.codigo = codigo;
        this.codigoFuncionario = codigoFuncionario;
        this.finalizado = finalizado;
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return "Código: " + codigo +
                ", Código do Funcionario: " + codigoFuncionario +
                ", finalizado: " + finalizado +
                ", descricao: " + descricao;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public int getCodigoFuncionario() {
        return codigoFuncionario;
    }

    public void setCodigoFuncionario(int codigoFuncionario) {
        this.codigoFuncionario = codigoFuncionario;
    }

    public boolean isFinalizado() {
        return finalizado;
    }

    public void setFinalizado(boolean finalizado) {
        this.finalizado = finalizado;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
