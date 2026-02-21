/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ingressosjogos.bd.util;
import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionPostgres {

    private static String URL = "jdbc:postgresql://localhost:5432/ingressos";
    private static String USER = "postgres";
    private static String PASSWORD = "postgres";

    // public ConnectionPostgres(){
    //     this.URL = "jdbc:postgresql://localhost:5432/postgres";
    //     this.USER = "postgres";
    //     this.PASSWORD = "1234";
    // }

    public static Connection getConection()throws Exception{
        Class.forName("org.postgresql.Driver");
        return DriverManager.getConnection(URL, USER, PASSWORD);
        
    }


       
}