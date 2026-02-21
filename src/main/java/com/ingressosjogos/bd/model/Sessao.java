/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ingressosjogos.bd.model;

/**
 *
 * @author janailson
 */
public class Sessao {
    // Guarda se quem logou é administrador
    public static boolean isAdmin = false;
    
    // Guarda os dados do torcedor logado (útil para quando você for fazer a compra do ingresso!)
    public static Torcedor torcedorLogado = null;
}
